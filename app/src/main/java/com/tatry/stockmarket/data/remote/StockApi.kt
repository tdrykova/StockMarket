package com.tatry.stockmarket.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getStockList(
        @Query("apikey") apikey: String = API_KEY
    ): ResponseBody

    companion object {
        const val BASE_URL = "https://www.alphavantage.co"
        const val API_KEY = "6IDM91ZF2EE43FA1"
    }
}