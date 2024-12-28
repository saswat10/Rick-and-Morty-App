package com.saswat10.rnmapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saswat10.network.KtorClient
import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.domain.Episode
import com.saswat10.rnmapp.components.common.CharacterImage
import com.saswat10.rnmapp.components.common.CharacterNameComponent
import com.saswat10.rnmapp.components.common.DataPoint
import com.saswat10.rnmapp.components.common.DataPointComponent
import com.saswat10.rnmapp.components.common.LoadingIndicator
import com.saswat10.rnmapp.components.common.Toolbar
import com.saswat10.rnmapp.components.episode.EpisodeListItem
import com.saswat10.rnmapp.ui.theme.DraculaBackground
import com.saswat10.rnmapp.ui.theme.DraculaOrange
import kotlinx.coroutines.launch

@Composable
fun CharacterEpisodeScreen(characterId: Int, ktorClient: KtorClient, onBackClicked:()->Unit) {
    var character by remember { mutableStateOf<Character?>(null) }
    var episode by remember { mutableStateOf<List<Episode>>(emptyList()) }

    LaunchedEffect(key1 = Unit, block = {
        ktorClient.getCharacter(characterId)
            .onSuccess { singleCharacter ->
                character = singleCharacter
                launch {
                    ktorClient.getEpisodes(character!!.episodeIds)
                        .onSuccess {
                            episode = it
                        }
                        .onFailure {
                            // todo later
                        }
                }
            }
            .onFailure {
                // todo later
            }
    })

    character?.let {
        MainScreen(character!!, episode, onBackClicked)
    } ?: LoadingIndicator()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(character: Character, episodes: List<Episode>, onBackClicked:()->Unit) {
    val episodeSeasonMap = episodes.groupBy { it.seasonNumber }

    Column {
        Toolbar("Episodes List", onBackAction = {onBackClicked()})
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item { CharacterNameComponent(character.name) }
            item { Spacer(Modifier.height(16.dp)) }
            item {
                LazyRow {
                    episodeSeasonMap.forEach {
                        val title = "Season ${it.key}"
                        val number = "${it.value.size} ep"
                        item {
                            DataPointComponent(DataPoint(title, number))
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
            item {
                CharacterImage(
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    character.image,
                    character.name
                )
            }
            item { Spacer(Modifier.height(16.dp)) }

            episodeSeasonMap.forEach { mapEp ->
                stickyHeader { SeasonHeader(mapEp.key) }
                items(mapEp.value) {
                    Spacer(Modifier.height(16.dp))
                    EpisodeListItem(it)
                }
            }

            item { Spacer(Modifier.height(128.dp)) }
        }
    }
}

@Composable
fun SeasonHeader(seasonNumber: Int) {
    Column(
        modifier = Modifier
            .background(DraculaBackground)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .background(DraculaBackground)
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Season $seasonNumber",
                fontSize = 32.sp,
                lineHeight = 32.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }
        HorizontalDivider(color = DraculaOrange)
    }
}
