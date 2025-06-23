package com.example.listo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.listo.shopping.model.Items

@Database(
    entities = [Items::class],
    version = 4
)
abstract class ItemsDatabase: RoomDatabase(){
    abstract val dao: ItemsDao
}