package com.cafeapp.mycafe.application

import android.app.Application
import com.prof.dz.frameworks.koin.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CafeApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CafeApp)
            modules(
                listOf(
                    application,
                    categoryListViewModel,
                    categoryViewModel,
                    dishViewModel,
                    dishListViewModel,
                    dishAddViewModel,
                    orderListViewModel,
                    deliveryAddViewModel,
                    takeawayAddViewModel,
                    tableViewModel
                )
            )
        }
    }
}