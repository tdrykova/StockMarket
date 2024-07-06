package com.tatry.stockmarket.data.mapper

import com.tatry.stockmarket.data.local.CompanyListEntity
import com.tatry.stockmarket.domain.model.CompanyList

fun CompanyListEntity.toCompanyList(): CompanyList {
    return CompanyList(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyList.toCompanyListEntity(): CompanyListEntity {
    return CompanyListEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}