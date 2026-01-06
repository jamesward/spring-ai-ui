package com.example.demo

import com.jamesward.springaiui.ChatRequest
import com.jamesward.springaiui.ChatService
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.chat.model.Generation
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service

@SpringBootApplication
class DemoApplicationTest

@Service
class FakeChatService : ChatService {
    override fun invocations(request: ChatRequest): ChatResponse =
        ChatResponse.builder().generations(
            listOf(
                Generation(
                    AssistantMessage.builder().content("This is a fake response to: ${request.prompt}").build()
                )
            )
        ).build()
}

fun main(args: Array<String>) {
    runApplication<DemoApplicationTest>(*args)
}
