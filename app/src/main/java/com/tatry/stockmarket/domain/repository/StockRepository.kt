package com.tatry.stockmarket.domain.repository

import com.tatry.stockmarket.domain.model.CompanyList
import com.tatry.stockmarket.util.Resource
import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getCompanyList(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyList>>> // flow for local cash
}