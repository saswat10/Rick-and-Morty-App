package com.saswat10.rnmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.saswat10.network.KtorClient
import com.saswat10.rnmapp.ui.theme.RnMAppTheme
import com.saswat10.network.models.domain.Character

class MainActivity : ComponentActivity() {

    private val ktorClient = KtorClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            var character by remember { mutableStateOf<Character?>(null) }

            LaunchedEffect(key1 =Unit, block = {
                character = ktorClient.getCharacter(55)
            })

            RnMAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        Text(text = character?.name ?: "no character")
                        Text(text = character?.species ?: "no character")
                        Text(text = character?.gender?.displayName ?: "no character")
                        Text(text = character?.created ?: "no character")
                    }
                }
            }
        }
    }
}
