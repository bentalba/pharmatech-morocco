package com.pharmatech.morocco.core.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Timber.d("FCM Message received: ${message.data}")

        message.notification?.let { notification ->
            Timber.d("Notification: ${notification.title} - ${notification.body}")
            // TODO: Show notification
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Timber.d("New FCM token: $token")
        // TODO: Send token to server
    }
}

