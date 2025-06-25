package com.example.listo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.listo.auth.presentation.AuthViewModel
import com.example.listo.auth.presentation.LoginScreen
import com.example.listo.auth.presentation.SignUpScreen
import com.example.listo.shopping.data.room.ItemsDatabase
import com.example.listo.shopping.presentation.LocationUtils
import com.example.listo.shopping.presentation.MainViewModel
import com.example.listo.shopping.presentation.firebaseUser
import com.example.listo.shopping.presentation.list_screen.Shopping
import com.example.listo.shopping.presentation.location_screen.LocationScreen
import com.example.listo.ui.theme.ListoTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(applicationContext, ItemsDatabase::class.java, "items.db")
            .fallbackToDestructiveMigration(true).build()
    }
    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val authViewModel: AuthViewModel= viewModel()
            val navController = rememberNavController()
            val firebase = Firebase
            val context = applicationContext
            val locationUtils = LocationUtils(context)

            ListoTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation(
                        navController = navController,
                        viewModel = viewModel,
                        authViewModel=authViewModel,
                        firebase=firebase,
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
    authViewModel: AuthViewModel,
    firebase: Firebase,
    locationUtils: LocationUtils
) {
    val context = LocalContext.current    //should be removed or not
    val startDestination =
        if(firebase.auth.currentUser!=null){ "Main_Screen" }
        else "LoginScreen"
    NavHost(navController = navController, startDestination = startDestination)
    {
        composable("LoginScreen") {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            ) {
                authViewModel.login { isSuccessful,errorMessage->
                    if (isSuccessful) {
                        navController.navigate("Main_Screen") { popUpTo("LoginScreen") { inclusive = true } }
                    }
                    else  Toast.makeText(context,errorMessage?:"Something Went Wrong",Toast.LENGTH_LONG).show()
                }
            }
        }
        composable("SignUpScreen"){
            SignUpScreen(
                navController = navController,
                authViewModel = authViewModel
            ){
                authViewModel.signUp { isSuccessful,errorMessage->
                    if(isSuccessful){ navController.navigate("Main_Screen"){ popUpTo("SignUpScreen"){inclusive= true} } }
                    else Toast.makeText(context,errorMessage?:"Something Went Wrong",Toast.LENGTH_LONG).show()
                }
            }
        }
        composable("Main_Screen") {
            LaunchedEffect(Unit) {
                viewModel.setLoading(true)
            }
            firebaseUser(firebase, onResult = { userEmail, userName ->
                viewModel.getUserItems(userEmail, userName)
            })
            Shopping(
                context = context,
                navController = navController,
                viewModel = viewModel,
                locationUtils = locationUtils
            )
        }
        composable("Location_Screen"){
            viewModel.location.value?.let { currentLocation ->
                LocationScreen(currentLocation) { newLoc ->
                    viewModel.fetchAddress("${newLoc.latitude},${newLoc.longitude}")
                    navController.navigateUp()
                }
            }
        }
    }
}
