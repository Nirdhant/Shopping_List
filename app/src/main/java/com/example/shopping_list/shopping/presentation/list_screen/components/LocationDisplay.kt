package com.example.shopping_list.shopping.presentation.list_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopping_list.shopping.model.LocationData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationDisplay(currentLocation: LocationData, onSetLocation:(LocationData)->Unit)
{
    val loc = remember{ mutableStateOf(LatLng(currentLocation.latitude,currentLocation.longitude)) }
    var cameraPosition= rememberCameraPositionState{ position= CameraPosition.fromLatLngZoom(loc.value,30f) }

    Column(modifier=Modifier.fillMaxSize()) {
           GoogleMap(modifier= Modifier.padding(top = 8.dp).fillMaxWidth().weight(1f),
               cameraPositionState = cameraPosition,
               onMapClick = {loc.value = it}) {
               Marker(state = MarkerState(position=loc.value))
           }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    val newLoc = LocationData(loc.value.latitude,loc.value.longitude)
                    onSetLocation(newLoc)
                }) {
              Text("Set Location" ,fontSize=12.sp ,fontWeight=FontWeight.Bold)
            }
    }
}
