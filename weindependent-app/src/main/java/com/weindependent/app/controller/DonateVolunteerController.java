package com.weindependent.app.controller;

import com.weindependent.app.convertor.DonateVolunteerConverter;
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

    @Autowired
    private DonateVolunteerConverter donateVolunteerConverter;

    /**
     * Step 1: Upload resume file
     */
    @PostMapping(value = "/upload-resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadedFileVO uploadResume(@RequestPart("resume") MultipartFile resumeFile) {
        if (resumeFile == null || resumeFile.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        if (resumeFile.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("Resume file too large (max 5MB)");
        }

        return fileService.uploadFile(
                resumeFile,
                null,
                GoogleDriveFileCategoryEnum.DONATE_VOLUNTEER_RESUME
        );
    }

    /**
     * Step 2: Submit metadata + resume info
     */
    @PostMapping(value = "/apply", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String submitApplication(@Validated @RequestBody DonateVolunteerQry application) {

        DonateVolunteerDO volunteerDO = donateVolunteerConverter.toDO(application);
        donateVolunteerMapper.insert(volunteerDO);

        return "Application submitted successfully";
    }
}


