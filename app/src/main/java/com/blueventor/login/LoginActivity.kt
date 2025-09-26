package com.blueventor.login

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blueventor.R
import com.blueventor.databinding.ActivityLoginBinding
import com.blueventor.menu.MainActivity
import com.blueventor.network.UiState
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.util.setOnclick
import com.blueventor.util.showAlert
import com.blueventor.util.startNewActivity
import com.blueventor.viewmodel.CheckCompanyDomainViewModel
import com.blueventor.viewmodel.VendorLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    val vendorLoginViewModel: VendorLoginViewModel by viewModels()

    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager.saveString("userAuth", "")



        setOnclick(binding.btnLogin) {
            val email = binding.etEmailPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.equals("")) {
                showAlert("Enter the valid mobile number")
            } else if (password.equals("")) {
                showAlert("Enter the valid password")
            } else {

                lifecycleScope.launch {
                    val request = Requestloginaccess(
                        phone = email,
                        password = password,
                        country_code = "+91",
                        deviceid = "",
                        devicetoken = ""
                    )
                    vendorLoginViewModel.getVendorLoginAccess(request)
                }
            }
        }
//
//        binding!!.btnLogin.setOnClickListener {
//
//
//
//        }


        calLoginAPi()
    }

    private fun calLoginAPi() {
        val userId = sessionManager.getString("_id", "Not Found")

        if (userId != "Not Found" && userId.isNotEmpty()) {
            startNewActivity<MainActivity>()
            finish()
        }
        lifecycleScope.launch {
            vendorLoginViewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {}
                    is UiState.Success<*> -> {
                        val response = state.data as RespondeLoginAccess
                        if (response != null) {
                            logDebugMessage("user_id_testing", response.detail.id.toString())
                            showAlert(response.message)

                            if (response.user_key != null) {

                                sessionManager.saveString("userAuth", response.user_key)

                            }
                            sessionManager.saveString("userAuth", response.user_key)
                            sessionManager.saveString("username", response.detail.name)
                            sessionManager.saveString("_id", response.detail.id.toString())
                            sessionManager.saveString(
                                "company_id",
                                response.detail.company_id.toString()
                            )
                            sessionManager.saveString("email", response.detail.email)
                            sessionManager.saveString("phone", response.detail.phone)
                            sessionManager.saveString(
                                "company_name",
                                response.detail.company_info.company_name
                            )
                            sessionManager.saveString(
                                "company_address",
                                response.detail.company_info.company_address
                            )
                            startNewActivity<MainActivity>()
                            finish()

                        }
                    }

                    is UiState.Error -> {
                        showAlert(state.message)
                    }

                    is UiState.Idle -> {}
                    else -> {}
                }
            }
        }
    }


    fun addValues(x: Int, y: Int, func: (Int, Int) -> Int): Int {
        return func(x, y)

    }
}