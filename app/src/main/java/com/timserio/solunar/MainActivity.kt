package com.timserio.solunar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.timserio.home_presentation.HomeScreen
import com.timserio.home_presentation.HomeViewModel
import com.timserio.solunar.navigation.Route
import com.timserio.solunar.ui.theme.SolunarTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPermissionsApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SolunarTheme {
                val navController = rememberNavController()
                val homeViewModel: HomeViewModel = hiltViewModel()
                val homeState = homeViewModel.state.collectAsState().value
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = Route.HOME
                ) {
                    composable(Route.HOME) {
                        HomeScreen(
                            state = homeState,
                            onEvent = {
                                homeViewModel.state.handleEvent(it)
                            }
                        )
                    }
                }
            }
        }
    }
}
