package com.saswat10.rnmapp.components.common

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun CharacterImage(
    imageUrl : String,
    contentDesc: String
){
    SubcomposeAsyncImage(
        model = imageUrl,
        contentDescription = contentDesc,
        modifier = Modifier.fillMaxWidth().aspectRatio(1f).clip(RoundedCornerShape(12.dp)),
        loading = {
            LoadingIndicator()
        }
    )
}
