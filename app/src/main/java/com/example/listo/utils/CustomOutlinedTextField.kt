package com.example.listo.utils

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier= Modifier,
    label: String,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit) {

    val textFieldError = remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier,
        singleLine = true,
        label = { Text(label) },
        supportingText = { if (textFieldError.value) Text("*${label} Cannot Be Empty",color = MaterialTheme.colorScheme.error) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        value = value,
        onValueChange = {
            onValueChange(it)
            if(it.isBlank()){ textFieldError.value=true }
            else textFieldError.value=false },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}