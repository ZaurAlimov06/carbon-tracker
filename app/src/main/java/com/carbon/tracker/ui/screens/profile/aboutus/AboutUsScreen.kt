package com.carbon.tracker.ui.screens.profile.aboutus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carbon.tracker.BuildConfig
import com.carbon.tracker.R
import com.carbon.tracker.ui.theme.CarbonTrackerTheme
import com.carbon.tracker.ui.theme.Dimension
import com.carbon.tracker.ui.theme.LocalSpacing

@Composable
fun AboutUsScreen(
    spacing: Dimension = LocalSpacing.current
) {
    Column(
        modifier = Modifier
            .padding(
                horizontal = spacing.spaceScreenHorizontalPadding,
                vertical = spacing.spaceScreenVerticalPadding
            )
    ) {
        Text(
            text = stringResource(id = R.string.about_us_title_text_about_us),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1.0f)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    text = stringResource(id = R.string.about_us_text),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Text(
            text = "v${BuildConfig.VERSION_NAME}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewAboutUsScreen() {
    CarbonTrackerTheme {
        AboutUsScreen()
    }
}