package com.example.xcaiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoveAppTest {

    @Resource
    private LoveApp loveApp;
    
    @Test
    void TestChat() {
        String chatId = UUID.randomUUID().toString();
        //第一轮
        String message = "你好，我是芦笑存";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        System.out.println("第一轮回复：" + answer);
        
        //第二轮
        message = "我想让另一半（hpy）更爱我";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        System.out.println("第二轮回复：" + answer);
        
        //第三轮 - 测试是否能记住名字（RETRIEVE_SIZE=1，只能取到最近 1 条历史）
        message = "我的另一半叫什么来着？我刚和你说过，帮我回忆一下";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        System.out.println("第三轮回复（应该记不住 hpy 了）：" + answer);
    }

    @Test
    void TestChatWithClearMemory() {
        String chatId = UUID.randomUUID().toString();
        
        //第一轮
        String message = "你好，我叫张三";
        String answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        System.out.println("第一轮回复：" + answer);
        
        //清除记忆
        
        //第二轮 - 清除后询问名字，应该不记得了
        message = "我叫什么名字？";
        answer = loveApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        System.out.println("清除记忆后的回复（应该不知道张三）：" + answer);
    }

    @Test
    void TestChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        String message = "可以给我推荐一个对象吗？我喜欢打篮球";
        String answer =  loveApp.doChatWithRag(message, chatId);
        Assertions.assertNotNull(answer);
    }
}