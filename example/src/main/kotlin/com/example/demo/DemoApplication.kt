package com.example.demo

import com.jamesward.springaiui.ChatRequest
import com.jamesward.springaiui.ChatService
import com.jamesward.springaiui.ChatUI
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@SpringBootApplication
@Import(ChatUI::class)
@EnableWebMvc
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

class ExampleService(private val chatClient: ChatClient) : ChatService {
    override fun invocations(request: ChatRequest): ChatResponse =
        chatClient
            .prompt()
            .user(request.prompt)
            .call()
            .chatResponse()!!
    // todo: null
}

@Configuration
class ChatServiceConfig {
    @Bean
    @ConditionalOnMissingBean(ChatService::class)
    fun chatService(chatClientBuilder: ChatClient.Builder): ChatService =
        ExampleService(chatClientBuilder.build())
}




