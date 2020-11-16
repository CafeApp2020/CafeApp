package com.prof.dz.frameworks.koin

import androidx.room.Room
import com.cafeapp.mycafe.frameworks.room.CafeDataBase
import com.cafeapp.mycafe.frameworks.room.RoomCategoryDataSource
import com.cafeapp.mycafe.frameworks.room.RoomDishDataSource
import com.cafeapp.mycafe.frameworks.view.categoryadd.CategoryAddFragment
import com.cafeapp.mycafe.frameworks.view.categorylist.CategoryListFragment
import com.cafeapp.mycafe.frameworks.view.dishesadd.DishesAddFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryAddViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryListViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.DishesAddViewModel
import com.cafeapp.mycafe.use_case.data.CategoryRepository
import com.cafeapp.mycafe.use_case.data.DishRepository
import com.cafeapp.mycafe.use_case.data.ICategoryDataSource
import com.cafeapp.mycafe.use_case.data.IDishDataSource
import com.cafeapp.mycafe.use_case.interactors.categories.CategoryInteractor
import com.cafeapp.mycafe.use_case.interactors.categories.ICategoryInteractor
import com.cafeapp.mycafe.use_case.interactors.dishes.DishInteractor
import com.cafeapp.mycafe.use_case.interactors.dishes.IDishInteractor
import com.cafeapp.mycafe.use_case.repositories.ICategoryRepository
import com.cafeapp.mycafe.use_case.repositories.IDishRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), CafeDataBase::class.java, "cafedb").build() }
    single { get<CafeDataBase>().categoryDao()}
    single { get<CafeDataBase>().dishesDao()}
    single<ICategoryDataSource>{RoomCategoryDataSource(get())}
    single<ICategoryRepository>{CategoryRepository(get()) }
    single<IDishDataSource>{RoomDishDataSource(get())}
    single<IDishRepository>{DishRepository(get())}
}

val categoryListViewModel = module {
    scope(named<CategoryListFragment>()) {
        scoped<ICategoryInteractor> { CategoryInteractor(get()) }
        scoped { CategoryListViewModel(get()) }
    }
}

val categoryViewModel = module {
    scope(named<CategoryAddFragment>()) {
        scoped<ICategoryInteractor> { CategoryInteractor(get()) }
        scoped { CategoryAddViewModel(get()) }
    }
}

val dishViewModel = module {
   scope(named<DishesAddFragment>()) {
        scoped<IDishInteractor>{DishInteractor(get()) }
        scoped { DishesAddViewModel(get()) }
    }
}


