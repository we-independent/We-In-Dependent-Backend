package com.weindependent.app.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@EnableScheduling
public class DatabaseMonitorConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseMonitorConfig.class);
    
    @Autowired
    private DruidDataSource druidDataSource;
    
    private final AtomicLong lastConnectionCount = new AtomicLong(0);
    private final AtomicLong lastActiveConnectionCount = new AtomicLong(0);
    
    @PostConstruct
    public void init() {
        logger.info("数据库监控配置已启动");
        logDatabaseStatus();
    }
    
    /**
     * 每30秒监控一次数据库连接池状态
     */
    @Scheduled(fixedRate = 30000)
    public void monitorDatabaseConnections() {
        try {
            DruidStatManagerFacade facade = DruidStatManagerFacade.getInstance();
            
            long currentConnectionCount = druidDataSource.getConnectCount();
            long currentActiveConnections = druidDataSource.getActiveCount();
            long currentPoolingCount = druidDataSource.getPoolingCount();
            
            // 检查连接数变化
            long connectionDiff = currentConnectionCount - lastConnectionCount.get();
            long activeDiff = currentActiveConnections - lastActiveConnectionCount.get();
            
            if (connectionDiff > 0 || activeDiff > 0) {
                logger.info("数据库连接池状态变化 - 总连接数: {}, 活跃连接: {}, 池中连接: {}, 新增连接: {}, 活跃变化: {}", 
                    currentConnectionCount, currentActiveConnections, currentPoolingCount, connectionDiff, activeDiff);
            }
            
            // 检查连接池是否接近满载
            if (currentActiveConnections > 15) {
                logger.warn("数据库连接池活跃连接数较高: {} / {}", currentActiveConnections, druidDataSource.getMaxActive());
            }
            
            // 检查是否有连接泄漏
            if (currentActiveConnections > 0 && currentPoolingCount == 0) {
                logger.warn("可能存在数据库连接泄漏 - 活跃连接: {}, 池中连接: {}", currentActiveConnections, currentPoolingCount);
            }
            
            lastConnectionCount.set(currentConnectionCount);
            lastActiveConnectionCount.set(currentActiveConnections);
            
        } catch (Exception e) {
            logger.error("监控数据库连接时发生错误", e);
        }
    }
    
    /**
     * 每5分钟检查一次连接池健康状态
     */
    @Scheduled(fixedRate = 300000)
    public void checkDatabaseHealth() {
        try {
            // 测试数据库连接
            try (Connection connection = druidDataSource.getConnection()) {
                if (connection.isValid(3)) {
                    logger.info("数据库连接健康检查通过");
                } else {
                    logger.error("数据库连接健康检查失败 - 连接无效");
                }
            } catch (Exception e) {
                logger.error("数据库连接健康检查失败", e);
            }
            
            // 记录详细的连接池统计信息
            logDatabaseStatus();
            
        } catch (Exception e) {
            logger.error("检查数据库健康状态时发生错误", e);
        }
    }
    
    /**
     * 记录数据库连接池详细状态
     */
    private void logDatabaseStatus() {
        try {
            logger.info("=== 数据库连接池状态 ===");
            logger.info("最大连接数: {}", druidDataSource.getMaxActive());
            logger.info("最小空闲连接数: {}", druidDataSource.getMinIdle());
            logger.info("初始连接数: {}", druidDataSource.getInitialSize());
            logger.info("当前活跃连接数: {}", druidDataSource.getActiveCount());
            logger.info("当前池中连接数: {}", druidDataSource.getPoolingCount());
            logger.info("总连接数: {}", druidDataSource.getConnectCount());
            logger.info("连接等待次数: {}", druidDataSource.getWaitThreadCount());
            logger.info("连接等待时间: {}ms", druidDataSource.getNotEmptyWaitCount());
            logger.info("连接池是否关闭: {}", druidDataSource.isClosed());
            logger.info("========================");
        } catch (Exception e) {
            logger.error("记录数据库状态时发生错误", e);
        }
    }
} 