package com.pulse.plannex.core.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.pulse.plannex.features.accessControl.data.repositories.AndroidHapticFeedbackManager
import com.pulse.plannex.features.accessControl.domain.repositories.HapticFeedbackManager
import com.pulse.plannex.features.auth.data.BiometricAuthenticatorImpl
import com.pulse.plannex.features.auth.domain.BiometricAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HardwareModule {

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideBiometricAuthenticator(@ApplicationContext context: Context): BiometricAuthenticator {
        return BiometricAuthenticatorImpl(context)
    }

    @Provides
    @Singleton
    fun provideHapticFeedbackManager(@ApplicationContext context: Context): HapticFeedbackManager {
        return AndroidHapticFeedbackManager(context)
    }
}
