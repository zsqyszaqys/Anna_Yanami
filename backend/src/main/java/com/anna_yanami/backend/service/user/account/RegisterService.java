package com.anna_yanami.backend.service.user.account;

import com.anna_yanami.backend.config.ResponseResult;

import java.util.Map;

public interface RegisterService {
    Map<String, String> register(String name, String email, String password);
}
