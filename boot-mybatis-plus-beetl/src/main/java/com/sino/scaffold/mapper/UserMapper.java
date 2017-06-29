package com.sino.scaffold.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.sino.scaffold.common.SinoMapper;
import com.sino.scaffold.model.User;

/**
 * User 表数据库控制层接口
 */
public interface UserMapper extends SinoMapper<User> {

	/**
     * 自定义注入方法
     */
    int deleteAll();

    @Select("select test_id as id, name, age, test_type from user")
    List<User> selectListBySQL();

}