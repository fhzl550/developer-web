package com.example.developerweb.core.allTable.service;

import com.example.developerweb.common.dto.FileRequest;
import com.example.developerweb.common.utils.FileUtil;
import com.example.developerweb.core.allTable.dao.TableDao;
import com.example.developerweb.core.allTable.dto.PageDto;
import com.example.developerweb.core.allTable.dto.TableDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class TableService {
    @Autowired
    TableDao tableDao;
    @Autowired
    FileUtil fileUtil;
    
    /**************************************************
     * methodName   : getTablesService
     * @role        : 테이블 리스트 출력 및 페이징
     * @author      : Yun Usang
     * @since       : 2025.02.10
     * @return      : Map<String, Object>
     * @param       : page(내가 보고 있는 페이지 번호),
     *                pageSize(페이지당 게시물의 수),
     *                searchKey(검색어)
     *
     * @memo        : Dto 타입을 반환 하지 않고 특정한 데이터만 전달 하기 위해 Map 타입 사용
     **************************************************/
    public Map<String, Object> getTablesService(int page, int pageSize, String searchKey) {
        /*
         * offset 계산
         * page(내가 보고 있는 페이지) = 2 페이지
         * pageSize(페이지 마다 보여 줄 데이터의 수) = 10 개
         * (3 - 1) * 10 = 20 일때 20번 데이터 부터 30번 데이터 까지 보여준다.
         */
        int offset = (page - 1) * pageSize;
        String search = "";

        if(searchKey.isEmpty() || searchKey.length() < 1) {
            search = "";
        } else {
            search = searchKey;
        }
        //데이터 조회
        List<TableDto> items = tableDao.getPageNation(offset, pageSize, search);
        //총 데이터의 수 조회
        int totalCount = tableDao.getAllTable(search);
        //페이지 정보 생성
        PageDto pageInfo = new PageDto(page, pageSize, totalCount);

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);         //리스트 출력 객체
        result.put("pageInfo", pageInfo);   //페이지 네이션 객체

        return result;
    }

    public int registerTable(TableDto tableDto, MultipartFile file) {
        FileRequest fileRequest = fileUtil.uploadFile(file, "table");
        tableDto.setFileRequest(fileRequest);
        return tableDao.getTableInsert(tableDto);
    }

    public TableDto getDetail(int seq) {
        TableDto tableDto = tableDao.getTableDetail(seq);
        FileRequest fileRequest = tableDao.getFileDetail(seq);
        if(fileRequest != null) {
            tableDto.setFileRequest(fileRequest);
        }
        return tableDto;
    }

    public int deleteTable(int seq, String orFilename) {
        FileRequest fileRequest = fileUtil.deleteTable(orFilename, "table");
        //Dto 파일 성공 여부 : default-false
        if(!fileRequest.isCompleted()) {
            throw new RuntimeException("파일 삭제 실패:" + orFilename);
        }
        TableDto tableDto = tableDao.getTableDetail(seq);
        if(tableDto == null) {
            throw new RuntimeException("삭제할 게시판 글이 존재 하지 않습니다.");
        }
        return tableDao.getTableDelete(seq);
    }

    public int updateTable(TableDto tableDto, MultipartFile file, String originalFile) {
        FileRequest fileRequest = fileUtil.updateFile(file, "table", originalFile);
        tableDto.setFileRequest(fileRequest);
        return tableDao.getTableUpdate(tableDto);
    }
}
