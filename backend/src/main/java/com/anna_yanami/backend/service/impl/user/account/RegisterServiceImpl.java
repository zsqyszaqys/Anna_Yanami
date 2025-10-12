package com.anna_yanami.backend.service.impl.user.account;

import com.anna_yanami.backend.mapper.UserMapper;
import com.anna_yanami.backend.pojo.User;
import com.anna_yanami.backend.service.user.account.RegisterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static boolean isValidEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    @Override
    public Map<String, String> register(String name, String email, String password) {
        Map<String, String> map = new HashMap<>();

        // 参数校验
        if (name == null || name.trim().isEmpty()) {
            map.put("error_message", "用户名不可以为空哦~");
            return map;
        }

        if (password == null || password.trim().isEmpty()) {
            map.put("error_message", "密码栏太空虚了~");
            return map;
        }

        if (name.length() > 100) {
            map.put("error_message", "太长的名字放不下捏~");
            return map;
        }

        if (password.length() > 100 || password.length() < 6) {
            map.put("error_message", "密码长度应在6-100位之间~");
            return map;
        }

        if(email == null || email.isEmpty()){
            map.put("error_message", "邮箱没有填写喵~");
            return map;
        }

        if(!isValidEmail(email)){
            map.put("error_message", "邮箱格式不对喵~");
            return map;
        }



        // 检查邮箱是否已存在
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", email);
        List<User> users = userMapper.selectList(userQueryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "已经见过面了就来登录喵~");
            return map;
        }

        try {
            // 注册逻辑
            String encodedPassword = passwordEncoder.encode(password);
            String photo = "https://cdn.acwing.com/media/user/profile/photo/475125_lg_825b7a9b90.png";
            User user = new User(null, name, email, encodedPassword, photo);
            userMapper.insert(user);

            map.put("error_message", "success");
            return map;

        } catch (Exception e) {
            // 处理数据库异常等系统错误
            map.put("error_message", "系统繁忙，请稍后重试~");
            return map;
        }
    }
}
