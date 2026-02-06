package ru.practicum.android.diploma.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.network.api.industries.IndustriesInteractor
import ru.practicum.android.diploma.domain.network.models.industries.IndustryModel
import ru.practicum.android.diploma.domain.prefs.PrefsInteractor
import ru.practicum.android.diploma.presentation.models.IndustryUiState
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

class FilterOptionViewModel(
    val industryInteractor: IndustriesInteractor,
    val prefsInteractor: PrefsInteractor
) : ViewModel() {
    private var searchJob: Job? = null
    private val _filterUiState = mutableStateOf<IndustryUiState>(IndustryUiState.OnSelect(emptyList()))
    val filterUiState: State<IndustryUiState> get() = _filterUiState
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _selectedIndustry = MutableStateFlow<IndustryModel?>(null)
    val selectedIndustry: StateFlow<IndustryModel?> = _selectedIndustry.asStateFlow()

    init {
        val prefsId = prefsInteractor.getFilterSettings()?.industry ?: ""
        val prefsName = prefsInteractor.getFilterSettings()?.industryName ?: ""

        if (prefsId.isNotEmpty() && prefsName.isNotEmpty()) {
            val chosenIndustry = IndustryModel(prefsId, prefsName)
            _selectedIndustry.value = chosenIndustry
            _searchQuery.value = prefsName
            _filterUiState.value = IndustryUiState.Selected(chosenIndustry)
        } else {
            searchIndustries("")
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun searchIndustries(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            try {
                when (val resource = industryInteractor.getIndustries()) {
                    is Resource.Success -> {
                        val allIndustries = resource.data ?: emptyList()
                        val filteredIndustries = if (query.isNotEmpty()) {
                            allIndustries.filter { industry ->
                                industry.name.contains(query, ignoreCase = true)
                            }
                        } else {
                            allIndustries
                        }

                        val currentSelected = _selectedIndustry.value
                        if (currentSelected != null && filteredIndustries.any { it.id == currentSelected.id }) {
                            _filterUiState.value = IndustryUiState.OnSelect(filteredIndustries)
                        } else {
                            _filterUiState.value = IndustryUiState.OnSelect(filteredIndustries)
                        }
                    }

                    is Resource.Error -> {
                        handleError(resource.message ?: "")
                    }
                }
            } catch (e: IOException) {
                Log.d("Exception Message", "Exception $e")
                handleError(NETWORK_ERROR)
            }
        }
    }

    fun selectedIndustry(industry: IndustryModel) {
        _selectedIndustry.value = industry
        _searchQuery.value = industry.name
        _filterUiState.value = IndustryUiState.Selected(industry)
    }

    private fun handleError(errorMessage: String?) {
        _filterUiState.value = IndustryUiState.Empty
    }

    fun checkAndUpdateFromSharedPrefs() {
        val prefsId = prefsInteractor.getFilterSettings()?.industry ?: ""
        val prefsName = prefsInteractor.getFilterSettings()?.industryName ?: ""
        val currentSelectedId = _selectedIndustry.value?.id

        // Если в SharedPrefs есть отрасль, но она отличается от текущей
        if (prefsId.isNotEmpty() && prefsName.isNotEmpty()) {
            if (currentSelectedId != prefsId) {
                val chosenIndustry = IndustryModel(prefsId, prefsName)
                _selectedIndustry.value = chosenIndustry
                _searchQuery.value = prefsName
                _filterUiState.value = IndustryUiState.Selected(chosenIndustry)
            }
        } else {
            // Если в SharedPrefs нет отрасли, но она есть в ViewModel
            if (currentSelectedId != null) {
                _selectedIndustry.value = null
                _searchQuery.value = ""
                searchIndustries("")
            }
        }
    }
}
