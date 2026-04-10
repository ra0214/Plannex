package com.pulse.plannex

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.pulse.plannex.features.event.data.worker.SyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class PlannexApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        setupNightlySync()
    }

    private fun setupNightlySync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        // Programamos que se intente cada 30 minutos. 
        // El SyncWorker tiene la lógica interna para ignorar las horas fuera de 00:00 - 05:00.
        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(30, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                30,
                TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "NightlySync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}
