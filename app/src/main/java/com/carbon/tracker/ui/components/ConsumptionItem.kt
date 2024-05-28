package com.carbon.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbon.tracker.R
import com.carbon.tracker.ui.model.ConsumptionItemValue
import com.carbon.tracker.ui.model.ConsumptionType
import com.carbon.tracker.ui.theme.CarbonTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumptionItem(
    titleText: String?,
    color: Color,
    dataList: List<ConsumptionItemValue>
) {
    val isOpened = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = if (isOpened.value) {
                    RoundedCornerShape(
                        topStart = 16.dp, topEnd = 16.dp
                    )
                } else {
                    MaterialTheme.shapes.medium
                }
            )
            .clickable(
                onClick = {
                    isOpened.value = !isOpened.value
                }
            )
    ) {

        Row(
            modifier = Modifier
                .background(
                    color = color,
                    shape = if (isOpened.value) {
                        RoundedCornerShape(
                            topStart = 16.dp, topEnd = 16.dp
                        )
                    } else {
                        MaterialTheme.shapes.medium
                    }
                )
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titleText ?: "",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1.0f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Icon(
                painter = painterResource(
                    if (isOpened.value) {
                        R.drawable.ic_arrow_drop_up
                    } else {
                        R.drawable.ic_arrow_drop_down
                    }
                ),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = ""
            )
        }



        if (isOpened.value) {
            Column(
                modifier = Modifier
                    .border(width = 1.dp, color = color)
                    .padding(16.dp)
            ) {
                dataList.forEach { consItem ->
                    OutlinedTextField(
                        value = consItem.keyValue.second,
                        label = {
                            Text(
                                text = consItem.keyValue.first,
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        onValueChange = {
                            if (it.isEmpty()) {
                                consItem.onChange(it)
                                return@OutlinedTextField
                            }
                            when (consItem.consumptionType) {
                                ConsumptionType.TEXT_DOUBLE -> {
                                    if (it.toDoubleOrNull() != null) {
                                        consItem.onChange(it)
                                    }
                                }

                                ConsumptionType.TEXT_INT -> {
                                    if (it.toIntOrNull() != null) {
                                        consItem.onChange(it)
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        maxLines = 1,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground
                        ),
                        textStyle = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConsumptionItem() {
    CarbonTrackerTheme {
        ConsumptionItem(
            titleText = "Text",
            color = MaterialTheme.colorScheme.secondary,
            dataList = listOf(
                ConsumptionItemValue(
                    consumptionType = ConsumptionType.TEXT_DOUBLE,
                    keyValue = Pair("Bus", "2"),
                    onChange = {}
                )
            )
        )
    }
}