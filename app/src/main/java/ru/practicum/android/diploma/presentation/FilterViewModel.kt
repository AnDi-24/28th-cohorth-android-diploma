package ru.practicum.android.diploma.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.prefs.FilterSettingsModel
import ru.practicum.android.diploma.domain.prefs.PrefsInteractor

class FilterViewModel(
    private val prefsInteractor: PrefsInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilterSettingsModel>(FilterSettingsModel())
    val filterState: StateFlow<FilterSettingsModel> = _uiState

    private var _appliedFilters: FilterSettingsModel = FilterSettingsModel()

    private var _sessionSnapshot: FilterSettingsModel? = null

    fun loadFilters() {
        viewModelScope.launch {
            val savedFilters = prefsInteractor.getFilterSettings()
            if (savedFilters != null) {
                _appliedFilters = savedFilters
                _uiState.value = savedFilters
            }
        }
    }

    fun takeSessionSnapshot() {
        _sessionSnapshot = _appliedFilters.copy()
    }

    fun updateIndustry(industry: String) {
        val current = _uiState.value
        _uiState.value = current.copy(industry = industry)
    }

    fun updateIndustryName(industryName: String) {
        val current = _uiState.value
        _uiState.value = current.copy(industryName = industryName)
    }

    fun updateSalary(salary: Int) {
        val current = _uiState.value
        _uiState.value = current.copy(salary = salary)
    }

    fun updateShowSalary(showSalary: Boolean) {
        val current = _uiState.value
        _uiState.value = current.copy(showSalary = showSalary)
    }

    fun applyFilters() {
        val filtersToApply = _uiState.value.copy()
        _appliedFilters = filtersToApply
        viewModelScope.launch {
            prefsInteractor.updateData(filtersToApply)
        }
    }

    fun restoreSessionSnapshot() {
        val snapshot = _sessionSnapshot ?: return
        _uiState.value = snapshot
        _appliedFilters = snapshot
        viewModelScope.launch {
            prefsInteractor.updateData(snapshot)
        }
    }

    fun resetFilters() {
        _uiState.value = FilterSettingsModel()
    }

    fun getAppliedFilters(): FilterSettingsModel = _appliedFilters

    fun getSessionSnapshot(): FilterSettingsModel? = _sessionSnapshot
}
