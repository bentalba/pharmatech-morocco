package com.pharmatech.morocco.core.utils

import androidx.compose.ui.graphics.Color

object AppColors {
    // Primary Colors
    val PrimaryGradientStart = Color(0xFF667EEA)
    val PrimaryGradientEnd = Color(0xFF764BA2)

    // Health Colors
    val HealthGreen = Color(0xFF10B981)
    val HealthGreenLight = Color(0xFF34D399)
    val HealthGreenDark = Color(0xFF059669)

    // Alert Colors
    val AlertAmber = Color(0xFFF59E0B)
    val AlertRed = Color(0xFFEF4444)
    val AlertYellow = Color(0xFFFBBF24)

    // Premium
    val PremiumGold = Color(0xFFFBBF24)

    // Backgrounds
    val NeuralDark = Color(0xFF0F172A)
    val GlassWhite = Color(0x1AFFFFFF)

    // Status Colors
    val SuccessGreen = Color(0xFF22C55E)
    val ErrorRed = Color(0xFFEF4444)
    val WarningOrange = Color(0xFFF97316)
    val InfoBlue = Color(0xFF3B82F6)

    // Category Colors
    val CategoryPurple = Color(0xFF8B5CF6)
    val CategoryPink = Color(0xFFEC4899)
    val CategoryIndigo = Color(0xFF6366F1)
    val CategoryTeal = Color(0xFF14B8A6)
    val CategoryOrange = Color(0xFFF97316)
}

object AppDimensions {
    const val SpacingXS = 4
    const val SpacingSM = 8
    const val SpacingMD = 16
    const val SpacingLG = 24
    const val SpacingXL = 32
    const val SpacingXXL = 48

    const val CornerRadiusSM = 8
    const val CornerRadiusMD = 12
    const val CornerRadiusLG = 16
    const val CornerRadiusXL = 24

    const val IconSizeSM = 20
    const val IconSizeMD = 24
    const val IconSizeLG = 32
    const val IconSizeXL = 48

    const val ButtonHeight = 56
    const val CardElevation = 4
}

object AnimationDurations {
    const val FAST = 150
    const val NORMAL = 300
    const val SLOW = 500
}

sealed class UIState<out T> {
    object Idle : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String) : UIState<Nothing>()
}

