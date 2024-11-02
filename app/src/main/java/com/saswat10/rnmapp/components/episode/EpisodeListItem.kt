package com.saswat10.rnmapp.components.episode

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saswat10.network.models.domain.DraculaYellow
import com.saswat10.network.models.domain.Episode
import com.saswat10.rnmapp.components.common.DataPoint
import com.saswat10.rnmapp.components.common.DataPointComponent

@Composable
fun EpisodeListItem(
    episode: Episode
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        DataPointComponent(
            DataPoint(
                "Episode", episode.episodeNumber.toString()
            )
        )

        Spacer(Modifier.width(48.dp))
        Column {
            Text(
                text = episode.name, fontSize = 18.sp,
                color = DraculaYellow,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = episode.airDate, fontSize = 12.sp,
                color = DraculaYellow,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

}


@Preview
@Composable
fun PreviewScreen() {
    EpisodeListItem(
        episode = Episode(
            id = 1,
            name = "Richie Rich",
            seasonNumber = 1,
            episodeNumber = 13,
            airDate = "September 10, 2017",
            characterInEpisode = listOf(1, 3, 5, 7, 10)
        )
    )
}
