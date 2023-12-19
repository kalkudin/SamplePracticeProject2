package com.example.samplepracticeproject2

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

    // StateFlow to observe saved credentials
    private val _savedCredentials = MutableStateFlow<LoginInfo?>(null)
    val savedCredentials: StateFlow<LoginInfo?> get() = _savedCredentials

    // StateFlow to observe retrieved email and password
    private val _retrievedCredentials = MutableStateFlow<LoginInfo?>(null)
    val retrievedCredentials: StateFlow<LoginInfo?> get() = _retrievedCredentials

    fun saveCredentials(email: String, password: String) {
        viewModelScope.launch {
            PreferencesDataStoreManager.writeEmailAndPassword(email, password)
        }
    }

    fun retrieveCredentials() {
        viewModelScope.launch {
            Log.d("SharedViewModel", "retrieveCredentials: Start")
            PreferencesDataStoreManager.getEmail().collect { retrievedEmail ->
                Log.d("SharedViewModel", "retrieveCredentials: Retrieved email: $retrievedEmail")
                PreferencesDataStoreManager.getPassword().collect { retrievedPassword ->
                    Log.d(
                        "SharedViewModel",
                        "retrieveCredentials: Retrieved password: $retrievedPassword"
                    )
                    _retrievedCredentials.value = LoginInfo(retrievedEmail, retrievedPassword)
                    Log.d("SharedViewModel", "retrieveCredentials: Updated _retrievedCredentials")
                }
            }
            Log.d("SharedViewModel", "retrieveCredentials: End")
        }
    }

    fun clearSession() {
        viewModelScope.launch {
            PreferencesDataStoreManager.clearSession()
            _savedCredentials.value = null

            Log.d("SharedViewModel", "Session cleared")
        }
    }

    fun checkSavedCredentials() {
        viewModelScope.launch {
            Log.d("SharedViewModel", "checkSavedCredentials: Checking saved credentials")
            PreferencesDataStoreManager.getEmail().collect { email ->
                PreferencesDataStoreManager.getPassword().collect { password ->
                    val credentialsExist = email.isNotEmpty() && password.isNotEmpty()
                    _savedCredentials.value = if (credentialsExist) LoginInfo(email, password) else null
                    Log.d(
                        "SharedViewModel",
                        "checkSavedCredentials: Saved credentials checked. Exist: $credentialsExist"
                    )
                }
            }
        }
    }
}
