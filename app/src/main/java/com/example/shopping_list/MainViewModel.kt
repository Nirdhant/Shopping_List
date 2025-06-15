package com.example.shopping_list

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping_list.shopping.data.RetroFitClient
import com.example.shopping_list.shopping.model.AddressResult
import com.example.shopping_list.shopping.model.Items
import com.example.shopping_list.shopping.model.LocationData
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _sItem = mutableStateOf(listOf<Items>())
    val sItem : State<List<Items>> = _sItem

    private val _iName = mutableStateOf("")
    val iName: State<String> = _iName

    private val _iQty = mutableStateOf("")
    val iQty: State<String> = _iQty

    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog

    private val _location = mutableStateOf<LocationData?>(null)   //what if i use mutableStateFlow
    val location: State<LocationData?> = _location

    private val _address = mutableStateOf((listOf<AddressResult>()))
    val address: State<List<AddressResult>> = _address

    fun addItem(address: String) {
        if (_iName.value.isNotBlank()){
            val newItem= Items(
                id = _sItem.value.size+1,
                name = _iName.value,
                qty = _iQty.value.toIntOrNull()?:0,
                address = address
            )
            _sItem.value += newItem
            _iName.value=""
            _iQty.value=""
            _showDialog.value=false
        }
    }

    fun removeItem(item: Items){
        _sItem.value -= item
    }
    fun setEditingItem(id: Int){
        _sItem.value= _sItem.value.map{it.copy(isEditing = it.id == id)}
    }
    fun updateItem(id: Int, name: String, qty: Int, address: String) {
        _sItem.value = _sItem.value.map {
            if (it.id == id) { it.copy(name = name, qty = qty, address = address, isEditing = false) }
            else { it.copy(isEditing = false) }
        }
    }

    fun updateName(name:String){
        _iName.value=name
    }
    fun updateQty(qty: String){
        _iQty.value=qty
    }
    fun setDialog(show: Boolean){
        _showDialog.value=show
    }

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
