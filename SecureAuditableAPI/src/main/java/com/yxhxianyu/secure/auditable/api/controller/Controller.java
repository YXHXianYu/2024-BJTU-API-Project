package com.yxhxianyu.secure.auditable.api.controller;

import com.yxhxianyu.secure.auditable.api.pojo.StudentPojo;
import com.yxhxianyu.secure.auditable.api.service.StudentService;
import com.yxhxianyu.secure.auditable.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author YXH_XianYu
 * @date 2024/4/26 10:28
 **/
@RestController
public class Controller {

    /* ----- ----- Service ----- ----- */

    @Autowired
    private StudentService studentService;

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final AtomicLong requestCounter = new AtomicLong(0);
    private final AtomicLong requestTimeSum = new AtomicLong(0);

    /* ----- ----- Student ----- ----- */

    /**
     * 创建单个学生
     */
    @PostMapping("/students")
    public String createStudent(@RequestBody HashMap<String, Object> student) {
        return Utils.getResponse(410, "This API is disabled");
//        try {
//            return studentService.insertStudent(
//                    (String) student.get("name"),
//                    (Integer) student.get("gender"),
//                    (Integer) student.get("age")
//            );
//        } catch (Exception e) {
//            return Utils.getResponse(400, "Bad Request");
//        }
    }

    /**
     * 获取单个学生
     */
    @GetMapping("/students/{id}")
    public String getStudent(@PathVariable() String id) {
        StudentPojo studentPojo = studentService.getStudentByUUID(id);
        if (studentPojo == null) {
            return Utils.getResponse(404, "Student not found");
        } else {
            return Utils.getOkResponse(
                    "Success",
                    studentPojo.toHashMap()
            );
        }
    }

    /**
     * 获取所有学生
     */
    @GetMapping("/students")
    public String getAllStudents(@RequestParam(value = "limit", required = false) Integer limit) {

        long startTime = System.currentTimeMillis();

        // solve
        int limitValue = limit == null ? 1000 : max(1, min(limit, 1000));
        List<StudentPojo> students = studentService.getStudents(limitValue);

        String response = Utils.getOkResponse(
                "Success",
                new HashMap<String, Object>() {{
                    put("students", students);
                }}
        );

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("Request received at [{}] processed in {} ms", getDateTime(startTime), duration);
        requestCounter.incrementAndGet();
        requestTimeSum.addAndGet(duration);
        return response;
    }

    /**
     * Log QPS
     */
    private static final int qpsLogDuration = 5000;
    @Scheduled(fixedRate = qpsLogDuration)
    public void logQPS() {
        long startTime = System.currentTimeMillis() - qpsLogDuration;
        long requestCount = requestCounter.getAndSet(0);
        long requestTime = requestTimeSum.getAndSet(0);
        logger.info(
                "[{}] QPS: {}; Average Duration: {} ms",
                getDateTime(startTime),
                requestCount / 10.0,
                (requestCount == 0 ? 0 : requestTime / requestCount)
        );
    }

    private String getDateTime(long posixTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(posixTime / 1000), ZoneId.of("UTC+8"));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
