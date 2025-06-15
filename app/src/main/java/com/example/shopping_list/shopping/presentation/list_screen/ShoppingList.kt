package com.example.shopping_list.shopping.presentation.list_screen

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.shopping_list.MainActivity
import com.example.shopping_list.MainViewModel
import com.example.shopping_list.shopping.presentation.list_screen.components.DisplayList
import com.example.shopping_list.utils.LocationUtils

@Composable
fun Shopping(context:Context,navController:NavController,viewModel:MainViewModel,locationUtils: LocationUtils)
{
    val sItem = viewModel.sItem.value
    val iName = viewModel.iName.value
    val iQty = viewModel.iQty.value
    val showDialog = viewModel.showDialog.value
    val showEditDialog = viewModel.showEditDialog.value

    val formattedAddress = viewModel.address
    val address =formattedAddress.value.firstOrNull()?.formatted_address ?:"No Address"
    //-------------------------------Permission Launcher--------------------------------------------
    val permissionLauncher= rememberLauncherForActivityResult(contract= ActivityResultContracts.RequestMultiplePermissions(),
        onResult ={permissions->  //for every permission requested
            if(permissions[Manifest.permission.ACCESS_FINE_LOCATION]==true && permissions[Manifest.permission.ACCESS_COARSE_LOCATION]==true)
            {
                locationUtils.requestLocationUpdates(viewModel)
            }
            else {
                //Ask for permission and let the user know why we want the location
                val rationaleRequired= ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,Manifest.permission.ACCESS_FINE_LOCATION)
                        ||
                        ActivityCompat.shouldShowRequestPermissionRationale(context,Manifest.permission.ACCESS_COARSE_LOCATION)
                if(rationaleRequired){
                    Toast.makeText(context,"Location is required to find you", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(context,"Location is required ,Enable it in settings", Toast.LENGTH_LONG).show()
                }
                //why we want to have the permission(reason),initially it is true
            }
        })

    DisplayList(sItem,viewModel)
        if (showDialog || showEditDialog) {
            AlertDialog(
                shape=CutCornerShape(10.dp),
                title = {
                    if (showEditDialog)
                    Text("Edit Shopping Items", style=TextStyle(fontSize =23.sp ,fontWeight=FontWeight.Bold ,fontFamily=FontFamily.Monospace))
                    else
                    Text("Add Shopping Items", style=TextStyle(fontSize =23.sp ,fontWeight=FontWeight.Bold ,fontFamily=FontFamily.Monospace))
                },
                text = { Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                    OutlinedTextField(
                        modifier= Modifier.fillMaxWidth().padding(8.dp),
                        singleLine=true,
                        value=iName,
                        onValueChange={viewModel.updateName(it)}
                    )
                    OutlinedTextField(
                        modifier= Modifier.fillMaxWidth().padding(8.dp),
                        singleLine=true,
                        value=iQty,
                        onValueChange={viewModel.updateQty(it)}
                    )
                    Button(onClick ={
                        if(locationUtils.hasLocationPermission(context)) {
                            locationUtils.requestLocationUpdates(viewModel)
                            navController.navigate("Location_Display"){ this.launchSingleTop }
                        }
                        else {
                            permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION))
                        }
                    }) {
                        Text(text ="Location" ,fontSize =10.sp , fontWeight= FontWeight.Bold ,textAlign=TextAlign.Center)
                    }
                } },
                confirmButton = {
                    Row(modifier = Modifier.fillMaxWidth().padding(1.dp),horizontalArrangement=Arrangement.SpaceBetween) {
                        Button(
                            shape=CutCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color.White, Color.Black),
                            onClick = {
                                if (showEditDialog) viewModel.editItem(iName,iQty,address)
                                else viewModel.addItem(address)
                            }
                        ) {
                            if (showEditDialog)
                                Text("Edit Item", style =TextStyle(fontSize =17.sp, fontWeight=FontWeight.Bold))
                            else
                                Text("Add Item", style =TextStyle(fontSize =17.sp, fontWeight=FontWeight.Bold))
                        }
                        Button(
                            shape= CutCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(Color.White, Color.Black),
                            onClick={
                                if(showEditDialog) viewModel.setEditDialog(false)
                                else viewModel.setDialog(false)
                            }
                        ) {
                            Text("Cancel",style =TextStyle(fontSize =17.sp, fontWeight=FontWeight.Bold))
                        }
                    } },
                onDismissRequest = {
                    if(showEditDialog) viewModel.setEditDialog(false)
                    else viewModel.setDialog(false)
                }
            )
        }
    }





