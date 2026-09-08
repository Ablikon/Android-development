package kz.nazar.attention.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Today
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Top-level destinations. The screens reached from them - session detail, chain detail and the
 * usage-access onboarding - are pushed on top and are added in later iterations.
 */
enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
) {
    Today("today", "Today", Icons.Filled.Today),
    Chains("chains", "Chains", Icons.Filled.AccountTree),
    Settings("settings", "Settings", Icons.Filled.Settings),
}
