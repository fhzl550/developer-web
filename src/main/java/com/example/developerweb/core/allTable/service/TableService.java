package com.example.developerweb.core.allTable.service;

import com.example.developerweb.common.dto.FileRequest;
import com.example.developerweb.common.utils.FileUtil;
import com.example.developerweb.core.allTable.controller.TableController;
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
import java.util.Objects;

@Slf4j
@Service
public class TableService {
    @Autowired
    TableDao tableDao;
    @Autowired
    FileUtil fileUtil;

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

    //TODO : [25.01.05]파일 보여주는 부분 확실하게 공부하기 특히 어떻게 가져와서 어떻게 변환 했는지를 중점으로 공부히기
    public TableDto getDetail(int seq) {
        TableDto tableDto = tableDao.getTableDetail(seq);
        FileRequest fileRequest = tableDao.getFileDetail(seq);
        if(fileRequest != null) {
            tableDto.setFileRequest(fileRequest);
        }
        return tableDto;
    }
}
