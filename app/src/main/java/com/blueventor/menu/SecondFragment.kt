package com.blueventor.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.blueventor.R
import com.blueventor.databinding.FragmentSecondBinding
import com.blueventor.session.SessionManager
import com.blueventor.util.logDebugMessage
import dagger.hilt.android.AndroidEntryPoint
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


logDebugMessage("userAuth",userId)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}