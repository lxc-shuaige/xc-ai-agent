package com.xc.ai.agent.service;

import com.xc.ai.agent.model.dto.ChatRequest;
import com.xc.ai.agent.model.vo.ChatVO;

import java.util.concurrent.CompletableFuture;

/**
 * AI 智能体对话服务
 */
public interface AgentService {

    /**
     * 同步对话
     */
    ChatVO chat(ChatRequest request);

    /**
     * 异步对话（高并发场景，不阻塞 Tomcat 线程）
     */
    CompletableFuture<ChatVO> chatAsync(ChatRequest request);
}
