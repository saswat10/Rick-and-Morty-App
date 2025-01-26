package com.saswat10.rnmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saswat10.network.KtorClient
import com.saswat10.network.models.domain.DraculaYellow
import com.saswat10.rnmapp.screens.AllEpisodesScreen
import com.saswat10.rnmapp.screens.CharacterDetailsScreen
import com.saswat10.rnmapp.screens.CharacterEpisodeScreen
import com.saswat10.rnmapp.screens.HomeScreen
import com.saswat10.rnmapp.screens.SearchScreen
import com.saswat10.rnmapp.ui.theme.DraculaBackground
import com.saswat10.rnmapp.ui.theme.DraculaComment
import com.saswat10.rnmapp.ui.theme.DraculaCurrentLine
import com.saswat10.rnmapp.ui.theme.DraculaForeground
import com.saswat10.rnmapp.ui.theme.DraculaOrange
import com.saswat10.rnmapp.ui.theme.DraculaPink
import com.saswat10.rnmapp.ui.theme.DraculaPurple
import com.saswat10.rnmapp.ui.theme.RnMAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

sealed class NavDestination(val title: String, val route: String, val icon: ImageVector) {
    object Home : NavDestination("Home", "home_screen", Icons.Rounded.Home)
    object Episodes : NavDestination("Episodes", "episodes_screen", Icons.Rounded.PlayArrow)
    object Search : NavDestination("Search", "search_screen", Icons.Rounded.Search)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var ktorClient: KtorClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            val items = listOf(NavDestination.Home, NavDestination.Episodes, NavDestination.Search)
            var selectedIndex by remember{ mutableIntStateOf(0)}

            RnMAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            containerColor = Color(
                                ColorUtils.blendARGB(
                                    DraculaBackground.toArgb(),
                                    DraculaCurrentLine.toArgb(),
                                    0.3f
                                )
                            ),
                        ) {
                            items.forEachIndexed { index, screen ->
                                NavigationBarItem(
                                    icon = { Icon(screen.icon, null) },
                                    label = { Text(screen.title) },
                                    selected = index == selectedIndex,
                                    onClick = {
                                        selectedIndex = index
                                        navController.navigate(screen.route){
                                            // Pop up to the start destination of the graph to
                                            // avoid building up a large stack of destinations
                                            // on the back stack as users select items
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            // Avoid multiple copies of the same destination when
                                            // reselecting the same item
                                            launchSingleTop = true
                                            // Restore state when reselecting a previously selected item
                                            restoreState = true
                                        }

                                    },
                                    colors = NavigationBarItemColors(
                                        selectedIconColor = DraculaPurple,
                                        selectedTextColor = DraculaPurple,
                                        selectedIndicatorColor = DraculaCurrentLine,
                                        unselectedIconColor = DraculaForeground,
                                        unselectedTextColor = DraculaForeground,
                                        disabledIconColor = DraculaYellow,
                                        disabledTextColor = DraculaPink,
                                    )
                                )

                            }
                        }
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {

                        NavHost(
                            navController = navController,
                            startDestination = "home_screen"
                        ) {
                            composable("home_screen") {
                                HomeScreen(
                                    onCharacterSelected = { characterId ->
                                        navController.navigate("character_details/$characterId")
                                    }
                                )
                            }
                            composable(
                                route = "character_details/{characterId}",
                                arguments = listOf(navArgument("characterId") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                val characterId: Int =
                                    backStackEntry.arguments?.getInt("characterId") ?: -1
                                CharacterDetailsScreen(
                                    characterId = characterId,
                                    onEpisodeClicked = { navController.navigate("character_episode/$it") },
                                    onBackClicked = { navController.navigateUp() }
                                )
                            }
                            composable(
                                route = "character_episode/{characterId}",
                                arguments = listOf(navArgument("characterId") {
                                    type = NavType.IntType
                                })
                            ) { backStackEntry ->
                                val characterId: Int =
                                    backStackEntry.arguments?.getInt("characterId") ?: -1
                                CharacterEpisodeScreen(characterId, ktorClient, onBackClicked = {
                                    navController.navigateUp()
                                })
                            }
                            composable(
                                route = NavDestination.Episodes.route
                            ) {
                                AllEpisodesScreen()
                            }
                            composable(
                                route = NavDestination.Search.route
                            ) {
                                SearchScreen(navController = navController)
                            }
                        }

                    }
                }
            }
        }
    }
}
