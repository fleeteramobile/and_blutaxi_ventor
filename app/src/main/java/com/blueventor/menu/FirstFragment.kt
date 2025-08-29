package com.blueventor.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.blueventor.R
import com.blueventor.databinding.FragmentFirstBinding
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestCheckCompanyDomain
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.util.showAlert
import com.blueventor.util.startNewActivity
import com.blueventor.viewmodel.VendorLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    val vendorLoginViewModel: VendorLoginViewModel by viewModels()
    @Inject
    lateinit var sessionManager: SessionManager
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = sessionManager.getString("_id", "Not Found")

        if (userId != "Not Found" && userId.isNotEmpty()) {
           findNavController().navigate(R.id.SecondFragment )
        }
        lifecycleScope.launch {
            vendorLoginViewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {}
                    is UiState.Success<*> -> {
                        val response = state.data as RespondeLoginAccess
                        if (response != null) {
                            logDebugMessage("user_id_testing",response.detail.id.toString())
                            requireActivity().showAlert(response.message)

                            if (response.user_key!=null)
                            {

                                sessionManager.saveString("userAuth",response.user_key)

                            }
                            sessionManager.saveString("userAuth",response.user_key)
                            sessionManager.saveString("username",response.detail.name)
                            sessionManager.saveString("_id",response.detail.id.toString())
                            sessionManager.saveString("company_id",response.detail.company_id.toString())
                            sessionManager.saveString("email",response.detail.email)
                            sessionManager.saveString("phone",response.detail.phone)
                            findNavController().navigate(R.id.SecondFragment )

                        }
                    }

                    is UiState.Error -> {
                        requireActivity().showAlert(state.message )
                    }
                    is UiState.Idle -> {}
                    else -> {}
                }
            }
        }

        _binding!!.btnLogin.setOnClickListener {
            val email = binding.etEmailPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            if (email.equals("")) {
                requireActivity().showAlert("Enter the valid mobile number")
            } else if (password.equals("")) {
                requireActivity().showAlert("Enter the valid password")
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}