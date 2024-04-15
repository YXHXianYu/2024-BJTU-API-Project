package com.kl.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kl.wechat.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
