package com.anna_yanami.backend.controller.user.account;

import com.anna_yanami.backend.config.ResponseResult;
import com.anna_yanami.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class InfoServiceController {
    @Autowired
    private InfoService infoService;

    @GetMapping("/user/account/info")
    public ResponseResult<Map<String, String>> getInfo(){
        return ResponseResult.success(200, "success", infoService.getInfo());
    }

}
