package com.example.listo.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.listo.R
import com.example.listo.core.presentation.utils.CustomOutlinedTextField

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel, onLogin:()->Unit) {
    val email = authViewModel.email.value
    val password = authViewModel.password.value
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(id = R.drawable.shape),
                modifier = Modifier.fillMaxWidth(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = "Login",
                color = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 70.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 25.sp
            )
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                CustomOutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    label = "Email",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }) ,
                    value = email,
                    onValueChange = { authViewModel.updateEmail(it) }
                )
                CustomOutlinedTextField(
                    label = "Password",
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.NumberPassword),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        onLogin()
                    }) ,
                    value = password,
                    onValueChange = { authViewModel.updatePassword(it) }
                )
                ElevatedButton(
                    shape = ButtonDefaults.filledTonalShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(0.67f),
                    onClick = { onLogin() }
                ) {
                    Text("Login", fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text("Don't Have Account? ", color = MaterialTheme.colorScheme.onBackground)
                    Text(
                        "Sign Up",
                        modifier = Modifier.clickable {
                            authViewModel.clearEntries()
                            navController.navigate("SignUpScreen"){
                                popUpTo("LoginScreen"){inclusive= true}
                            } },
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
