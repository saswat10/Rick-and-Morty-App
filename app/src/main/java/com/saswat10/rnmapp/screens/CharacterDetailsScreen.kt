package com.saswat10.rnmapp.screens


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.saswat10.rnmapp.components.character.CharacterNamePlateComponent
import com.saswat10.rnmapp.components.common.CharacterImage
import com.saswat10.rnmapp.components.common.DataPointComponent
import com.saswat10.rnmapp.components.common.LoadingIndicator
import com.saswat10.rnmapp.components.common.Toolbar
import com.saswat10.rnmapp.ui.theme.DraculaForeground
import com.saswat10.rnmapp.viewmodels.CharacterDetailsViewState
import com.saswat10.rnmapp.viewmodels.CharacterViewModel


@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    viewModel: CharacterViewModel = hiltViewModel(),
    onEpisodeClicked: (Int) -> Unit,
    onBackClicked: () -> Unit
) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchCharacter(characterId)
    })

    val state by viewModel.stateFlow.collectAsState()

    Column {

        Toolbar(title = "Character Details", onBackAction = { onBackClicked() })
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            when (val viewState = state) {
                is CharacterDetailsViewState.Error -> TODO()
                is CharacterDetailsViewState.Loading -> {
                    item {
                        LoadingIndicator()
                    }
                }

                is CharacterDetailsViewState.Success -> {

                    item {
                        CharacterNamePlateComponent(
                            name = viewState.character.name,
                            status = viewState.character.status
                        )
                    }

                    item { Spacer(Modifier.height(8.dp)) }
                    // Image
                    item {
                        CharacterImage(
                            Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(12.dp)),
                            viewState.character.image,
                            viewState.character.name
                        )
                    }


                    item { Spacer(Modifier.height(8.dp)) }
                    // Data Points
                    items(viewState.characterDataPoints) {
                        Spacer(Modifier.height(12.dp))
                        DataPointComponent(it)
                    }

                    item { Spacer(Modifier.height(32.dp)) }
                    //Button
                    item {
                        OutlinedButton(
                            shape = RoundedCornerShape(6.dp),
                            onClick = { onEpisodeClicked(characterId) }) {
                            Text(
                                "View All Episodes",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = DraculaForeground
                            )
                        }

                    }

                    item { Spacer(Modifier.height(64.dp)) }
                }

            }
        }
    }
}
