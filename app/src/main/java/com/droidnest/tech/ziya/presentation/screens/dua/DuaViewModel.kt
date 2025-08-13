package com.droidnest.tech.ziya.presentation.screens.dua

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidnest.tech.ziya.domain.model.Dua
import com.droidnest.tech.ziya.domain.use_cases.ZiyaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DuaViewModel @Inject constructor(
    private val useCases: ZiyaUseCases
) : ViewModel() {

    private val _dailyDuas = MutableStateFlow<List<Dua>>(emptyList())
    val dailyDuas = _dailyDuas.asStateFlow()

    private val _namazDuas = MutableStateFlow<List<Dua>>(emptyList())
    val namazDuas = _namazDuas.asStateFlow()

    init {
        loadDailyDuaInInit()
        loadNamazDuasInInit()
    }

    fun loadDailyDuaInInit() {
        viewModelScope.launch {
            loadDailyDuas()
        }
    }

    fun loadNamazDuasInInit() {
        viewModelScope.launch {
            loadNamazDuas()
        }
    }

    fun loadDailyDuas() {
        viewModelScope.launch {
            val duas = useCases.getDailyDuasUseCase()
            _dailyDuas.value = duas
        }
    }

    fun loadNamazDuas() {
        viewModelScope.launch {
            val duas = useCases.getNamazDuasUseCase()
            _namazDuas.value = duas
        }
    }
}
