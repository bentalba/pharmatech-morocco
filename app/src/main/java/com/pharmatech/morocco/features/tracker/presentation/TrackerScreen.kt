package com.pharmatech.morocco.features.tracker.presentation

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
import com.pharmatech.morocco.ui.theme.HealthGreen
import com.pharmatech.morocco.ui.theme.ShifaaColors
import java.text.SimpleDateFormat
import java.util.*

data class MedicationSchedule(
    val id: String,
    val medicationName: String,
    val dosage: String,
    val time: String,
    val isTaken: Boolean,
    val scheduledDate: Date
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(navController: NavController) {
    val mockSchedule = remember {
        listOf(
            MedicationSchedule(
                id = "1",
                medicationName = "Doliprane 1000mg",
                dosage = "1 tablet",
                time = "08:00",
                isTaken = true,
                scheduledDate = Date()
            ),
            MedicationSchedule(
                id = "2",
                medicationName = "Amoxicilline 500mg",
                dosage = "1 capsule",
                time = "12:00",
                isTaken = true,
                scheduledDate = Date()
            ),
            MedicationSchedule(
                id = "3",
                medicationName = "Omeprazole 20mg",
                dosage = "1 capsule",
                time = "14:30",
                isTaken = false,
                scheduledDate = Date()
            ),
            MedicationSchedule(
                id = "4",
                medicationName = "Doliprane 1000mg",
                dosage = "1 tablet",
                time = "20:00",
                isTaken = false,
                scheduledDate = Date()
            )
        )
    }

    val takenCount = mockSchedule.count { it.isTaken }
    val totalCount = mockSchedule.size
    val adherenceRate = if (totalCount > 0) (takenCount * 100 / totalCount) else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Medication Tracker", style = MaterialTheme.typography.titleLarge)
                        Text(
                            "$takenCount of $totalCount doses taken today",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add tracker */ },
                containerColor = ShifaaColors.Gold
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ShifaaColors.PharmacyGreen
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Today's Progress",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = ShifaaColors.GoldLight
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$adherenceRate%",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = ShifaaColors.GoldLight,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Adherence Rate",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ShifaaColors.IvoryWhite
                                )
                            }
                            HorizontalDivider(
                                modifier = Modifier
                                    .height(48.dp)
                                    .width(1.dp),
                                color = ShifaaColors.GoldLight.copy(alpha = 0.3f)
                            )
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$takenCount/$totalCount",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ShifaaColors.IvoryWhite
                                )
                                Text(
                                    text = "Doses Taken",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = ShifaaColors.IvoryWhite
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
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
                            Column {
                                Text(
                                    text = "Weekly Streak",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "Keep up the great work!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            Text(
                                text = "5 days",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = ShifaaColors.Gold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = 0.71f,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = ShifaaColors.Gold,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Today's Schedule",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(mockSchedule) { schedule ->
                MedicationScheduleCard(
                    schedule = schedule,
                    onTaken = { /* TODO: Mark as taken */ }
                )
            }
        }
    }
}

@Composable
fun MedicationScheduleCard(
    schedule: MedicationSchedule,
    onTaken: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (schedule.isTaken)
                HealthGreen.copy(alpha = 0.05f)
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = if (schedule.isTaken) HealthGreen.copy(alpha = 0.1f) else ShifaaColors.Gold.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (schedule.isTaken) Icons.Default.CheckCircle else Icons.Default.Schedule,
                        contentDescription = null,
                        tint = if (schedule.isTaken) HealthGreen else ShifaaColors.Gold,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Column {
                    Text(
                        text = schedule.medicationName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = schedule.dosage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = schedule.time,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }

            if (schedule.isTaken) {
                Surface(
                    color = HealthGreen.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = HealthGreen
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Taken",
                            style = MaterialTheme.typography.labelMedium,
                            color = HealthGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                Button(
                    onClick = onTaken,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ShifaaColors.Gold
                    )
                ) {
                    Text("Take")
                }
            }
        }
    }
}

