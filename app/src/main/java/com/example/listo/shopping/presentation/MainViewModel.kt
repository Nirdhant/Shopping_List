package com.example.listo.shopping.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listo.shopping.data.networking.RetroFitClient
import com.example.listo.shopping.data.room.ItemsDao
import com.example.listo.shopping.model.AddressResult
import com.example.listo.shopping.model.Items
import com.example.listo.shopping.model.LocationData
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

class MainViewModel(private val itemsDao: ItemsDao): ViewModel() {
    private val _userEmail = mutableStateOf("")

    private val _userName = mutableStateOf("")
    val userName: State<String> =_userName

    private val _sItem = mutableStateOf(listOf<Items>())
    val sItem : State<List<Items>> = _sItem

    private val _newItem =mutableStateOf<Items?>(null)

    private val _iName = mutableStateOf("")
    val iName: State<String> = _iName

    private val _iQty = mutableStateOf("")
    val iQty: State<String> = _iQty

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    private val _showEditDialog = mutableStateOf(false)
    val showEditDialog: State<Boolean> = _showEditDialog

    private val _location = mutableStateOf<LocationData?>(null)   //what if i use mutableStateFlow
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf((listOf<AddressResult>()))
    val address: State<List<AddressResult>> = _address

    private val _editItemIndex = mutableStateOf<Int?>(null)

    private val _isLoading = mutableStateOf(false)
    val isLoading : State<Boolean> = _isLoading

    fun setLoading(value: Boolean){
    _isLoading.value=value
    }
    fun logOUt(){
        Firebase.auth.signOut()
    }
    fun setUser(newEmail:String,newName:String){
        _userEmail.value=newEmail
        _userName.value=newName
    }
    fun getUserItems(newEmail: String,newName: String){
        setUser(newEmail,newName)
        viewModelScope.launch{
            _sItem.value = itemsDao.getItems(_userEmail.value)
            setLoading(false)
        }
    }
    fun addItem(address: String) {
        if (_iName.value.isNotBlank()){
            _newItem.value= Items(
                id = _sItem.value.maxOfOrNull { it.id }?.plus(1) ?: 1,
                userEmail = _userEmail.value,
                name = _iName.value,
                qty = _iQty.value.toIntOrNull()?:0,
                address = address
            )
            viewModelScope.launch {
                try {
                    itemsDao.upsertItem(_newItem.value!!)
                } catch (e: Exception) {
                    Log.e("AddItemCrash", "Failed to insert: ${e.message}")
                }
            }

            _showDialog.value=false
            _sItem.value +=_newItem.value!!
            _iName.value=""
            _iQty.value=""
            _address.value= emptyList()
        }
    }
    fun editItem(name: String, qty: String, address: String) {
        _sItem.value = _sItem.value.map {
            if(it.id == _editItemIndex.value){
                _newItem.value = Items(
                    id= it.id,
                    userEmail = it.userEmail,
                    name = name,
                    qty = qty.toIntOrNull() ?: 0,
                    address = address
                )
                it.copy(name=name ,qty = qty.toIntOrNull() ?: 0, address = address)
            }
            else it
        }
        viewModelScope.launch{
            itemsDao.upsertItem(_newItem.value!!)
        }
        _showEditDialog.value = false
        _iName.value =""
        _iQty.value =""
        _address.value=emptyList()
    }

    fun removeItem(item: Items){

        _sItem.value -= item     //how this mutable list works
        viewModelScope.launch{
            itemsDao.deleteItem(item)
        }
    }
    fun updateName(name:String){
        _iName.value=name
    }
    fun updateQty(qty: String){
        _iQty.value=qty
    }
    //-------------------------------Dialog----------------------------------
    fun setDialog(show: Boolean){
        _showDialog.value=show
    }
    fun setEditDialog(show:Boolean,itemId:Int?=null){
        itemId?.let {
            _sItem.value.map {
                if(it.id == itemId){
                    _editItemIndex.value=it.id
                    _iName.value=it.name
                    _iQty.value=it.qty.toString()
                    _showEditDialog.value =true
                }
                else _showEditDialog.value = false
            }
        }
        _showEditDialog.value = show
    }
    //----------------------------------Location----------------------------------
    fun updateLocation(newLocation: LocationData) { _location.value = newLocation }
    fun fetchAddress(latlng: String) {
        viewModelScope.launch{
            try {
                val response = RetroFitClient.create().getResponse(latlng,"AIzaSyBEFPXJNMyeOJG4RHV48YgnPhhjCAb6spA")
                _address.value = response.results
            }
            catch (e: Exception) {
                Log.d("ER1", "${e.cause} ${e.message}")
            }
        }
    }
}
