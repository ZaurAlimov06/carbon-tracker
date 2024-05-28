package com.carbon.tracker.ui.screens.welcome.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carbon.tracker.R
import com.carbon.tracker.ui.theme.CarbonTrackerTheme
import com.carbon.tracker.ui.theme.PrimaryColor

@Composable
fun WelcomeSwitch(
    welcomeStateChange: (Boolean) -> Unit,
    welcomeState: Boolean
) {

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.welcome_switch_button_login),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        welcomeStateChange(true)

                    }
                )
                .drawBehind {
                    if (welcomeState) {
                        val strokeWidthPx = 1.dp.toPx()
                        val verticalOffset = size.height + 2.sp.toPx()
                        drawLine(
                            color = PrimaryColor,
                            strokeWidth = strokeWidthPx,
                            start = Offset(0f, verticalOffset),
                            end = Offset(size.width, verticalOffset)
                        )
                    }
                },
            style = MaterialTheme.typography.titleMedium,
            color = if (welcomeState) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground
            },
        )

        Spacer(modifier = Modifier.width(30.dp))

        Text(
            text = stringResource(id = R.string.welcome_switch_button_register),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        welcomeStateChange(false)
                    }
                )
                .drawBehind {
                    if (!welcomeState) {
                        val strokeWidthPx = 1.dp.toPx()
                        val verticalOffset = size.height + 2.sp.toPx()
                        drawLine(
                            color = PrimaryColor,
                            strokeWidth = strokeWidthPx,
                            start = Offset(0f, verticalOffset),
                            end = Offset(size.width, verticalOffset)
                        )
                    }
                },
            style = MaterialTheme.typography.titleMedium,
            color = if (!welcomeState) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onBackground
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun WelcomeSwitchPreview() {
    CarbonTrackerTheme {
        WelcomeSwitch(
            welcomeState = true,
            welcomeStateChange = {}
        )
    }
}