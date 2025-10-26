package com.pharmatech.morocco.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// SHIFAA Brand Colors - Matching the logo
object ShifaaColors {
    // Primary - Gold/Golden
    val Gold = Color(0xFFD4AF37)
    val GoldLight = Color(0xFFFFD700)
    val GoldDark = Color(0xFFB8860B)

    // Secondary - Green (Pharmacy Symbol)
    val PharmacyGreen = Color(0xFF2D5F3F)
    val PharmacyGreenLight = Color(0xFF4A7C5D)
    val PharmacyGreenDark = Color(0xFF1A3D28)

    // Tertiary - Teal (Background)
    val TealDark = Color(0xFF1B4D52)
    val TealMedium = Color(0xFF2C6B6F)
    val TealLight = Color(0xFF3D8B8F)

    // Accent Colors
    val Emerald = Color(0xFF50C878)
    val DarkGreen = Color(0xFF013220)

    // Neutral Colors
    val IvoryWhite = Color(0xFFFFFFF0)
    val CreamWhite = Color(0xFFFFF8DC)
    val CharcoalBlack = Color(0xFF1C1C1C)
    val WarmGray = Color(0xFF3E3E3E)
}

// Light Theme - Premium look
val ShifaaLightColorScheme = lightColorScheme(
    primary = ShifaaColors.Gold,
    onPrimary = Color.White,
    primaryContainer = ShifaaColors.GoldLight.copy(alpha = 0.15f),
    onPrimaryContainer = ShifaaColors.GoldDark,

    secondary = ShifaaColors.PharmacyGreen,
    onSecondary = Color.White,
    secondaryContainer = ShifaaColors.PharmacyGreenLight.copy(alpha = 0.15f),
    onSecondaryContainer = ShifaaColors.PharmacyGreenDark,

    tertiary = ShifaaColors.TealMedium,
    onTertiary = Color.White,
    tertiaryContainer = ShifaaColors.TealLight.copy(alpha = 0.15f),
    onTertiaryContainer = ShifaaColors.TealDark,

    background = ShifaaColors.IvoryWhite,
    onBackground = ShifaaColors.CharcoalBlack,
    surface = Color.White,
    onSurface = ShifaaColors.CharcoalBlack,
    surfaceVariant = ShifaaColors.CreamWhite,
    onSurfaceVariant = ShifaaColors.WarmGray,

    error = Color(0xFFBA1A1A),
    onError = Color.White
)

// Dark Theme - Luxurious dark look
val ShifaaDarkColorScheme = darkColorScheme(
    primary = ShifaaColors.GoldLight,
    onPrimary = ShifaaColors.CharcoalBlack,
    primaryContainer = ShifaaColors.GoldDark,
    onPrimaryContainer = ShifaaColors.GoldLight,

    secondary = ShifaaColors.Emerald,
    onSecondary = ShifaaColors.CharcoalBlack,
    secondaryContainer = ShifaaColors.PharmacyGreen,
    onSecondaryContainer = ShifaaColors.PharmacyGreenLight,

    tertiary = ShifaaColors.TealLight,
    onTertiary = Color.White,
    tertiaryContainer = ShifaaColors.TealDark,
    onTertiaryContainer = ShifaaColors.TealLight,

    background = ShifaaColors.DarkGreen,
    onBackground = ShifaaColors.IvoryWhite,
    surface = ShifaaColors.TealDark,
    onSurface = ShifaaColors.IvoryWhite,
    surfaceVariant = ShifaaColors.WarmGray,
    onSurfaceVariant = ShifaaColors.CreamWhite,

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

// Extended color properties for special use cases
object ShifaaExtendedColors {
    val hospitalRed = Color(0xFFDC143C)
    val clinicBlue = Color(0xFF4682B4)
    val successGreen = Color(0xFF228B22)
    val warningOrange = Color(0xFFFF8C00)
    val infoBlue = Color(0xFF1E90FF)
}

// Additional gradient and UI colors for compatibility
val PrimaryGradientStart = ShifaaColors.Gold
val PrimaryGradientEnd = ShifaaColors.GoldDark
val HealthGreen = ShifaaColors.PharmacyGreen
val PremiumGold = ShifaaColors.Gold
val NeuralDark = ShifaaColors.TealDark
val ErrorRed = Color(0xFFBA1A1A)
val Gray100 = Color(0xFFF5F5F5)
val Gray200 = Color(0xFFEEEEEE)
val Gray400 = Color(0xFFBDBDBD)
val Gray600 = Color(0xFF757575)
val Gray700 = Color(0xFF616161)

