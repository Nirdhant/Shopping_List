package com.example.listo.shopping.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Items(
    @PrimaryKey val id:Int,
    val userEmail: String,
    val name:String,
    val qty:Int,
    val address:String = "",
)