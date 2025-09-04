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
import com.blueventor.databinding.FragmentSecondBinding
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestDashBoardDetails
import com.blueventor.network.request.Requestloginaccess
import com.blueventor.network.response.RespondeLoginAccess
import com.blueventor.network.response.ResponseDashBoardDetails
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import com.blueventor.util.showAlert
import com.blueventor.viewmodel.DashboardViewModel
import com.blueventor.viewmodel.VendorLoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class SecondFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val dashboardViewModel: DashboardViewModel by viewModels()
var company_id = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = sessionManager.getString("userAuth", "Not Found")
         company_id = sessionManager.getString("company_id", "Not Found")


logDebugMessage("userAuth",userId)
loadDashBoadApi()
        getDashBoadDetails()

    }

    private fun getDashBoadDetails() {
        lifecycleScope.launch {
            dashboardViewModel.uiState.collect { state ->
                when (state) {
                    is UiState.Loading -> {}
                    is UiState.Success<*> -> {
                        val response = state.data as ResponseDashBoardDetails
                        if (response != null) {

                            logDebugMessage("active_driver",response.count.approved_driver)
                            binding.totalAmount.setText(response.count.total_amount.toString())
                            binding.cashPayment.setText(response.count.cash_payment.toString())
                            binding.activeDriver.setText(response.count.active_driver.toString())
                            binding.inactiveDriver.setText(response.count.inactive_driver.toString())
                            binding.blockedDriver.setText(response.count.blocked_driver.toString())

                            binding.cashPayment.setText(response.count.cash_payment.toString())
                            binding.cardPayment.setText(response.count.card_payment.toString())
                            binding.discoundPayment.setText(response.count.total_amount.toString())

                        }
                    }

                    is UiState.Error -> {
                        requireActivity().showAlert(state.message )
                    }
                    is UiState.Idle -> {}
                    else -> {}
                }
            }
        }    }

    private fun loadDashBoadApi() {
        lifecycleScope.launch {
            val request = RequestDashBoardDetails(
                company_id = "3",
                start_date = "30-07-2025",
                end_date = "22-08-2025"

            )
            dashboardViewModel.getDashBoardDetails(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}