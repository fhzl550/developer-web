package com.example.developerweb.core.allTable.dao;

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
 * 2024. 10. 18.           YUN                 memo
 */
@Mapper
public interface TableDao {
    //페이지 네이션
    public List<TableDto> getPageNation(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("searchKey") String searchKey);

    //총 데이터 수
    public Integer getAllTable(@Param("searchKey") String searchKey);

    //페이지 등록
    public int getTableInsert(TableDto tableDto);
}
