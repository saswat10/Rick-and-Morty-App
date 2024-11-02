package com.saswat10.network.models.domain

data class Episode (
    val id : Int,
    val name: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val airDate: String,
    val characterInEpisode: List<Int>
)
