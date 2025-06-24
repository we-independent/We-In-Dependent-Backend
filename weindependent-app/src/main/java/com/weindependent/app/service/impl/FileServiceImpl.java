package com.weindependent.app.service.impl;
import javax.annotation.PostConstruct;

import com.google.api.client.http.ByteArrayContent;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.weindependent.app.config.GoogleDriveFolderProperties;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.vo.UploadedFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements IFileService {
    @Value("${google.drive.app-name}")
    private String appName;

    @Autowired
    public GoogleDriveFolderProperties folderProps;

    private Drive drive;

    @PostConstruct
    private void initDrive() {
        try {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            InputStream configFileStream = FileServiceImpl.class.getClassLoader()
                    .getResourceAsStream("secret/google-drive-config.json");
            this.drive = new Drive.Builder(httpTransport, new GsonFactory(), fromServiceAccount(configFileStream))
                    .setApplicationName(appName)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Google Drive", e);
        }
    }



    // 一些scope权限
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_APPDATA,DriveScopes.DRIVE,DriveScopes.DRIVE_METADATA,DriveScopes.DRIVE_READONLY,DriveScopes.DRIVE_METADATA_READONLY,DriveScopes.DRIVE_SCRIPTS);

    @Override
    public UploadedFileVO uploadFile(MultipartFile file, String fileName, GoogleDriveFileCategoryEnum category) {
        String mimeType = file.getContentType();
        ByteArrayContent byteArrayContent;
        try{
            byteArrayContent = new ByteArrayContent(mimeType, file.getBytes());
        } catch (Exception e){
            log.error("Failed to read file: " + fileName, e);
            throw new RuntimeException("Failed to read file: "+ fileName);
        }


        // Generate random file name if fileName is null or empty
        if (fileName == null || fileName.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename != null && originalFilename.contains(".") ?
                    originalFilename.substring(originalFilename.lastIndexOf('.') + 1) : "bin";
            String baseName = originalFilename != null && originalFilename.contains(".") ?
                    originalFilename.substring(0, originalFilename.lastIndexOf('.')) : "file";
            fileName = baseName + "_" + System.currentTimeMillis() + "." + suffix;
        }

        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        String parentFolderId = folderProps.getIds().get(category);
        if (parentFolderId == null) {
            throw new RuntimeException("Invalid category: no folder mapped for category " + category);
        }
        fileMetadata.setParents(Collections.singletonList(parentFolderId));

        File uploadedFile;
        try{
            uploadedFile = drive.files()
                    .create(fileMetadata, byteArrayContent)
                    .setFields("id,name")
                    .execute();
        }
        catch (IOException e){
            throw new RuntimeException("Failed to upload file to google drive", e);
        }


        log.info("upload FolderId: {} FileId: {}, FileName: {}", parentFolderId, uploadedFile.getId(), uploadedFile.getName());
        UploadedFileVO uploadedFileVO = new UploadedFileVO();
        uploadedFileVO.setFileKey(uploadedFile.getId());
        uploadedFileVO.setFileName(uploadedFile.getName());
        uploadedFileVO.setFilePath("https://cdn.weindependent.org/image/" + uploadedFile.getId());
        fileMetadata.setMimeType(mimeType);
        return uploadedFileVO;
    }



    @Override
    @Async
    public void deleteFile(String fileId) {
        try {
            drive.files().delete(fileId).execute();
            log.info("Deleted file from Google Drive: {}", fileId);
        } catch (IOException e) {
            log.error("Failed to delete file from Google Drive", e);
            throw new RuntimeException("Failed to delete file from Google Drive");
        }
    }


    /**
     * 校验认证权限
     * @param configJsonFile
     * @return
     * @throws IOException
     */

    private static HttpRequestInitializer fromServiceAccount(InputStream configJsonFile) throws IOException {
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(configJsonFile).createScoped(SCOPES);
        return new HttpCredentialsAdapter(credentials);
    }


    /**
     * 暴露接口查重
     */

    @Override
    public Drive getDrive() {
        return this.drive;
    }

}