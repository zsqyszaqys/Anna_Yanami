package com.anna_yanami.backend.controller.user.account;

import com.anna_yanami.backend.config.ResponseResult;
import com.anna_yanami.backend.mapper.UserMapper;
import com.anna_yanami.backend.pojo.User;
import com.anna_yanami.backend.service.user.account.LoginService;
import com.anna_yanami.backend.utils.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/user/account/token")
    public ResponseResult<String> get_token(@RequestParam Map<String, String> map){
        try {
            // 从参数中获取用户名和密码
            String email = map.get("email");
            String password = map.get("password");

            // 参数校验
            if (email == null || email.trim().isEmpty()) {
                return ResponseResult.fail("邮箱不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseResult.fail("密码不能为空");
            }

            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email);
            User user = userMapper.selectOne(queryWrapper);

            // 业务逻辑 - 生成token
            String token = loginService.get_token(user.getName(), password);

            // 返回成功结果
            return ResponseResult.success(200,"登录成功", token);

        } catch (ServiceException e) {
            // 业务异常
            throw e; // 会被全局异常处理器捕获
        } catch (Exception e) {
            // 其他异常
            return ResponseResult.fail("系统异常，请稍后重试");
        }
    }
}
