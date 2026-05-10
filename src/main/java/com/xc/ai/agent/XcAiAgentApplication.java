package com.xc.ai.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * XC AI Agent —— 基于 Spring AI 的智能体后端服务
 * <p>
 * 技术栈：Spring Boot 3.5 + Spring AI + DashScope(通义千问) + RAG + MCP
 */
@SpringBootApplication
public class XcAiAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(XcAiAgentApplication.class, args);
    }
}
