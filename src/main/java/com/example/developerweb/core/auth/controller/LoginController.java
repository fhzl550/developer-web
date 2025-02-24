package com.example.developerweb.core.auth.controller;

import com.example.developerweb.core.auth.dto.LoginDto;
import com.example.developerweb.core.auth.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/com")
public class LoginController {
    @Autowired
    LoginService loginService;

    @GetMapping("/login")
    public String login() {
        return "/com/login";
    }

    @GetMapping("/signup")
    public String signup() {
        return "/com/register";
    }


    //TODO : 비밀번호, 전화번호 암호화 공통 잡기(복호화)
    @ResponseBody
    @PostMapping("/signup")
    public int registerUser(@RequestBody LoginDto loginDto) {
        return loginService.registerUser(loginDto);
    }
}
