package com.timserio.home_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.timserio.core.R
import com.timserio.core_ui.spacing
import com.timserio.core_ui.ui.theme.shapeScheme
import com.timserio.home_presentation.HomeEvent
import com.timserio.home_presentation.HomeState

@Composable
@ExperimentalPermissionsApi
fun LandscapeContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    if (state.isLocationRequestSuccessful != null) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = MaterialTheme.spacing.spaceMedium,
                    vertical = MaterialTheme.spacing.spaceMedium
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = MaterialTheme.shapeScheme.medium,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(MaterialTheme.spacing.spaceMedium)
                ) {
                    if (state.isLocationRequestSuccessful == true && (state.isLoading || state.isSolunarResponseSuccessful == true)) {
                        val majorOneContentDesc = stringResource(id = R.string.major_one)
                        val majorTwoContentDesc = stringResource(id = R.string.major_two)
                        val minorOneContentDesc = stringResource(id = R.string.minor_one)
                        val minorTwoContentDesc = stringResource(id = R.string.minor_two)
                        LocationText(isLoading = state.isLoading, text = state.locationName)
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
                        DateSelector(state.isLoading, state.date, { onEvent(HomeEvent.OnPreviousDayClick) }, { onEvent(HomeEvent.OnNextDayClick) })
                        HorizontalSeparator()
                        Row(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val wrapContentModifier = Modifier
                                .wrapContentHeight()
                                .wrapContentWidth()
                            Column(modifier = wrapContentModifier.weight(1f)) {
                                SolunarTitle(R.string.majors)
                                DynamicSolunarText(
                                    isLoading = state.isLoading,
                                    text = state.majorOne,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .semantics {
                                            contentDescription = majorOneContentDesc
                                        },
                                    widthOfLoadingView = 175
                                )
                                DynamicSolunarText(
                                    isLoading = state.isLoading,
                                    text = state.majorTwo,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .semantics {
                                            contentDescription = majorTwoContentDesc
                                        },
                                    widthOfLoadingView = 175
                                )
                            }
                            VerticalSeparator()
                            Column(modifier = wrapContentModifier.weight(1f)) {
                                SolunarTitle(R.string.minors)
                                DynamicSolunarText(
                                    isLoading = state.isLoading,
                                    text = state.minorOne,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .semantics {
                                            contentDescription = minorOneContentDesc
                                        },
                                    widthOfLoadingView = 175
                                )
                                DynamicSolunarText(
                                    isLoading = state.isLoading,
                                    text = state.minorTwo,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .semantics {
                                            contentDescription = minorTwoContentDesc
                                        },
                                    widthOfLoadingView = 175
                                )
                            }
                            VerticalSeparator()
                            Column(modifier = wrapContentModifier.weight(1f)) {
                                SolunarTitle(R.string.day_rating)
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
                                DayRatingView(
                                    isLoading = state.isLoading,
                                    value = state.dayRating.second,
                                    name = state.dayRating.first.toString(),
                                    heightWidth = Pair(100, 100),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                )
                            }
                        }
                    } else {
                        ErrorText(
                            isLocationRequestSuccessful = state.isLocationRequestSuccessful,
                            isLocationPermissionGranted = locationPermissionsGranted(),
                            isSolunarResponseSuccessful = state.isSolunarResponseSuccessful
                        )
                    }
                }
            }
        }
    }
}
