package com.droidnest.tech.ziya.presentation.screens.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidnest.tech.ziya.domain.use_cases.ZiyaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val useCases: ZiyaUseCases
) : ViewModel() {

    private val _userName = MutableStateFlow<String?>(null)
    val userName = _userName.asStateFlow()

    private val _selectedDistrict = MutableStateFlow<String?>(null)
    val selectedDistrict = _selectedDistrict.asStateFlow()

    init {
        viewModelScope.launch {
            _userName.value = useCases.readNameUseCase().first()
            _selectedDistrict.value = useCases.readDistrictUseCase().first()
        }
    }

    fun saveUserName(name: String) {
        viewModelScope.launch {
            useCases.saveNameUseCase(name)
            _userName.value = name
        }
    }

    fun saveSelectedDistrict(district: String) {
        viewModelScope.launch {
            useCases.saveDistrictUseCase(district)
            _selectedDistrict.value = district
        }
    }
}
