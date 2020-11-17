package com.cafeapp.mycafe.interface_adapters.viewmodels.categories

import com.less.repository.db.room.CategoryEntity

class CategoryAddViewState(val category: CategoryEntity? = null,
                           val error: Throwable? = null,
                           val delOk:Boolean=false,
                           val delError:Throwable?=null,
                           val saveOk:Boolean=false,
                           val loadOk:Boolean=false,
                           val saveErr:Throwable?=null) {
}