package com.saswat10.rnmapp.components.character

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saswat10.network.models.domain.CharacterStatus
import com.saswat10.rnmapp.ui.theme.DraculaBackground

@Composable
fun CharacterStatusComponent(
    characterStatus: CharacterStatus
) {
    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = characterStatus.color,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Status: ${characterStatus.displayName}",
            color = characterStatus.color,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

