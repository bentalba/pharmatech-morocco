package com.pharmatech.morocco.features.pharmacy.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.pharmatech.morocco.features.pharmacy.domain.model.PharmacyData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PharmacyMapScreen() {
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    // Launch permission request on first composition
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val pharmacies = remember { PharmacyData.getAllPharmacies() }
    val kenitiraPharmacy = pharmacies.first()
    
    // Camera position centered on Kenitra pharmacy
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(kenitiraPharmacy.latitude, kenitiraPharmacy.longitude),
            15f
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pharmacies Près de Vous") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (hasLocationPermission) {
                FloatingActionButton(
                    onClick = {
                        // Center on user location (we'll implement this later)
                    }
                ) {
                    Icon(Icons.Default.MyLocation, "Ma position")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (hasLocationPermission) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = true,
                        myLocationButtonEnabled = false
                    )
                ) {
                    // Add marker for each pharmacy
                    pharmacies.forEach { pharmacy ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(pharmacy.latitude, pharmacy.longitude)
                            ),
                            title = pharmacy.name,
                            snippet = "${pharmacy.address}\n${pharmacy.phoneNumber}"
                        )
                    }
                }

                // Pharmacy info card at the bottom
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = kenitiraPharmacy.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = kenitiraPharmacy.address,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = kenitiraPharmacy.city,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "📞 ${kenitiraPharmacy.phoneNumber}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "💵 Paiement: Espèces uniquement",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "🕐 Ouvert Lun-Ven: 09:00-12:30, 15:00-19:30",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = "🕐 Samedi: 09:00-13:00",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                // Show permission required message
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📍 Autorisation de localisation requise",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Pour trouver les pharmacies près de vous, nous avons besoin d'accéder à votre position.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        ) {
                            Text("Autoriser la localisation")
                        }
                    }
                }
            }
        }
    }
}
