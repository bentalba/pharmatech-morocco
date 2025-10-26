package com.pharmatech.morocco.features.insurance.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pharmatech.morocco.ui.theme.ShifaaColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsurancePortalScreen(navController: NavController) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = ShifaaColors.TealDark
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.HealthAndSafety,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = ShifaaColors.Gold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Portail d'Assurance Maladie",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = ShifaaColors.GoldLight
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Accédez aux services CNSS et CNOPS",
                            style = MaterialTheme.typography.bodyMedium,
                            color = ShifaaColors.IvoryWhite
                        )
                    }
                }
            }

            // CNSS Section
            item {
                InsuranceCard(
                    title = "CNSS",
                    subtitle = "Caisse Nationale de Sécurité Sociale",
                    icon = Icons.Default.BusinessCenter,
                    features = listOf(
                        "Vérification de l'affiliation",
                        "Droits aux prestations",
                        "Pharmacies conventionnées",
                        "Remboursement des médicaments",
                        "Attestation d'assurance"
                    ),
                    color = ShifaaColors.TealMedium
                )
            }

            // CNOPS Section
            item {
                InsuranceCard(
                    title = "CNOPS",
                    subtitle = "Caisse Nationale des Organismes de Prévoyance Sociale",
                    icon = Icons.Default.AccountBalance,
                    features = listOf(
                        "Consultation de droits",
                        "Tiers payant",
                        "Liste des médicaments remboursables",
                        "Taux de remboursement",
                        "Déclaration de pharmacien"
                    ),
                    color = ShifaaColors.PharmacyGreen
                )
            }

            // Coming Soon message
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = ShifaaColors.Gold.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = ShifaaColors.Gold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Fonctionnalités à venir",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ShifaaColors.Gold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Les services CNSS/CNOPS seront bientôt disponibles avec :",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• Vérification en temps réel\n• Remboursements automatiques\n• Pharmacies partenaires\n• Historique des prestations",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Quick Links
            item {
                Text(
                    text = "Liens utiles",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickLinkCard(
                        title = "Site web CNSS",
                        subtitle = "www.cnss.ma",
                        icon = Icons.Default.Language
                    )
                    QuickLinkCard(
                        title = "Site web CNOPS",
                        subtitle = "www.cnops.org.ma",
                        icon = Icons.Default.Language
                    )
                    QuickLinkCard(
                        title = "Service client CNSS",
                        subtitle = "0800 00 555",
                        icon = Icons.Default.Phone
                    )
                    QuickLinkCard(
                        title = "Service client CNOPS",
                        subtitle = "0537 27 45 89",
                        icon = Icons.Default.Phone
                    )
                }
            }
        }
    }
}

@Composable
fun InsuranceCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    features: List<String>,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = ShifaaColors.Gold
                )
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = ShifaaColors.GoldLight
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ShifaaColors.IvoryWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = ShifaaColors.Gold.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Services disponibles :",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = ShifaaColors.GoldLight
            )

            Spacer(modifier = Modifier.height(8.dp))

            features.forEach { feature ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = ShifaaColors.Emerald
                    )
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ShifaaColors.IvoryWhite
                    )
                }
            }
        }
    }
}

@Composable
fun QuickLinkCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = ShifaaColors.TealMedium
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = ShifaaColors.Gold
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = ShifaaColors.GoldLight
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ShifaaColors.IvoryWhite
                )
            }
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                tint = ShifaaColors.Gold
            )
        }
    }
}
