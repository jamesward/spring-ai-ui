package com.jamesward.springaiui

import com.github.wakingrufus.htmx.HttpVerb
import com.github.wakingrufus.htmx.hxPost
import com.github.wakingrufus.htmx.hxSwap
import com.github.wakingrufus.htmx.hxTarget
import com.github.wakingrufus.htmx.swap.HxSwapType
import com.github.wakingrufus.htmx.template.htmxTemplate
import com.github.wakingrufus.khtmx.spring.SpringHtmxDsl
import kotlinx.html.*
import org.springframework.ai.chat.model.ChatResponse

// Request param can be user defined
@JvmRecord
data class ChatRequest(val prompt: String)


// todo: ChatResponse can't be nullable
interface ChatService {
    fun invocations(request: ChatRequest): ChatResponse
}


val chatResponseTemplate = htmxTemplate<ChatResponse> {
    div(classes = "message assistant-message") {
        div(classes = "message-content") {
            +(it.result.output.text ?: "Could not get response")
        }
    }
}


// todo: streaming
class ChatUI : SpringHtmxDsl({
    val invocationPath = "/chat"

    page("/") {
        style {
            unsafe {
                raw("""
                    * {
                        box-sizing: border-box;
                        margin: 0;
                        padding: 0;
                    }
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                        background: #f5f5f5;
                        height: 100vh;
                        display: flex;
                        flex-direction: column;
                    }
                    .chat-container {
                        display: flex;
                        flex-direction: column;
                        height: 100vh;
                        max-width: 800px;
                        margin: 0 auto;
                        width: 100%;
                        background: white;
                        box-shadow: 0 0 20px rgba(0,0,0,0.1);
                    }
                    .chat-header {
                        padding: 20px;
                        background: #2563eb;
                        color: white;
                        text-align: center;
                        font-size: 1.25rem;
                        font-weight: 600;
                    }
                    .messages {
                        flex: 1;
                        overflow-y: auto;
                        padding: 20px;
                        display: flex;
                        flex-direction: column;
                        gap: 16px;
                    }
                    .message {
                        max-width: 80%;
                        animation: fadeIn 0.3s ease;
                    }
                    @keyframes fadeIn {
                        from { opacity: 0; transform: translateY(10px); }
                        to { opacity: 1; transform: translateY(0); }
                    }
                    .user-message {
                        align-self: flex-end;
                    }
                    .user-message .message-content {
                        background: #2563eb;
                        color: white;
                        border-radius: 18px 18px 4px 18px;
                        padding: 12px 16px;
                    }
                    .assistant-message {
                        align-self: flex-start;
                    }
                    .assistant-message .message-content {
                        background: #e5e7eb;
                        color: #1f2937;
                        border-radius: 18px 18px 18px 4px;
                        padding: 12px 16px;
                        white-space: pre-wrap;
                    }
                    .chat-input {
                        padding: 16px 20px;
                        border-top: 1px solid #e5e7eb;
                        background: white;
                    }
                    .chat-input form {
                        display: flex;
                        gap: 12px;
                    }
                    .chat-input input[type="text"] {
                        flex: 1;
                        padding: 12px 16px;
                        border: 1px solid #d1d5db;
                        border-radius: 24px;
                        font-size: 1rem;
                        outline: none;
                        transition: border-color 0.2s;
                    }
                    .chat-input input[type="text"]:focus {
                        border-color: #2563eb;
                    }
                    .chat-input button {
                        padding: 12px 24px;
                        background: #2563eb;
                        color: white;
                        border: none;
                        border-radius: 24px;
                        font-size: 1rem;
                        font-weight: 500;
                        cursor: pointer;
                        transition: background 0.2s;
                    }
                    .chat-input button:hover {
                        background: #1d4ed8;
                    }
                    .chat-input button:disabled {
                        background: #9ca3af;
                        cursor: not-allowed;
                    }
                    .htmx-request button {
                        background: #9ca3af;
                    }
                    .htmx-request input {
                        opacity: 0.7;
                    }
                """.trimIndent())
            }
        }
        div(classes = "chat-container") {
            div(classes = "chat-header") {
                +"AI Chat Assistant"
            }
            div(classes = "messages") {
                id = "messages"
                div(classes = "message assistant-message") {
                    div(classes = "message-content") {
                        +"Hello! How can I help you today?"
                    }
                }
            }
            // todo: any better way to template the user's messages?
            div(classes = "chat-input") {
                form {
                    hxPost(invocationPath)
                    hxTarget("#messages")
                    hxSwap {
                        style(HxSwapType.BeforeEnd)
                    }
                    attributes["hx-on-htmx-before-request"] = """
                        var msg = document.getElementById('prompt-input').value;
                        var userDiv = document.createElement('div');
                        userDiv.className = 'message user-message';
                        userDiv.innerHTML = '<div class="message-content">' + msg.replace(/</g, '&lt;').replace(/>/g, '&gt;') + '</div>';
                        document.getElementById('messages').appendChild(userDiv);
                    """.trimIndent()
                    attributes["hx-on-htmx-after-request"] = "this.reset(); document.getElementById('messages').scrollTop = document.getElementById('messages').scrollHeight;"
                    textInput(name = "prompt") {
                        id = "prompt-input"
                        placeholder = "Type your message..."
                        attributes["autocomplete"] = "off"
                        attributes["required"] = "true"
                    }
                    button(type = ButtonType.submit) {
                        +"Send"
                    }
                }
            }
        }
    }

    route(HttpVerb.POST, invocationPath, ChatService::invocations, chatResponseTemplate)
})
