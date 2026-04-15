package com.pulse.plannex.core.di

import com.pulse.plannex.features.accessControl.data.repositories.AccessControlRepositoryImpl
import com.pulse.plannex.features.accessControl.domain.repositories.AccessControlRepository
import com.pulse.plannex.features.auth.data.repositories.AuthRepositoryImpl
import com.pulse.plannex.features.auth.domain.repositories.AuthRepository
import com.pulse.plannex.features.event.data.repositories.ObjectsRepositoryImlp
import com.pulse.plannex.features.event.data.repositories.SyncPrefsRepositoryImpl
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository
import com.pulse.plannex.features.event.domain.repositories.SyncPrefsRepository
import com.pulse.plannex.features.location.data.repositories.LocationRepositoryImpl
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
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
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindObjectRepository(
        impl: ObjectsRepositoryImlp
    ): ObjectRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        impl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindAccessControlRepository(
        impl: AccessControlRepositoryImpl
    ): AccessControlRepository

    @Binds
    @Singleton
    abstract fun bindSyncPrefsRepository(
        impl: SyncPrefsRepositoryImpl
    ): SyncPrefsRepository

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
