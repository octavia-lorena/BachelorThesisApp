package com.example.bachelorthesisapp.presentation.ui.components

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.bachelorthesisapp.presentation.ui.theme.Coral
import com.example.bachelorthesisapp.presentation.ui.theme.Ochre
import kotlinx.coroutines.launch

@Composable
fun FormTextField(
    labelText: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    keyboardType: KeyboardType,
    passwordVisible: Boolean = true
) {
    val focusManager = LocalFocusManager.current
    TextField(
        label = { Text(text = labelText, color = Color.DarkGray) },
        value = value,
        modifier = Modifier.width(350.dp),
        onValueChange = onValueChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Transparent,
            focusedIndicatorColor = Coral,
            unfocusedIndicatorColor = Color.Gray,
            cursorColor = Color.Gray
        ),
        isError = error != null,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = keyboardType
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}
