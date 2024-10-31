package com.saswat10.rnmapp.components.character

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saswat10.network.models.domain.CharacterStatus
import com.saswat10.rnmapp.ui.theme.DraculaOrange

@Composable
fun CharacterNamePlateComponent(
    name: String,
    status: CharacterStatus
) {

    Column(
       modifier = Modifier.fillMaxWidth()
        ) {
        CharacterStatusComponent(status)
        Spacer(Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.SemiBold,
            color = DraculaOrange
        )
    }

}


@Preview
@Composable
fun NamePlate() {
    CharacterNamePlateComponent("Rick Sanchez", CharacterStatus.Alive)
}
