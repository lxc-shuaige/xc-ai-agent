package com.xc.ai.agent.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 令牌桶限流过滤器
 * <p>
 * 基于 IP 的简单限流：每秒最多 5 个请求，突发容量 10
 */
@Slf4j
@Component
public class RateLimitFilter implements Filter {

    /**
     * 每秒生成的令牌数
     */
    private static final double TOKENS_PER_SECOND = 5.0;

    /**
     * 桶容量
     */
    private static final long MAX_TOKENS = 10;

    private final ConcurrentHashMap<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String ip = request.getRemoteAddr();
        TokenBucket bucket = buckets.computeIfAbsent(ip, k -> new TokenBucket());

        if (bucket.tryAcquire()) {
            chain.doFilter(request, response);
        } else {
            log.warn("限流触发: ip={}", ip);
            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(429);
            httpResp.setContentType("application/json;charset=UTF-8");
            httpResp.getWriter().write("{\"code\":429,\"message\":\"请求过于频繁，请稍后重试\"}");
        }
    }

    /**
     * 简单令牌桶实现
     */
    private static class TokenBucket {
        private final AtomicLong tokens = new AtomicLong(MAX_TOKENS);
        private volatile long lastRefillTime = System.nanoTime();

        boolean tryAcquire() {
            refill();
            long current = tokens.get();
            while (current > 0) {
                if (tokens.compareAndSet(current, current - 1)) {
                    return true;
                }
                current = tokens.get();
            }
            return false;
        }

        private void refill() {
            long now = System.nanoTime();
            long elapsedNanos = now - lastRefillTime;
            long newTokens = (long) (elapsedNanos / 1_000_000_000.0 * TOKENS_PER_SECOND);
            if (newTokens > 0) {
                lastRefillTime = now;
                tokens.updateAndGet(current -> Math.min(current + newTokens, MAX_TOKENS));
            }
        }
    }
}
