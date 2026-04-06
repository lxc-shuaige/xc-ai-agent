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

    @Test
    void TestChatWithTools(){
        testMessage("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？");


        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");


        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");


        testMessage("执行 Python3 脚本来生成数据分析报告");


        testMessage("保存我的恋爱档案为文件");


        testMessage("生成一份‘七夕约会计划’PDF，包含餐厅预订、活动流程和礼物清单");

    }

    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = loveApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithMcp() {
        String chatId = UUID.randomUUID().toString();
        String message = "我的另一半居住在上海静安区，请帮我找到 5 公里内合适的约会地点";
        String answer =  loveApp.doChatWithMcp(message, chatId);
    }
}