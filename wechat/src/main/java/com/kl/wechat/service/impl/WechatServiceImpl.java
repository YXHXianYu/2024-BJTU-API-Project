package com.kl.wechat.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.kl.wechat.entity.Offer;
import com.kl.wechat.entity.User;
import com.kl.wechat.service.OfferService;
import com.kl.wechat.service.UserService;
import com.kl.wechat.service.WechatService;
import com.kl.wechat.utils.Constants;
import com.kl.wechat.utils.Lexer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("wechatService")
public class WechatServiceImpl implements WechatService {

    @Autowired
    private OfferService offerService;

    @Autowired
    private UserService userService;

    @Value("${wechat.token}")
    private String token;

    @Override
    public ResponseEntity<String> wechatHeartbeat(String signature, String timestamp, String nonce, String echostr) {
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (String s : arr) {
            content.append(s);
        }
        String tmpStr = content.toString();
        tmpStr = org.apache.commons.codec.digest.DigestUtils.sha1Hex(tmpStr);
        if (tmpStr.equals(signature)) {
            return ResponseEntity.ok(echostr);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid signature");
        }
    }

    @Override
    public ResponseEntity<String> wechatDistributor(String requestBody) {
        try {
            Document document = DocumentHelper.parseText(requestBody);
            Element root = document.getRootElement();
            String msgType = root.elementText("MsgType");
            String fromUser = root.elementText("FromUserName");
            String toUser = root.elementText("ToUserName");

            if ("text".equals(msgType)) {
                String content = root.elementText("Content");
                User user = userService.getUserByUsername(fromUser);
                if (user == null) {
                    userService.createUser(fromUser);
                    user = userService.getUserByUsername(fromUser);
                }
                String userRole = getUserRole(user.getState());
                String reply;
                try {
                    System.out.println("进入try语句: " + content);
                    Map<String, Object> tokens = Lexer.resolve(content, userRole);
                    System.out.println("tokens: " + tokens);
                    if (tokens != null) {
                        reply = wechatCommandDistributor(user.getUsername(), tokens, userRole);
                    } else {
                        // 修改这里
                        if (content.equalsIgnoreCase("help") || content.equalsIgnoreCase("帮助")) {
                            reply = Constants.MESSAGE_HELP;
                        } else {
                            reply = Constants.MESSAGE_UNKNOWN_COMMAND;
                        }
                    }
                } catch (Exception e) {
                    reply = Constants.MESSAGE_UNKNOWN_COMMAND;
                }
                return ResponseEntity.ok(wechatReturnData(fromUser, toUser, reply));
            } else if ("image".equals(msgType)) {
                return ResponseEntity.ok(wechatReturnData(fromUser, toUser, "暂不支持图片消息"));
            } else {
                return ResponseEntity.ok(wechatReturnData(fromUser, toUser, "请发送文本消息"));
            }
        } catch (DocumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid XML");
        }
    }

    @Override
    public String wechatCommandDistributor(String fromUser, Map<String, Object> tokens, String userRole) {
        if (tokens == null) {
            return "token为空";
        }
        String command = (String) tokens.get("command");
        System.out.println("command: " + command);
        int checkResult = userService.checkUserState(fromUser, command, true);
        if (checkResult == Constants.AUTHORITY_CHECK_USER_NOT_EXIST) {
            userService.createUser(fromUser);
            checkResult = userService.checkUserState(fromUser, command, true);
        }
        if (checkResult != Constants.AUTHORITY_CHECK_PASS) {
            return "";
        }
        switch (command) {
            case "help":
                return Constants.MESSAGE_HELP;
            case "commit":
                offerService.createOffer(tokens, userService.getUserByUsername(fromUser).getId());
                return Constants.MESSAGE_COMMIT_SUCCESS;
            case "update":
                offerService.updateOffer(tokens, userService.getUserByUsername(fromUser).getId());
                return "Offer更新成功";
            case "delete":
                offerService.deleteOffer(tokens, userService.getUserByUsername(fromUser).getId());
                return "Offer删除成功";
            case "query":
                Map<String, Object> queryParams = new HashMap<>();
                if (tokens.containsKey("company")) {
                    queryParams.put("company", tokens.get("company"));
                }
                if (tokens.containsKey("city")) {
                    queryParams.put("city", tokens.get("city"));
                }
                if (tokens.containsKey("position")) {
                    queryParams.put("position", tokens.get("position"));
                }
                if (tokens.containsKey("page")) {
                    queryParams.put("page", tokens.get("page"));
                } else {
                    queryParams.put("page", 1);
                }
                queryParams.put("sort-new", tokens.getOrDefault("sort-new", false));
                queryParams.put("sort-salary", tokens.getOrDefault("sort-salary", false));

                IPage<Offer> offerPage = offerService.listOffersWithPage(queryParams);
                return offerPage.getRecords().isEmpty() ? "No offers found" : offerPage.getRecords().toString();
            case "group-commit":
                List<Map<String, Object>> offers = (List<Map<String, Object>>) tokens.get("offers");
                offerService.createOffers(offers, userService.getUserByUsername(fromUser).getId());
                return Constants.MESSAGE_COMMIT_SUCCESS;
            case "ban":
                userService.banUser((String) tokens.get("username"));
                return "成功封禁用户";
            case "unban":
                userService.unbanUser((String) tokens.get("username"));
                return "成功解禁用户";
            default:
                throw new IllegalArgumentException("Unknown command: " + command);
        }
    }

    private String getUserRole(int state) {
        return switch (state) {
            case Constants.USER_STATE_ADMIN -> "admin";
            case Constants.USER_STATE_BANNED -> "banned";
            case Constants.USER_STATE_SPIDER -> "spider";
            default -> "guest";
        };
    }

    private String wechatReturnData(String toUser, String fromUser, String content) {
        return String.format(
                "<xml>\n" +
                        "  <ToUserName><![CDATA[%s]]></ToUserName>\n" +
                        "  <FromUserName><![CDATA[%s]]></FromUserName>\n" +
                        "  <CreateTime>%d</CreateTime>\n" +
                        "  <MsgType><![CDATA[text]]></MsgType>\n" +
                        "  <Content><![CDATA[%s]]></Content>\n" +
                        "</xml>",
                toUser, fromUser, new Date().getTime() / 1000, content);
    }
}