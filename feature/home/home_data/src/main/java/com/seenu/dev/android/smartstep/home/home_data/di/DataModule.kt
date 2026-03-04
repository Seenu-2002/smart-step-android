package com.seenu.dev.android.smartstep.home.home_data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.seenu.dev.android.smartstep.home.home_data.BatteryOptimizationRepositoryImpl
import com.seenu.dev.android.smartstep.home.home_data.PreferenceManagerImpl
import com.seenu.dev.android.smartstep.home.home_data.dataStore
import com.seenu.dev.android.smartstep.home.home_domain.BatteryOptimizationRepository
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val homeDataModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }

    single<PreferenceManager> { PreferenceManagerImpl(get()) }

    single<BatteryOptimizationRepository> { BatteryOptimizationRepositoryImpl(androidContext()) }

}