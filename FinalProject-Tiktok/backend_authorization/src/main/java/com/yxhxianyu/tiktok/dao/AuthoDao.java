package com.yxhxianyu.tiktok.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxhxianyu.tiktok.pojo.AuthoPojo;
import org.apache.ibatis.annotations.Mapper;

/**
 * DAO类，作为一个中间层，提供一个Java代码到数据库的映射
 * @author YXH_XianYu
 **/
@Mapper
public interface AuthoDao extends BaseMapper<AuthoPojo> {
}
