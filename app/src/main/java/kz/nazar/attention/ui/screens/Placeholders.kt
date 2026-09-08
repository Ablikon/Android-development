package kz.nazar.attention.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
private fun ScreenScaffold(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun TodayScreen(modifier: Modifier = Modifier) = ScreenScaffold(
    title = "Today",
    subtitle = "Today's attention sessions, how badly each one was fragmented, and the longest " +
        "unbroken stretch of focus.",
    modifier = modifier,
)

@Composable
fun ChainsScreen(modifier: Modifier = Modifier) = ScreenScaffold(
    title = "Chains",
    subtitle = "Entry points ranked by the time lost to the chains they started, and the " +
        "app-to-app transition graph behind them.",
    modifier = modifier,
)

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) = ScreenScaffold(
    title = "Settings",
    subtitle = "Usage access, which apps count as focus versus drift, chain thresholds and " +
        "data export.",
    modifier = modifier,
)
