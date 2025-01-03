package com.saswat10.network

import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.domain.Episode
import com.saswat10.network.models.domain.CharacterPage
import com.saswat10.network.models.domain.EpisodePage
import com.saswat10.network.models.remote.RemoteCharacter
import com.saswat10.network.models.remote.RemoteCharacterPage
import com.saswat10.network.models.remote.RemoteEpisode
import com.saswat10.network.models.remote.RemoteEpisodePage
import com.saswat10.network.models.remote.toDomainCharacter
import com.saswat10.network.models.remote.toDomainCharacterPage
import com.saswat10.network.models.remote.toDomainEpisode
import com.saswat10.network.models.remote.toDomainEpisodePage
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {
    private val client = HttpClient(OkHttp) {
        defaultRequest { url("https://rickandmortyapi.com/api/") }

        install(Logging) {
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private var characterCache = mutableMapOf<Int, Character>()
    suspend fun getCharacter(id: Int): ApiOperation<Character> {
        characterCache[id]?.let {
            return ApiOperation.Success(it)
        }
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
        }
    }

    suspend fun getEpisode(episodeId: Int): ApiOperation<Episode> {
        return safeApiCall {

            client.get("episode/$episodeId")
                .body<RemoteEpisode>()
                .toDomainEpisode()
        }
    }

    suspend fun getEpisodes(episodeIds: List<Int>): ApiOperation<List<Episode>> {
        return if (episodeIds.size == 1) {
            getEpisode(episodeIds[0]).mapSuccess {
                listOf(it)
            }
        } else {
            val idCommaSeparated = episodeIds.joinToString(separator = ",")
            return safeApiCall {

                client.get("episode/$idCommaSeparated")
                    .body<List<RemoteEpisode>>()
                    .map { it.toDomainEpisode() }
            }
        }
    }

    suspend fun getCharacterByPage(
        pageNumber: Int,
    ): ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber")
                .body<RemoteCharacterPage>()
                .toDomainCharacterPage()
        }
    }
    suspend fun getCharacterByPageQuery(
        pageNumber: Int,
        queryParams: Map<String, String>
    ): ApiOperation<CharacterPage> {
        return safeApiCall {
            client.get("character/?page=$pageNumber") {
                url {
                    parameters.append("page", pageNumber.toString())
                    queryParams.forEach { parameters.append(it.key, it.value) }
                }
            }
                .body<RemoteCharacterPage>()
                .toDomainCharacterPage()
        }
    }

    suspend fun getEpisodesByPage(pageIndex: Int): ApiOperation<EpisodePage> {
        return safeApiCall {
            client.get("episode") {
                url {
                    parameters.append("page", pageIndex.toString())
                }
            }
                .body<RemoteEpisodePage>()
                .toDomainEpisodePage()
        }
    }

    suspend fun getAllEpisodes(): ApiOperation<List<Episode>> {
        val data = mutableListOf<Episode>()
        var exception: Exception? = null

        getEpisodesByPage(pageIndex = 1).onSuccess { firstPage ->
            val totalPageCount = firstPage.info.pages
            data.addAll(firstPage.episodes)

            repeat(totalPageCount - 1) { index ->
                getEpisodesByPage(pageIndex = index + 2).onSuccess { nextPage ->
                    data.addAll(nextPage.episodes)
                }.onFailure { error ->
                    exception = error
                }

                if (exception != null) {
                    return@onSuccess
                }
            }
        }.onFailure {
            exception = it
        }

        return exception?.let { ApiOperation.Failure(it) } ?: ApiOperation.Success(data)
    }

    suspend fun searchAllCharactersByName(searchQuery: String): ApiOperation<List<Character>> {
        val data = mutableListOf<Character>()
        var exception: Exception? = null

        getCharacterByPageQuery(pageNumber = 1, mapOf("name" to searchQuery))
            .onSuccess { firstPage ->
                val totalPageCount = firstPage.info.pages
                data.addAll(firstPage.characters)

                repeat(totalPageCount - 1) { index ->
                    getCharacterByPageQuery(pageNumber = index + 2, mapOf("name" to searchQuery))
                        .onSuccess { nextPage ->
                            data.addAll(nextPage.characters)
                        }.onFailure { error ->
                            exception = error
                        }

                    if (exception != null) {
                        return@onSuccess
                    }
                }
            }.onFailure {
                exception = it
            }

        return exception?.let { ApiOperation.Failure(it) } ?: ApiOperation.Success(data)
    }

    private inline fun <T> safeApiCall(apiCall: () -> T): ApiOperation<T> {
        return try {
            ApiOperation.Success(data = apiCall())
        } catch (e: Exception) {
            ApiOperation.Failure(e = e)
        }
    }
}


sealed interface ApiOperation<T> {
    data class Success<T>(val data: T) : ApiOperation<T>
    data class Failure<T>(val e: Exception) : ApiOperation<T>

    fun <R> mapSuccess(transform: (T) -> R): ApiOperation<R> {
        return when (this) {
            is Success -> Success(transform(data))
            is Failure -> Failure(e)
        }
    }

    // this needs to be suspend in order to call
    // getEpisodesByPage within getAllEpisodes
    suspend fun onSuccess(block: suspend (T) -> Unit): ApiOperation<T> {
        if (this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception) -> Unit): ApiOperation<T> {
        if (this is Failure) block(e)
        return this
    }
}
