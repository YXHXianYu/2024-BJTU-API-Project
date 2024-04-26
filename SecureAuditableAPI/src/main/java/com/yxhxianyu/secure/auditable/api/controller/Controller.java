package com.yxhxianyu.secure.auditable.api.controller;

import com.yxhxianyu.secure.auditable.api.pojo.StudentPojo;
import com.yxhxianyu.secure.auditable.api.service.StudentService;
import com.yxhxianyu.secure.auditable.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.min;

/**
 * @author YXH_XianYu
 * @date 2024/4/26 10:28
 **/
@RestController
public class Controller {

    /* ----- ----- Service ----- ----- */

    @Autowired
    StudentService studentService;

    /* ----- ----- Student ----- ----- */

    /**
     * 创建单个学生
     */
    @PostMapping("/students")
    public String createStudent(@RequestBody HashMap<String, Object> student) {
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
    @GetMapping("/students")
    public String getAllStudents(@RequestParam(value = "limit", required = false) Integer limit) {
        int limitValue = limit == null ? 1000 : min(limit, 1000);
        List<StudentPojo> students = studentService.getStudents(limitValue);
        return Utils.getOkResponse(
                "Success",
                new HashMap<String, Object>() {{
                    put("students", students);
                }}
        );
    }
}
