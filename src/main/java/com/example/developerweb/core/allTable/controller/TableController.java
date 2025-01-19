package com.example.developerweb.core.allTable.controller;

import com.example.developerweb.common.dto.FileRequest;
import com.example.developerweb.common.utils.FileUtil;
import com.example.developerweb.core.allTable.dao.TableDao;
import com.example.developerweb.core.allTable.dto.TableDto;
import com.example.developerweb.core.allTable.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/tables")
public class TableController {
    @Autowired
    TableService tableService;
    @Autowired
    FileUtil fileUtil;
    @Autowired
    private TableDao tableDao;

    @GetMapping("/table")
    public String table() {
        return "/tables/tables";
    }
    /*
     * <pre>테이블 리스 출력 및 페이징</pre>
     *
     * @param tableDto
     * @param tablesName (검색어)
     * @param page       (내가 보고 있는 페이지 번호)
     * @param pageSize   (페이지당 게시물의 수)
     * @return List<TableDto>
     * @since 2024.11.17
     * @author Yun
     */
    @ResponseBody
    @PostMapping("/table")
    public Map<String, Object> getTables (@RequestBody Map<String, Object> req) {
        int page = (Integer) req.get("page");
        int pageSize = (Integer) req.get("pageSize");
        String searchKey = (String) req.get("searchKey");

        return tableService.getTablesService(page, pageSize, searchKey);
    }

    @ResponseBody
    @PostMapping("/register")
    public int registerTable(@RequestPart("reqDs") TableDto req,
                             @RequestPart("fileRequest") MultipartFile file) {
        return tableService.registerTable(req, file);
    }

    //detail의 경우 @RequestParam으로 받았어도 된다.
    @ResponseBody
    @PostMapping("/detail")
    public TableDto detailTable(@RequestBody Map<String, Object> reqDs) {
        int seq = (Integer) reqDs.get("tablesSeq");
        return tableService.getDetail(seq);
    }

    @ResponseBody
    @PostMapping("/delete")
    public int deleteTable(@RequestBody Map<String, Object> reqDs) {
        int seq = Integer.parseInt(reqDs.get("tablesSeq").toString());
        String orFilename = (String) reqDs.get("orFileName");
        log.info("seq : {}", seq);
        log.info("orFilename : {}", orFilename);
        return tableService.deleteTable(seq, orFilename);
    }
}
