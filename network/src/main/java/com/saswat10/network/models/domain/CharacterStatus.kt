package com.saswat10.network.models.domain

import androidx.compose.ui.graphics.Color

val DraculaRed = Color(0xFFff5555)
val DraculaGreen = Color(0xFF50fa7b)
val DraculaYellow = Color(0xFFf1fa8c)

sealed class CharacterStatus(val displayName:String, val color: Color ) {
    object Dead: CharacterStatus("Dead", DraculaRed)
    object Unknown: CharacterStatus("Unknown", DraculaYellow)
    object Alive: CharacterStatus("Alive", DraculaGreen)
}
