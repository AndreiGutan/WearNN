package com.example.wearnn.services

import android.content.Context
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.WearableListenerService

class ListenerService : WearableListenerService(), MessageClient.OnMessageReceivedListener {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        if (messageEvent.path == "/auth_token_path") {
            val authToken = String(messageEvent.data)
            // Use the authToken to authenticate in the Wear OS app
        }
    }

    override fun onCreate() {
        super.onCreate()
        Wearable.getMessageClient(this).addListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Wearable.getMessageClient(this).removeListener(this)
    }
}