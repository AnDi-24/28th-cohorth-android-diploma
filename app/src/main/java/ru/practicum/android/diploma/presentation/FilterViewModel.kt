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
    private val _filterState = MutableStateFlow<FilterSettingsModel>(FilterSettingsModel())
    val filterState: StateFlow<FilterSettingsModel> = _filterState

    init {
        loadFilters()
    }

    fun loadFilters() {
        viewModelScope.launch {
            val savedFilters = prefsInteractor.getFilterSettings()
            if (savedFilters != null) {
                _filterState.value = savedFilters
            }
        }
    }

    fun updateIndustry(industry: String) {
        val current = _filterState.value
        val updated = current.copy(industry = industry)
        _filterState.value = updated
        prefsInteractor.updateData(updated)
    }

    fun updateIndustryName(industryName: String) {
        val current = _filterState.value
        val updated = current.copy(industryName = industryName)
        _filterState.value = updated
        prefsInteractor.updateData(updated)
    }

    fun updateSalary(salary: Int) {
        viewModelScope.launch {
            val current = _filterState.value
            val updated = current.copy(salary = salary)
            _filterState.value = updated
            prefsInteractor.updateData(updated)
        }
    }

    fun updateShowSalary(showSalary: Boolean) {
        val current = _filterState.value
        val updated = current.copy(showSalary = showSalary)
        _filterState.value = updated
        prefsInteractor.updateData(updated)
    }
}
