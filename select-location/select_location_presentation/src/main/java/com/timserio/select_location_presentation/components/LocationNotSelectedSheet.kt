package com.timserio.select_location_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.timserio.core.R
import com.timserio.core_ui.spacing
import com.timserio.core_ui.ui.theme.SolunarBgLight

@Composable
fun LocationNotSelectedSheet(alpha: Float) {
    Column(
        modifier = Modifier
            .padding(vertical = MaterialTheme.spacing.spaceLarge)
            .fillMaxWidth()
            .alpha(alpha)
    ) {

        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = MaterialTheme.spacing.spaceMedium),
            text = stringResource(id = R.string.select_location),
            color = SolunarBgLight,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
        Text(
            modifier = Modifier
                .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.select_location_msg),
            color = SolunarBgLight
        )
    }
}
