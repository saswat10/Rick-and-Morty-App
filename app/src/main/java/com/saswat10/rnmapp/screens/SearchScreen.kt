package com.saswat10.rnmapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.delete
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.domain.CharacterStatus
import com.saswat10.rnmapp.components.character.CharacterListItem
import com.saswat10.rnmapp.components.common.DataPoint
import com.saswat10.rnmapp.components.common.Toolbar
import com.saswat10.rnmapp.ui.theme.DraculaBackground
import com.saswat10.rnmapp.ui.theme.DraculaCurrentLine
import com.saswat10.rnmapp.ui.theme.DraculaForeground
import com.saswat10.rnmapp.ui.theme.DraculaOrange
import com.saswat10.rnmapp.ui.theme.DraculaPurple
import com.saswat10.rnmapp.ui.theme.DraculaYellow
import com.saswat10.rnmapp.viewmodels.ScreenState
import com.saswat10.rnmapp.viewmodels.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navController: NavController
) {
    DisposableEffect(key1 = Unit) {
        val job = viewModel.observeUserSearch()
        onDispose { job.cancel() }
    }
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize(),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Toolbar("Search")
        AnimatedVisibility(visible = screenState is ScreenState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .height(6.dp)
                    .fillMaxWidth(),
                color = DraculaOrange
            )
        }
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
        when (val state = screenState) {
            ScreenState.Empty -> {
                Spacer(Modifier.height(6.dp))
                Text("Search Characters!!", fontSize = 24.sp)
            }

            ScreenState.Loading -> {}
            is ScreenState.Content -> {
                Column {
                    SearchScreenContent(
                        state, viewModel::toggleStatus,
                        navController = navController
                    )
                }
            }

            is ScreenState.Error -> {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Info,
                        contentDescription = null,
                        Modifier.size(80.dp),
                        tint = DraculaPurple
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        state.message,
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        { viewModel.basicTextState.clearText() },
                        colors = ButtonColors(
                            containerColor = DraculaPurple,
                            contentColor = DraculaBackground,
                            disabledContentColor = DraculaBackground,
                            disabledContainerColor = Color.DarkGray
                        )
                    ) {
                        Text("Clear Search")
                    }
                }

            }
        }
    }
}

@Composable
private fun SearchScreenContent(
    content: ScreenState.Content,
    onClicked: (CharacterStatus) -> Unit,
    navController: NavController
) {
    Text(
        text = "${content.results.size} results for query '${content.userQuery}'",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )
    Spacer(Modifier.height(12.dp))

    Row(
        Modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        content.filterState.statuses.forEach { status ->
            val isSelected = content.filterState.selectedStatuses.contains(status)
            val contentColor = if (isSelected) DraculaYellow else DraculaCurrentLine
            val count = content.results.filter { it.status == status }.size
            Row(
                modifier = Modifier
                    .border(
                        color = contentColor,
                        width = 1.dp,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable { onClicked(status) }
                    .clip(shape = RoundedCornerShape(4.dp)),
            ) {
                Text(
                    text = count.toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = DraculaBackground,
                    modifier = Modifier
                        .background(contentColor)
                        .padding(6.dp)
                )
                Text(
                    text = status.displayName,
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                    color = contentColor,
                    modifier = Modifier.padding(6.dp)
                )
            }
        }
    }
    Spacer(Modifier.height(8.dp))

    LazyColumn(
        contentPadding = PaddingValues(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.clipToBounds()
    ) {
        val filteredItems =
            content.results.filter { content.filterState.selectedStatuses.contains(it.status) }
        items(filteredItems, key = { character -> character.id }) { character ->
            val dataPoints = buildList {
                add(DataPoint("Last know location", character.location.name))
                add(DataPoint("Species", character.species))
                add(DataPoint("Gender", character.gender.displayName))
                character.type.takeIf { it.isNotEmpty() }?.let { type ->
                    add(DataPoint("Type", type))
                }
                add(DataPoint("Origin", character.origin.name))
                add(DataPoint("Episode Count", character.episodeIds.size.toString()))
            }
            CharacterListItem(
                modifier = Modifier.animateItem(),
                character = character,
                onClick = {navController.navigate("character_details/${character.id}")},
                characterDataPoint = dataPoints
            )
        }
    }
}
