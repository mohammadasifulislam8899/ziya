package com.droidnest.tech.ziya.presentation.screens.surah

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.droidnest.tech.ziya.domain.model.Surah
import com.droidnest.tech.ziya.domain.model.SurahInfo
import com.droidnest.tech.ziya.domain.use_cases.ZiyaUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuranViewModel @Inject constructor(
    private val ziyaUseCases: ZiyaUseCases,
) : ViewModel() {

    private val _surahInfoList = MutableStateFlow<List<SurahInfo>>(emptyList())
    val surahInfoList = _surahInfoList.asStateFlow()

    private val _surahDetails = MutableStateFlow(Surah())
    val surahDetails = _surahDetails.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Loading states
    private val _isLoadingSurahList = MutableStateFlow(false)
    val isLoadingSurahList = _isLoadingSurahList.asStateFlow()

    private val _isLoadingSurahDetails = MutableStateFlow(false)
    val isLoadingSurahDetails = _isLoadingSurahDetails.asStateFlow()

    init {
        loadSurahs()
    }

    private fun loadSurahs() {
        viewModelScope.launch {
            try {
                _isLoadingSurahList.value = true
                _errorMessage.value = null
                _surahInfoList.value = ziyaUseCases.getSurahListUseCase()
            } catch (e: Exception) {
                _errorMessage.value = "সূরা তালিকা লোড করতে সমস্যা হয়েছে: ${e.message}"
            } finally {
                _isLoadingSurahList.value = false
            }
        }
    }

    fun loadSurahDetails(number: Int) {
        viewModelScope.launch {
            try {
                _isLoadingSurahDetails.value = true
                _errorMessage.value = null

                val surah = ziyaUseCases.getSurahDetailsUseCase(number = number)
                if (surah != null) {
                    _surahDetails.value = surah
                } else {
                    _errorMessage.value = "সূরা পাওয়া যায়নি। অনুগ্রহ করে আবার চেষ্টা করুন।"
                }
            } catch (e: Exception) {
                _errorMessage.value = "সূরা লোড করতে সমস্যা হয়েছে: ${e.message}"
            } finally {
                _isLoadingSurahDetails.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun retrySurahDetails(number: Int) {
        loadSurahDetails(number)
    }
}