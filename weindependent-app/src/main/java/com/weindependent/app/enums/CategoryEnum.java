package com.weindependent.app.enums;

public enum CategoryEnum {
    VISA("1", "Visa & Policy"),
    LIVING("2", "US Living Guide"),
    HEALTH("3", "Health & Wellness"),
    CAREER("4", "Career Compass"),
    COMMUNITY("7", "Community Story");//预留

    private final String code;
    private final String name;

    CategoryEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }

    public static String getNameByCode(String code) {
        for (CategoryEnum c : values()) {
            if (c.code.equals(code)) {
                return c.name;
            }
        }
        return COMMUNITY.name;
    }
    
    public static String getCodeByName(String name) {
        for (CategoryEnum c : values()) {
            if (c.name.equals(name)) {
                return c.code;
            }
        }
        return COMMUNITY.code; // 默认返回 COMMUNITY 的 code
    }
}