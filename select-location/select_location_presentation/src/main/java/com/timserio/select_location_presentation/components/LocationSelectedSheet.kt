package com.timserio.select_location_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.timserio.core.R
import com.timserio.core_ui.spacing
import com.timserio.core_ui.ui.theme.FailureRed
import com.timserio.core_ui.ui.theme.SuccessGreen
import com.timserio.select_location_presentation.SelectLocationEvent

@Composable
fun LocationSelectedSheet(
    alpha: Float,
    onEvent: (SelectLocationEvent) -> Unit,
    onLocationSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = MaterialTheme.spacing.spaceLarge)
            .alpha(alpha)
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.CenterVertically)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val cancel = stringResource(id = R.string.cancel)
            IconButton(
                modifier = Modifier.semantics { contentDescription = cancel },
                onClick = { onEvent(SelectLocationEvent.OnCancelClick) }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = cancel,
                    modifier = Modifier.height(50.dp).width(50.dp),
                    tint = FailureRed
                )
            }
            val cancelText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = FailureRed)) {
                    append(cancel)
                }
            }
            ClickableText(
                text = cancelText,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { onEvent(SelectLocationEvent.OnCancelClick) }
            )
        }
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.CenterVertically)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val selectLocation = stringResource(id = R.string.select_location)
            IconButton(
                modifier = Modifier.semantics { contentDescription = selectLocation },
                onClick = { onLocationSelected() }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = selectLocation,
                    modifier = Modifier.height(50.dp).width(50.dp),
                    tint = SuccessGreen
                )
            }
            val selectText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = SuccessGreen)) {
                    append(selectLocation)
                }
            }
            ClickableText(
                text = selectText,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { onLocationSelected() }
            )
        }
    }
}
