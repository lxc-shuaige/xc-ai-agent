package com.xc.ai.agent.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对话响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "对话响应")
public class ChatVO {

    @Schema(description = "会话ID")
    private String chatId;

    @Schema(description = "AI回复内容")
    private String answer;

    @Schema(description = "使用的模式: basic/rag/tools/mcp")
    private String mode;
}
