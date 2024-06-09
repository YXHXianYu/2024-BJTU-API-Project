package com.yxhxianyu.tiktok.controller;

import com.yxhxianyu.tiktok.pojo.UserPojo;
import com.yxhxianyu.tiktok.service.UserService;
import com.yxhxianyu.tiktok.service.VideoService;
import com.yxhxianyu.tiktok.utils.Result;
import com.yxhxianyu.tiktok.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

import static com.yxhxianyu.tiktok.utils.Util.checkPermission;

/**
 * @author YXH_XianYu
 **/
@RestController
public class VideoController {

    @Autowired
    VideoService videoService;

    @Autowired
    UserService userService;

    @RequestMapping(
            value = "/api/v1/videos",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Object> createVideo(
            @RequestParam("file")MultipartFile file,
            @RequestParam("title")String title,
            @RequestParam("token")String token
    ) {
        Result<UserPojo> user = userService.getUserByToken(token);
        if (user.err != null) {
            return Util.getResponse(401, "Unauthorized");
        }

        Result<String> res = videoService.createVideo(file, title, user.val.getUuid());
        if (res.err != null) {
            return Util.getResponse(422, res.err);
        }
        return Util.getOkResponse("创建成功");
    }

}
