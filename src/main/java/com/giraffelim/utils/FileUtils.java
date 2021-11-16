package com.giraffelim.utils;

import lombok.extern.java.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Log
public class FileUtils {

    private FileUtils() { }

    public static String uploadFile(String uploadPath, String originFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();
        String extension = originFileName.substring(originFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + savedFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName;
    }

    public static void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);

        if (deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
