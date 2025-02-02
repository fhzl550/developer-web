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
     * @since       : 2025.02.01
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

    /**************************************************
     * methodName   : registerTable
     * @role        : 테이블 등록
     * @author      : Yun Usang
     * @since       : 2025.02.02
     * @return      : int
     * @param       : tableDto(내가 등록하는 데이터),
     *                file(내가 등록하는 파일),
     *
     * @memo        : fileUtil 의 파일 등록 공통을 사용
     *                fileRequest 객체로 변환 후 tableDto 에
     *                있는 fileRequest 객체에 set하여 DB 등록
     **************************************************/
    public int registerTable(TableDto tableDto, MultipartFile file) {
        FileRequest fileRequest = fileUtil.uploadFile(file, "table");
        tableDto.setFileRequest(fileRequest);
        return tableDao.getTableInsert(tableDto);
    }

    /**************************************************
     * methodName   : getDetail
     * @role        : 테이블 상세보기
     * @author      : Yun Usang
     * @since       : 2025.02.02
     * @return      : TableDto
     * @param       : seq(상세보기 시퀀스),
     *
     * @memo        : 반환 타입을 TableDto 가 아닌 Map 타입으로
     *                가져왔어도 됬다 파라미터로 seq만 가져오면 되기 때문에
     **************************************************/
    public TableDto getDetail(int seq) {
        TableDto tableDto = tableDao.getTableDetail(seq);
        FileRequest fileRequest = tableDao.getFileDetail(seq);
        if(fileRequest != null) {
            tableDto.setFileRequest(fileRequest);
        }
        return tableDto;
    }

    /**************************************************
     * methodName   : deleteTable
     * @role        : 테이블 삭제
     * @author      : Yun Usang
     * @since       : 2025.02.02
     * @return      : int
     * @param       : seq(삭제를 위한 시퀀스),
     *                orFilename(파일의 삭제를 위한 파라미터)
     *
     * @memo        : fileUtil 에서 삭제여부를 판단하기 위하여
     *                fileRequest 에 isCompleted 라는 것을 만들어
     *                성공이면 true 실패이면 false를 반환
     **************************************************/
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

    /**************************************************
     * methodName   : updateTable
     * @role        : 테이블 수정
     * @author      : Yun Usang
     * @since       : 2025.02.02
     * @return      : int
     * @param       : tableDto(테이블 수정에대한 정보),
     *                file(파일의 수정을 위한 정보),
     *                originalFile(기존파일 삭제 후 새 파일 등록을 위한 정보)
     *
     * @memo        : fileUtil의 updateFile 공통을 사용하여
     *                파일을 수정 한다. 수정한 파일을 fileRequest 객체로 변환하여
     *                DB에 등록 할 수 있게 한다.
     **************************************************/
    public int updateTable(TableDto tableDto, MultipartFile file, String originalFile) {
        FileRequest fileRequest = fileUtil.updateFile(file, "table", originalFile);
        tableDto.setFileRequest(fileRequest);
        return tableDao.getTableUpdate(tableDto);
    }
}
