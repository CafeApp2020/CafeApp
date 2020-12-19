package com.cafeapp.mycafe.frameworks.view.mainactivity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.frameworks.view.utils.BottomNavigationDrawerFragment
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(bottom_app_bar)

        navController = findNavController(R.id.nav_host_fragment)
        toolbar.setupWithNavController(navController)

        // sharedModel предназначен для получения сообщений от фрагментов и для дальнейшего переключения на фрагмент
        // ответсвенного за обработку данного типа сообщений
        val sharedModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        sharedModel.getSelected().observe(this) { msg ->
            when (msg.stateName) {
                MsgState.ADDCATEGORY -> navController.navigate(R.id.nav_categoryadd)  // открываем фрагмент добавления/редактирования категории
                MsgState.ADDDISH -> navController.navigate(R.id.nav_dishesadd)    // открываем фрагмент добавления  блюда
                MsgState.EDITDISH -> navController.navigate(R.id.nav_dishesadd)  // открываем фрагмент  редактирования блюда
                MsgState.CATEGORYLISTOPEN -> navController.navigate(R.id.nav_categorylist) // открываем фрагмент со списком категорий
                MsgState.OPENDISH -> navController.navigate(R.id.nav_dish) // открываем фрагмент с блюдом
                MsgState.DISHESLIST -> navController.navigate(R.id.nav_disheslist)    // открываем фрагмент со списком блюд
                MsgState.SETTOOLBARTITLE -> setTitle(msg.value)
                MsgState.DELIVERYADD -> navController.navigate(R.id.nav_deliveryadd)  // новая доставка
                MsgState.DELEVERYOPEN -> navController.navigate(R.id.nav_deliveryadd)  // открываем существующую доставку
                MsgState.TAKEAWAYADD -> navController.navigate(R.id.nav_takeawayadd) // новый заказ на вынос
                MsgState.TAKEAWAYOPEN -> navController.navigate(R.id.nav_takeawayadd) // открываем существующий заказ на вынос
                MsgState.SELECTDISHTOORDER -> navController.navigate(R.id.nav_categorylist) // открываем фрагмент со списком категорий в режиме добавления блюд к заказу
                MsgState.OPENFORORDER -> navController.navigate(R.id.nav_disheslist) // открываем фрагмент со списком блюд в режиме заказа
                MsgState.RETURNSELECTEDDISHLIST -> navController.navigate(R.id.nav_orderlist)
            }
        }
    }

    private fun setTitle(name: Any) {
        if (name is String)
            toolbar.title = name
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.nav_categorylist -> navController.navigate(R.id.nav_categorylist)
            R.id.nav_tablelist -> navController.navigate(R.id.nav_tablelist)
            R.id.nav_orderlist -> navController.navigate(R.id.nav_orderlist)
            android.R.id.home -> {
                val bottomNavDrawerFragment = BottomNavigationDrawerFragment()
                bottomNavDrawerFragment.show(supportFragmentManager, bottomNavDrawerFragment.tag)
            }
        }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}