package com.example.listo.auth.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.listo.auth.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel:ViewModel() {
    private val _email = mutableStateOf<String>("")
    val email: State<String> = _email

    private val _password = mutableStateOf<String>("")
    val password: State<String> = _password

    private val _name = mutableStateOf<String>("")
    val name: State<String> = _name


    private val auth = Firebase.auth
    private val fireStore= Firebase.firestore

    fun updateEmail(email: String) { _email.value = email }
    fun updateName(name: String) { _name.value = name }
    fun updatePassword(password: String) { _password.value = password }
    fun clearEntries(){
        _email.value = ""
        _password.value = ""
    }

    fun signUp(onResult:(Pair<Boolean, String?>,Pair<String?, String?>)-> Unit) {

        if(_name.value.isBlank() && _email.value.isBlank() && _password.value.isBlank()){
            onResult(Pair(false,"Please Enter All The Fields"), Pair(null,null))
            return
        }
        auth.createUserWithEmailAndPassword(_email.value, _password.value).addOnCompleteListener {
            if (it.isSuccessful) {
                val userId = it.result?.user?.uid
                val user = User(userId!!, _email.value, _name.value)
                fireStore.collection("users").document(_email.value).set(user).
                    addOnCompleteListener {
                        if (it.isSuccessful) {
                            onResult(Pair(true, null), Pair(_email.value, _name.value))
                        } else {
                            onResult(Pair(false,it.exception?.localizedMessage),Pair(null,null))
                        }
                    }
            } else {
                onResult(Pair(false,it.exception?.localizedMessage),Pair(null,null))
            }
        }
    }
    fun login(onResult: (Pair<Boolean, String?>,Pair<String?,String?>) -> Unit){
        if(_email.value.isBlank() && _password.value.isBlank()){
            onResult(Pair(false,"Please Enter All The Fields"),Pair(null,null))
            return
        }
        auth.signInWithEmailAndPassword(_email.value,_password.value).addOnCompleteListener {
            if (it.isSuccessful){
                fireStore.collection("users").document(_email.value).get().addOnSuccessListener{
                    _name.value=it.getString("name")?:"*_*"
                    onResult(Pair(true,null), Pair(_email.value,_name.value))
                }
            }
            else{ onResult(Pair(false,it.exception?.localizedMessage), Pair(null,null)) }
        }
    }
}