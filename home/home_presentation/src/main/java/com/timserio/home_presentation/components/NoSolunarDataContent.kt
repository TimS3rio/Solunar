package com.timserio.home_presentation.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.timserio.core.R
import com.timserio.core_ui.spacing
import com.timserio.home_presentation.RequestLocationState

@Composable
fun NoSolunarDataContent(
    requestLocationState: RequestLocationState?,
    selectLocationEvent: () -> Unit,
    getCurrentLocationEvent: () -> Unit
) {
    val text = when {
        requestLocationState == null -> stringResource(id = R.string.get_location_prompt)
        requestLocationState != RequestLocationState.LOCATION_REQUEST_SUCCESSFUL -> {
            stringResource(id = R.string.no_location_permission_msg)
        }
        else -> stringResource(id = R.string.error_msg)
    }
    val selectALocation = stringResource(id = R.string.select_a_location)
    val getCurrentLocation = stringResource(id = R.string.get_current_location)
    SolunarText(
        text = text,
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = text
            }
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
    SetLocationButton(
        modifier = Modifier.semantics { contentDescription = selectALocation },
        text = R.string.select_a_location,
        icon = R.drawable.ic_place,
        onClick = selectLocationEvent
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
    SetLocationButton(
        modifier = Modifier.semantics { contentDescription = getCurrentLocation },
        text = R.string.get_current_location,
        icon = R.drawable.ic_location,
        onClick = getCurrentLocationEvent
    )
}

@Composable
private fun SetLocationButton(
    modifier: Modifier,
    @StringRes text: Int,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        onClick = { onClick() }
    ) {
        Image(
            painterResource(id = icon),
            modifier = Modifier.size(24.dp),
            contentDescription = stringResource(id = text)
        )
        Text(
            text = stringResource(id = text),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .offset(x = (-12).dp)
        )
    }
}
