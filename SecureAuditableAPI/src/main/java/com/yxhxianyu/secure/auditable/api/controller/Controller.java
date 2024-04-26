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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private final ArrayList<AtomicLong> requestSubCounter = new ArrayList<>(10){{for(int i = 0; i < 10; i++) add(new AtomicLong(0));}};
    private final ArrayList<AtomicLong> requestSubTimeSum = new ArrayList<>(10){{for(int i = 0; i < 10; i++) add(new AtomicLong(0));}};

    /* ----- ----- Student ----- ----- */

    /**
     * 创建单个学生
     */
    @PostMapping("/students")
    public String createStudent(@RequestBody HashMap<String, Object> student) {
//        return Utils.getResponse(410, "This API is disabled");
        try {
            return studentService.insertStudent(
                    (String) student.get("name"),
                    (Integer) student.get("gender"),
                    (Integer) student.get("age")
            );
        } catch (Exception e) {
            return Utils.getResponse(400, "Bad Request");
        }
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
    private static final int QPS_LOG_DURATION = 10000;
    private static final int QPS_LOG_DURATION_SECONDS = QPS_LOG_DURATION / 1000;
    private static final int QPS_LIMIT = 10;
    @GetMapping("/students")
    public String getAllStudents(@RequestParam(value = "limit", required = false) Integer limit) {

        long requestCount = requestCounter.get();
        if (requestCount / QPS_LOG_DURATION_SECONDS >= QPS_LIMIT) {
            logger.warn("Too Many Requests");
            return Utils.getResponse(429, "Too Many Requests");
        }

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
        logger.info("Request received at [{}] processed in {} ms. User request {} students info", getDateTime(startTime), duration, limitValue);
        requestCounter.incrementAndGet();
        requestTimeSum.addAndGet(duration);
        requestSubCounter.get((limitValue - 1) / 100).incrementAndGet();
        requestSubTimeSum.get((limitValue - 1) / 100).addAndGet(duration);
        return response;
    }

    /**
     * Log QPS
     */
    @Scheduled(fixedRate = QPS_LOG_DURATION)
    public void logQPS() {

        long startTime = System.currentTimeMillis() - QPS_LOG_DURATION;
        long requestCount = requestCounter.getAndSet(0);
        long requestTime = requestTimeSum.getAndSet(0);
        logger.info(
                "[{}] QPS: {}; Average Duration: {} ms",
                getDateTime(startTime),
                1.0 * requestCount / QPS_LOG_DURATION_SECONDS,
                (requestCount == 0 ? 0 : requestTime / requestCount)
        );

        for (int i = 0; i < 10 && i < requestSubCounter.size(); i++) {
            long subRequestCount = requestSubCounter.get(i).getAndSet(0);
            long subRequestTime = requestSubTimeSum.get(i).getAndSet(0);
            logger.info(
                    "[{}] QPS: {}; Average Duration: {} ms; Limit: {}-{}",
                    getDateTime(startTime),
                    1.0 * subRequestCount / QPS_LOG_DURATION_SECONDS,
                    (subRequestCount == 0 ? 0 : subRequestTime / subRequestCount),
                    i * 100 + 1,
                    (i + 1) * 100
            );
        }
    }

    private String getDateTime(long posixTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochSecond(posixTime / 1000), ZoneId.of("UTC+8"));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
