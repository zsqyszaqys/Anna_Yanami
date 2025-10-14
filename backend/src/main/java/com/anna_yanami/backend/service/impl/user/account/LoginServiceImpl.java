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
    public String get_token(String loginId, String password) {
        // loginId 可以是用户名、邮箱、手机号中的任意一种
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginId, password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        return JwtUtil.createJWT(user.getId().toString());
    }
}
