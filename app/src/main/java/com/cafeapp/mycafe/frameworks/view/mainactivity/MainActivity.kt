package com.cafeapp.mycafe.frameworks.view.mainactivity

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navController = findNavController(R.id.nav_host_fragment)
        val navView: NavigationView = findViewById(R.id.nav_view)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_tablelist, R.id.nav_categorylist, R.id.nav_orderlist
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // sharedModel предназначен для получения сообщений от фрагментов и для дальнейшего переключения на фрагмент
        // ответсвенного за обработку данного типа сообщений
        val sharedModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        sharedModel.getSelected().observe(this, { msg ->
            when (msg.stateName) {
                MsgState.ADDCATEGORY -> navController.navigate(R.id.nav_categoryadd)  // открываем фрагмент добавления/редактирования категории
                MsgState.ADDDISH -> navController.navigate(R.id.nav_dishesadd)    // открываем фрагмент добавления/редактирования блюда
                MsgState.CATEGORYLISTOPEN -> navController.navigate(R.id.nav_categorylist) // открываем фрагмент со списком категорий
                MsgState.DISH -> navController.navigate(R.id.nav_dish) // открываем фрагмент с блюдом
                MsgState.DISHESLIST -> navController.navigate(R.id.nav_disheslist)    // открываем фрагмент со списком блюд
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}