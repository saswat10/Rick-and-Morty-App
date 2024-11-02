package com.saswat10.rnmapp.components.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.saswat10.rnmapp.ui.theme.DraculaOrange

@Composable
fun CharacterNameComponent(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.displayMedium,
        fontWeight = FontWeight.SemiBold,
        color = DraculaOrange
    )
}
