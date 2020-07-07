package com.jibergroup.arabus.di

import com.google.gson.Gson
import com.jibergroup.arabus.data.db.DatabaseHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {

    //room persistence library
    single { DatabaseHelper.getInstance(androidContext()) }
    single { get<DatabaseHelper>().wordsDao() }

    //gson
    single { Gson() }
}