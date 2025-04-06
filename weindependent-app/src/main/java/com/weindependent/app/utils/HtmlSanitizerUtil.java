package com.weindependent.app.utils;

public class HtmlSanitizerUtil {
    /**
     * 清洗 HTML，防止非法 XML 实体导致 PDF 渲染失败
     * 例如：把 "&R" 转换为 "&amp;R"，保留 &nbsp; 等合法实体
     */
    public static String cleanHtmlForXml(String html) {
        if (html == null) return null;
        return html.replaceAll("&(?![a-zA-Z]{2,6};)", "&amp;");
    }
}