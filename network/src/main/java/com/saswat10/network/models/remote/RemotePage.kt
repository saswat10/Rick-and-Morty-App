package com.saswat10.network.models.remote

import com.saswat10.network.models.domain.CharacterPage
import com.saswat10.network.models.domain.EpisodePage
import kotlinx.serialization.Serializable

@Serializable
data class Info(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)

@Serializable
data class RemoteCharacterPage(
    val info: Info,
    val results: List<RemoteCharacter>
)

@Serializable
data class RemoteEpisodePage(
    val info: Info,
    val results: List<RemoteEpisode>
)

fun RemoteCharacterPage.toDomainCharacterPage(): CharacterPage {
    return CharacterPage(
        info = CharacterPage.Info(
            count = info.count,
            prev = info.prev,
            next = info.next,
            pages = info.pages
        ),
        characters = results.map { it.toDomainCharacter() }
    )
}

fun RemoteEpisodePage.toDomainEpisodePage(): EpisodePage {
    return EpisodePage(
        info = EpisodePage.Info(
            count = info.count,
            prev = info.prev,
            next = info.next,
            pages = info.pages
        ),
        episodes = results.map { it.toDomainEpisode() }
    )
}