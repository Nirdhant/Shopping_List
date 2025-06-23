package com.example.listo

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.listo.shopping.model.Items

@Dao
interface ItemsDao {
    @Upsert
    suspend fun upsertItem(item: Items)     //usd of suspend

    @Delete
    suspend fun deleteItem(item:Items)

    @Query("SELECT * FROM items WHERE userEmail = :email ORDER BY id ASC")
    suspend fun getItems(email:String): List<Items>
}