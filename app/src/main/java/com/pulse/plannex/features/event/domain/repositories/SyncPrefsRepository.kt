package com.pulse.plannex.features.event.domain.repositories

interface SyncPrefsRepository {
    fun getLastSyncDate(): Long
    fun saveSyncDate(timestamp: Long)
    fun isSyncDoneToday(): Boolean
}
