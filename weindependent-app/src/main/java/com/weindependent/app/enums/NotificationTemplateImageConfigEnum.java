package com.weindependent.app.enums;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationTemplateImageConfigEnum {

    GENERAL("general", Arrays.asList(
            new ImageSlot("banner", "顶部横幅图", 1),
            new ImageSlot("content", "正文插图", 3),
            new ImageSlot("icon", "图标图", 1)
    )),

    PROGRAMS_OR_FEATURES("programs_or_features", Arrays.asList(
            new ImageSlot("banner", "顶部横幅图", 1),
            new ImageSlot("content", "详情配图", 2)
    )),

    HOLIDAY("holiday", Arrays.asList(
            new ImageSlot("banner", "节日横幅", 1)
    )),
    
    MONTHLY_HIGHLIGHT("monthly_highlight", Arrays.asList(
            new ImageSlot("content", "正文插图", 1)
    ));


    private final String templateType;
    private final List<ImageSlot> imageSlots;

    @Getter
    @AllArgsConstructor
    public static class ImageSlot {
        private String imgType;
        private String position;
        private int count;
    }
}
