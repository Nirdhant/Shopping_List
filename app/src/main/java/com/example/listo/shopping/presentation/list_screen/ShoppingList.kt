package com.example.listo.shopping.presentation.list_screen

import android.Manifest
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.listo.MainActivity
import com.example.listo.MainViewModel
import com.example.listo.shopping.presentation.list_screen.components.DisplayList
import com.example.listo.utils.CustomOutlinedTextField
import com.example.listo.utils.LocationUtils

@Composable
fun Shopping(context: Context, navController: NavController, viewModel: MainViewModel, locationUtils: LocationUtils) {
    val userName = viewModel.userName.value
    val sItem = viewModel.sItem.value
    val iName = viewModel.iName.value
    val iQty = viewModel.iQty.value
    val showDialog = viewModel.showDialog.value
    val showEditDialog = viewModel.showEditDialog.value

    val formattedAddress = viewModel.address
    val address = formattedAddress.value.firstOrNull()?.formatted_address ?: "No Address"

    val focusManager = LocalFocusManager.current
    val focusNameRequester = remember { FocusRequester() }
    val focusQtyRequester = remember { FocusRequester() }
    // --------------------- Location Permission ---------------------
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            locationUtils.requestLocationUpdates(viewModel)
        } else {
            val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                context as MainActivity, Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            )

            if (rationaleRequired) {
                Toast.makeText(context, "Location is required to find you", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Location is required, Enable it in settings", Toast.LENGTH_LONG).show()
            }
        }
    }

    // --------------------- List Display ---------------------
    DisplayList(userName,sItem,viewModel,navController)
    // --------------------- Add/Edit Dialog ---------------------
    if (showDialog || showEditDialog) {
        LaunchedEffect(Unit) {
            focusNameRequester.requestFocus()
        }

        AlertDialog(
            shape = RoundedCornerShape(45.dp),
            title = {
                Text(if (showEditDialog) "Edit Shopping Items" else "Add Shopping Items",
                    style = TextStyle(fontSize = 22.sp,fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurface),
                    textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    CustomOutlinedTextField(
                        modifier = Modifier.focusRequester(focusNameRequester),
                        label="Name",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { if (iName.isNotBlank()){ focusQtyRequester.requestFocus()} }),
                        value = iName,
                        onValueChange = { viewModel.updateName(it) },
                        )
                    CustomOutlinedTextField(
                        modifier = Modifier.focusRequester(focusQtyRequester),
                        label="Qty",
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                                if (showEditDialog) { viewModel.editItem(iName, iQty, address) }
                                else { viewModel.addItem(address) }

                        }),
                        value = iQty,
                        onValueChange = { viewModel.updateQty(it) },
                        )
                    Button(onClick = {
                            if (locationUtils.hasLocationPermission(context)) {
                                locationUtils.requestLocationUpdates(viewModel)
                                navController.navigate("Location_Screen") { launchSingleTop = true }
                            } else {
                                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.width(150.dp).height(35.dp)
                    ) {
                        Text(text="Set Location",fontSize=14.sp,fontWeight=FontWeight.SemiBold,textAlign = TextAlign.Center)
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color.Red)
                    }
                }
            },
            confirmButton = {
                Row(modifier = Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceAround) {
                    OutlinedButton(
                        onClick = {
                            if (showEditDialog) { viewModel.setEditDialog(false) }
                            else { viewModel.setDialog(false) }
                        }
                    ) {
                        Text("Cancel", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor=MaterialTheme.colorScheme.primary,
                            contentColor=MaterialTheme.colorScheme.onPrimary
                        ),
                        onClick = {
                            if (showEditDialog) { viewModel.editItem(iName, iQty, address) }
                            else { viewModel.addItem(address) }
                        }
                    ) {
                        Text(if (showEditDialog) "Edit Item" else "Add Item",fontSize=16.sp,fontWeight=FontWeight.Bold)
                    }
                }
            },
            onDismissRequest = {
                if (showEditDialog) { viewModel.setEditDialog(false) }
                else { viewModel.setDialog(false) }
            }
        )
    }
}
