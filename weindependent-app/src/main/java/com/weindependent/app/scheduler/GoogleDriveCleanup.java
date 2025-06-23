package com.weindependent.app.scheduler;

import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import com.weindependent.app.config.GoogleDriveFolderProperties;
import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import com.weindependent.app.service.IFileService;
import com.weindependent.app.database.mapper.dashboard.DashboardEventMapper; // Assume this queries your event.banner_id
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import javax.annotation.Resource;

@Slf4j
@Component
public class GoogleDriveCleanup {

    @Resource
    private IFileService fileService;

    @Autowired
    public GoogleDriveFolderProperties folderProps;

    @Resource
    private DashboardEventMapper dashboardEventMapper;

    @Scheduled(cron = "0 0 3 * * ?") // Runs daily at 3 AM
    public void cleanUnusedFiles() {
        try {
            Set<String> usedFileIds = new HashSet<>(dashboardEventMapper.getAllBannerUrls());
            String eventFolderParentId = folderProps.getIds().get(GoogleDriveFileCategoryEnum.EVENT_BANNER);

            List<File> files = fileService.getDrive().files()
                    .list()
                    .setQ(String.format("'%s' in parents and trashed = false", eventFolderParentId))
                    .setFields("files(id,name,createdTime)")
                    .execute()
                    .getFiles();

            Instant tenDaysAgo = Instant.now().minus(java.time.Duration.ofDays(10));

            for (File file : files) {
                if (!usedFileIds.contains("https://cdn.weindependent.org/image/"+file.getId())) {
                    long createdMillis = file.getCreatedTime().getValue();
                    if (Instant.ofEpochMilli(createdMillis).isBefore(tenDaysAgo)) {
                        log.info("Marked for deletion: {}", file.getName());
                        fileService.deleteFile(file.getId());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to clean up unused Google Drive files", e);
        }
    }
}