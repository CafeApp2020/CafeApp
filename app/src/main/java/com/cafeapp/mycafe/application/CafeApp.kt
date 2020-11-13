package com.cafeapp.mycafe.application

import android.app.Application
import com.prof.dz.frameworks.koin.application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CafeApp : Application()  {

    override fun onCreate() {
        super.onCreate()

  startKoin {
            androidContext(applicationContext)
            modules(listOf (application))
        }

     //   startKoin { androidContext(this@CafeApp) }
    }
}
