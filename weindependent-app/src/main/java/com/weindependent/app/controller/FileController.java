package com.weindependent.app.controller;

import com.weindependent.app.dto.FileUploadQry;
import com.weindependent.app.service.FileService;
import com.weindependent.app.vo.UploadedFileVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Tag(name = "文件管理")
@Slf4j
@RestController
@RequestMapping("/api/upload")
//@RequestMapping(value = "/file", consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
public class FileController {
    @Resource
    private FileService fileService;


    @PostMapping(consumes = "multipart/form-data")
//    public ResponseEntity<R<String>> uploadFile(@Valid @ModelAttribute FileUploadQry qry) {
//    public String uploadFile(@Valid @ModelAttribute FileUploadQry qry) {
    public String uploadFile(FileUploadQry qry) {
        log.info("qry is {}", qry);
        // 基础校验（@Valid 需要结合校验注解使用）
        if (qry.getFile().isEmpty()) {
            return "";
//            return ResponseEntity.badRequest().body(R.error("文件不能为空"));
        }
        return "";
//        return ResponseEntity.ok(R.ok("文件上传成功"));
    }

    // 统一响应结构示例
    public static class R<T> {
        private int code;
        private String msg;
        private T data;

        public R(int i, String 成功, T data) {
        }

        public static <T> R<T> ok(T data) {
            return new R<>(200, "成功", data);
        }

        public static <T> R<T> error(String msg) {
            return new R<>(500, msg, null);
        }

        // 构造方法和 getter/setter 省略
    }

//    @PostMapping(value = "/file/upload", consumes= MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value="/upload", consumes= "multipart/form-data")
//    @PostMapping(value="/upload")
//    public String uploadFile(FileUploadQry fileUploadQry) {
//    public String uploadFile(String category) {
//    public String uploadFile(@RequestParam("file") MultipartFile file) {
//    public String uploadFile() {
    public UploadedFileVO uploadFile1(FileUploadQry fileUploadQry) {
//    public String uploadFile(@RequestBody FileUploadQry fileUploadQry, HttpServletRequest request) {
//    public String uploadFile(MultipartFile file, String category, HttpServletRequest request) {
        MultipartFile file = fileUploadQry.getFile();
        String category = fileUploadQry.getCategory();
        log.info("file is {}", file);
        log.info("category is {}", category);
//        return "";
//        log.info("{}, {}", file, category);
//        MultipartFile file = new MultipartFile() {
//            @Override
//            public String getName() {
//                return null;
//            }
//
//            @Override
//            public String getOriginalFilename() {
//                return null;
//            }
//
//            @Override
//            public String getContentType() {
//                return null;
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public long getSize() {
//                return 0;
//            }
//
//            @Override
//            public byte[] getBytes() throws IOException {
//                return new byte[0];
//            }
//
//            @Override
//            public InputStream getInputStream() throws IOException {
//                return null;
//            }
//
//            @Override
//            public void transferTo(File dest) throws IOException, IllegalStateException {
//
//            }
//        };
//        String category = "blog";
        return fileService.uploadFile(file, category);
    }
}
