package com.example.shopping_list

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.shopping_list.utils.LocationUtils
import com.example.shopping_list.shopping.presentation.list_screen.Shopping
import com.example.shopping_list.shopping.presentation.list_screen.components.LocationDisplay
import com.example.shopping_list.ui.theme.Shopping_ListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            val navController = rememberNavController()
            val context = applicationContext
            val locationUtils = LocationUtils(context)

            Shopping_ListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        navController = navController,
                        viewModel = viewModel,
                        locationUtils = locationUtils
                    )
                }
            }
        }
    }
}

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    locationUtils: LocationUtils
) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "Main_Screen") {
        composable("Main_Screen") {
            Shopping(context = context,
                navController = navController,
                viewModel = viewModel,
                locationUtils = locationUtils
            )
        }

        dialog("Location_Display", dialogProperties = DialogProperties(usePlatformDefaultWidth = true, dismissOnBackPress = true)) {
            viewModel.location.value?.let { currentLocation ->
                LocationDisplay(currentLocation) { newLoc ->
                    viewModel.fetchAddress("${newLoc.latitude},${newLoc.longitude}")
                    navController.navigateUp()  // safer than popBackStack
                }
            }
        }
    }
}
