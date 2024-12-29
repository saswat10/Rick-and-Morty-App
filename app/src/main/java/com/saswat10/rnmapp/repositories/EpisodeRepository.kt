package com.saswat10.rnmapp.repositories

import com.saswat10.network.ApiOperation
import com.saswat10.network.KtorClient
import com.saswat10.network.models.domain.Episode
import javax.inject.Inject

class EpisodeRepository @Inject constructor(private val ktorClient: KtorClient) {
    suspend fun fetchAllEpisodes(): ApiOperation<List<Episode>> = ktorClient.getAllEpisodes()
}