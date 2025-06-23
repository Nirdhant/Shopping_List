package com.example.listo

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listo.shopping.data.RetroFitClient
import com.example.listo.shopping.model.AddressResult
import com.example.listo.shopping.model.Items
import com.example.listo.shopping.model.LocationData
import kotlinx.coroutines.launch

class MainViewModel(private val itemsDao: ItemsDao): ViewModel() {
    private val _sItem = mutableStateOf(listOf<Items>())
    val sItem : State<List<Items>> = _sItem

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

    fun getItem(){
        viewModelScope.launch{
            itemsDao.getItems().collect{
                _sItem.value=it
            }
        }
    }
    fun addItem(address: String) {
        if (_iName.value.isNotBlank()){
            val newItem= Items(
                id = _sItem.value.size+1,
                name = _iName.value,
                qty = _iQty.value.toIntOrNull()?:0,
                address = address
            )
            viewModelScope.launch{
                itemsDao.upsertItem(newItem)
            }
            _showDialog.value=false
            _sItem.value += newItem
            _iName.value=""
            _iQty.value=""
            _address.value= emptyList()
        }
    }
    fun editItem(name: String, qty: String, address: String) {
        _sItem.value = _sItem.value.map {
            if(it.id == _editItemIndex.value){
                it.copy(name=name ,qty = qty.toIntOrNull() ?: 0, address = address, isEditing = false)
            }
            else it

        }

        _showEditDialog.value = false
        _iName.value =""
        _iQty.value =""
        _address.value=emptyList()
    }

    fun removeItem(item: Items){
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
