package com.saswat10.network.models.domain

import androidx.compose.ui.graphics.Color

sealed class CharacterStatus(val displayName:String, val color: Color ) {
    object Dead: CharacterStatus("Dead", Color.Red)
    object Unknown: CharacterStatus("Unknown", Color.Yellow)
    object Alive: CharacterStatus("Alive", Color.Green)
}
