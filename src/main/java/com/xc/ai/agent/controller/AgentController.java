package com.xc.ai.agent.controller;

import com.xc.ai.agent.common.Result;
import com.xc.ai.agent.model.dto.ChatRequest;
import com.xc.ai.agent.model.vo.ChatVO;
import com.xc.ai.agent.service.AgentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * AI 智能体对话控制器
 */
@Tag(name = "AI 智能体", description = "恋爱心理专家智能体对话接口")
@RestController
@RequestMapping("/agent")
public class AgentController {

    @Resource
    private AgentService agentService;

    @Operation(summary = "智能体对话（同步）", description = """
            支持四种对话模式：
            - basic: 基础对话（带记忆 + 缓存）
            - rag: 知识库增强（基于恋爱文档向量检索）
            - tools: 工具调用（文件操作、网页搜索、PDF生成等）
            - mcp: MCP协议工具（高德地图等外部服务）
            """)
    @PostMapping("/chat")
    public Result<ChatVO> chat(@Valid @RequestBody ChatRequest request) {
        ChatVO result = agentService.chat(request);
        return Result.success(result);
    }

    @Operation(summary = "智能体对话（异步）", description = """
            异步对话接口，立即返回，不阻塞 Tomcat 线程。
            适用于高并发场景，底层使用虚拟线程执行 AI 调用。
            """)
    @PostMapping("/chat/async")
    public CompletableFuture<Result<ChatVO>> chatAsync(@Valid @RequestBody ChatRequest request) {
        return agentService.chatAsync(request)
                .thenApply(Result::success);
    }

    @Operation(summary = "健康检查")
    @GetMapping("/health")
    public Result<String> health() {
        return Result.success("ok");
    }
}
