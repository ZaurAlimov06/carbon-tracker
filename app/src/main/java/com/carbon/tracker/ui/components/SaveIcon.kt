package com.carbon.tracker.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.carbon.tracker.R

@Composable
fun IconSave(
    onClick: () -> Unit
) {
    Icon(
        modifier = Modifier
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = rememberRipple(
                    color = MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    onClick()
                }
            ),
        painter = painterResource(id = R.drawable.ic_save),
        contentDescription = "IconSave",
        tint = MaterialTheme.colorScheme.primary
    )
}