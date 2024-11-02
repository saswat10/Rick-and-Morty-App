package com.saswat10.rnmapp.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saswat10.rnmapp.ui.theme.DraculaOrange


data class DataPoint(
    val title: String,
    val content: String
)

@Composable
fun DataPointComponent(
    dataPoint: DataPoint,
){
    Column(
        modifier = Modifier.padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = dataPoint.title, style = MaterialTheme.typography.labelLarge, color = DraculaOrange)
        Text(text = dataPoint.content, style = MaterialTheme.typography.titleLarge)
    }
}


@Preview
@Composable
fun DataComponentPreview(){
    DataPointComponent(DataPoint("Last known location", "Rick Sanchez"))
}
