package com.tatry.stockmarket.presentation.company_list

import com.tatry.stockmarket.domain.model.CompanyList

data class CompanyListState (
    val companyList: List<CompanyList> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)