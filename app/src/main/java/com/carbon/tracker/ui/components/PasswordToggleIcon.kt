package com.carbon.tracker.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import com.carbon.tracker.R

@Composable
fun IconPasswordVisibility(
    onStateChange: (Boolean) -> Unit
) {
    val state = remember { mutableStateOf(false) }

    IconToggleButton(
        checked = state.value,
        onCheckedChange = {
            state.value = it
            onStateChange(state.value)
        },
        enabled = true
    ) {
        Icon(
            painter = if (state.value) {
                painterResource(id = R.drawable.ic_pass_visible)
            } else {
                painterResource(id = R.drawable.ic_pass_invisible)
            },
            contentDescription = "IconInputTrailing",
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}