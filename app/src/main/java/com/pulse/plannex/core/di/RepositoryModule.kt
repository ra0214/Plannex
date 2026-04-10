package com.pulse.plannex.core.di

import com.pulse.plannex.core.network.EventApi
import com.pulse.plannex.features.accessControl.data.repositories.AccessControlRepositoryImpl
import com.pulse.plannex.features.accessControl.domain.repositories.AccessControlRepository
import com.pulse.plannex.features.event.data.repositories.ObjectsRepositoryImlp
import com.pulse.plannex.features.event.data.repositories.SyncPrefsRepositoryImpl
import com.pulse.plannex.features.event.domain.repositories.ObjectRepository
import com.pulse.plannex.features.event.domain.repositories.SyncPrefsRepository
import com.pulse.plannex.features.location.data.repositories.LocationRepositoryImpl
import com.pulse.plannex.features.location.domain.repositories.LocationRepository
import com.pulse.plannex.features.event.data.datasource.local.dao.EventDao
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideObjectRepository(api: EventApi, eventDao: EventDao): ObjectRepository {
        return ObjectsRepositoryImlp(api, eventDao)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(fusedLocationClient: FusedLocationProviderClient): LocationRepository {
        return LocationRepositoryImpl(fusedLocationClient)
    }

    @Provides
    @Singleton
    fun provideAccessControlRepository(api: EventApi): AccessControlRepository {
        return AccessControlRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSyncPrefsRepository(impl: SyncPrefsRepositoryImpl): SyncPrefsRepository {
        return impl
    }
}
