package com.weindependent.app.controller;

import com.weindependent.app.database.dataobject.DonateVolunteerDO;
import com.weindependent.app.database.mapper.weindependent.DonateVolunteerMapper;
import com.weindependent.app.dto.DonateVolunteerQry;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.vo.UploadedFileVO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/volunteer")
public class DonateVolunteerController {

    @Autowired
    private IFileService fileService;

    @Autowired
    private DonateVolunteerMapper donateVolunteerMapper;

    /**
     * Step 1: Upload resume file
     */
    @PostMapping(value = "/upload-resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadResume(@RequestPart("resume") MultipartFile resumeFile) {
        try {
            if (resumeFile == null || resumeFile.isEmpty()) {
                return ResponseEntity.badRequest().body("Resume file is required");
            }

            if (resumeFile.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().body("Resume file too large (max 5MB)");
            }

            UploadedFileVO uploadedFileVO = fileService.uploadFile(resumeFile, null, GoogleDriveFileCategoryEnum.DONATE_APPLICATION_RESUME);
            return ResponseEntity.ok(uploadedFileVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error uploading resume");
        }
    }

    /**
     * Step 2: Submit metadata + resume info
     */
    @PostMapping(value = "/apply", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submitApplication(@Validated @RequestBody DonateVolunteerQry application) {
        try {
            if (application.getResumeDriveId() == null || application.getResumeDriveId().isEmpty()) {
                return ResponseEntity.badRequest().body("Resume must be uploaded first");
            }

            DonateVolunteerDO volunteerDO = new DonateVolunteerDO();
            BeanUtils.copyProperties(application, volunteerDO);

            if (application.getInterests() != null) {
                volunteerDO.setInterests(String.join(",", application.getInterests()));
            }

            volunteerDO.setResumeDriveId(application.getResumeDriveId());
            volunteerDO.setResumeFileName(application.getResumeFileName());
            volunteerDO.setResumeUrl(application.getResumeUrl());
            volunteerDO.setCreateTime(LocalDateTime.now());

            donateVolunteerMapper.insert(volunteerDO);

            return ResponseEntity.ok("Application submitted successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing application");
        }
    }
}


