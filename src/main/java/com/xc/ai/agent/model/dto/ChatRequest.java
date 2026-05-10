package com.xc.ai.agent.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对话请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "对话请求")
public class ChatRequest {

    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "用户消息", example = "你好，我想咨询恋爱问题", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;

    @Schema(description = "会话ID（不传则自动生成）", example = "uuid-xxxx")
    private String chatId;

    @Schema(description = "对话模式: basic / rag / tools / mcp", example = "basic", defaultValue = "basic")
    private String mode;
}
