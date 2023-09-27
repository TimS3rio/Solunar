package com.timserio.home_presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.timserio.core_ui.spacing

@Composable
fun VerticalSeparator() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
    Divider(
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .fillMaxHeight()
            .width(1.dp)
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
}

@Composable
fun HorizontalSeparator() {
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
    Divider(
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))
}
