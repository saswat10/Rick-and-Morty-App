package com.saswat10.network

import com.saswat10.network.models.domain.Character
import com.saswat10.network.models.remote.RemoteCharacter
import com.saswat10.network.models.remote.toDomainCharacter
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

        install(Logging){
            logger = Logger.SIMPLE
        }

        install(ContentNegotiation){
            json(Json{
                ignoreUnknownKeys = true
            })
        }
    }

    private var characterCache = mutableMapOf<Int, Character>()
    suspend fun getCharacter(id: Int): ApiOperation<Character> {
        characterCache[id]?.let{
            return ApiOperation.Success(it)
        }
        return safeApiCall {
            client.get("character/$id")
                .body<RemoteCharacter>()
                .toDomainCharacter()
        }
    }

    private inline fun<T> safeApiCall(apiCall:() ->T): ApiOperation<T>{
        return try{
            ApiOperation.Success(data = apiCall())
        }catch (e: Exception){
            ApiOperation.Failure(e = e)
        }
    }
}


sealed interface ApiOperation<T>{
    data class Success<T>(val data: T): ApiOperation<T>
    data class Failure<T>(val e: Exception):ApiOperation<T>

    fun onSuccess(block: (T)-> Unit): ApiOperation<T>{
        if(this is Success) block(data)
        return this
    }

    fun onFailure(block: (Exception)-> Unit): ApiOperation<T>{
        if(this is Failure) block(e)
        return this
    }
}
