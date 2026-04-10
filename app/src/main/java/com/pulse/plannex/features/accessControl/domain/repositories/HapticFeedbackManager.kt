package com.pulse.plannex.features.accessControl.domain.repositories

interface HapticFeedbackManager {
    fun successVibration()
    fun errorVibration()
}
