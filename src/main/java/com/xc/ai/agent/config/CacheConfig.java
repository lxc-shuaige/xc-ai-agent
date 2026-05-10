package com.xc.ai.agent.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 本地缓存配置
 * <p>
 * 缓存 AI 对话结果，相同问题短时间内不重复调用大模型
 */
@Configuration
public class CacheConfig {

    /**
     * AI 对话结果缓存
     * <p>
     * - 最大 1000 条
     * - 写入后 10 分钟过期
     * - 软引用回收（内存不足时 GC 回收）
     */
    @Bean
    public Cache<String, String> chatCache() {
        return CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .softValues()
                .recordStats()
                .build();
    }
}
