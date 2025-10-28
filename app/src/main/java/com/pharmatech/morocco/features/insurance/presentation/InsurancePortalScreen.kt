package com.pharmatech.morocco.features.insurance.presentation

import androidx.compose.animation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pharmatech.morocco.features.insurance.domain.model.InsuranceProvider
import com.pharmatech.morocco.features.medication.domain.model.Medication
import com.pharmatech.morocco.ui.components.GlassmorphicCard
import com.pharmatech.morocco.ui.components.GradientButton
import com.pharmatech.morocco.ui.theme.ShifaaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsurancePortalScreen(
    navController: NavController,
    viewModel: InsuranceViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CNSS / CNOPS") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ShifaaColors.TealDark,
                    titleContentColor = ShifaaColors.GoldLight
                )
            )
        }
    ) { paddingValues ->
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            ShifaaColors.TealDark,
                            ShifaaColors.TealDark.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            // Main content - Column instead of LazyColumn (content fits on screen)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Header Card
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color.White.copy(alpha = 0.08f),
                    borderColor = ShifaaColors.Gold.copy(alpha = 0.3f)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Calculate,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = ShifaaColors.Gold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Calculateur de Remboursement",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = ShifaaColors.GoldLight,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Estimez votre remboursement instantanément",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ShifaaColors.IvoryWhite.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Provider Selection Field
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { viewModel.onShowProviderDialog() },
                    backgroundColor = Color.White.copy(alpha = 0.05f),
                    borderColor = ShifaaColors.Gold.copy(alpha = 0.3f)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "ASSUREUR",
                            style = MaterialTheme.typography.labelSmall,
                            color = ShifaaColors.Gold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = state.selectedProvider?.displayName ?: "Sélectionner...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (state.selectedProvider != null) {
                                    Color.White
                                } else {
                                    Color.White.copy(alpha = 0.5f)
                                }
                            )
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = ShifaaColors.Gold
                            )
                        }
                    }
                }

                // Medication Selection Field
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        if (viewModel.isMedicationSelectionEnabled()) {
                            viewModel.onShowMedicationDialog()
                        }
                    },
                    backgroundColor = Color.White.copy(
                        alpha = if (viewModel.isMedicationSelectionEnabled()) 0.05f else 0.02f
                    ),
                    borderColor = ShifaaColors.Gold.copy(
                        alpha = if (viewModel.isMedicationSelectionEnabled()) 0.3f else 0.1f
                    )
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "MÉDICAMENT",
                            style = MaterialTheme.typography.labelSmall,
                            color = ShifaaColors.Gold.copy(
                                alpha = if (viewModel.isMedicationSelectionEnabled()) 1f else 0.5f
                            ),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (state.selectedMedication != null &&
                                    state.selectedProvider != null) {
                                    // Show checkmark if medication is eligible
                                    // Check eligibility from ReimbursementRateDatabase
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = ShifaaColors.Emerald,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    text = state.selectedMedication?.name
                                        ?: if (viewModel.isMedicationSelectionEnabled()) {
                                            "Rechercher un médicament..."
                                        } else {
                                            "Sélectionnez d'abord un assureur"
                                        },
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = if (state.selectedMedication != null) {
                                        Color.White
                                    } else {
                                        Color.White.copy(alpha = 0.5f)
                                    }
                                )
                            }
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = ShifaaColors.Gold.copy(
                                    alpha = if (viewModel.isMedicationSelectionEnabled()) 1f else 0.5f
                                )
                            )
                        }
                    }
                }

                // Calculate Button
                GradientButton(
                    text = "CALCULER LE REMBOURSEMENT",
                    onClick = { viewModel.onCalculateClicked() },
                    enabled = viewModel.isCalculateEnabled(),
                    colors = listOf(ShifaaColors.Gold, ShifaaColors.GoldLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (!viewModel.isCalculateEnabled()) {
                                Modifier.alpha(0.5f)
                            } else {
                                Modifier
                            }
                        )
                )

                // Results Card (conditional with animation)
                AnimatedVisibility(
                    visible = state.calculationResult != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    state.calculationResult?.let { result ->
                        GlassmorphicCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color.White.copy(alpha = 0.08f),
                            borderColor = ShifaaColors.Gold.copy(alpha = 0.4f)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                // Title
                                Text(
                                    text = "Résultat du Calcul",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ShifaaColors.GoldLight
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = ShifaaColors.Gold.copy(alpha = 0.2f))
                                Spacer(modifier = Modifier.height(12.dp))

                                // Result Row 1: Original Price
                                ResultRow(
                                    label = "Prix original",
                                    value = "${String.format("%.2f", result.originalPrice)} DH",
                                    valueColor = Color.White
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Result Row 2: Reimbursement Percentage
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Taux de remboursement",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${String.format("%.0f", result.reimbursementPercentage)}%",
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (result.reimbursementPercentage > 0) {
                                                Color.White
                                            } else {
                                                Color.Gray
                                            }
                                        )
                                        if (result.reimbursementPercentage == 0.0) {
                                            Text(
                                                text = "(Non éligible)",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Result Row 3: Refund Amount
                                ResultRow(
                                    label = "Montant remboursé",
                                    value = "${String.format("%.2f", result.refundAmount)} DH",
                                    valueColor = if (result.refundAmount > 0) {
                                        ShifaaColors.Emerald
                                    } else {
                                        Color.Gray
                                    }
                                )

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = ShifaaColors.Gold.copy(alpha = 0.3f))
                                Spacer(modifier = Modifier.height(16.dp))

                                // Result Row 4: Final Cost (emphasized)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Reste à payer",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${String.format("%.2f", result.finalCost)} DH",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = ShifaaColors.GoldLight
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialogs
        ProviderSelectionDialog(
            showDialog = state.showProviderDialog,
            selectedProvider = state.selectedProvider,
            onProviderSelected = { provider -> viewModel.onProviderSelected(provider) },
            onDismiss = { viewModel.onDismissProviderDialog() }
        )

        MedicationSearchDialog(
            showDialog = state.showMedicationDialog,
            searchQuery = state.searchQuery,
            searchResults = state.medicationSearchResults,
            onQueryChange = { query -> viewModel.onMedicationSearchQueryChanged(query) },
            onMedicationSelected = { medication -> viewModel.onMedicationSelected(medication) },
            onDismiss = { viewModel.onDismissMedicationDialog() }
        )
    }
}

/**
 * Helper composable for result rows
 */
@Composable
private fun ResultRow(
    label: String,
    value: String,
    valueColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = valueColor
        )
    }
}

/**
 * Provider Selection Dialog
 * Allows user to choose between CNSS and CNOPS
 */
@Composable
fun ProviderSelectionDialog(
    showDialog: Boolean,
    selectedProvider: InsuranceProvider?,
    onProviderSelected: (InsuranceProvider) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Sélectionner votre assureur",
                    style = MaterialTheme.typography.titleLarge,
                    color = ShifaaColors.GoldLight
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // CNSS Card
                    ProviderCard(
                        provider = InsuranceProvider.CNSS,
                        isSelected = selectedProvider == InsuranceProvider.CNSS,
                        onClick = { onProviderSelected(InsuranceProvider.CNSS) }
                    )

                    // CNOPS Card
                    ProviderCard(
                        provider = InsuranceProvider.CNOPS,
                        isSelected = selectedProvider == InsuranceProvider.CNOPS,
                        onClick = { onProviderSelected(InsuranceProvider.CNOPS) }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Annuler", color = ShifaaColors.Gold)
                }
            },
            containerColor = ShifaaColors.TealDark,
            textContentColor = ShifaaColors.IvoryWhite
        )
    }
}

/**
 * Provider Card for dialog
 */
@Composable
private fun ProviderCard(
    provider: InsuranceProvider,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = ShifaaColors.TealMedium
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.HealthAndSafety,
                    contentDescription = null,
                    tint = ShifaaColors.Gold
                )
                Text(
                    text = provider.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = ShifaaColors.IvoryWhite
                )
            }
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = ShifaaColors.Emerald
                )
            }
        }
    }
}

/**
 * Medication Search Dialog
 * Shows searchable list of medications with eligibility highlighting
 */
@Composable
fun MedicationSearchDialog(
    showDialog: Boolean,
    searchQuery: String,
    searchResults: List<Pair<Medication, Boolean>>,
    onQueryChange: (String) -> Unit,
    onMedicationSelected: (Medication) -> Unit,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Rechercher un médicament",
                    style = MaterialTheme.typography.titleLarge,
                    color = ShifaaColors.GoldLight
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Search Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onQueryChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Nom du médicament...")
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = ShifaaColors.Gold
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onQueryChange("") }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = ShifaaColors.Gold
                                    )
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ShifaaColors.Gold,
                            unfocusedBorderColor = ShifaaColors.Gold.copy(alpha = 0.5f),
                            focusedLabelColor = ShifaaColors.Gold,
                            cursorColor = ShifaaColors.Gold,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    // Results List
                    if (searchQuery.isEmpty()) {
                        // Initial state
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    text = "Commencez à taper pour rechercher...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else if (searchResults.isEmpty()) {
                        // Empty state
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.SearchOff,
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    text = "Aucun médicament trouvé",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        // Results
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(searchResults) { (medication, isEligible) ->
                                MedicationListItem(
                                    medication = medication,
                                    isEligible = isEligible,
                                    onClick = { onMedicationSelected(medication) }
                                )
                            }
                        }
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
}

/**
 * Medication List Item with eligibility highlighting
 */
@Composable
private fun MedicationListItem(
    medication: Medication,
    isEligible: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = if (isEligible) {
                ShifaaColors.TealMedium
            } else {
                ShifaaColors.TealMedium.copy(alpha = 0.5f)
            }
        ),
        border = if (isEligible) {
            androidx.compose.foundation.BorderStroke(
                1.dp,
                ShifaaColors.Gold.copy(alpha = 0.4f)
            )
        } else {
            null
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isEligible) 2.dp else 0.dp
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Leading icon
            Icon(
                if (isEligible) Icons.Default.CheckCircle else Icons.Default.Info,
                contentDescription = null,
                tint = if (isEligible) ShifaaColors.Emerald else Color.Gray,
                modifier = Modifier.size(24.dp)
            )

            // Medication info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isEligible) FontWeight.SemiBold else FontWeight.Normal,
                    color = Color.White
                )
                Text(
                    text = medication.composition,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
                Text(
                    text = "${String.format("%.2f", medication.ppv)} DH",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ShifaaColors.GoldLight
                )
            }

            // Trailing arrow
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = ShifaaColors.Gold
            )
        }
    }
}
