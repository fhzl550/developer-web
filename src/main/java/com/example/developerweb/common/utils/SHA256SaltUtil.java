package com.example.developerweb.common.utils;

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
}
