package com.yxhxianyu.secure.auditable.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.secure.auditable.api.dao.StudentDao;
import com.yxhxianyu.secure.auditable.api.pojo.StudentPojo;
import com.yxhxianyu.secure.auditable.api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Student表的服务
 * @author YXH_XianYu
 * @date 2024/4/26 10:28
 **/
@Service
public class StudentService {

    @Autowired
    StudentDao studentDao;

    /**
     * 插入一条新的用户
     * 并返回该用户的UUID
     * 若insert失败，则返回以"ERROR"开头的字符串
     */
    public String insertStudent(String name, Integer gender, Integer age) {
        String uuid = UUID.randomUUID().toString();
        try {
            studentDao.insert(new StudentPojo(uuid, name, gender, age));
            return Utils.getOkResponse(
                    "Success",
                    new HashMap<String, Object>(){{
                        put("uuid", uuid);
                        put("name", name);
                        put("gender", gender);
                        put("age", age);
                    }}
            );
        } catch (DuplicateKeyException e) {
            System.out.println("Insert failed: duplicate name");
            return Utils.getResponse(
                    409,
                    "Insert failed: duplicate name",
                    new HashMap<String, Object>(){{
                        put("name", name);
                        put("gender", gender);
                        put("age", age);
                    }}
            );
        } catch (DataIntegrityViolationException e) {
            System.out.println("Insert failed: data integrity violation");
            return Utils.getResponse(
                    400,
                    "Insert failed: data integrity violation",
                    new HashMap<String, Object>(){{
                        put("name", name);
                        put("gender", gender);
                        put("age", age);
                    }}
            );
        }
    }

    /**
     * 根据UUID删除一条用户
     * 在得到UUID时，请注意你的StudentPojo对象非空，否则会在函数外部产生NullPointerException
     */
    public void deleteStudentByUUID(String uuid) {
        studentDao.deleteById(uuid);
    }

    /**
     * 根据名称删除一条用户
     */
    public void deleteStudentByName(String name) {
        StudentPojo student = getStudentByName(name);
        if(student != null)
            studentDao.deleteById(student.getUuid());
    }

    // 不支持修改

    /**
     * 根据UUID查询一条用户信息
     */
    @Nullable
    public StudentPojo getStudentByUUID(String uuid) {
        return studentDao.selectById(uuid);
    }

    /**
     * 根据名字查询一条用户信息
     */
    @Nullable
    public StudentPojo getStudentByName(String name) {
        return studentDao.selectOne(new QueryWrapper<StudentPojo>().eq("name", name));
    }

    /**
     * 查询所有用户
     */
    public List<StudentPojo> getStudents() {
        return studentDao.selectList(new QueryWrapper<>());
    }

    /**
     * 查询所有用户（只查询limit个 **随机** 学生信息）
     */
    public List<StudentPojo> getStudents(Integer limit) {
        List<StudentPojo> students = studentDao.selectList(new QueryWrapper<>());
        if (students.size() <= limit) {
            return students;
        } else {
            return students.subList(0, limit);
        }
    }
}
