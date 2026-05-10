package com.xc.ai.agent.rag;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * RAG 向量存储配置
 * <p>
 * 通过 rag.enabled=true 启用（默认关闭，因为需要有效的 Embedding API Key）。
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "rag.enabled", havingValue = "true")
public class LoveAppStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;

    @Bean
    VectorStore loveappVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> documentList = loveAppDocumentLoader.loadMarkdowns();
        simpleVectorStore.add(documentList);
        log.info("RAG 向量存储初始化成功，加载 {} 篇文档", documentList.size());
        return simpleVectorStore;
    }
}
