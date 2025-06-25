package com.example.listo.shopping.presentation

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

fun firebaseUser(firebase: Firebase,onResult:(String, String)->Unit){
    val userEmail = firebase.auth.currentUser?.email
    if(userEmail!=null){
        firebase.firestore.collection("users").document(userEmail).get().addOnSuccessListener{
            val name=it.getString("name")!!
            onResult(userEmail,name)
        }
    }
}