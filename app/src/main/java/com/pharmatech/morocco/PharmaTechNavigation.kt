package com.pharmatech.morocco

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pharmatech.morocco.features.medication.presentation.MedicationListScreen
import com.pharmatech.morocco.features.pharmacy.presentation.OnCallPharmacyScreen
import com.pharmatech.morocco.features.pharmacy.presentation.PharmacyMapScreen
import com.pharmatech.morocco.features.profile.presentation.ProfileScreen

/**
 * Screen Definitions
 * Defines all main screens in the app
 */
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Pharmacies : Screen("pharmacies", "Pharmacies", Icons.Default.LocalPharmacy)
    object DeGarde : Screen("de_garde", "De Garde", Icons.Default.NightlightRound)
    object Medications : Screen("medications", "Médicaments", Icons.Default.MedicalServices)
    object Profile : Screen("profile", "Profil", Icons.Default.Person)
    
    companion object {
        fun getAllScreens() = listOf(Pharmacies, DeGarde, Medications, Profile)
    }
}

/**
 * PharmaTech Morocco Main Navigation
 * Entry point with bottom navigation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmaTechNavigation() {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Pharmacies) }
    
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Screen.getAllScreens().forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                screen.icon, 
                                contentDescription = screen.title
                            ) 
                        },
                        label = { Text(screen.title) },
                        selected = selectedScreen == screen,
                        onClick = { selectedScreen = screen },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        // Screen Content
        when (selectedScreen) {
            Screen.Pharmacies -> PharmacyMapScreen(
                modifier = Modifier.padding(paddingValues)
            )
            Screen.DeGarde -> OnCallPharmacyScreen()
            Screen.Medications -> MedicationListScreen()
            Screen.Profile -> ProfileScreen()
        }
    }
}
