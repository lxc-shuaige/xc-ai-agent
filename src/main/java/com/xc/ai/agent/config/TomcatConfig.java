package com.xc.ai.agent.config;

import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * Tomcat 高并发优化配置
 * <p>
 * - 虚拟线程（Java 21+）：AI 调用是 IO 密集型，虚拟线程可承载万级并发
 * - 连接超时：防止慢客户端占用连接
 */
@Configuration
public class TomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> {
            // Java 21 虚拟线程 —— IO 密集型场景的最佳选择
            factory.setTomcatProtocolHandlerCustomizer(protocolHandler ->
                    protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor())
            );

            // 连接器调优
            factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
                // 连接超时 30s（默认 20s）
                connector.setProperty("connectionTimeout", "30000");
                // 保持长连接
                connector.setProperty("keepAliveTimeout", "15000");
                // 最大连接数
                connector.setProperty("maxConnections", "10000");
                // 等待队列
                connector.setProperty("acceptCount", "500");
            });
        };
    }
}
