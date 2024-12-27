package com.saswat10.rnmapp.components.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saswat10.network.models.domain.DraculaGreen
import com.saswat10.network.models.domain.DraculaYellow
import com.saswat10.rnmapp.R
import com.saswat10.rnmapp.ui.theme.DraculaOrange

@Composable
fun Toolbar(
    title: String,
    onBackAction: (() -> Unit)? = null
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (onBackAction != null) {
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, top = 16.dp)
//                        .size(24.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onBackAction() },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back_arrow),
                        contentDescription = "Back Action"
                    )
                }
            }
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
                text = title,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider(color = DraculaOrange)

    }
}