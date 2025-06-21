package com.example.listo.shopping.presentation.list_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.listo.MainViewModel
import com.example.listo.shopping.model.Items

@Composable
fun DisplayList(sItem: List<Items>, viewModel: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp),horizontalAlignment = Alignment.CenterHorizontally){
        if (sItem.isEmpty()) {
                Button(
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ), onClick = {
                        viewModel.setDialog(true)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Item")
                }
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(items = sItem, key = { itemIndex, _ -> itemIndex }) { itemIndex, eachItem ->
                if (!eachItem.isEditing) {
                    Spacer(modifier=Modifier.height(8.dp))
                    ShoppingItems(
                        itemIndex = itemIndex,
                        lastItemIndex = sItem.size,
                        obj = eachItem,
                        onAddClick = { viewModel.setDialog(true) },
                        onDelete = {
                            viewModel.removeItem(eachItem)
                             },
                        onEdit = { viewModel.setEditDialog(true, eachItem.id) }
                    )
                }
            }
        }
    }
}
