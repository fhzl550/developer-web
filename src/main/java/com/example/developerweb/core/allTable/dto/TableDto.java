package com.example.developerweb.core.allTable.dto;

import com.example.developerweb.common.dto.FileRequest;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TableDto {
    private int tablesSeq;                      //데이터 행 고유번호
    private String tablesName;                  //담당자 이름
    private String tablesPosition;              //담당자 업무
    private String tablesOffice;                //담당자 근무지
    private int tablesAge;                      //담당자 나이
    private Timestamp tablesStartDate;          //담당자 입사 날짜
    private int tablesSalary;                   //담당자
    /*private FileRequest fileRequest;*/            //파일 정보 객체
}
