package com.weindependent.app.config;

import com.weindependent.app.enums.GoogleDriveFileCategoryEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Setter
@Component
@ConfigurationProperties(prefix = "google.drive.folder")
@Getter
public class GoogleDriveFolderProperties {
    private Map<GoogleDriveFileCategoryEnum, String> ids;

}
