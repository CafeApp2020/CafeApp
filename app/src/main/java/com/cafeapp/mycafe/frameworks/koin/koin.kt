package com.prof.dz.frameworks.koin

import androidx.room.Room
import com.cafeapp.mycafe.frameworks.room.CafeDataBase
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), CafeDataBase::class.java, "cafedb").build() }
    single { get<CafeDataBase>().categoryDao()}
    single { get<CafeDataBase>().dishesDao()}
}



