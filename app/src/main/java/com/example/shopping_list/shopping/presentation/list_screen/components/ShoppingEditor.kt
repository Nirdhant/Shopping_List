package com.example.shopping_list.shopping.presentation.list_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.shopping.model.Items

@Composable
fun ShoppingEditor(obj:Items,onEditComplete:(String,Int)->Unit)
{
    var eItem by remember { mutableStateOf(obj.name) }
    var eQty by remember { mutableStateOf(obj.qty.toString()) }
    var isEditing by remember { mutableStateOf(obj.isEditing) }

    Row(modifier = Modifier.fillMaxWidth().padding(8.dp).border(border = BorderStroke(2.dp, Color.Black)),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Column() {
            BasicTextField(
                modifier = Modifier.wrapContentSize().padding(8.dp),
                textStyle=TextStyle(fontSize=30.sp ,fontWeight=FontWeight.Bold ,fontFamily=FontFamily.Monospace),
                singleLine = true,
                value = eItem,
                onValueChange = { eItem = it }
            )
            BasicTextField(
                modifier = Modifier.wrapContentSize().padding(8.dp),
                textStyle=TextStyle(fontSize=30.sp ,fontWeight=FontWeight.Bold ,fontFamily=FontFamily.Monospace),
                singleLine = true,
                value = eQty,
                onValueChange = { eQty = it }
            )
        }
        Button(
            shape= CutCornerShape(20),
            colors=ButtonDefaults.buttonColors(Color.Black, Color.White),
            onClick = {
                isEditing = false
                onEditComplete(eItem, eQty.toIntOrNull() ?: 1)
            }
        ) {
            Text(text = "Save", style=TextStyle(fontFamily = FontFamily.Monospace ,fontWeight= FontWeight.ExtraBold ,fontSize =17.sp))
        }
    }
}