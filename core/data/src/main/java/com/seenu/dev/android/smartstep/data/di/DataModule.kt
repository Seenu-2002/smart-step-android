package com.seenu.dev.android.smartstep.data.di

import com.seenu.dev.android.smartstep.data.PermissionRepositoryImpl
import com.seenu.dev.android.smartstep.data.repository.UserConfigRepositoryImpl
import com.seenu.dev.android.smartstep.domain.repository.PermissionRepository
import com.seenu.dev.android.smartstep.domain.repository.UserConfigRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    single<PermissionRepository> { PermissionRepositoryImpl(androidContext()) }
    single<UserConfigRepository> { UserConfigRepositoryImpl(androidContext()) }
}