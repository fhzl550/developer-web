package com.example.developerweb.common.utils;

import com.example.developerweb.common.dto.FileRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Component
public class FileUtil {
    @Value("${file.upload-path}")
    private String uploadService;


    /* service.propertise에 기본 경로 저장 */
    private String uploadPath;

    @PostConstruct
    public void init() {
        // 유효성 검사
        if (uploadService == null || uploadService.isEmpty()) {
            throw new IllegalArgumentException("file.upload-path 설정이 누락되었습니다.");
        }

        // 경로 설정
        uploadPath = Paths.get(uploadService).toString();

        log.info("파일 업로드 경로: {}", uploadPath);
    }

    //중복 파일명 방지
    //TODO : UUID 를 사용하고 나서는 DB 에 파일이름,파일경로,파일크기, 파일 원본 이름이 안넘어감
    public static String uniqueFileName(String originalFilename) {
        int doIndex = originalFilename.lastIndexOf(".");                    //파일확장자 앞에 이름의 개수를 추출
        String baseName = originalFilename.substring(0, doIndex);               //파일 이름만 추출
        String extension = originalFilename.substring(doIndex);                 //파일 확장자만 추출

        String uuId = UUID.randomUUID().toString().substring(0, 8);
        return baseName + "_" + uuId + extension;
    }

    LocalDate now = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    String date = now.format(formatter);
    /*
    * 단일 파일 업로드
    * @param multipartFile - 파일 객체
    * @return DB에 저장할 파일 정보
    */
    public FileRequest uploadFile(MultipartFile file, String subPath) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
        }
        //MultipartFile 형식의 파일 확장자 구하기
        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        //Mac 에서 파일의 원본 이름 구하기(윈도우와 맥의 한글 처리 방식이 다름)
        String originFilename = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);
        //파일 이름에서 "."만 띄어낸 숫자를 구함
        int indexOf = originFilename.lastIndexOf(".");
        //파일의 이름만 구하기
        String fileNm = originFilename.substring(0, indexOf);

        //업로드 파일명
        String saveFile = fileNm + "_" + date + "." + fileExtension;
        //filepath 경로 : service.propertise + 파일 업로드 시 서브 패스 + 파일 이름
        String filePath = Paths.get(uploadService, subPath, saveFile).toString();
        // filePath의 경로에 파일 저장 경로를 생성하기 위해 사용
        File fileUpload = new File(filePath);

        try {
            //mkdirs() : C 디렉토리가 없으면 하위 디렉토리 생성 불가
            fileUpload.getParentFile().mkdirs();
            //fileUpload 경로에 파일 저장
            file.transferTo(fileUpload);
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드중 오류가 발생 했습니다.",e);
        }
        //FileReqeust Dto로 반환 / Builder 패턴
        return FileRequest.builder()
                .orFileName(file.getOriginalFilename())
                .fileName(saveFile)
                .filePath(filePath)
                .fileSize((int) file.getSize())
                .build();
    }
}
