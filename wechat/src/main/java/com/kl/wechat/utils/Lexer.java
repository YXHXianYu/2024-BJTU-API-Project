package com.kl.wechat.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    private static final Map<String, Map<String, String>> patterns = new HashMap<>();

    static {
        patterns.put("help", Map.of("base", "^(?:帮助|help)$"));
        patterns.put("commit", Map.of("base", "(?:新增offer|提交offer|commit)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)"));
        patterns.put("update", Map.of("base", "(?:修改offer|更新offer|update)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)"));
        patterns.put("delete", Map.of("base", "(?:删除offer|delete)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)"));
        patterns.put("query", Map.of(
                "base", "(?:查询offer|搜索offer|find offer|query|search)(?:\\s+(.*))?$",
                "company", "(?:--company|-co)\\s+(\\S+)",
                "city", "(?:--city|-ci)\\s+(\\S+)",
                "position", "(?:--position|-po)\\s+(\\S+)",
                "page", "(?:--page|-pa)\\s+(\\d+)",
                "sort-new", "(--sort-new)",
                "sort-salary", "(--sort-salary)"
        ));
        patterns.put("group-commit", Map.of(
                "base", "group-commit\\s+(.+)",
                "sub", "(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\d+)"
        ));
        patterns.put("ban", Map.of("base", "(?:ban|封禁)\\s+(\\S+)"));
        patterns.put("unban", Map.of("base", "(?:unban|解禁)\\s+(\\S+)"));
    }

    public static Map<String, Object> resolve(String content, String userRole) {
        System.out.println("Resolve接收到的command: " + content);
        Map<String, Object> tokens = tokenize(content);
        System.out.println("Command " + content + " was tokenized to " + (tokens != null ? tokens : "None"));
        return tokens;
    }

    private static String reWrapper(String pattern, String string) {
        Matcher matcher = Pattern.compile(pattern).matcher(string);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static Integer reWrapperInt(String pattern, String string) {
        String result = reWrapper(pattern, string);
        return result != null ? Integer.parseInt(result) : null;
    }

    private static boolean reWrapperBool(String pattern, String string) {
        return Pattern.compile(pattern).matcher(string).find();
    }

    private static Map<String, Object> tokenize(String command) {
        System.out.println("Tokenize接收到的command: " + command);
        for (Map.Entry<String, Map<String, String>> entry : patterns.entrySet()) {
            String name = entry.getKey();
            Map<String, String> pattern = entry.getValue();
            Matcher matcher = Pattern.compile(pattern.get("base")).matcher(command);
            if (matcher.matches()) {
                switch (name) {
                    case "help":
                        return Map.of("command", "help");
                    case "commit":
                        return Map.of(
                                "command", "commit",
                                "company", matcher.group(1),
                                "city", matcher.group(2),
                                "position", matcher.group(3),
                                "salary", Integer.parseInt(matcher.group(4))
                        );
                    case "update":
                        return Map.of(
                                "command", "update",
                                "company", matcher.group(1),
                                "city", matcher.group(2),
                                "position", matcher.group(3),
                                "salary", Integer.parseInt(matcher.group(4))
                        );
                    case "delete":
                        return Map.of(
                                "command", "delete",
                                "company", matcher.group(1),
                                "city", matcher.group(2),
                                "position", matcher.group(3)
                        );
                    case "query":
                        String queryParams = matcher.group(1);
                        if (queryParams == null) {
                            queryParams = "";
                        }
                        Map<String, Object> queryResult = new HashMap<>();
                        queryResult.put("command", "query");

                        // 分别匹配可选参数
                        if (pattern.containsKey("company")) {
                            String company = reWrapper(pattern.get("company"), queryParams);
                            if (company != null) queryResult.put("company", company);
                        }
                        if (pattern.containsKey("city")) {
                            String city = reWrapper(pattern.get("city"), queryParams);
                            if (city != null) queryResult.put("city", city);
                        }
                        if (pattern.containsKey("position")) {
                            String position = reWrapper(pattern.get("position"), queryParams);
                            if (position != null) queryResult.put("position", position);
                        }
                        if (pattern.containsKey("page")) {
                            Integer page = reWrapperInt(pattern.get("page"), queryParams);
                            if (page != null) queryResult.put("page", page);
                        }
                        // 处理排序选项
                        queryResult.put("sort-new", reWrapperBool(pattern.get("sort-new"), queryParams));
                        queryResult.put("sort-salary", reWrapperBool(pattern.get("sort-salary"), queryParams));

                        return queryResult;
                    case "group-commit":
                        String offersString = matcher.group(1);
                        Matcher subMatcher = Pattern.compile(pattern.get("sub")).matcher(offersString);
                        List<Map<String, Object>> offers = new ArrayList<>();
                        while (subMatcher.find()) {
                            offers.add(Map.of(
                                    "company", subMatcher.group(1),
                                    "city", subMatcher.group(2),
                                    "position", subMatcher.group(3),
                                    "salary", Integer.parseInt(subMatcher.group(4))
                            ));
                        }
                        return Map.of("command", "group-commit", "offers", offers);
                    case "ban":
                        return Map.of(
                                "command", "ban",
                                "username", matcher.group(1)
                        );
                    case "unban":
                        return Map.of(
                                "command", "unban",
                                "username", matcher.group(1)
                        );
                    default:
                        throw new IllegalArgumentException("Unknown command name: " + name);
                }
            }
        }
        return null;
    }
}