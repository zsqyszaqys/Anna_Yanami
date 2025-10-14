package com.anna_yanami.backend.service.impl;

import com.anna_yanami.backend.mapper.UserMapper;
import com.anna_yanami.backend.pojo.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 1. 先按用户名查找
        queryWrapper.eq("name", loginId);
        User user = userMapper.selectOne(queryWrapper);

        // 2. 如果没找到，按邮箱查找
        if (user == null) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", loginId);
            user = userMapper.selectOne(queryWrapper);
        }

        // 3. 如果还没找到，按手机号查找
        if (user == null) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", loginId);
            user = userMapper.selectOne(queryWrapper);
        }

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + loginId);
        }

        return new UserDetailsImpl(user);
    }
}
