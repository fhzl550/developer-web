package com.example.developerweb.common.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class FileRequest {
    private String fileName;                    //업로드 파일명
    private String filePath;                    //업로드 파일 경로
    private int fileSize;                       //업로드 파일 사이즈
    private String orFileName;                  //오리지널 파일명

    @Builder
    public FileRequest(String orFileName, String fileName, int fileSize, String filePath) {
        this.orFileName = orFileName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.filePath = filePath;
    }
}
