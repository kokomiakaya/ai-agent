package com.yupi.yuaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;

public class Langchain4jInvoke {
    public static void main(String[] args) {
        QwenChatModel qwenChatModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen3.8-flash")
                .build();
        String s = qwenChatModel.chat("你好，我是agent学习的小白，现在在做一个超级智能体的项目");
        System.out.println(s);
    }
}
