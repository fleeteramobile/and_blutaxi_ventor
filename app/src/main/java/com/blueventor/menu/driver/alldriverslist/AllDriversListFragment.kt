package com.blueventor.menu.driver.alldriverslist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blueventor.R
import com.blueventor.databinding.FragmentAllDriversListBinding
import com.blueventor.menu.MainActivity
import com.blueventor.network.UiState
import com.blueventor.network.request.RequestGetAllDriverList
import com.blueventor.session.SessionManager
import com.blueventor.util.hideView
import com.blueventor.util.logDebugMessage
import com.blueventor.util.setImageOnclick
import com.blueventor.util.setOnclick
import com.blueventor.util.showView
import com.blueventor.network.viewmodel.AllDriverListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AllDriversListFragment : Fragment(), ShowAllDriverDetails {

    @Inject
    lateinit var sessionManager: SessionManager
    lateinit var _binding: FragmentAllDriversListBinding
    private val binding get() = _binding!!

    val tripDetailsViewModel: AllDriverListViewModel by viewModels()
    private lateinit var tripObserver: AllDriverLifecycleobserver
    private var companyId = ""
    private var tripId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tripId = sessionManager.getString("trip_id", "Not Found")
        companyId = sessionManager.getString("company_id", "Not Found")

        tripObserver = AllDriverLifecycleobserver(
            tripDetailsViewModel,
            companyId,
            tripId,
            lifecycleScope
        )
        lifecycle.addObserver(tripObserver)

        observegetAllDriverList()

    }

    private fun observegetAllDriverList() {
        lifecycleScope.launch {
            tripDetailsViewModel.uiState.collect { state ->

                when (state) {
                    is UiState.Error -> {
                        _binding.recyclerViewAllDrivers.hideView()

                        _binding.noData.showView()
                    }

                    UiState.Idle -> {

                    }

                    UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        logDebugMessage("alldriversListTesting","working_inside")
                     //   val response = it.data as ResponseAllDriverList
                        val response = state.data

                        if (response.status == 1) {
                            _binding.noData.hideView()
                            _binding.recyclerViewAllDrivers.showView()
                            _binding.recyclerViewAllDrivers.apply {
                                layoutManager =
                                    LinearLayoutManager(
                                        requireActivity()
                                    )
                                adapter =
                                    AllDriverListAdapter(this@AllDriversListFragment, response.data)

                            }

                        } else {
                            _binding.recyclerViewAllDrivers.hideView()

                            _binding.noData.showView()

                        }
                    }
                }

            }

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAllDriversListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setImageOnclick(binding.ivBack)
        {
            (activity as? MainActivity)?.toggleDrawer()
        }
    }

    class AllDriverLifecycleobserver(
        private val viewModel: AllDriverListViewModel,
        private val companyId: String,
        private val tripId: String,
        private val lifecycleScope: LifecycleCoroutineScope
    ) : DefaultLifecycleObserver {
        private var job: Job? = null

        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
        }

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
        }

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            lifecycleScope.launch {
                val request = RequestGetAllDriverList(
                    company_id = companyId,

                    )
                viewModel.getDriverDetails(request)
            }

        }

        override fun onPause(owner: LifecycleOwner) {
            super.onPause(owner)
        }

        override fun onStop(owner: LifecycleOwner) {
            super.onStop(owner)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
        }
    }

    override fun showDriverDetails(data: ResponseAllDriverList.Data) {
        sessionManager.saveString("driver_id",data.id.toString())
        findNavController().navigate(R.id.driveDashBoardFragment)
    }

    override fun callDriver(responseData: ResponseAllDriverList.Data) {

    }


}
