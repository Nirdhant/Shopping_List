package com.example.listo.shopping.presentation.location_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listo.shopping.model.LocationData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationScreen(currentLocation: LocationData, onSetLocation: (LocationData) -> Unit) {
    val loc = remember { mutableStateOf(LatLng(currentLocation.latitude, currentLocation.longitude)) }

    val cameraPosition = rememberCameraPositionState { position = CameraPosition.fromLatLngZoom(loc.value, 17f)}

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(modifier = Modifier.fillMaxWidth().weight(1f),cameraPositionState = cameraPosition,
            onMapClick = { loc.value = it }
        ) {
            Marker(state = MarkerState(position = loc.value))
        }
        Spacer(modifier = Modifier.height(2.dp))
        Button(modifier = Modifier.align(Alignment.CenterHorizontally),
            colors=ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary),
            onClick = {
                val newLoc = LocationData(loc.value.latitude, loc.value.longitude)
                onSetLocation(newLoc)
            }
        ) {
            Text(text="Confirm",fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}
