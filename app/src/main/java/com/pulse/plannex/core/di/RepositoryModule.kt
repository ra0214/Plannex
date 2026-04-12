package com.pulse.plannex.core.di

import com.pulse.plannex.features.notification.data.repositories.FCMTokenRepositoryImpl
import com.pulse.plannex.features.notification.data.repositories.InvitationRepositoryImpl
import com.pulse.plannex.features.notification.domain.repositories.FCMTokenRepository
import com.pulse.plannex.features.notification.domain.repositories.InvitationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFCMTokenRepository(
        impl: FCMTokenRepositoryImpl
    ): FCMTokenRepository

    @Binds
    @Singleton
    abstract fun bindInvitationRepository(
        impl: InvitationRepositoryImpl
    ): InvitationRepository
}
