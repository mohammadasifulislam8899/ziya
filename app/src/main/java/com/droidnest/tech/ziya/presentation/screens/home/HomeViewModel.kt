package com.droidnest.tech.ziya.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidnest.tech.ziya.domain.model.Hadith
import com.droidnest.tech.ziya.domain.model.PrayerTime
import com.droidnest.tech.ziya.domain.use_cases.ZiyaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: ZiyaUseCases
) : ViewModel() {

    private val _todayPrayersTime = MutableStateFlow<PrayerTime?>(null)
    val todayPrayerTime = _todayPrayersTime.asStateFlow()

    private val _hadithList = MutableStateFlow<List<Hadith>>(emptyList())
    val hadithList = _hadithList.asStateFlow()
    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()
    val selectedDistrict = useCases.readDistrictUseCase().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
    init {
        observeDistrict()
        loadHadithInInit()
        loadNameInInit()
    }
    private fun loadHadithInInit() {
        viewModelScope.launch {
            loadHadith()
        }
    }
    private fun loadNameInInit() {
        viewModelScope.launch {
            loadName()
        }
    }
    private fun observeDistrict() {
        viewModelScope.launch {
            selectedDistrict.collectLatest { district ->
                val safeDistrict = district ?: "chittagong"
                val result = useCases.todayPrayerTimeUseCase(safeDistrict)
                _todayPrayersTime.value = result
            }
        }
    }
     private suspend fun loadHadith() {
        val hadith = useCases.getRandomHadithUseCase()
        _hadithList.value = hadith
    }

    fun refreshHadith() {
        viewModelScope.launch {
            loadHadith()
        }
    }
    fun saveDistrict(district: String) {
        viewModelScope.launch {
            useCases.saveDistrictUseCase(district)
        }
    }
    fun loadName() {
        viewModelScope.launch {
            useCases.readNameUseCase().collect { nameFromFlow ->
                _name.value = nameFromFlow ?: ""
            }
        }
    }
}
