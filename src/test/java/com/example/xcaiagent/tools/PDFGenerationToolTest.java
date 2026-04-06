package com.example.xcaiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PDFGenerationToolTest {

    @Test
    void generatePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "lxc-shuaige.pdf";
        String content = "存神很帅~";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}