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
import ru.practicum.android.diploma.util.ErrorHandler
import ru.practicum.android.diploma.util.Resource
import java.io.IOException

class FilterOptionViewModel(
    val industryInteractor: IndustriesInteractor,
    val prefsInteractor: PrefsInteractor
) : ViewModel() {
    private var searchJob: Job? = null
    private val _filterUiState = mutableStateOf<IndustryUiState>(IndustryUiState.OnSelect(emptyList()))
    val filterUiState: State<IndustryUiState> get() = _filterUiState
    private val _selectedIndustry = MutableStateFlow<IndustryModel?>(null)
    val selectedIndustry: StateFlow<IndustryModel?> = _selectedIndustry.asStateFlow()

    init {
        loadSavedIndustry()
    }

    fun loadSavedIndustry() {
        viewModelScope.launch {
            val prefs = prefsInteractor.getFilterSettings()
            val prefsId = prefs?.industry ?: ""
            val prefsName = prefs?.industryName ?: ""

            if (prefsId.isNotEmpty() && prefsName.isNotEmpty()) {
                _selectedIndustry.value = IndustryModel(prefsId, prefsName)
                searchIndustries("", shouldSelectSaved = true)
            } else {
                resetChoice()
                searchIndustries("")
            }
        }
    }

    fun searchIndustries(query: String, shouldSelectSaved: Boolean = false) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            try {
                when (val resource = industryInteractor.getIndustries()) {
                    is Resource.Success -> {
                        val allIndustries = resource.data ?: emptyList()
                        val filteredIndustries = if (query.isNotEmpty()) {
                            allIndustries.filter { it.name.contains(query, ignoreCase = true) }
                        } else {
                            allIndustries
                        }

                        _filterUiState.value = IndustryUiState.OnSelect(filteredIndustries)

                        if (shouldSelectSaved) {
                            val currentPrefs = prefsInteractor.getFilterSettings()
                            val savedIndustry = _selectedIndustry.value

                            if (savedIndustry != null &&
                                currentPrefs?.industry == savedIndustry.id &&
                                currentPrefs.industryName == savedIndustry.name
                            ) {
                                if (filteredIndustries.any { it.id == savedIndustry.id }) {
                                    _selectedIndustry.value = savedIndustry
                                } else {
                                    resetChoice()
                                }
                            } else {
                                resetChoice()
                            }
                        }
                    }

                    is Resource.Error -> handleError(resource.message ?: "")
                }
            } catch (e: IOException) {
                handleError(ErrorHandler.handleNetworkError(e))
            }
        }
    }

    fun selectedIndustry(industry: IndustryModel) {
        _selectedIndustry.value = industry
    }

    fun resetChoice() {
        _selectedIndustry.value = null
    }

    private fun handleError(errorMessage: String?) {
        _filterUiState.value = IndustryUiState.Empty
        errorMessage?.let { Log.d("FilterOptionViewModel", it) }
    }
}
