package com.saswat10.rnmapp.screens


import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.saswat10.network.KtorClient
import com.saswat10.network.models.domain.Character
import com.saswat10.rnmapp.components.character.CharacterNamePlateComponent
import com.saswat10.rnmapp.components.common.CharacterImage
import com.saswat10.rnmapp.components.common.DataPoint
import com.saswat10.rnmapp.components.common.DataPointComponent
import com.saswat10.rnmapp.components.common.LoadingIndicator
import kotlinx.coroutines.delay

@Composable
fun CharacterDetailsScreen(
    ktorClient: KtorClient,
    characterId: Int,
    onEpisodeClicked: (Int)->Unit
) {
    var character by remember { mutableStateOf<Character?>(null) }

    val characterDataPoints by remember {
        derivedStateOf {
            buildList {
                character?.let { character ->
                    add(DataPoint("Last know location", character.location.name))
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Gender", character.gender.displayName))
                    character.type.takeIf { it.isNotEmpty() }?.let { type ->
                        add(DataPoint("Type", type))
                    }
                    add(DataPoint("Species", character.species))
                    add(DataPoint("Origin", character.origin.name))
                    add(DataPoint("Episode Count", character.episodeUrls.size.toString()))
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit, block = {
//        delay(500)
        ktorClient.getCharacter(characterId)
            .onSuccess {
                character = it
            }
            .onFailure {
                // TODO()
            }
    })

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(all = 16.dp)
    ) {
        if (character == null) {
            item {
                LoadingIndicator()
            }
            return@LazyColumn
        }

        // Name Plate
        item {
            CharacterNamePlateComponent(
                name = character!!.name,
                status = character!!.status
            )
        }

        item { Spacer(Modifier.height(8.dp)) }
        Log.d("Image", character!!.image)
        // Image
        item {
            CharacterImage(character!!.image, character!!.name)
        }


        item { Spacer(Modifier.height(8.dp)) }
        // Data Points
        items(characterDataPoints) {
            Spacer(Modifier.height(12.dp))
            DataPointComponent(it)
        }

        item { Spacer(Modifier.height(8.dp)) }
        //Button
        item {
            Button(
                onClick = { onEpisodeClicked(characterId) },
                content = { Text(" View All Episodes") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item { Spacer(Modifier.height(64.dp)) }
    }
}
