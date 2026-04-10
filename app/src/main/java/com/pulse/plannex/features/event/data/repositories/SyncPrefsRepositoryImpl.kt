package com.pulse.plannex.features.event.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.pulse.plannex.features.event.domain.repositories.SyncPrefsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SyncPrefsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SyncPrefsRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)

    override fun getLastSyncDate(): Long {
        return prefs.getLong("last_sync_timestamp", 0L)
    }

    override fun saveSyncDate(timestamp: Long) {
        prefs.edit().putLong("last_sync_timestamp", timestamp).apply()
    }

    override fun isSyncDoneToday(): Boolean {
        val lastSync = getLastSyncDate()
        if (lastSync == 0L) return false

        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val lastDate = sdf.format(Date(lastSync))
        val todayDate = sdf.format(Date())

        return lastDate == todayDate
    }
}
