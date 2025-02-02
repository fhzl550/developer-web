package com.example.developerweb.core.auth.dao;

import com.example.developerweb.core.auth.dto.LoginDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginDao {

    public int registerUser(LoginDto loginDto);
}
