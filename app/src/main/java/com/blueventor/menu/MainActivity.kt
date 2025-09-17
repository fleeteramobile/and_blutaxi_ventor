package com.blueventor.menu


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.blueventor.R
import com.blueventor.databinding.ActivityMainBinding
import com.blueventor.login.LoginActivity
import com.blueventor.session.SessionManager
import com.blueventor.splash.SplashActivity
import com.blueventor.util.startNewActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var companyName: TextView

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // DrawerLayout setup
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.SecondFragment, R.id.driverDetailsFragment, R.id.cabsListFragment, R.id.tripListFragment), // top-level destinations
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)
//company_address
        // Status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        val headerView: View =
            binding.navigationView.getHeaderView(0)

        companyName =  headerView.findViewById(R.id.companyName)
       val company_name = sessionManager.getString("company_name", "Not Found")

        companyName.setText(company_name)

        // Light status bar icons (optional)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                  navController.navigate(R.id.SecondFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_profile -> {
                    navController.navigate(R.id.driverDetailsFragment)
                    // add your logout logic
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_settings -> {
                    navController.navigate(R.id.cabsListFragment)
                    // add your logout logic
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_trip -> {
                    navController.navigate(R.id.tripListFragment)
                    // add your logout logic
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_logout -> {
                    sessionManager.clearSession()
                    lifecycleScope.launch {
                        delay(100)
                        startNewActivity<SplashActivity>()
                        finish()
                    }

                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(menuItem, navController)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }



    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun toggleDrawer() {
        val drawer = binding.drawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.openDrawer(GravityCompat.START)
        }


    }



}
