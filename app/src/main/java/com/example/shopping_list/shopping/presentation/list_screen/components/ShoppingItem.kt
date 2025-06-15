package com.example.shopping_list.shopping.presentation.list_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.shopping.model.Items

@Composable
fun Items(lastItemIndex:Int, obj:Items, onAddClick:()->Unit, onEdit:()->Unit, onDelete:()->Unit) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
    Button(
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        border = BorderStroke(width = 1.dp, color = Color.Green),
        onClick = { onEdit() },
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(obj.address, fontSize = 16.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Justify)
            HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(2.dp), color = Color.Yellow)
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Component("Name", obj.name)
                VerticalDivider(color = Color.Yellow, modifier = Modifier.fillMaxHeight())   //check height use box if not showing
                Component("Qty", obj.qty.toString())
                IconButton(onClick = { onDelete() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
            }
        }
    }
        if(obj.id == lastItemIndex){
            Badge(modifier=Modifier.padding(4.dp).fillMaxWidth().height(30.dp).clickable{onAddClick()}){
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    }
}


@Composable
fun Component(type:String,value:String){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(type, fontSize = 18.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Justify)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Normal)
    }
}
