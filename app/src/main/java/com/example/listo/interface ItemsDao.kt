package com.example.listo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.listo.shopping.model.Items
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemsDao {
    @Upsert
    suspend fun upsertItem(item: Items)

    @Delete
    suspend fun deleteItem(item:Items)

    @Query("SELECT * FROM items ORDER BY id ASC")
     fun getItems(): Flow<List<Items>>
}