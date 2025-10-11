package com.anna_yanami.backend.service.user.account;

import com.anna_yanami.backend.config.ResponseResult;
import org.springframework.stereotype.Service;

public interface LoginService {
    ResponseResult get_token(String email, String password);
}
