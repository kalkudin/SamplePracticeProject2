package com.example.samplepracticeproject2

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.samplepracticeproject2.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun setUp() {
        Log.d("LoginFragment", "setUp: Checking saved credentials")

        // Observe saved credentials using SharedViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.checkSavedCredentials()
            }
        }

        binding.submit.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            sharedViewModel.saveCredentials(email, password)
            navigateToHomeFragment()
        }
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
    }
}
