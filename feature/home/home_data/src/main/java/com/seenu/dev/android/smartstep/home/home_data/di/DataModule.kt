package com.seenu.dev.android.smartstep.home.home_data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.seenu.dev.android.smartstep.home.home_data.BatteryOptimizationRepositoryImpl
import com.seenu.dev.android.smartstep.home.home_data.PreferenceManagerImpl
import com.seenu.dev.android.smartstep.home.home_data.StepRepositoryImpl
import com.seenu.dev.android.smartstep.home.home_data.dataStore
import com.seenu.dev.android.smartstep.home.home_data.local.StepDatabase
import com.seenu.dev.android.smartstep.home.home_domain.BatteryOptimizationRepository
import com.seenu.dev.android.smartstep.home.home_domain.PreferenceManager
import com.seenu.dev.android.smartstep.home.home_domain.StepRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val homeDataModule = module {
    single<DataStore<Preferences>> { androidContext().dataStore }

    single<PreferenceManager> { PreferenceManagerImpl(get()) }

    single<BatteryOptimizationRepository> { BatteryOptimizationRepositoryImpl(androidContext()) }

    single {
        Room.databaseBuilder(
            androidContext(),
            StepDatabase::class.java,
            "smartstep_db"
        ).build()
    }

    single { get<StepDatabase>().stepDao }

    single<StepRepository> { StepRepositoryImpl(stepDao = get()) }
}