package com.saswat10.rnmapp.components.character

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.saswat10.network.models.domain.Character
import com.saswat10.rnmapp.components.common.DataPoint

@Composable
fun CharacterListItem(
    modifier: Modifier,
    character: Character,
    characterDataPoints : List<DataPoint>,
    onClick:() -> Unit
){

}
