package com.saswat10.rnmapp.repositories

import com.saswat10.network.ApiOperation
import com.saswat10.network.KtorClient
import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.domain.Page
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val ktorClient: KtorClient) {

    suspend fun fetchCharacterPage(pageNumber: Int): ApiOperation<Page>{
        return ktorClient.getCharacterByPage(pageNumber)
    }

    suspend fun fetchCharacter(characterId: Int): ApiOperation<Character> {
        return ktorClient.getCharacter(characterId)
    }
}