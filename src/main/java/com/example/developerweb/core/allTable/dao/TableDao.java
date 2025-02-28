package com.example.developerweb.core.allTable.dao;

import com.example.developerweb.common.dto.FileRequest;
import com.example.developerweb.core.allTable.dto.TableDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * packageName      : com.example.developerweb.core.dao
 * fileName         : TableDao
 * author           : YUN
 * date             : 2024. 10. 18.
 * description      :
 * ========================================================
 * DATE             AUTHOR              MEMO
 * ---------------------------------------------------------
 * 2024. 10. 18.           YUN
 */
@Mapper
public interface TableDao {
    //페이지 네이션
    public List<TableDto> getPageNation(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("searchKey") String searchKey);

    //총 데이터 수
    public Integer getAllTable(@Param("searchKey") String searchKey);

    //페이지 등록
    public int getTableInsert(TableDto tableDto);

    //페이지 상세보기
    public TableDto getTableDetail(@Param("tablesSeq") int seq);

    public FileRequest getFileDetail(@Param("tablesSeq") int seq);

    //페이지 삭제(폴더 파일 삭제)
    public int getTableDelete(@Param("tablesSeq") int seq);

    //업로드 파일명에 맞는 원본 파일명 찾기
    public String getOrFileNm(@Param("orFileName") String orFileNm);

    public int getTableUpdate(TableDto tableDto);
}
