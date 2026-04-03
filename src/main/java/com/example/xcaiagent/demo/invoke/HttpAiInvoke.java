package com.example.xcaiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

public class HttpAiInvoke {
    public static void main(String[] args) {
        // 1. 你的 API-KEY（直接替换成真实值）
        String apiKey = TestAPIkey.API_KEY;
        
        // 2. 请求地址
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 3. 构造请求体（和 curl 里的 data 完全一致）
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "qwen-plus");

        // input 部分
        Map<String, Object> input = new HashMap<>();
        Map<String, String> msg1 = new HashMap<>();
        msg1.put("role", "system");
        msg1.put("content", "You are a helpful assistant.");

        Map<String, String> msg2 = new HashMap<>();
        msg2.put("role", "user");
        msg2.put("content", "你是谁？");

        input.put("messages", new Map[]{msg1, msg2});
        requestBody.put("input", input);

        // parameters 部分
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("result_format", "message");
        requestBody.put("parameters", parameters);

        // 4. 发送 POST 请求（Hutool 核心代码）
        try (HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(requestBody))  // 转 JSON
                .timeout(10000)
                .execute()) {

            // 5. 获取结果
            System.out.println("响应状态码：" + response.getStatus());
            System.out.println("响应结果：" + response.body());
        }
    }
}