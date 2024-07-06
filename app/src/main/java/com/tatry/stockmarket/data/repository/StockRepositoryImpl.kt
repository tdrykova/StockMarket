package com.tatry.stockmarket.data.repository

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import com.tatry.stockmarket.data.csv.CSVParser
import com.tatry.stockmarket.data.local.StockDatabase
import com.tatry.stockmarket.data.mapper.toCompanyList
import com.tatry.stockmarket.data.mapper.toCompanyListEntity
import com.tatry.stockmarket.data.remote.StockApi
import com.tatry.stockmarket.domain.model.CompanyList
import com.tatry.stockmarket.domain.repository.StockRepository
import com.tatry.stockmarket.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StockRepositoryImpl @Inject constructor(
    val stockApi: StockApi,
    val db: StockDatabase,
    val companyListParser: CSVParser<CompanyList>
) : StockRepository {

    private val stockDao = db.stockDao

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getCompanyList(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyList>>> {
        return flow {
            emit(Resource.Loading(true))
            val localCompanyList = stockDao.searchCompanyList(query)
            emit(Resource.Success(
                data = localCompanyList.map { it.toCompanyList() }
            ))

            val isDbEmpty = localCompanyList.isEmpty() && query.isBlank()
            val shouldJustLoadFromCache = !isDbEmpty && !fetchFromRemote

            if (shouldJustLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            val remoteCompanyList = try {
                val response = stockApi.getStockList()
//                response.byteStream() // for csv file
                companyListParser.parse(response.byteStream())

            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("IOException"))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("HttpException"))
                null
            }

            remoteCompanyList?.let { companyList ->
                stockDao.clearCompanyList()
                stockDao.insertCompanyList(
                    companyList.map { it.toCompanyListEntity() }
                )
//                emit(Resource.Success(data = companyList)) // not 1 sourse of data
                emit(Resource.Success(
                    data = stockDao
                        .searchCompanyList("")
                        .map { it.toCompanyList() }))
                emit(Resource.Loading(false))


            }
        }
    }


}