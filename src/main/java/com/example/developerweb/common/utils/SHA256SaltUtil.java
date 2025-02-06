package com.example.developerweb.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * packageName      : com.example.developerweb.common.utils
 * fileName         : SHA256SaltUtil
 * author           : YUN
 * date             : 2025. 2. 5.
 * description      :
 * ========================================================
 * MEMO
 * ---------------------------------------------------------
 * SHA-256 + 솔트를 이용하여 회원 가입 시 비밀번호 단방향 암호화 진행하는 클래스
 */
@Slf4j
@Component
public class SHA256SaltUtil {

    /**************************************************
     * methodName   : generateSalt
     * @role        : SHA-256 해싱을 위한 솔트 생성
     * @author      : Yun Usang
     * @since       : 2025.02.05
     * @return      : String
     *
     * @memo        : 회원가입 시 동일한 비밀번호로 가입 하더라도 랜덤 해시값을 주고 동일 방지
     **************************************************/
    public static String generateSalt() {
        //암호학적으로 안전한 난수를 생성하는 클래스
        SecureRandom random = new SecureRandom();
        //16바이트 길이의 예측 불가능한 난수를 생성 해서 salt 배열에 저장
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        //salt 바이트배열을 Base64 문자열로 변환
        return Base64.getEncoder().encodeToString(salt);
    }

    /**************************************************
     * methodName   : hashPassword
     * @role        : SHA-256 해싱 암호화
     * @author      : Yun Usang
     * @since       : 2025.02.06
     * @return      : String
     *
     * @memo        : SHA-256 해싱 암호화 로직
     *                digest() 를 이용하여 "솔트+비밀번호"를 하나로 처리하여 해시값 생성
     *                생성된 해시값을 byte b 에 하당 후 16진수로 변환
     **************************************************/
    public static String hashPassword(String password, String salt) {
        try {
            //데이터를 해시(Hash) 로 처리
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //해시값 생성 후 salt 를 추가 하는 과정(salt 란 랜덤으로 생성된 문자열)
            md.update(salt.getBytes());
            //digest() 메서드는 입력된 데이터를 해시 처리 후 byte[] 형태로 반환 한다.
            //digest()는 "솔트+비밀번호"를 하나로 처리하여 해시값 생성
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            //생성된 해시값을 하나씩 꺼내서 byte b 에 할당하고 각 바이트를 16진수로 변환
            for (byte b : hashedBytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
