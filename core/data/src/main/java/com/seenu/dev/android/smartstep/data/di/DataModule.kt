package com.seenu.dev.android.smartstep.data.di

import com.seenu.dev.android.smartstep.data.PermissionRepositoryImpl
import com.seenu.dev.android.smartstep.domain.PermissionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreDataModule = module {
    single<PermissionRepository> { PermissionRepositoryImpl(androidContext()) }

}