package com.pharmatech.morocco.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pharmatech.morocco.ui.navigation.Screen
import com.pharmatech.morocco.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        ShifaaColors.TealDark,
                        ShifaaColors.PharmacyGreen
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo and Title
            Icon(
                imageVector = Icons.Default.LocalPharmacy,
                contentDescription = "SHIFAA Logo",
                modifier = Modifier.size(70.dp),
                tint = ShifaaColors.GoldLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Créer un compte",
                style = MaterialTheme.typography.headlineMedium,
                color = ShifaaColors.GoldLight,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Rejoignez SHIFAA Premium",
                style = MaterialTheme.typography.bodyMedium,
                color = ShifaaColors.IvoryWhite.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nom complet") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ShifaaColors.GoldLight,
                    unfocusedBorderColor = ShifaaColors.IvoryWhite.copy(alpha = 0.5f),
                    focusedLabelColor = ShifaaColors.GoldLight,
                    unfocusedLabelColor = ShifaaColors.IvoryWhite.copy(alpha = 0.7f),
                    cursorColor = ShifaaColors.GoldLight,
                    focusedLeadingIconColor = ShifaaColors.GoldLight,
                    unfocusedLeadingIconColor = ShifaaColors.IvoryWhite.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ShifaaColors.GoldLight,
                    unfocusedBorderColor = ShifaaColors.IvoryWhite.copy(alpha = 0.5f),
                    focusedLabelColor = ShifaaColors.GoldLight,
                    unfocusedLabelColor = ShifaaColors.IvoryWhite.copy(alpha = 0.7f),
                    cursorColor = ShifaaColors.GoldLight,
                    focusedLeadingIconColor = ShifaaColors.GoldLight,
                    unfocusedLeadingIconColor = ShifaaColors.IvoryWhite.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Masquer" else "Afficher"
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ShifaaColors.GoldLight,
                    unfocusedBorderColor = ShifaaColors.IvoryWhite.copy(alpha = 0.5f),
                    focusedLabelColor = ShifaaColors.GoldLight,
                    unfocusedLabelColor = ShifaaColors.IvoryWhite.copy(alpha = 0.7f),
                    cursorColor = ShifaaColors.GoldLight,
                    focusedLeadingIconColor = ShifaaColors.GoldLight,
                    unfocusedLeadingIconColor = ShifaaColors.IvoryWhite.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmer mot de passe") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (confirmPasswordVisible) "Masquer" else "Afficher"
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = ShifaaColors.GoldLight,
                    unfocusedBorderColor = ShifaaColors.IvoryWhite.copy(alpha = 0.5f),
                    focusedLabelColor = ShifaaColors.GoldLight,
                    unfocusedLabelColor = ShifaaColors.IvoryWhite.copy(alpha = 0.7f),
                    cursorColor = ShifaaColors.GoldLight,
                    focusedLeadingIconColor = ShifaaColors.GoldLight,
                    unfocusedLeadingIconColor = ShifaaColors.IvoryWhite.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = {
                    isLoading = true
                    // Simulate registration - navigate to home
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ShifaaColors.GoldLight,
                    contentColor = ShifaaColors.TealDark
                ),
                enabled = !isLoading && name.isNotEmpty() && email.isNotEmpty() && 
                         password.isNotEmpty() && password == confirmPassword
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = ShifaaColors.TealDark
                    )
                } else {
                    Text(
                        text = "S'inscrire",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Link
            TextButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Text(
                    text = "Déjà un compte ? Se connecter",
                    color = ShifaaColors.IvoryWhite
                )
            }
        }
    }
}
