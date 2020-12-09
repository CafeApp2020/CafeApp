package com.prof.dz.frameworks.koin

import androidx.room.Room
import com.cafeapp.mycafe.frameworks.room.CafeDataBase
import com.cafeapp.mycafe.frameworks.room.RoomCategoryDataSource
import com.cafeapp.mycafe.frameworks.room.RoomDishDataSource
import com.cafeapp.mycafe.frameworks.view.categoryadd.CategoryAddFragment
import com.cafeapp.mycafe.frameworks.view.categorylist.CategoryListFragment
import com.cafeapp.mycafe.frameworks.view.dish.DishFragment
import com.cafeapp.mycafe.frameworks.view.dishesadd.DishesAddFragment
import com.cafeapp.mycafe.frameworks.view.dishlist.DishListFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryAddViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryListViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish.DishViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishlist.DishListViewModel
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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), CafeDataBase::class.java, "cafedb3").build() }
    single { get<CafeDataBase>().categoryDao() }
    single { get<CafeDataBase>().dishesDao() }
    single<ICategoryDataSource> { RoomCategoryDataSource(get()) }
    single<ICategoryRepository> { CategoryRepository(get()) }
    single<IDishDataSource> { RoomDishDataSource(get()) }
    single<IDishRepository> { DishRepository(get()) }
}

val categoryListViewModel = module {
    scope(named<CategoryListFragment>()) {
        scoped<ICategoryInteractor> { CategoryInteractor(get()) }
        scoped { CategoryListViewModel(get()) }
        scoped { CategoryAddViewModel(get()) }
    }
}

val categoryViewModel = module {
    scope(named<CategoryAddFragment>()) {
        scoped<ICategoryInteractor> { CategoryInteractor(get()) }
        scoped { CategoryAddViewModel(get()) }
    }
}

val dishListViewModel = module {
    scope(named<DishListFragment>()) {
        scoped<IDishInteractor> { DishInteractor(get()) }
        scoped { DishListViewModel(get()) }
        scoped { DishViewModel(get()) }
    }
}

val dishViewModel = module {
    scope(named<DishFragment>()) {
        scoped<IDishInteractor> { DishInteractor(get()) }
        scoped { DishViewModel(get()) }
    }
}

val dishAddViewModel = module {
    scope(named<DishesAddFragment>()) {
        scoped<IDishInteractor> { DishInteractor(get()) }
        scoped { DishViewModel(get()) }
    }
}