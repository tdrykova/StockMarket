package com.tatry.stockmarket.presentation.company_list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tatry.stockmarket.domain.repository.StockRepository
import com.tatry.stockmarket.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListViewModel @Inject constructor(
    private val repository: StockRepository
): ViewModel() {

    var state by mutableStateOf(CompanyListState())

    private var searchJob: Job? = null

    fun onEvent(event: CompanyListEvent) {
        when(event) {
            is CompanyListEvent.Refresh -> {
                getCompanyList(fetchFromRemote = true)
            }
            is CompanyListEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyList()
                }
            }
        }
    }

    fun getCompanyList(
        query: String = state.searchQuery.lowercase(),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository
                .getCompanyList(fetchFromRemote, query)
                .collect{ result ->
                    when(result) {
                        is Resource.Success -> {
                            result.data?.let { list ->
                                state = state.copy(companyList = list)
                            }

                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }


                }
        }
    }
}