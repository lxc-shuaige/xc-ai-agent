package com.xc.ai.agent.service.impl;

import com.xc.ai.agent.advisor.MyLoggerAdvisor;
import com.xc.ai.agent.common.BusinessException;
import com.xc.ai.agent.model.dto.ChatRequest;
import com.xc.ai.agent.model.vo.ChatVO;
import com.xc.ai.agent.service.AgentService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * AI 智能体对话服务实现
 */
@Slf4j
@Service
public class AgentServiceImpl implements AgentService {

    private static final String SYSTEM_PROMPT = """
            扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。
            围绕单身、恋爱、已婚三种状态提问：
            单身状态询问社交圈拓展及追求心仪对象的困扰；
            恋爱状态询问沟通、习惯差异引发的矛盾；
            已婚状态询问家庭责任与亲属关系处理的问题。
            引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。""";

    private final ChatClient chatClient;

    @Resource
    private VectorStore loveappVectorStore;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    public AgentServiceImpl(ChatModel dashscopeChatModel) {
        ChatMemory chatMemory = new InMemoryChatMemory();
        this.chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        new MyLoggerAdvisor()
                )
                .build();
    }

    @Override
    public ChatVO chat(ChatRequest request) {
        String chatId = request.getChatId() != null ? request.getChatId() : UUID.randomUUID().toString();
        String mode = request.getMode() != null ? request.getMode() : "basic";

        log.info("[Agent Chat] chatId={}, mode={}, message={}", chatId, mode, request.getMessage());

        ChatResponse chatResponse = switch (mode) {
            case "rag" -> doChatWithRag(request.getMessage(), chatId);
            case "tools" -> doChatWithTools(request.getMessage(), chatId);
            case "mcp" -> doChatWithMcp(request.getMessage(), chatId);
            case "basic" -> doChat(request.getMessage(), chatId);
            default -> throw new BusinessException(400, "不支持的对话模式: " + mode + "，可选值: basic, rag, tools, mcp");
        };

        String content = chatResponse.getResult().getOutput().getText();
        log.info("[Agent Response] chatId={}, answer={}", chatId, content);

        return ChatVO.builder()
                .chatId(chatId)
                .answer(content)
                .mode(mode)
                .build();
    }

    // ==================== 四种对话模式 ====================

    private ChatResponse doChat(String message, String chatId) {
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
    }

    private ChatResponse doChatWithRag(String message, String chatId) {
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(new QuestionAnswerAdvisor(loveappVectorStore))
                .call()
                .chatResponse();
    }

    private ChatResponse doChatWithTools(String message, String chatId) {
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(allTools)
                .call()
                .chatResponse();
    }

    private ChatResponse doChatWithMcp(String message, String chatId) {
        return chatClient.prompt()
                .user(message)
                .advisors(spec -> spec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
    }
}
