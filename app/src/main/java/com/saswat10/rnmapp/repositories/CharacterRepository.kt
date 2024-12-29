package com.saswat10.rnmapp.repositories

import com.saswat10.network.ApiOperation
import com.saswat10.network.KtorClient
import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.domain.CharacterPage
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val ktorClient: KtorClient) {

    suspend fun fetchCharacterPage(
        pageNumber: Int,
        params: Map<String, String> = emptyMap()
    ): ApiOperation<CharacterPage> {
        return ktorClient.getCharacterByPage(pageNumber, params)
    }

    suspend fun fetchCharacter(characterId: Int): ApiOperation<Character> {
        return ktorClient.getCharacter(characterId)
    }

    suspend fun fetchAllCharacterByName(searchQuery: String): ApiOperation<List<Character>> {
        return ktorClient.searchAllCharactersByName(searchQuery)
    }
}