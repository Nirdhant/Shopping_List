package com.example.shopping_list.shopping.presentation.list_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shopping_list.MainViewModel
import com.example.shopping_list.shopping.model.Items


@Composable
fun DisplayList(sItem: List<Items>,viewModel: MainViewModel){
    Column(modifier = Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally) {
        if (sItem.isEmpty()){
            Button(
                shape=RoundedCornerShape(12.dp),
                colors=ButtonDefaults.buttonColors(Color.Black,Color.White),
                onClick = { viewModel.setDialog(true) }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
        LazyColumn(modifier= Modifier.fillMaxWidth().padding(2.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            items(sItem, key ={ eachItem->eachItem.id} ) { eachItem ->      //we can use a keyword instead of it,this is it
                if(!eachItem.isEditing) {
                    Items(
                        lastItemIndex = sItem.size,
                        eachItem,
                        onAddClick = {viewModel.setDialog(true)},
                        onEdit = {
                            viewModel.setEditDialog(true,eachItem.id) },
                        onDelete = { viewModel.removeItem(eachItem) }
                    )
                }
            }
        }
    }
}
