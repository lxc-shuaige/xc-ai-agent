package com.xc.ai.agent.service;

import com.xc.ai.agent.model.dto.ChatRequest;
import com.xc.ai.agent.model.vo.ChatVO;

/**
 * AI 智能体对话服务
 */
public interface AgentService {

    /**
     * 对话
     */
    ChatVO chat(ChatRequest request);
}
