package com.pharmatech.morocco.features.medication.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pharmatech.morocco.ui.navigation.Screen
import com.pharmatech.morocco.ui.theme.ShifaaColors
import com.pharmatech.morocco.ui.theme.HealthGreen

data class MedicationItem(
    val id: String,
    val name: String,
    val genericName: String,
    val laboratory: String,
    val dosage: String,
    val form: String,
    val price: Double,
    val isReimbursable: Boolean,
    val reimbursementRate: Int,
    val requiresPrescription: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val mockMedications = remember {
        listOf(
            MedicationItem(
                id = "1",
                name = "Doliprane 1000mg",
                genericName = "Paracétamol",
                laboratory = "Sanofi",
                dosage = "1000mg",
                form = "Tablet",
                price = 25.50,
                isReimbursable = true,
                reimbursementRate = 70,
                requiresPrescription = false
            ),
            MedicationItem(
                id = "2",
                name = "Amoxicilline 500mg",
                genericName = "Amoxicilline",
                laboratory = "GlaxoSmithKline",
                dosage = "500mg",
                form = "Capsule",
                price = 45.00,
                isReimbursable = true,
                reimbursementRate = 90,
                requiresPrescription = true
            ),
            MedicationItem(
                id = "3",
                name = "Ibuprofène 400mg",
                genericName = "Ibuprofène",
                laboratory = "Pfizer",
                dosage = "400mg",
                form = "Tablet",
                price = 30.00,
                isReimbursable = true,
                reimbursementRate = 70,
                requiresPrescription = false
            ),
            MedicationItem(
                id = "4",
                name = "Omeprazole 20mg",
                genericName = "Omeprazole",
                laboratory = "AstraZeneca",
                dosage = "20mg",
                form = "Capsule",
                price = 55.00,
                isReimbursable = true,
                reimbursementRate = 70,
                requiresPrescription = true
            ),
            MedicationItem(
                id = "5",
                name = "Cétirizine 10mg",
                genericName = "Cétirizine",
                laboratory = "Johnson & Johnson",
                dosage = "10mg",
                form = "Tablet",
                price = 35.00,
                isReimbursable = false,
                reimbursementRate = 0,
                requiresPrescription = false
            ),
            MedicationItem(
                id = "6",
                name = "Métformine 850mg",
                genericName = "Métformine",
                laboratory = "Merck",
                dosage = "850mg",
                form = "Tablet",
                price = 40.00,
                isReimbursable = true,
                reimbursementRate = 100,
                requiresPrescription = true
            )
        )
    }

    val filteredMedications = mockMedications.filter { medication ->
        medication.name.contains(searchQuery, ignoreCase = true) ||
                medication.genericName.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Médicaments", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "${filteredMedications.size} medications available",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search medications...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                trailingIcon = {
                    Row {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredMedications.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No medications found",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                } else {
                    items(filteredMedications) { medication ->
                        MedicationCard(medication = medication)
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationCard(medication: MedicationItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = medication.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Generic: ${medication.genericName}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = HealthGreen
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = medication.laboratory,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                if (medication.requiresPrescription) {
                    Surface(
                        color = Color.Red.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.MedicalServices,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color.Red
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Rx",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${medication.dosage} - ${medication.form}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (medication.isReimbursable) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocalOffer,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = ShifaaColors.PharmacyGreen
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Reimbursement: ${medication.reimbursementRate}%",
                                style = MaterialTheme.typography.bodySmall,
                                color = ShifaaColors.PharmacyGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${medication.price} MAD",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ShifaaColors.Gold
                    )
                    if (medication.isReimbursable) {
                        val finalPrice = medication.price * (100 - medication.reimbursementRate) / 100
                        Text(
                            text = "After reimbursement: %.2f MAD".format(finalPrice),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO: Show details */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Details")
                }
                Button(
                    onClick = { /* TODO: Find in pharmacy */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        Icons.Default.LocalPharmacy,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Find")
                }
            }
        }
    }
}

