package com.prof.dz.frameworks.koin

import androidx.room.Room
import com.cafeapp.mycafe.frameworks.room.CafeDataBase
import com.cafeapp.mycafe.frameworks.room.CategoryDataSource
import com.cafeapp.mycafe.frameworks.room.DishDataSource
import com.cafeapp.mycafe.frameworks.room.OrderDataSource
import com.cafeapp.mycafe.frameworks.view.menu.categoryadd.CategoryAddFragment
import com.cafeapp.mycafe.frameworks.view.menu.categorylist.CategoryListFragment
import com.cafeapp.mycafe.frameworks.view.orders.delivery.DeliveryMainFragment
import com.cafeapp.mycafe.frameworks.view.menu.dish.DishFragment
import com.cafeapp.mycafe.frameworks.view.menu.dishesadd.DishesAddFragment
import com.cafeapp.mycafe.frameworks.view.menu.dishlist.DishListFragment
import com.cafeapp.mycafe.frameworks.view.orders.orderList.OrderListFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.orders.OrderViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.DishViewModel
import com.cafeapp.mycafe.use_case.data.*
import com.cafeapp.mycafe.use_case.interactors.categories.CategoryInteractor
import com.cafeapp.mycafe.use_case.interactors.categories.ICategoryInteractor
import com.cafeapp.mycafe.use_case.interactors.dishes.DishInteractor
import com.cafeapp.mycafe.use_case.interactors.dishes.IDishInteractor
import com.cafeapp.mycafe.use_case.interactors.orders.IOrderInteractor
import com.cafeapp.mycafe.use_case.interactors.orders.OrderInteractor
import com.cafeapp.mycafe.use_case.repositories.ICategoryRepository
import com.cafeapp.mycafe.use_case.repositories.IDishRepository
import com.cafeapp.mycafe.use_case.repositories.IOrderRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), CafeDataBase::class.java, "cafedb7").build() }
    single { get<CafeDataBase>().categoryDao() }
    single { get<CafeDataBase>().dishesDao() }
    single { get<CafeDataBase>().orderDao() }
    single<ICategoryDataSource> { CategoryDataSource(get()) }
    single<ICategoryRepository> { CategoryRepository(get()) }
    single<IDishDataSource> { DishDataSource(get()) }
    single<IDishRepository> { DishRepository(get()) }
    single<IOrderDataSource> { OrderDataSource(get()) }
    single<IOrderRepository> { OrderRepository(get()) }
}

val categoryListViewModel = module {
    scope(named<CategoryListFragment>()) {
        scoped<ICategoryInteractor> { CategoryInteractor(get()) }
        scoped { CategoryViewModel(get()) }
    }
}

val categoryViewModel = module {
    scope(named<CategoryAddFragment>()) {
        scoped<ICategoryInteractor> { CategoryInteractor(get()) }
        scoped { CategoryViewModel(get()) }
    }
}

val dishListViewModel = module {
    scope(named<DishListFragment>()) {
        scoped<IDishInteractor> { DishInteractor(get()) }
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

val orderListViewModel = module {
    scope(named<OrderListFragment>()) {
        scoped<IOrderInteractor> { OrderInteractor(get()) }
        scoped { OrderViewModel(get()) }
    }
}

val deliveryAddViewModel = module {
    scope(named<DeliveryMainFragment>()) {
        scoped<IOrderInteractor> { OrderInteractor(get()) }
        scoped { OrderViewModel(get()) }
    }
}