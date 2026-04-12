package com.pulse.plannex.core.notification

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pulse.plannex.features.notification.domain.repositories.FCMTokenRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlannexMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationManager: PlannexNotificationManager

    @Inject
    lateinit var fcmTokenRepository: FCMTokenRepository

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        
        val title = message.notification?.title ?: message.data["title"] ?: "Notificación"
        val body = message.notification?.body ?: message.data["body"] ?: ""
        
        notificationManager.showNotification(title, body)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        scope.launch {
            fcmTokenRepository.saveToken(token)
            // If user is logged in, you might want to register it here too.
            // But usually, we do it at login or when the app starts.
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
