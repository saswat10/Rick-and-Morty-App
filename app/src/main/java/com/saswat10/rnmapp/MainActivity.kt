package com.saswat10.rnmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saswat10.network.KtorClient
import com.saswat10.rnmapp.screens.CharacterDetailsScreen
import com.saswat10.rnmapp.screens.CharacterEpisodeScreen
import com.saswat10.rnmapp.ui.theme.RnMAppTheme
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {

    private val ktorClient = KtorClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()

            RnMAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {

                        NavHost(
                            navController = navController,
                            startDestination = "character_details"
                        ) {
                            composable("character_details") {
                                CharacterDetailsScreen(ktorClient, 3) {
                                    navController.navigate("character_episode/$it")
                                }
                            }
                            composable(
                                route = "character_episode/{characterId}",
                                arguments = listOf(navArgument("characterId"){type = NavType.IntType})
                            ) { backStackEntry ->
                                val characterId: Int =
                                    backStackEntry.arguments?.getInt("characterId") ?: -1
                                CharacterEpisodeScreen(characterId, ktorClient)
                            }
                        }

                    }
                }
            }
        }
    }
}
