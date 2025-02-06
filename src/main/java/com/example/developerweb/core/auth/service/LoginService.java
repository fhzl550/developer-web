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

        /*
        * TODO : salt 를 따로 DB 에 저장
        * 로그인 시 salt 와 해싱된 비밀번호를 함쳐서 비교하고 로그인 시키기
        * */
        loginDto.setUserSalt(salt);
        loginDto.setUserPassword(hashedPassword);

        return loginDao.registerUser(loginDto);
    }
}
