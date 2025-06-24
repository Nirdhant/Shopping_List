package com.example.listo.shopping.presentation.list_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.listo.MainViewModel
import com.example.listo.R
import com.example.listo.shopping.model.Items

@Composable
fun DisplayList(userName:String,sItem: List<Items>, viewModel: MainViewModel,navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
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
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Item", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
        }
        Column(modifier = Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth()){
                Badge(
                    modifier = Modifier.align(Alignment.TopStart).padding(2.dp),
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer){
                    Text(
                        text = "Hi, $userName",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd).padding(2.dp).width(50.dp).height(30.dp).clickable{
                        viewModel.logOUt()
                        navController.navigate("LoginScreen"){ popUpTo("Main_Screen"){ inclusive=true } }},
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer){
                    Icon(painter = painterResource(R.drawable.logout), contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(items = sItem, key = { itemIndex, eachItem -> eachItem.id }) { itemIndex, eachItem ->
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
