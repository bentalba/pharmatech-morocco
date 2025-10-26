package com.pharmatech.morocco.features.hospital.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.pharmatech.morocco.features.hospital.domain.model.Hospital
import com.pharmatech.morocco.features.hospital.domain.model.HospitalType
import com.pharmatech.morocco.features.hospital.domain.model.HealthFacilityDatabase
import com.pharmatech.morocco.ui.theme.ShifaaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalMapScreen(navController: NavController) {
    var selectedType by remember { mutableStateOf<HospitalType?>(null) }
    var selectedHospital by remember { mutableStateOf<Hospital?>(null) }
    var showTypeFilterDialog by remember { mutableStateOf(false) }

    val allHospitals = remember { HealthFacilityDatabase.getAllHospitals() }
    val displayedHospitals = remember(selectedType) {
        if (selectedType == null) allHospitals
        else HealthFacilityDatabase.getHospitalsByType(selectedType!!)
    }

    // Default camera position (Morocco center)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(33.5731, -7.5898),
            6f
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Hôpitaux Publics")
                        if (selectedType != null) {
                            Text(
                                text = selectedType!!.fullName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ShifaaColors.TealDark,
                    titleContentColor = ShifaaColors.GoldLight
                ),
                actions = {
                    // Filter button
                    IconButton(onClick = { showTypeFilterDialog = true }) {
                        Icon(
                            Icons.Default.FilterList,
                            "Filtrer",
                            tint = ShifaaColors.GoldLight
                        )
                    }

                    // Clear filter button
                    if (selectedType != null) {
                        IconButton(onClick = { selectedType = null }) {
                            Icon(
                                Icons.Default.Clear,
                                "Effacer filtre",
                                tint = ShifaaColors.GoldLight
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = true
                )
            ) {
                // Add markers for each hospital
                displayedHospitals.forEach { hospital ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(hospital.latitude, hospital.longitude)
                        ),
                        title = hospital.name,
                        snippet = "${hospital.type.fullName}\n${hospital.address ?: ""}",
                        onClick = {
                            selectedHospital = hospital
                            true
                        }
                    )
                }
            }

            // Hospital type chips (horizontal scrollable)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // All types chip
                item {
                    HospitalTypeChip(
                        label = "Tous",
                        count = allHospitals.size,
                        isSelected = selectedType == null,
                        color = ShifaaColors.Gold,
                        onClick = { selectedType = null }
                    )
                }

                // Individual type chips
                items(HospitalType.values().toList()) { type ->
                    val count = HealthFacilityDatabase.getHospitalsByType(type).size
                    if (count > 0) {
                        HospitalTypeChip(
                            label = type.abbreviation,
                            count = count,
                            isSelected = selectedType == type,
                            color = Color(android.graphics.Color.parseColor(type.color)),
                            onClick = { selectedType = type }
                        )
                    }
                }
            }

            // Selected hospital info card
            if (selectedHospital != null) {
                HospitalInfoCard(
                    hospital = selectedHospital!!,
                    onDismiss = { selectedHospital = null },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }

            // Empty state
            if (displayedHospitals.isEmpty()) {
                EmptyHospitalState(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }

    // Type filter dialog
    if (showTypeFilterDialog) {
        HospitalTypeFilterDialog(
            onDismiss = { showTypeFilterDialog = false },
            onTypeSelected = { type ->
                selectedType = type
                showTypeFilterDialog = false
            }
        )
    }
}

@Composable
fun HospitalTypeChip(
    label: String,
    count: Int,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) color else color.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = count.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        leadingIcon = if (isSelected) {
            { Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp)) }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = if (isSelected) ShifaaColors.TealMedium else MaterialTheme.colorScheme.surface,
            labelColor = if (isSelected) ShifaaColors.GoldLight else MaterialTheme.colorScheme.onSurface,
            selectedContainerColor = ShifaaColors.TealMedium,
            selectedLabelColor = ShifaaColors.GoldLight
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = color,
            selectedBorderColor = ShifaaColors.Gold,
            borderWidth = 2.dp,
            selectedBorderWidth = 2.dp
        )
    )
}

@Composable
fun HospitalInfoCard(
    hospital: Hospital,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = ShifaaColors.TealDark
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = hospital.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ShifaaColors.GoldLight
                    )
                    Surface(
                        color = Color(android.graphics.Color.parseColor(hospital.type.color)),
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            text = "${hospital.type.abbreviation} - ${hospital.type.fullName}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        "Fermer",
                        tint = ShifaaColors.GoldLight
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(
                icon = Icons.Default.LocationOn,
                label = "Région",
                value = "${hospital.region} - ${hospital.province}",
                iconColor = ShifaaColors.Gold
            )

            if (hospital.address != null) {
                InfoRow(
                    icon = Icons.Default.Home,
                    label = "Adresse",
                    value = hospital.address,
                    iconColor = ShifaaColors.Gold
                )
            }

            if (hospital.phoneNumber != null) {
                InfoRow(
                    icon = Icons.Default.Phone,
                    label = "Téléphone",
                    value = hospital.phoneNumber,
                    iconColor = ShifaaColors.Gold
                )
            }

            if (hospital.emergencyServices) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocalHospital,
                        "Services d'urgence",
                        tint = ShifaaColors.Emerald,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Services d'urgence disponibles",
                        style = MaterialTheme.typography.bodySmall,
                        color = ShifaaColors.Emerald,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    iconColor: Color = MaterialTheme.colorScheme.primary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = ShifaaColors.CreamWhite.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = ShifaaColors.IvoryWhite
            )
        }
    }
}

@Composable
fun EmptyHospitalState(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = ShifaaColors.TealDark.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.LocalHospital,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = ShifaaColors.Gold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Aucun hôpital trouvé",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = ShifaaColors.GoldLight
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Aucun hôpital ne correspond aux critères de filtrage sélectionnés.",
                style = MaterialTheme.typography.bodyMedium,
                color = ShifaaColors.IvoryWhite
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Importez les données des hôpitaux pour commencer",
                style = MaterialTheme.typography.bodySmall,
                color = ShifaaColors.Gold
            )
        }
    }
}

@Composable
fun HospitalTypeFilterDialog(
    onDismiss: () -> Unit,
    onTypeSelected: (HospitalType?) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Filtrer par type d'hôpital",
                color = ShifaaColors.Gold,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // All hospitals option
                HospitalTypeDialogItem(
                    type = null,
                    count = HealthFacilityDatabase.getAllHospitals().size,
                    onClick = { onTypeSelected(null) }
                )

                Divider()

                // Each type
                HospitalType.values().forEach { type ->
                    val count = HealthFacilityDatabase.getHospitalsByType(type).size
                    HospitalTypeDialogItem(
                        type = type,
                        count = count,
                        onClick = { onTypeSelected(type) }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fermer", color = ShifaaColors.Gold)
            }
        },
        containerColor = ShifaaColors.TealDark,
        textContentColor = ShifaaColors.IvoryWhite
    )
}

@Composable
fun HospitalTypeDialogItem(
    type: HospitalType?,
    count: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = ShifaaColors.TealMedium
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = type?.abbreviation ?: "Tous",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ShifaaColors.GoldLight
                )
                if (type != null) {
                    Text(
                        text = type.fullName,
                        style = MaterialTheme.typography.bodySmall,
                        color = ShifaaColors.IvoryWhite.copy(alpha = 0.8f)
                    )
                }
            }

            Surface(
                color = if (type != null) {
                    Color(android.graphics.Color.parseColor(type.color))
                } else {
                    ShifaaColors.Gold
                },
                shape = CircleShape
            ) {
                Text(
                    text = count.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
