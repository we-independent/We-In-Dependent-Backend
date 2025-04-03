package com.weindependent.app.aspect;

import com.weindependent.app.enums.ErrorCode;
import com.weindependent.app.exception.SignatureAuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Aspect
@Component
public class SignatureAuthAspect {
    private final static String SECRET_KEY = "Xw5SkQM6a4YR3bzVBKt68CsrM84WxUmH";
    public final static String[] SIGN_HEADERS = new String[]{
            "version",
            "timestamp",
            "sign"
    };

    public final static String VERSION = "version";
    public final static String TIMESTAMP = "timestamp";
    public final static String SIGN = "sign";

    /**
     * 签名验证  表示所有的带有SignValidate的注解
     */
    @Pointcut("@annotation(com.weindependent.app.annotation.SignatureAuth)")
    public void signatureAuth() {
    }

    @Around("signatureAuth()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        // 获取当前的Request的实体
        HttpServletRequest request = attributes.getRequest();
        Map<String, String> signHeader = getSignHeader(request);
        //校验header
        ErrorCode checkResult = checkHeader(signHeader);
        if (!ObjectUtils.isEmpty(checkResult)) {
            throw new SignatureAuthException(checkResult);
        }
        checkResult = verifySign(signHeader);
        if (!ObjectUtils.isEmpty(checkResult)) {
            throw new SignatureAuthException(checkResult);
        }

        return joinPoint.proceed();
    }

    /**
     * 获取header签名用的字段
     */
    public static Map<String, String> getSignHeader(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap();
        List<String> signList = Arrays.asList(SIGN_HEADERS);
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (signList.contains(headerName)) {
                String headerValue = request.getHeader(headerName);
                headerMap.put(headerName, headerValue);
            }
        }
        return headerMap;
    }

    /**
     * 校验header内参数
     *
     * @param signHeader header签名参数
     * @return CommonResponse 校验结果
     */
    private ErrorCode checkHeader(Map<String, String> signHeader) {
        if (!signHeader.containsKey(VERSION) || StringUtils
                .isBlank(signHeader.get(VERSION))) {
            return ErrorCode.NON_VERSION;
        }
        if (!signHeader.containsKey(TIMESTAMP) || StringUtils
                .isBlank(signHeader.get(TIMESTAMP))) {
            return ErrorCode.NON_TIMESTAMP;
        }
        if (!signHeader.containsKey(SIGN) || StringUtils
                .isBlank(signHeader.get(SIGN))) {
            return ErrorCode.NON_SIGN;
        }
        return null;
    }

    private ErrorCode verifySign(Map<String, String> signHeader) {
        String sign = getSign(signHeader.get(VERSION), signHeader.get(TIMESTAMP));
        if (StringUtils.isBlank(sign)) {
            return ErrorCode.NON_SIGN;
        }
        if (!sign.equals(signHeader.get(SIGN))) {
            return ErrorCode.SIGN_AUTH_FAILED;
        }
        return null;
    }

    private String getSign(String version, String timestamp) {
        return DigestUtils.md5Hex(version+timestamp+SECRET_KEY);
    }
}
