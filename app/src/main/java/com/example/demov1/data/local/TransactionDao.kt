package com.example.demov1.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.demov1.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions")
    suspend fun getAll(): Flow<List<Transaction>>

    @Update
    suspend fun update(transaction: Transaction)
    @Delete
    suspend fun  delete(transaction: Transaction);


}