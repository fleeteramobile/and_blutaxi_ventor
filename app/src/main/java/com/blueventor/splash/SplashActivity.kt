package com.blueventor.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.blueventor.R
import com.blueventor.menu.MainActivity
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestCheckCompanyDomain
import com.blueventor.session.SessionManager
import com.blueventor.util.startNewActivity
import com.blueventor.viewmodel.CheckCompanyDomainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: CheckCompanyDomainViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color) // replace with your color

        // Optional: set light or dark status bar icons
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        lifecycleScope.launch {
            delay(3000)
            startNewActivity<MainActivity>()
            finish()
        }

//        val userId = sessionManager.getString("_id", "Not Found")
//        if (userId != "Not Found" && userId.isNotEmpty()) {
//            lifecycleScope.launch {
//                delay(3000)
//                startNewActivity<MainActivity>()
//                finish()
//            }
//        } else {
//            sessionManager.saveString("authkey", "")
//            sessionManager.saveString("userAuth", "")
//            val request = RequestCheckCompanyDomain(
//                company_domain = "devbluetaxi",
//                company_main_domain = "devbluetaxi",
//                device_type = "1"
//            )
//            viewModel.checkCompanyDomain(request)
//            lifecycleScope.launch {
//                viewModel.uiState.collect { state ->
//                    when (state) {
//                        is UiState.Loading -> {}
//                        is UiState.Success<*> -> {
//                            val response = state.data
//                            if (response != null)
//                            {
//                                lifecycleScope.launch {
//                                    delay(3000)
//                                    startNewActivity<MainActivity>()
//                                    finish()
//                                }
//
//                            }
//                        }
//
//                        is UiState.Error -> {}
//                        is UiState.Idle -> {}
//                        else -> {}
//                    }
//                }
//
//
//            }
//        }


    }
}