package com.tatry.stockmarket.presentation.company_list

sealed class CompanyListEvent {
    object Refresh: CompanyListEvent()
    data class OnSearchQueryChange(val query: String): CompanyListEvent()
}