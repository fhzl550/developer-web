package com.example.developerweb.core.allTable.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class PageDto {
    private int page;                           //현재 페이지
    private int pageSize;                       //한 페이지에 표시할 데이터 수
    private int totalCount;                     //전체 데이터 수
    private int totalPages;                     //전체 페이지 수
    private String tablesName;                  //담당자 이름

    public PageDto (int page, int pageSize, int totalCount) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.totalPages = (int) Math.ceil((double) totalCount / pageSize);

    }
}
