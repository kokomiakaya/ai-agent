# ai-agent(项目实际名 demo-ai)

> 当前版本:**v0.1**

## 项目简介

一个 AI Agent 学习 demo,用三种主流方式调用阿里云通义千问大模型(qwen3.8-flash):

1. **Spring AI Alibaba** — 通过 Spring 生态集成,提供 REST 接口与启动演示
2. **LangChain4j** — 社区 DashScope 模型调用
3. **DashScope 原生 SDK** — 多模态(图片理解)调用

> ⚠️ 仓库名为 ai-agent,但项目实际 artifactId 为 demo-ai(学习演示项目)。

## 技术栈

| 技术 | 版本 | 用途 |
| --- | --- | --- |
| Java | 21 | 运行环境 |
| Spring Boot | 3.5.8 | 基础框架 |
| Spring AI Alibaba | 1.1.2.0 | Agent 框架 + DashScope Starter |
| LangChain4j | 1.20.0-beta30 | DashScope 社区模型 |
| DashScope SDK | 2.23.2 | 阿里云原生 SDK(多模态) |
| Knife4j / springdoc | 4.4.0 / 2.8.17 | 接口文档 |
| Hutool HTTP | 5.8.36 | HTTP 工具 |
| Maven Wrapper | 3.10.0 | 构建(无需全局安装 Maven) |

## 功能列表

- [x] Spring AI Alibaba 调用通义千问(REST 接口 + 启动即演示)
- [x] LangChain4j 调用 DashScope
- [x] DashScope 原生 SDK 多模态图片理解
- [x] 健康检查接口
- [x] Swagger 接口文档

## API 端点

应用端口 8123,context-path 为 `/api`:

| 方法 | 路径 | 参数 | 说明 |
| --- | --- | --- | --- |
| GET | `/api/ai/chat` | `message`(必填,query) | 调用通义千问对话,返回回答文本 |
| GET | `/api/health` | 无 | 健康检查,返回 `ok` |

接口文档:http://localhost:8123/api/swagger-ui.html

## 快速开始

### 环境要求

- JDK 21
- 阿里云 DashScope API key(通过环境变量注入,见配置说明)

### 步骤

1. 设置环境变量(Windows):

```bash
setx DASHSCOPE_API_KEY "你的DashScope密钥"
```

> 设置后需重开终端/IDE 生效;也可在 IDEA 运行配置的 Environment variables 中设置。

2. 启动应用(自带 Maven wrapper,无需安装 Maven):

```bash
./mvnw spring-boot:run        # Windows cmd/PowerShell 用 mvnw.cmd
```

3. 验证:

```bash
curl http://localhost:8123/api/health
curl "http://localhost:8123/api/ai/chat?message=你好"
```

单独运行 demo:在 IDE 中直接运行 `Langchain4jInvoke` / `SdkAiInvoke` 的 main 方法(同样需要先设置环境变量)。

## 配置说明

- **API key 通过环境变量注入**:`application.yaml` 中为 `${DASHSCOPE_API_KEY:}` 占位符,`TestApiKey` 类从环境变量 `DASHSCOPE_API_KEY` 读取。仓库中不含任何真实密钥。
- ⚠️ **第三方中转端点**:`SdkAiInvoke` 中硬编码了非官方的中转地址 `https://maas.qianwenaiapi.com/api/v1`,请求会经过第三方服务器,请知悉。

## 项目结构

```
demo-ai/
├── mvnw / mvnw.cmd            # Maven wrapper(无需全局安装 Maven)
├── pom.xml                    # 依赖配置
├── .gitignore
├── HELP.md                    # Spring Initializr 生成的参考文档
└── src/
    ├── main/
    │   ├── java/com/yupi/yuaiagent/
    │   │   ├── DemoAiApplication.java       # 启动入口
    │   │   ├── controller/
    │   │   │   ├── AiController.java        # 对话接口
    │   │   │   └── HealthController.java    # 健康检查
    │   │   └── demo/invoke/
    │   │       ├── Langchain4jInvoke.java   # LangChain4j 调用 demo
    │   │       ├── SdkAiInvoke.java         # DashScope SDK 多模态 demo
    │   │       ├── SpringAiInvoke.java      # Spring AI 启动演示
    │   │       └── TestApiKey.java          # 环境变量读取 API key
    │   └── resources/application.yaml       # 配置(key 用环境变量占位)
    └── test/java/com/yupi/yuaiagent/DemoAiApplicationTests.java
```

## 已知限制

- `SpringAiInvoke` 是 `CommandLineRunner`,应用启动即调用一次模型,未设置 key 时启动失败
- chat 接口无鉴权,任何人可调用
- 测试仅为空壳,无 CI
- `SdkAiInvoke` 使用第三方中转端点

## 更新日志

### v0.1(2026-10-12)

- 初始版本:Spring AI Alibaba / LangChain4j / DashScope SDK 三种方式调用通义千问
- REST 对话接口与健康检查接口
- API key 环境变量化,仓库不提交明文密钥
