package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.util.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// 用来实现 user包中定义的接口

@Service
public class LoginServiceImpl implements LoginService {

//    验证用户是否登陆 一个接口 api
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
//        封装用户名 和 密码 因为在数据库存的时候不是存明文 此时传进去 不会传进明文密码 加密后的密码
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);

//        验证用户是否能够登陆 （账户和密码是否匹配) 如果登陆失败 会自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

//        api  getPrincipal 可以获取到代表当前用户的信息，
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

//        将用户封装成jwttoken
        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String, String> map = new HashMap<>();
        map.put("error_message","success");
//        "token" 和前端对应
        map.put("token",jwt);

        return map;
    }
}
