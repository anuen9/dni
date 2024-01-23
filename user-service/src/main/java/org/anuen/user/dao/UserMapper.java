package org.anuen.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.anuen.user.entity.po.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<User> {
}
