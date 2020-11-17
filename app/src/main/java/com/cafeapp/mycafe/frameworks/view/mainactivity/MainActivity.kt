package com.cafeapp.mycafe.frameworks.view.mainactivity

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.cafeapp.mycafe.R
import com.cafeapp.mycafe.use_case.utils.MsgState
import com.cafeapp.mycafe.use_case.utils.SharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_tablelist, R.id.nav_categorylist, R.id.nav_orderlist
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // sharedModel предназначен для получения сообщений от фрагментов и для дальнейшего переключения на фрагмент
        // ответсвенного за обработку данного типа сообщений
        val sharedModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        //
        sharedModel.getSelected().observe(this, { msg ->
          when (msg.stateName) {
             MsgState.ADDCATEGORY -> navController.navigate(R.id.nav_categoryadd)  // открываем фрагмент добавления/редактирования категории
             MsgState.ADDDISH -> navController.navigate(R.id.nav_dishesadd)    // открываем фрагмент добавления/редактирования блюда
             MsgState.DISHESLIST -> navController.navigate(R.id.nav_disheslist)    // открываем фрагмент со списком блюд
             MsgState.CATEGORYLISTOPEN -> navController.navigate(R.id.nav_categorylist) // открываем фрагмент со списком категорий
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