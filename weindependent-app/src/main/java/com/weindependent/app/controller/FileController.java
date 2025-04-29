package com.weindependent.app.controller;

import com.weindependent.app.dto.FileUploadQry;
import com.weindependent.app.service.FileService;
import com.weindependent.app.vo.UploadedFileVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name = "Google drive 文檔管理")
@Slf4j
@RestController
@RequestMapping(value = "api/file", produces = "application/json;charset=UTF-8")
public class FileController {
    @Resource
    private FileService fileService;

    @PostMapping( consumes="multipart/form-data")
    public UploadedFileVO uploadFile(@ModelAttribute @Valid FileUploadQry fileUploadQry) {
        if(!fileUploadQry.isValid()){
            throw new RuntimeException("Invalid fileuploadQry");
        }
        return fileService.uploadFile(fileUploadQry.getFile(),null,fileUploadQry.getCategory());
    }

    @DeleteMapping(consumes = "application/json;charset=UTF-8")
    public void deleteFile(@RequestParam("fileId") String fileId) {
        fileService.deleteFile(fileId);
    }

}
