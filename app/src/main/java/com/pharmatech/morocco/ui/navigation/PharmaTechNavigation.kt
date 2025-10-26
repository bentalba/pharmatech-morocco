package com.pharmatech.morocco.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pharmatech.morocco.features.auth.presentation.LoginScreen
import com.pharmatech.morocco.features.auth.presentation.RegisterScreen
import com.pharmatech.morocco.features.auth.presentation.SplashScreen
import com.pharmatech.morocco.features.home.presentation.HomeScreen
import com.pharmatech.morocco.features.pharmacy.presentation.PharmacyScreen
import com.pharmatech.morocco.features.medication.presentation.MedicationScreen
import com.pharmatech.morocco.features.tracker.presentation.TrackerScreen
import com.pharmatech.morocco.features.profile.presentation.ProfileScreen

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Home", Icons.Default.Home)
    object Pharmacy : BottomNavItem(Screen.Pharmacy.route, "Pharmacy", Icons.Default.LocalPharmacy)
    object Medication : BottomNavItem(Screen.Medication.route, "Medication", Icons.Default.Medication)
    object Tracker : BottomNavItem(Screen.Tracker.route, "Tracker", Icons.Default.CalendarMonth)
    object Profile : BottomNavItem(Screen.Profile.route, "Profile", Icons.Default.Person)
}

@Composable
fun PharmaTechNavigation() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Pharmacy,
        BottomNavItem.Medication,
        BottomNavItem.Tracker,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Show bottom bar only on main screens
            if (currentDestination?.route in bottomNavItems.map { it.route }) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Auth
            composable(Screen.Splash.route) {
                SplashScreen(navController = navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController = navController)
            }

            // Main Screens
            composable(Screen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(Screen.Pharmacy.route) {
                PharmacyScreen(navController = navController)
            }
            composable(Screen.Medication.route) {
                MedicationScreen(navController = navController)
            }
            composable(Screen.Tracker.route) {
                TrackerScreen(navController = navController)
            }
            composable(Screen.Profile.route) {
                ProfileScreen(navController = navController)
            }
        }
    }
}

