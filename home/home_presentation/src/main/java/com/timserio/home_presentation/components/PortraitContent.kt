package com.timserio.home_presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
fun PortraitContent(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    if (state.isLocationRequestSuccessful != null) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                .padding(top = MaterialTheme.spacing.spaceHuge),
            horizontalArrangement = Arrangement.Center
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
                        SolunarTitle(R.string.majors)
                        DynamicSolunarText(
                            isLoading = state.isLoading,
                            text = state.majorOne,
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = majorOneContentDesc
                                }
                        )
                        DynamicSolunarText(
                            isLoading = state.isLoading,
                            text = state.majorTwo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = majorTwoContentDesc
                                }
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
                        SolunarTitle(R.string.minors)
                        DynamicSolunarText(
                            isLoading = state.isLoading,
                            text = state.minorOne,
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = minorOneContentDesc
                                }
                        )
                        DynamicSolunarText(
                            isLoading = state.isLoading,
                            text = state.minorTwo,
                            modifier = Modifier
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = minorTwoContentDesc
                                }
                        )
                        HorizontalSeparator()
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
                        DayRatingView(
                            isLoading = state.isLoading,
                            value = state.dayRating.second,
                            name = state.dayRating.first.toString(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
                        SolunarTitle(R.string.day_rating)
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
