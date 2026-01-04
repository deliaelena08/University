package com.example.mycomposeapp.data.api

import android.annotation.SuppressLint
import android.util.Log
import com.example.mycomposeapp.data.repository.TokenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

class WebSocketManager(private val tokenManager: TokenManager) {

    private var stompClient: StompClient? = null

    private val SOCKET_URL = "ws://10.0.2.2:8080/socket/websocket"

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow = _messageFlow.asSharedFlow()

    @SuppressLint("CheckResult")
    fun connect() {
        CoroutineScope(Dispatchers.IO).launch {
            val token = tokenManager.token.first() ?: return@launch

            if (stompClient != null && stompClient!!.isConnected) {
                return@launch
            }

            stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL)

            val headers = listOf(
                StompHeader("app-auth", token)
            )

            stompClient?.connect(headers)

            stompClient?.lifecycle()?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> Log.d("WebSocket", "Connection Opened")
                    LifecycleEvent.Type.ERROR -> Log.e("WebSocket", "Error", lifecycleEvent.exception)
                    LifecycleEvent.Type.CLOSED -> Log.d("WebSocket", "Connection Closed")
                    else -> {}
                }
            }

            stompClient?.topic("/ws/posts")?.subscribe { topicMessage ->
                Log.d("WebSocket", "Received: ${topicMessage.payload}")
                CoroutineScope(Dispatchers.Main).launch {
                    _messageFlow.emit(topicMessage.payload)
                }
            }
        }
    }

    fun disconnect() {
        stompClient?.disconnect()
        stompClient = null
    }
}