package com.pulse.plannex.features.event.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository
import com.pulse.plannex.features.event.domain.repositories.SyncPrefsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ObjectRepository,
    private val syncPrefs: SyncPrefsRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): ListenableWorker.Result {
        if (syncPrefs.isSyncDoneToday()) {
            return ListenableWorker.Result.success()
        }

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        if (hour < 0 || hour >= 5) {
            return ListenableWorker.Result.success()
        }

        return try {
            val result = repository.refreshEventos()
            if (result.isSuccess) {
                syncPrefs.saveSyncDate(System.currentTimeMillis())
                ListenableWorker.Result.success()
            } else {
                ListenableWorker.Result.retry()
            }
        } catch (e: Exception) {
            ListenableWorker.Result.failure()
        }
    }
}
