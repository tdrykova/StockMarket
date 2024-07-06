package com.tatry.stockmarket.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyList(companyListEntity: List<CompanyListEntity>)

    @Query("DELETE FROM companylistentity")
    suspend fun clearCompanyList()

    @Query(
        """
            SELECT * FROM companylistentity 
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' 
            OR symbol == UPPER(:query)
        """
    )
    suspend fun searchCompanyList(query: String): List<CompanyListEntity>
}