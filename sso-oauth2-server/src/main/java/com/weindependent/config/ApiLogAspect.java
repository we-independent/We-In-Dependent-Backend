package com.weindependent.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;


//使用@Aspect注解将一个java类定义为切面类
//使用@Pointcut定义一个切入点，可以是一个规则表达式，比如下例中某个package下的所有函数，也可以是一个注解等。
//根据需要在切入点不同位置的切入内容
//使用@Before在切入点开始处切入内容
//使用@After在切入点结尾处切入内容
//使用@AfterReturning在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
//使用@Around在切入点前后切入内容，并自己控制何时执行切入点自身的内容
//使用@AfterThrowing用来处理当切入内容部分抛出异常之后的处理逻辑

/**
 * 切面拦截
 *
 * @author zetor
 * @date 9.5
 */
@Aspect
@Component
@Order(1) // 切面优先级 处理多个切面类执行顺序 由小到大顺序
public class ApiLogAspect {

    Logger log = LoggerFactory.getLogger(this.getClass());

    ThreadLocal<Long> startTime = new ThreadLocal();

    @Pointcut("execution(public * com.weindependent.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 记录请求起始时间
        startTime.set(System.currentTimeMillis());
        // 记录下请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //url
        log.info("ip={}, port={}, url={}", request.getRemoteAddr(), request.getRemotePort(), request.getRequestURI());
//        log.info("classMethod={}", joinPoint.getSignature().getDeclaringTypeName());
        //参数
        Enumeration<String> paramter = request.getParameterNames();
        while (paramter.hasMoreElements()) {
            String str = paramter.nextElement();
            log.info(str + "={}", request.getParameter(str));
        }
//        log.info("Rest - 调用方法Header参数 : " + JSON.toJSONString(RequestContext.getRemoteInfo()));
//        for (Object o : joinPoint.getArgs()) {
//            if (!ObjectUtils.isEmpty(o)) {
//                log.info("Rest - 调用方法参数 : " + JSON.toJSONString(o));
//            }
//        }
    }

    //    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
//        log.info("请求返回报文 : " + JSON.toJSONString(ret));
        // 处理完请求，返回内容
        log.info("请求耗时 : " + (System.currentTimeMillis() - startTime.get()) + "ms");
        startTime.remove();
    }
}
