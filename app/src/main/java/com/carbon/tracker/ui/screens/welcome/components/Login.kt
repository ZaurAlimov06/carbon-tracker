package com.carbon.tracker.ui.screens.welcome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbon.tracker.R
import com.carbon.tracker.ui.components.IconPasswordVisibility
import com.carbon.tracker.ui.theme.CarbonTrackerTheme

@ExperimentalMaterial3Api
@Composable
fun Login(
    onLoginClick: () -> Unit,
    onPassToggleStateChange: (Boolean) -> Unit,
    emailStateLogin: String,
    onEmailValueChanged: (String) -> Unit,
    passwordStateLogin: String,
    onPasswordValueChanged: (String) -> Unit,
    passVisibilityState: Boolean,
    isLoginButtonEnabled: Boolean
) {

    Column {
        OutlinedTextField(
            value = emailStateLogin,
            label = {
                Text(
                    text = stringResource(id = R.string.welcome_login_label_email_address),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            onValueChange = {
                onEmailValueChanged(it)
            },
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_mail),
                    contentDescription = stringResource(id = R.string.common_icon_content_description),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            maxLines = 1,
            textStyle = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = passwordStateLogin,
            label = {
                Text(
                    text = stringResource(id = R.string.welcome_login_label_password),
                    style = MaterialTheme.typography.titleMedium
                )
            },
            onValueChange = {
                onPasswordValueChanged(it)
            },
            modifier = Modifier
                .padding(top = 5.dp)
                .fillMaxWidth(),
            visualTransformation = if (passVisibilityState) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = stringResource(id = R.string.common_icon_content_description),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            trailingIcon = {
                IconPasswordVisibility(
                    onStateChange = {
                        onPassToggleStateChange(it)
                    }
                )
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
            ),
            singleLine = true,
            maxLines = 1,
            textStyle = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = {
                onLoginClick()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            enabled = isLoginButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = stringResource(id = R.string.welcome_login_button_login),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@ExperimentalMaterial3Api
@Composable
@Preview(showBackground = true)
fun PreviewLogin() {
    CarbonTrackerTheme {
        Login(
            onLoginClick = {},
            onPassToggleStateChange = {},
            emailStateLogin = "",
            onEmailValueChanged = { },
            passwordStateLogin = "",
            onPasswordValueChanged = { },
            passVisibilityState = true,
            isLoginButtonEnabled = true
        )
    }
}
