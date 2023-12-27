package com.timserio.solunar.navigation

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.timserio.home_presentation.HomeEvent
import com.timserio.home_presentation.HomeScreen
import com.timserio.home_presentation.HomeViewModel
import com.timserio.select_location_presentation.SelectLocationScreen
import com.timserio.select_location_presentation.SelectLocationViewModel

private const val SELECTED_LOCATION = "selected_location"

@Composable
fun SolunarNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    permissionResultLauncher: ActivityResultLauncher<String>
) {
    val homeState = homeViewModel.state.collectAsState().value
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Route.HOME
    ) {
        composable(Route.HOME) { entry ->
            val location = entry.savedStateHandle
                .get<Pair<Double, Double>>(SELECTED_LOCATION)
            HomeScreen(
                state = homeState,
                permissionResultLauncher = permissionResultLauncher,
                onSelectLocationClicked = { navController.navigate(Route.SELECT_LOCATION) },
                onEvent = {
                    homeViewModel.state.handleEvent(it)
                    if (it is HomeEvent.OnLocationSelectedFromMap) {
                        entry.savedStateHandle.remove<Pair<Double, Double>>(SELECTED_LOCATION)
                    }
                },
                locationFromMap = location
            )
        }
        composable(Route.SELECT_LOCATION) {
            val viewModel: SelectLocationViewModel = hiltViewModel()
            val state = viewModel.state.collectAsState().value
            SelectLocationScreen(
                latLong = homeState.latLong,
                state = state,
                onEvent = { viewModel.state.handleEvent(it) },
                onLocationSelected = {
                    state.selectedLocation?.let {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(SELECTED_LOCATION, Pair(it.latitude, it.longitude))
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}
