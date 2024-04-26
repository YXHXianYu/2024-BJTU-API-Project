package com.yxhxianyu.secure.auditable.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxhxianyu.secure.auditable.api.pojo.StudentPojo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * DAO类，作为一个中间层，提供一个Java代码到数据库的映射
 * @author YXH_XianYu
 * @date 2024/4/26 10:28
 **/
@Mapper
public interface StudentDao extends BaseMapper<StudentPojo> {
    @Select("SELECT * FROM student WHERE RAND() < 0.02")
    List<StudentPojo> selectRandomSample();
}
