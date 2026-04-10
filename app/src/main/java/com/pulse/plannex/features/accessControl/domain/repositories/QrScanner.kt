package com.pulse.plannex.features.accessControl.domain.repositories

import kotlinx.coroutines.flow.Flow

interface QrScanner {
    fun startScanning(): Flow<String?>
}
