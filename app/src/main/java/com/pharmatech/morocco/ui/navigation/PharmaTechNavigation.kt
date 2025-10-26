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
import com.pharmatech.morocco.features.hospital.presentation.HospitalMapScreen
import com.pharmatech.morocco.features.medication.presentation.MedicationScreen
import com.pharmatech.morocco.features.insurance.presentation.InsurancePortalScreen
import com.pharmatech.morocco.features.tracker.presentation.TrackerScreen
import com.pharmatech.morocco.features.profile.presentation.ProfileScreen
import com.pharmatech.morocco.ui.theme.ShifaaColors

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(Screen.Home.route, "Accueil", Icons.Default.Home)
    object Pharmacy : BottomNavItem(Screen.Pharmacy.route, "Pharmacies", Icons.Default.LocalPharmacy)
    object Hospital : BottomNavItem(Screen.Hospital.route, "Hôpitaux", Icons.Default.LocalHospital)
    object Medication : BottomNavItem(Screen.Medication.route, "Médicaments", Icons.Default.Medication)
    object Insurance : BottomNavItem(Screen.Insurance.route, "Assurance", Icons.Default.HealthAndSafety)
    object Profile : BottomNavItem(Screen.Profile.route, "Profil", Icons.Default.Person)
}

@Composable
fun PharmaTechNavigation() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Pharmacy,
        BottomNavItem.Hospital,
        BottomNavItem.Medication,
        BottomNavItem.Insurance,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Show bottom bar only on main screens
            if (currentDestination?.route in bottomNavItems.map { it.route }) {
                NavigationBar(
                    containerColor = ShifaaColors.TealDark,
                    contentColor = ShifaaColors.GoldLight
                ) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title,
                                    tint = if (currentDestination?.hierarchy?.any { it.route == item.route } == true)
                                        ShifaaColors.GoldLight
                                    else
                                        ShifaaColors.IvoryWhite.copy(alpha = 0.6f)
                                )
                            },
                            label = {
                                Text(
                                    item.title,
                                    color = if (currentDestination?.hierarchy?.any { it.route == item.route } == true)
                                        ShifaaColors.GoldLight
                                    else
                                        ShifaaColors.IvoryWhite.copy(alpha = 0.6f)
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = ShifaaColors.GoldLight,
                                selectedTextColor = ShifaaColors.GoldLight,
                                unselectedIconColor = ShifaaColors.IvoryWhite.copy(alpha = 0.6f),
                                unselectedTextColor = ShifaaColors.IvoryWhite.copy(alpha = 0.6f),
                                indicatorColor = ShifaaColors.PharmacyGreen
                            )
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
            composable(Screen.Hospital.route) {
                HospitalMapScreen(navController = navController)
            }
            composable(Screen.Medication.route) {
                MedicationScreen(navController = navController)
            }
            composable(Screen.Insurance.route) {
                InsurancePortalScreen(navController = navController)
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

