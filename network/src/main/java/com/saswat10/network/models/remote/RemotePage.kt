package com.saswat10.network.models.remote

import com.saswat10.network.models.domain.Page
import kotlinx.serialization.Serializable

@Serializable
data class RemoteCharacterPage(
    val info: Info,
    val results: List<RemoteCharacter>
) {
    @Serializable
    data class Info(
        val count: Int,
        val pages: Int,
        val next: String?,
        val prev: String?
    )
}

fun RemoteCharacterPage.toDomainCharacterPage(): Page {
    return Page(
        info = Page.Info(
            count = info.count,
            prev = info.prev,
            next = info.next,
            pages = info.pages
        ),
        characters = results.map { it.toDomainCharacter() }
    )
}