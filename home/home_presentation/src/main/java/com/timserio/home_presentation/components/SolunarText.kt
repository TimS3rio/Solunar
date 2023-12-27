package com.timserio.home_presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.timserio.core.R
import com.timserio.core_ui.spacing

@Composable
fun SolunarTitle(@StringRes text: Int) {
    SolunarText(
        text = stringResource(id = text),
        textStyle = MaterialTheme.typography.titleLarge,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SolunarText(
    text: String,
    textStyle: TextStyle,
    modifier: Modifier
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
        style = textStyle,
        color = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun ErrorText(
    isLocationRequestSuccessful: Boolean?,
    isSolunarResponseSuccessful: Boolean?
) {
    if (isLocationRequestSuccessful == false || isSolunarResponseSuccessful == false) {
        val errorMsg = if (isLocationRequestSuccessful == false) {
            stringResource(id = R.string.no_location_permission_msg)
        } else {
            stringResource(id = R.string.error_msg)
        }
        val errorTextContentDesc = stringResource(id = R.string.error_text)
        Text(
            text = errorMsg,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(0.dp, MaterialTheme.spacing.spaceHuge)
                .semantics {
                    contentDescription = errorTextContentDesc
                },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
fun DynamicSolunarText(
    modifier: Modifier,
    isLoading: Boolean,
    heightOfLoadingView: Int = 26,
    widthOfLoadingView: Int = 250,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge
) {
    if (isLoading || text.isNotBlank()) {
        ShimmerItem(
            isLoading = isLoading,
            contentAfterLoading = { SolunarText(text, textStyle, modifier) },
            modifier = modifier,
            height = heightOfLoadingView.dp,
            width = widthOfLoadingView.dp
        )
    }
}

@Composable
fun LocationText(isLoading: Boolean, text: String) {
    if (isLoading || text.isNotBlank()) {
        ShimmerItem(
            isLoading = isLoading,
            contentAfterLoading = {
                val locationNameContentDesc = stringResource(id = R.string.location_name)
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = locationNameContentDesc
                        },
                    text = text,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            height = 40.dp,
            width = 250.dp
        )
    }
}
