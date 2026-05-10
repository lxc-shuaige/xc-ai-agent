# XC AI Agent

> 基于 **Spring AI + Spring Boot 3.5** 的智能体后端服务

## 🚀 技术栈

| 技术 | 说明 |
|------|------|
| **Spring Boot 3.5** | 最新一代 Java 后端框架 |
| **Spring AI 1.0-M6** | Spring 官方 AI 集成框架 |
| **DashScope (通义千问)** | 阿里云大模型服务 |
| **Ollama** | 本地模型部署（支持 qwen3.5 等） |
| **RAG 向量检索** | 基于 Markdown 文档的知识库增强问答 |
| **MCP 协议** | 模型上下文协议，对接高德地图等外部服务 |
| **Knife4j** | Swagger/OpenAPI 接口文档 |
| **iText PDF** | PDF 文件生成 |
| **Jsoup** | 网页内容抓取 |
| **Hutool** | Java 工具类库 |

## 📁 项目结构

```
src/main/java/com/xc/ai/agent/
├── XcAiAgentApplication.java    # 启动类
├── advisor/                     # AI Advisor 链
│   ├── MyLoggerAdvisor.java     # 日志记录 Advisor
│   └── ReReadingAdvisor.java    # 重读增强 Advisor
├── common/                      # 公共组件
│   ├── Result.java              # 统一响应体
│   ├── BusinessException.java   # 业务异常
│   └── GlobalExceptionHandler.java  # 全局异常处理
├── config/                      # 配置类
├── constant/                    # 常量
├── controller/                  # REST 接口
│   └── AgentController.java     # 对话接口
├── model/
│   ├── dto/ChatRequest.java     # 请求 DTO
│   └── vo/ChatVO.java           # 响应 VO
├── rag/                         # RAG 知识库
│   ├── LoveAppDocumentLoader.java  # 文档加载器
│   └── LoveAppStoreConfig.java     # 向量存储配置
├── service/                     # 业务服务
│   ├── AgentService.java        # 服务接口
│   └── impl/AgentServiceImpl.java  # 服务实现
└── tools/                       # AI 工具集
    ├── FileOperationTool.java   # 文件读写
    ├── WebSearchTool.java       # 百度搜索
    ├── WebScrapingTool.java     # 网页抓取
    ├── ResourceDownloadTool.java # 资源下载
    ├── PDFGenerationTool.java   # PDF 生成
    ├── TerminalOperationTool.java # 终端命令
    └── ToolRegistration.java    # 工具注册
```

## 🎯 核心功能

### 1. 四种对话模式

| 模式 | 路径 | 说明 |
|------|------|------|
| **basic** | `POST /api/agent/chat` | 基础对话，带上下文记忆 |
| **rag** | `POST /api/agent/chat` | 知识库增强，基于恋爱文档向量检索 |
| **tools** | `POST /api/agent/chat` | 工具调用，可执行文件/搜索/PDF等操作 |
| **mcp** | `POST /api/agent/chat` | MCP 协议，对接高德地图等外部服务 |

### 2. RAG 知识库
- 自动加载 `resources/document/` 下的 Markdown 文档
- 使用 DashScope Embedding 进行向量化
- 基于 `SimpleVectorStore` 内存向量存储

### 3. Advisor 链
- **MessageChatMemoryAdvisor**: 对话记忆管理
- **MyLoggerAdvisor**: 请求/响应日志记录
- **QuestionAnswerAdvisor**: RAG 知识增强
- **ReReadingAdvisor**: 重复阅读增强理解

### 4. 工具集 (Function Calling)
- 📄 文件读写
- 🔍 百度搜索
- 🌐 网页抓取
- 📥 资源下载
- 📝 PDF 生成
- 💻 终端命令执行

## 🔧 快速开始

### 环境要求
- JDK 21+
- Maven 3.8+
- （可选）Ollama 本地模型

### 1. 配置环境变量

复制 `.env.example` 为 `.env`，填入你的 API Key：

```bash
cp .env.example .env
```

### 2. 启动项目

```bash
./mvnw spring-boot:run
```

### 3. 访问接口文档

- Swagger UI: http://localhost:8123/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8123/api/v3/api-docs

### 4. 测试对话

```bash
curl -X POST http://localhost:8123/api/agent/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "你好，我最近在恋爱中遇到了一些问题", "mode": "basic"}'
```

## 📡 API 接口

### 智能体对话

```
POST /api/agent/chat
Content-Type: application/json

{
  "message": "用户消息",
  "chatId": "可选，会话ID",
  "mode": "basic|rag|tools|mcp"
}
```

响应示例：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "chatId": "uuid-xxxx",
    "answer": "AI 回复内容...",
    "mode": "basic"
  },
  "timestamp": 1715241600000
}
```

### 健康检查

```
GET /api/agent/health
```

## 🛡️ 安全说明

- 所有 API Key 通过环境变量注入，不硬编码在配置文件中
- `application-local.yml` 已加入 `.gitignore`
- `TerminalOperationTool` 仅用于开发环境，生产环境应移除

## 📝 面试展示要点

1. **架构设计**: Controller → Service → AI 模型，清晰的分层架构
2. **设计模式**: 策略模式（四种对话模式）、Advisor 链模式
3. **AI 集成**: Spring AI 全套技术栈，多模型支持
4. **RAG**: 向量检索 + 知识库增强
5. **MCP**: 前沿的模型上下文协议
6. **工程化**: 统一响应体、全局异常处理、参数校验、API 文档
