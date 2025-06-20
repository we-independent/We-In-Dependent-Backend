package com.weindependent.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Slf4j
@Configuration
public class ThreadPoolConfig {

    @Value("${service.weindependent.thread-pool.corePoolSize}")
    private String corePoolSize;

    @Value("${service.weindependent.thread-pool.maximumPoolSize}")
    private String maximumPoolSize;

    @Value("${service.weindependent.thread-pool.keepAliveTime}")
    private String keepAliveTime;

    private TimeUnit unit = TimeUnit.MICROSECONDS;

    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    private ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.MAX_PRIORITY);
            return null;
        }
    };

    @Bean(name = "threadPool")
    public ThreadPoolExecutor executor() {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Integer.valueOf(corePoolSize), Integer.valueOf(maximumPoolSize), Long.valueOf(keepAliveTime), unit, workQueue, threadFactory, (r, executor) -> log.error("reject runable"));
        return threadPool;
    }
}
