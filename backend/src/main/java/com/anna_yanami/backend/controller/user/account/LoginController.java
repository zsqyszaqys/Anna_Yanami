package com.anna_yanami.backend.controller.user.account;

import com.anna_yanami.backend.config.ResponseResult;
import com.anna_yanami.backend.service.user.account.LoginService;
import com.anna_yanami.backend.utils.ServiceException;
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

    @PostMapping("/user/account/token")
    public ResponseResult<String> get_token(@RequestParam Map<String, String> map) {
        try {
            // 从参数中获取登录标识和密码
            String loginId = map.get("loginId");
            String password = map.get("password");

            // 参数校验
            if (loginId == null || loginId.trim().isEmpty()) {
                return ResponseResult.fail("登录标识不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                return ResponseResult.fail("密码不能为空");
            }

            // 直接调用登录服务，由UserDetailsService处理多种登录方式
            String token = loginService.get_token(loginId, password);

            // 返回成功结果
            return ResponseResult.success(200, "登录成功", token);

        } catch (ServiceException e) {
            // 业务异常
            throw e; // 会被全局异常处理器捕获
        } catch (Exception e) {
            // 其他异常
            return ResponseResult.fail("系统异常，请稍后重试");
        }
    }
}