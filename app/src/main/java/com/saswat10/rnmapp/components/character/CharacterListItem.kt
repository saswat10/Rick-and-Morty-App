package com.saswat10.rnmapp.components.character

import android.provider.ContactsContract.Data
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.domain.CharacterStatus
import com.saswat10.network.models.domain.DraculaYellow
import com.saswat10.rnmapp.components.common.CharacterImage
import com.saswat10.rnmapp.components.common.DataPoint
import com.saswat10.rnmapp.components.common.DataPointComponent
import com.saswat10.rnmapp.ui.theme.DraculaComment
import com.saswat10.rnmapp.ui.theme.DraculaCurrentLine
import com.saswat10.rnmapp.ui.theme.DraculaForeground
import com.saswat10.rnmapp.ui.theme.DraculaGreen
import com.saswat10.rnmapp.ui.theme.DraculaOrange
import com.saswat10.rnmapp.ui.theme.DraculaPink
import com.saswat10.rnmapp.ui.theme.DraculaPurple
import com.saswat10.rnmapp.ui.theme.DraculaRed

@Composable
fun CharacterGridItem(
    modifier: Modifier,
    character: Character,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(listOf(Color.Transparent, DraculaOrange)),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box() {
            CharacterImage(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
                character.image,
                character.name
            )
            CharacterStatusCircle(character.status)
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = character.name,
            color = DraculaOrange,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

}

@Composable
fun CharacterListItem(
    modifier: Modifier,
    character: Character,
//    onClick: () -> Unit,
    characterDataPoint: List<DataPoint>
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(Color.Transparent, DraculaPurple)),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
//            .clickable { onClick() }
            .height(140.dp),
    ) {
        Box {
            CharacterImage(
                Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
                character.image,
                character.name
            )
            CharacterStatusCircle(character.status)
        }
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            content = {
                items(
                    items = listOf(DataPoint(title = "Name", content = character.name)) + characterDataPoint,
                    key = { it.hashCode() }
                ) { dataPoint ->
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        DataPointComponent(sanitizeDataPoint(dataPoint = dataPoint), DraculaPink)
                    }
                }
            })
    }

}


@Composable
fun CharacterStatusCircle(status: CharacterStatus) {
    Box(
        modifier = Modifier
            .padding(top = 6.dp, start = 6.dp)
            .background(
                brush = Brush.radialGradient(listOf(Color.Black, Color.Transparent)),
                shape = CircleShape
            )
            .size(18.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(all = 2.dp)
                .background(
//                    brush = Brush.radialGradient(listOf(Color.Black, status.color)),
                    color = status.color,
                    shape = CircleShape
                )
                .size(10.dp)
        )
    }
}

private fun sanitizeDataPoint(dataPoint: DataPoint): DataPoint{
    val newDescription = if (dataPoint.content.length > 10){
        dataPoint.content.take(8) + ".."
    }else {
        dataPoint.content
    }

    return DataPoint(dataPoint.title, newDescription)
}
