package com.anna_yanami.backend.controller.user.account;

import com.anna_yanami.backend.config.ResponseResult;
import com.anna_yanami.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class RegisterController {
    @Autowired
    private RegisterService registerService;

    @PostMapping("/user/account/register")
    public ResponseResult<Map<String, String>> register(@RequestParam Map<String, String> map){
        String name = map.get("name");
        String email = map.get("email");
        String password = map.get("password");

        Map<String, String> res = registerService.register(name, email, password);
        if("success".equals(res.get("error_message"))){
            return ResponseResult.success(200, "success", res);
        }else{
            return ResponseResult.fail("fail",res);
        }
    }
}
