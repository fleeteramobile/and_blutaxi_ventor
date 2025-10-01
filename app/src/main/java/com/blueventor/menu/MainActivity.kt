package com.blueventor.menu


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
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
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDriverRecharge
import com.blueventor.network.response.ResponseDriverWalletRecharge
import com.blueventor.network.viewmodel.DriverRechargeViewModel
import com.blueventor.session.SessionManager
import com.blueventor.splash.SplashActivity
import com.blueventor.util.showAlert
import com.blueventor.util.startNewActivity
import com.google.android.material.navigation.NavigationView
import com.razorpay.PaymentResultListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PaymentResultListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var companyName: TextView
    val driverRechargeViewModel: DriverRechargeViewModel by viewModels()
    var driver_id = ""

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // DrawerLayout setup
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.SecondFragment,
                R.id.driverDetailsFragment,
                R.id.cabsListFragment,
                R.id.tripListFragment,
                R.id.nav_triptrack
            ), // top-level destinations
            binding.drawerLayout
        )
        loadBalance()
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigationView.setupWithNavController(navController)
//company_address
        // Status bar color
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
        val headerView: View =
            binding.navigationView.getHeaderView(0)

        companyName = headerView.findViewById(R.id.companyName)
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
                    navController.navigate(R.id.allDriversListFragment)
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

                R.id.nav_triptrack -> {
                    navController.navigate(R.id.driverCurrentLocationFragment)
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

    override fun onPaymentSuccess(p0: String?) {

        driver_id = sessionManager.getString("driver_id", "Not Found")
        val amt = sessionManager.getString("recharge_amount", "Not Found")


        driverRechargeViewModel.getDriverDetails(
            RequestDriverRecharge(
                driver_id = driver_id,
                transaction_id = p0!!,
                recharge_amount = amt


            )
        )
    }

    override fun onPaymentError(p0: Int, message: String?) {
        println("Payment_Error $message")

    }

    private fun loadBalance() {
        lifecycleScope.launch {
            driverRechargeViewModel.uiState.collect { cardetails ->
                when (cardetails) {
                    is UiState.Error -> {

                    }

                    UiState.Idle -> {

                    }

                    UiState.Loading -> {}
                    is UiState.Success -> {
                        val response = cardetails.data as ResponseDriverWalletRecharge
                        if (response != null) {
                            showAlert(response.message)

                            val navController =
                                findNavController(R.id.nav_host_fragment_content_main)
                            navController.navigate(R.id.driverRechargeFragment)
                        }
                    }
                }
            }
        }
    }
}
