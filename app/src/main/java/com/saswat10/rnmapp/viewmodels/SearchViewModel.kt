package com.saswat10.rnmapp.viewmodels

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saswat10.rnmapp.repositories.CharacterRepository
import com.saswat10.rnmapp.repositories.EpisodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject



@HiltViewModel
class SearchViewModel @Inject constructor(
    private val episodeRepository: EpisodeRepository,
    private val characterRepository: CharacterRepository
) : ViewModel() {
    val basicTextState = TextFieldState()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchTextState = snapshotFlow { basicTextState.text }
        .debounce(500)
        .mapLatest { if (it.isBlank()) "Awaiting command..." else it.toString() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 2000),
            initialValue = ""
        )
}