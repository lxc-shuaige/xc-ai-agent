package com.example.xcaiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.example.xcaiagent.constant.FileConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

public class ResourceDownloadTool {

    @Tool(description = "Download a resource from a given URL")
    public String downloadResource(@ToolParam(description = "URL of the resource to download") String url, @ToolParam(description = "Name of the file to save the downloaded resource") String fileName) {
        String fileDir = FileConstant.FILE_SAVE_DIR + "/download";
        String filePath = fileDir + "/" + fileName;
        HttpResponse resp = HttpRequest.get(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0 Safari/537.36")
                .header("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8")
                .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("Cache-Control", "no-cache")
                .header("Pragma", "no-cache")
                .header("Connection", "keep-alive")
                .header("Upgrade-Insecure-Requests", "1")
                // 可选：如果仍被拦截再加
                //.header("Referer", "https://www.codefather.cn/")
                .setFollowRedirects(true)
                .timeout(15000)
                .execute();

        if (resp.isOk()) {
            FileUtil.writeBytes(resp.bodyBytes(), filePath);
            return "Resource downloaded successfully to: " + filePath;
        }
        System.out.println("status=" + resp.getStatus());
        System.out.println("headers=" + resp.headers());
        return "Error downloading resource: status=" + resp.getStatus();
    }
}
