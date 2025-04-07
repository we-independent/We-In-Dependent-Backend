package com.weindependent.app.service.impl;

import cn.hutool.core.io.FileUtil;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.weindependent.app.enums.MineTypeOnGoogleDriveEnum;
import com.weindependent.app.service.FileService;
import com.weindependent.app.vo.UploadedFileVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Value("${google.drive.app-name}")
    private String appName;

    @Value("${google.drive.folder.parent-id}")
    private String parentFolderId;

    @Value("${google.drive.folder.blog-id}")
    private String blogFolderId;

    @Value("${file.upload.tmp-folder}")
    private String tmpFolder;

    // 一些scope权限
    private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE_APPDATA,DriveScopes.DRIVE,DriveScopes.DRIVE_METADATA,DriveScopes.DRIVE_READONLY,DriveScopes.DRIVE_METADATA_READONLY,DriveScopes.DRIVE_SCRIPTS);

    @Override
    public UploadedFileVO uploadFile(MultipartFile file, String category) {
        try {
            // 0.构建http传输
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // 1.根据json配置文件，构建输入流
            InputStream configFileStream = FileServiceImpl.class.getClassLoader().getResourceAsStream("secret/google-drive-config.json");

            // 2.创建Drive实例
            Drive drive = new Drive.Builder(httpTransport, new GsonFactory(), fromServiceAccount(configFileStream))
                    .setApplicationName(appName)
                    .build();

            // 3.指定上传文件的路径
//            java.io.File filePath = new java.io.File("/Users/libo/Downloads/音符.png");
            java.io.File filePath = new java.io.File(tmpFolder+"/"+file.getOriginalFilename());
            FileCopyUtils.copy(file.getBytes(), filePath);
            String suffix = FileUtil.getSuffix(filePath);
            String filename = FileUtil.getName(filePath);

            // 4.构建文件元数据：文件名，父目录，文件的mineType
            File fileMetadata = new File();
            fileMetadata.setName(filename+Base64.getEncoder().encodeToString(String.valueOf(System.currentTimeMillis()).getBytes("UTF-8")) + suffix);
            String targetFolderId = getFolderId(category);
            fileMetadata.setParents(Collections.singletonList(targetFolderId));
//            fileMetadata.setParents(Collections.singletonList(parentFolderId));
            String mineType = findMineTypeBySuffix(suffix);
            FileContent mediaContent = new FileContent(mineType, filePath);


            // 5.上传文件
            File uploadedFile = drive.files().create(fileMetadata, mediaContent)
                    .setFields("id,name")
                    .execute();

            log.info("upload FolderId: {} FileId: {}, FileName: ", targetFolderId, uploadedFile.getId(), uploadedFile.getName());

            String uploadedfilePath = uploadedFile.getWebViewLink();
            UploadedFileVO uploadedFileVO = new UploadedFileVO();
            uploadedFileVO.setFileName(filename);
            uploadedFileVO.setFileType(suffix);
            uploadedFileVO.setFilePath(uploadedfilePath);

            //@TODO 删除源文件


            return uploadedFileVO;
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取Google文件夹ID
     * @param category
     * @return
     */
    private String getFolderId(String category) {
        if (category.equals("blog")) {
            return blogFolderId;
        }
        return parentFolderId;
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
     * 根据后缀名获取文件类型
     * @param suffix
     * @return
     */
    public static String findMineTypeBySuffix(String suffix) {
        for (MineTypeOnGoogleDriveEnum type : MineTypeOnGoogleDriveEnum.values()) {
            if (type.getSuffix().equalsIgnoreCase(suffix)) {
                return type.getFileType();
            }
        }
        return "Unknown mine type";
    }
}