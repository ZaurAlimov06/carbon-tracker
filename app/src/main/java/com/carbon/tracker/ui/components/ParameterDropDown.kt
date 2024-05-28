package com.carbon.tracker.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbon.tracker.R
import kotlinx.coroutines.coroutineScope

@Composable
fun ParameterDropDown(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Start,
    dropDownList: List<String>,
    onClick: (String) -> Unit
) {
    var localeDropDownState by rememberSaveable {
        mutableStateOf(false)
    }
    val localDensity = LocalDensity.current

    var columnWidthDp by remember {
        mutableStateOf(0.dp)
    }

    LaunchedEffect(true) {
        coroutineScope {
            onClick(dropDownList[0])
        }
    }

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.medium
            )
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.secondary
                ),
                shape = MaterialTheme.shapes.medium
            )
            .clickable(
                onClick = {
                    localeDropDownState = true
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DropdownMenu(
            modifier = Modifier
                .width(columnWidthDp),
            expanded = localeDropDownState,
            onDismissRequest = {
                localeDropDownState = false
            }
        ) {
            dropDownList?.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 10.dp,
                                    vertical = 16.dp
                                ),
                            text = it,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = textAlign
                        )
                    },
                    onClick = {
                        localeDropDownState = false
                        onClick(it)
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 10.dp,
                    vertical = 16.dp
                )
                .onGloballyPositioned { coordinates ->
                    columnWidthDp = with(localDensity) {
                        coordinates.size.width.toDp() +
                            10.dp * 2
                    }
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .weight(1.0f),
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = textAlign
            )

            Icon(
                painter = painterResource(
                    if (localeDropDownState) {
                        R.drawable.ic_arrow_drop_up
                    } else {
                        R.drawable.ic_arrow_drop_down
                    }
                ),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = ""
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewParameterDropDown() {
    MaterialTheme {
        ParameterDropDown(
            modifier = Modifier
                .fillMaxWidth(),
            text = "aaa",
            dropDownList = listOf(
                "aaa", "bbb", "ccc"
            ),
            onClick = { }
        )
    }
}