package com.anna_yanami.backend.service.impl.user.account;

import com.anna_yanami.backend.pojo.User;
import com.anna_yanami.backend.service.impl.UserDetailsImpl;
import com.anna_yanami.backend.service.user.account.LoginService;
import com.anna_yanami.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String get_token(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(username, password);
        //检测是否登入（登入失败，自动处理报异常）
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //获取登入用户
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();
        //将用户转化为jwttoken

        return JwtUtil.createJWT(user.getId().toString());
    }
}
