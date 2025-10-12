package com.anna_yanami.backend.service.impl.user.account;

import com.anna_yanami.backend.pojo.User;
import com.anna_yanami.backend.service.impl.UserDetailsImpl;
import com.anna_yanami.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class InfoServiceImpl implements InfoService {

    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authenticationToken =  (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> res = new HashMap<>();
        res.put("id", user.getId().toString());
        res.put("name", user.getName());
        res.put("email", user.getEmail());
        res.put("password", user.getPassword());
        res.put("photo", user.getPhoto());

        return res;
    }
}
