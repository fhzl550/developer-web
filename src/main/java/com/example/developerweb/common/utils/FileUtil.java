package com.example.developerweb.common.utils;

import com.example.developerweb.common.dto.FileRequest;
import com.example.developerweb.core.allTable.dao.TableDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.UUID;

/**
 * packageName      : com.example.developerweb.common.utils
 * fileName         : FileUtil
 * author           : YUN
 * date             : 2025. 02. 02.
 * description      :
 * ========================================================
 * DATE             MEMO
 * ---------------------------------------------------------
 * 2025. 02. 02.    파일 등록,수정,삭제 공통 처리 클래스
 */

@Slf4j
@Component
public class FileUtil {
    private final TableDao tableDao;
    @Value("${file.upload-path}")
    private String uploadService;

    public FileUtil(TableDao tableDao) {
        this.tableDao = tableDao;
    }

    //중복 파일명 방지 파일명 변경 하고 업로드
    public static String uniqueFileName(String originalFilename) {
        int doIndex = originalFilename.lastIndexOf(".");                    //파일확장자 앞에 이름의 개수를 추출
        String baseName = originalFilename.substring(0, doIndex);               //파일 이름만 추출
        String extension = originalFilename.substring(doIndex);                 //파일 확장자만 추출

        String uuId = UUID.randomUUID().toString().substring(0, 8);
        return baseName + "_" + uuId + extension;
    }

    /**************************************************
     * @methodName   : uploadFile
     * @role        : 파일 등록을 위한 공통 로직
     * @author      : Yun Usang
     * @since       : 2025.02.02
     * @return      : FileRequest
     * @param       : file(등록하고자 하는 파일의 정보),
     *                subPath(중간 폴더 명)
     *
     * @memo        : 반환 타입이 FileRequest 인것은 파일 업로드 후
     *                관련 정보를 담아 반환 하기 위함 이다.
     **************************************************/
    public FileRequest uploadFile(MultipartFile file, String subPath) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
        }

        //Mac 에서 파일의 원본 이름 구하기(윈도우와 맥의 한글 처리 방식이 다름)
        String originFilename = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);

        //업로드 파일명
        String saveFile =uniqueFileName(originFilename);
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
                .orFileName(originFilename)
                .fileName(saveFile)
                .filePath(filePath)
                .fileSize((int) file.getSize())
                .build();
    }

    /**************************************************
     * @methodName   : deleteTable
     * @role        : 파일 삭제 로직 처리
     * @author      : Yun Usang
     * @since       : 2025.02.02
     * @return      : FileRequest
     * @param       : orFileNm(원본 파일이름),
     *                subPath(중간 폴더 명)
     *
     * @memo        : 반환 타입을 void 가 아니라 FileRequest로 설정
     *                게시판에서 파일을 삭제 하고 마는게 아니라
     *                파일의 경로를 추적하여 해당 폴더의 파일을 삭제 하기 위함
     *                getOrFileNm 을 통해 폴더에 업로드 되어 있는 파일의 이름을 찾음
     **************************************************/
    public FileRequest deleteTable(String orFileNm, String subPath) {
        FileRequest fileRequest = new FileRequest();
        String uuIdFileNm = tableDao.getOrFileNm(orFileNm);

        String filePath = Paths.get(uploadService, subPath, uuIdFileNm).toString();
        File targetFile = new File(filePath);

        /*
        * fileRequest에 set으로 삭제한 파일의 데이터를 넣는 이유는
        * 1. 삭제한 파일들의 데이터를 확인하기 위해서(삭제된 파일이 어떤 파일이였는지 추적하기 위해)
        * 2. UI 피드백 제공을 위해 사용(삭제 작업 후, 사용자에게 삭제된파일의 이름을 반환 하여 명확한 피드백 제공)
        * */
        fileRequest.setFilePath(filePath);
        fileRequest.setOrFileName(orFileNm);
        fileRequest.setCompleted(false);

        //조건 자체에서 파일 삭제를 시도 한다(targetFile.delete() 자체가 파일 삭제를 시도 하는 로직임)
        if(targetFile.exists() && targetFile.delete()) {
            fileRequest.setCompleted(true);
        } else {
            fileRequest.setCompleted(false);
            log.error("파일 삭제가 진행되지 않았습니다.:{}",fileRequest.isCompleted());
        }

        return fileRequest;
    }

    /**************************************************
     * @methodName  : updateFile
     * @role        : 파일 수정 로직처리
     * @author      : Yun Usang
     * @since       : 2025.02.02
     * @return      : FileRequest
     * @param       : originalFile(수정하기전에 원본 파일명, 삭제를 위한 파일명),
     *                subPath(중간 폴더 명),
     *                file(수정 해서 업로드하는 파일의 정보)
     *
     * @memo        : 반환 타입을 void 가 아니라 FileRequest로 설정
     *                FileRequest 에 각각의 맞는 DB를 저장해주기 위함
     **************************************************/
    public FileRequest updateFile(MultipartFile file, String subPath, String originalFile) {
        //경로에 저장되어 있는 변경된 파일명을 가져와서 원본 파일명과 서치 해서 원본 파일명 가져오기
        String uuidFileNm = tableDao.getOrFileNm(originalFile);
        //이미지 업로드 되어 있는 해당 파일 찾기
        String filePath = Paths.get(uploadService, subPath, uuidFileNm).toString();
        //파일을 삭제 하기 위해서 File 객체를 사용
        File targetFile = new File(filePath);

        if(!file.isEmpty()) {
            //exists 는 파일을 발견하면 true 파일을 찾을 수 없으면 false 반환
            if(targetFile.exists()) {
                targetFile.delete();
            }
        }

        //Mac 에서 파일의 원본 이름 구하기(윈도우와 맥의 한글 처리 방식이 다름) / 업로드된 파일의 원본 파일명 가져오기
        String originFilename = Normalizer.normalize(file.getOriginalFilename(), Normalizer.Form.NFC);
        //업로드 파일명으로 변환 UUID 사용(중복 업로드 방지)
        String saveFile =uniqueFileName(originFilename);
        //신규로 업로드한 파일의 업로드 경로를 가져옴
        String newFilepath = Paths.get(uploadService, subPath, saveFile).toString();
        File newFile = new File(newFilepath);
        try {
            //mkdirs() : C 디렉토리가 없으면 하위 디렉토리 생성 불가
            newFile.getParentFile().mkdirs();
            file.transferTo(newFile);
        } catch (Exception e) {
            throw new RuntimeException("파일 업로드중 오류가 발생하였습니다.",e);
        }

        return FileRequest.builder()
                .orFileName(originFilename)
                .fileName(saveFile)
                .filePath(newFilepath)
                .fileSize((int) file.getSize())
                .build();
    }
}
