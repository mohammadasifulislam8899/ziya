package com.droidnest.tech.ziya.presentation.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidnest.tech.ziya.data.dataStore.PreferencesManager
import com.droidnest.tech.ziya.domain.use_cases.ZiyaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val useCases: ZiyaUseCases
) : ViewModel() {

    private val _themeMode = MutableStateFlow("SYSTEM")
    val themeMode: StateFlow<String> = _themeMode
    val selectedDistrict = useCases.readDistrictUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    init {
        // Load saved theme from DataStore
        viewModelScope.launch {
            useCases.getThemeUseCase().collect { mode ->
                _themeMode.value = mode ?: "SYSTEM"
            }
        }
        loadName()
    }

    fun setTheme(mode: String) {
        viewModelScope.launch {
            _themeMode.value = mode           // update UI immediately
            useCases.saveThemeUseCase(mode)   // save to DataStore
        }
    }

    private fun loadName() {
        viewModelScope.launch {
            useCases.readNameUseCase().collect { nameFromFlow ->
                _name.value = nameFromFlow ?: ""
            }
        }
    }
    fun saveName(name: String) {
        viewModelScope.launch {
            useCases.saveNameUseCase(name=name)
        }
    }
    fun saveDistrict(district: String) {
        viewModelScope.launch {
            useCases.saveDistrictUseCase(district)
        }
    }
}
