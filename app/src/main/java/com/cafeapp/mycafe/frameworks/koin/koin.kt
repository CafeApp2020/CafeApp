package com.prof.dz.frameworks.koin

import androidx.room.Room
import com.cafeapp.mycafe.frameworks.room.CafeDataBase
import com.cafeapp.mycafe.frameworks.room.RoomCategoryDataSource
import com.cafeapp.mycafe.frameworks.room.RoomDishDataSource
import com.cafeapp.mycafe.frameworks.room.RoomOrderDataSource
import com.cafeapp.mycafe.frameworks.view.categoryadd.CategoryAddFragment
import com.cafeapp.mycafe.frameworks.view.categorylist.CategoryListFragment
import com.cafeapp.mycafe.frameworks.view.deliveryadd.DeliveryAddFragment
import com.cafeapp.mycafe.frameworks.view.dish.DishFragment
import com.cafeapp.mycafe.frameworks.view.dishesadd.DishesAddFragment
import com.cafeapp.mycafe.frameworks.view.dishlist.DishListFragment
import com.cafeapp.mycafe.frameworks.view.orderlist.OrderListFragment
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryAddViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.categories.CategoryListViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.delivery.DeliveryAddViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dish.DishViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.dishes.dishlist.DishListViewModel
import com.cafeapp.mycafe.interface_adapters.viewmodels.orderslist.OrderListViewModel
import com.cafeapp.mycafe.use_case.data.*
import com.cafeapp.mycafe.use_case.interactors.categories.CategoryInteractor
import com.cafeapp.mycafe.use_case.interactors.categories.ICategoryInteractor
import com.cafeapp.mycafe.use_case.interactors.dishes.DishInteractor
import com.cafeapp.mycafe.use_case.interactors.dishes.IDishInteractor
import com.cafeapp.mycafe.use_case.interactors.orderlist.IOrderInteractor
import com.cafeapp.mycafe.use_case.interactors.orderlist.OrderInteractor
import com.cafeapp.mycafe.use_case.repositories.ICategoryRepository
import com.cafeapp.mycafe.use_case.repositories.IDishRepository
import com.cafeapp.mycafe.use_case.repositories.IOrderRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val application = module {
    single { Room.databaseBuilder(get(), CafeDataBase::class.java, "cafedb5").build() }
    single { get<CafeDataBase>().categoryDao() }
    single { get<CafeDataBase>().dishesDao() }
    single { get<CafeDataBase>().orderDao()}
    single<ICategoryDataSource> { RoomCategoryDataSource(get()) }
    single<ICategoryRepository> { CategoryRepository(get()) }
    single<IDishDataSource> { RoomDishDataSource(get()) }
    single<IDishRepository> { DishRepository(get()) }
    single<IOrderDataSource> { RoomOrderDataSource(get()) }
    single<IOrderRepository> { OrderRepository(get()) }
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

 val orderListViewModel = module {
    scope(named<OrderListFragment>()) {
        scoped<IOrderInteractor> { OrderInteractor(get()) }
        scoped { OrderListViewModel(get()) }
}
}

val deliveryAddViewModel = module {
    scope(named<DeliveryAddFragment>()) {
        scoped<IOrderInteractor> {OrderInteractor(get()) }
        scoped { DeliveryAddViewModel(get()) }
    }
}
