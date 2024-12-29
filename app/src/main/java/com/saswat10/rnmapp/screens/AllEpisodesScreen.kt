package com.saswat10.rnmapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.rnmapp.components.common.LoadingIndicator
import com.saswat10.rnmapp.components.common.Toolbar
import com.saswat10.rnmapp.components.episode.EpisodeListItem
import com.saswat10.rnmapp.ui.theme.DraculaBackground
import com.saswat10.rnmapp.ui.theme.DraculaGreen
import com.saswat10.rnmapp.ui.theme.DraculaPurple
import com.saswat10.rnmapp.viewmodels.AllEpisodesViewModel
import com.saswat10.rnmapp.viewmodels.AllEpisodesViewState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AllEpisodesScreen(
    viewModel: AllEpisodesViewModel = hiltViewModel()
) {
    val uiState by viewModel.viewState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.refreshAllEpisodes()
    }

    when (val state = uiState) {
        is AllEpisodesViewState.Loading -> {
            LoadingIndicator()
        }

        is AllEpisodesViewState.Error -> {
            // todo
        }

        is AllEpisodesViewState.Success -> {
            Column {
                Toolbar("All Episodes")
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    state.data.forEach { mapEntry ->
                        val uniqueCharacterCount = mapEntry.value.flatMap {
                            it.characterInEpisode
                        }.toSet()
                        stickyHeader {
                            Column(modifier = Modifier.background(
                                DraculaBackground
                            ).fillMaxSize()) {
                                Text(
                                    text = mapEntry.key,
                                    style = MaterialTheme.typography.headlineMedium,

                                )
                                Text(
                                    text = "${uniqueCharacterCount.size} unique characters",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = DraculaGreen
                                    )
                                Spacer(
                                    modifier = Modifier
                                        .height(4.dp)
                                )
                                HorizontalDivider(color = DraculaPurple)
                            }
                        }
                        mapEntry.value.forEach { episode ->
                            item(key = episode.id) {
                                EpisodeListItem(episode)
                            }
                        }
                    }
                }
            }
        }
    }
}