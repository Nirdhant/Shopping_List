package com.example.listo.shopping.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Items(
    @PrimaryKey(autoGenerate = true) val id:Int,
    val name:String,
    val qty:Int,
    val address:String = "",
    val isEditing:Boolean = false
)