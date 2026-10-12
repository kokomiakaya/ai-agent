# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

Java 21 + Spring Boot 3.5.8 的 AI Agent 学习 demo,用三种方式调用通义千问(qwen3.8-flash):
Spring AI Alibaba、LangChain4j、DashScope 原生 SDK。

## 常用命令

- 启动应用:`./mvnw spring-boot:run`(Windows cmd/PowerShell 用 `mvnw.cmd`;项目自带 Maven wrapper,无需全局安装 Maven,需 JDK 21)
- 测试:`./mvnw test`
- 打包:`./mvnw package`
- 单独运行 demo:在 IDE 中直接运行 `Langchain4jInvoke` / `SdkAiInvoke` 的 main 方法

**运行任何调用模型的代码前,必须设置环境变量 `DASHSCOPE_API_KEY`(阿里云 DashScope 密钥),否则启动/调用失败。**

## 架构约定

- `controller` 层:REST 接口。`AiController`(ChatClient 调用模型)、`HealthController`(健康检查)
- `demo/invoke` 包:三种调用方式的独立演示类,不属于分层,是学习示例
- `server.port=8123`,`context-path=/api` → 实际访问路径 = 代码路径 + `/api` 前缀
- 配置集中在 `src/main/resources/application.yaml`;API key 一律通过环境变量注入(见下)

## 关键模式

- **API key 注入**:`application.yaml` 用 `${DASHSCOPE_API_KEY:}` 占位符;`TestApiKey.API_KEY = System.getenv("DASHSCOPE_API_KEY")`。禁止在任何源码/配置中硬编码真实 key。
- `SpringAiInvoke` 实现 `CommandLineRunner`:应用启动即调用一次模型,启动前必须设置好环境变量,否则启动报错。
- Swagger 文档:Knife4j + springdoc,访问 `/api/swagger-ui.html`。

## 环境陷阱(换机器先改这些)

- ⚠️ `SdkAiInvoke.java:17` 硬编码了第三方中转端点 `https://maas.qianwenaiapi.com/api/v1`(非阿里官方 `dashscope.aliyuncs.com`),请求会经过第三方。修改或正式使用时注意。
- 包名 `com.yupi.yuaiagent` 与 groupId `com.kokomi`、artifactId `demo-ai` 不一致,移动/新建文件时不要按 groupId 猜包名。
- 本机需代理访问 GitHub(`127.0.0.1:7897`,已写入仓库本地 `git config http.proxy`),push 前保持代理软件开启,端口变了需更新配置。

## 现状缺口

- 无鉴权:chat 接口裸奔,任何人可调用消耗 token
- 测试只有空壳 `DemoAiApplicationTests`
- 无 CI、错误处理简单
