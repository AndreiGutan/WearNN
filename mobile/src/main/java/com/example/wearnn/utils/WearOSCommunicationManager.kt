package com.example.wearnn.utils

import android.content.Context
import com.google.android.gms.wearable.Wearable

class WearOSCommunicationManager(private val context: Context) {

    fun sendAuthTokenToWear(authToken: String) {
        val sendMessageTask = Wearable.getNodeClient(context).connectedNodes
            .addOnSuccessListener { nodes ->
                nodes.forEach { node ->
                    Wearable.getMessageClient(context).sendMessage(node.id, "/auth_token", authToken.toByteArray())
                        .addOnSuccessListener {
                            // Handle success for this node
                        }
                        .addOnFailureListener {
                            // Handle failure for this node
                        }
                }
            }
            .addOnFailureListener {
                // Handle failure to get connected nodes
            }
    }
}
