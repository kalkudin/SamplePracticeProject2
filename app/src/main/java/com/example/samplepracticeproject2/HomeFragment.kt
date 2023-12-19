package com.example.samplepracticeproject2

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.samplepracticeproject2.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun setUp() {
        setLogoutClickListener()

        // Retrieve credentials when HomeFragment is created
        sharedViewModel.retrieveCredentials()

        // Observe the retrieved credentials using SharedViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.retrievedCredentials.collect { loginInfo ->
                    loginInfo?.let { info ->
                        binding.email.text = info.userName
                        binding.password.text = info.password
                    }
                }
            }
        }
    }

    private fun setLogoutClickListener() {
        binding.logout.setOnClickListener {
            sharedViewModel.clearSession()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }
}

