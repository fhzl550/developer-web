package com.example.developerweb.core.auth.service;

import com.example.developerweb.common.utils.SHA256SaltUtil;
import com.example.developerweb.core.auth.dao.LoginDao;
import com.example.developerweb.core.auth.dto.LoginDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {
    @Autowired
    LoginDao loginDao;

    public int registerUser(LoginDto loginDto) {
        String password = loginDto.getUserPassword();
        String salt = SHA256SaltUtil.generateSalt();

        String hashedPassword = SHA256SaltUtil.hashPassword(password, salt);

        loginDto.setUserSalt(salt);
        loginDto.setUserPassword(hashedPassword);

        return loginDao.registerUser(loginDto);
    }
}
