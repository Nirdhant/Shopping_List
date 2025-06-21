package com.example.listo.shopping.model

data class Items(
    val id:Int,
    val name:String,
    val qty:Int,
    val address:String = "",
    val isEditing:Boolean = false
)