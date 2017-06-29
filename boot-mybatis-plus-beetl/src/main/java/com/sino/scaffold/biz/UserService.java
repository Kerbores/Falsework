package com.sino.scaffold.biz;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.sino.scaffold.mapper.UserMapper;
import com.sino.scaffold.model.User;

/**
 * @author kerbores
 *
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
