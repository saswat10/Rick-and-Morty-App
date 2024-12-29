package com.saswat10.rnmapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.delete
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.rnmapp.components.common.Toolbar
import com.saswat10.rnmapp.ui.theme.DraculaBackground
import com.saswat10.rnmapp.ui.theme.DraculaCurrentLine
import com.saswat10.rnmapp.ui.theme.DraculaForeground
import com.saswat10.rnmapp.viewmodels.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar("Search")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(color = DraculaCurrentLine, shape = RoundedCornerShape(50))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
            )
            BasicTextField(
                state = viewModel.basicTextState,
                textStyle = TextStyle(
                    color = Color.White,
                ) + MaterialTheme.typography.titleMedium,
                cursorBrush = Brush.linearGradient(
                    0.0f to Color.White,
                    0.3f to Color.Green,
                    1.0f to Color.Blue,
                    start = Offset(0.0f, 50.0f),
                    end = Offset(0.0f, 100.0f)
                ),
                modifier = Modifier.weight(1f)
            )
            AnimatedVisibility(visible = viewModel.basicTextState.text.isNotBlank()) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        viewModel.basicTextState.edit { delete(0, length) }
                    })
            }


        }
        val searchText by viewModel.searchTextState.collectAsState()
        Text(searchText)
    }
}