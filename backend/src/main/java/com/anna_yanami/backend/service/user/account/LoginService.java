package com.anna_yanami.backend.service.user.account;

public interface LoginService {
    //获取jwt_token
    String get_token(String username, String password);
}
