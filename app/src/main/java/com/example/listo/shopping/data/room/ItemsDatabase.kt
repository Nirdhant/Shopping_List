package com.example.listo.shopping.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.listo.shopping.model.Items

@Database(
    entities = [Items::class],
    version = 6
)
abstract class ItemsDatabase: RoomDatabase(){
    abstract val dao: ItemsDao
}