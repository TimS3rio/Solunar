package com.timserio.home_presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.timserio.core.R

@Composable
fun PermissionDialog(
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider()
                val buttonText = if (isPermanentlyDeclined) {
                    stringResource(id = R.string.grant_permission)
                } else {
                    stringResource(id = R.string.ok)
                }
                Text(
                    text = buttonText,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onOkClick()
                            }
                        }
                        .padding(16.dp)
                        .semantics { contentDescription = buttonText }
                )
            }
        },
        title = {
            val titleContentDesc = stringResource(id = R.string.permission_required)
            Text(
                modifier = Modifier.semantics { contentDescription = titleContentDesc },
                text = stringResource(id = R.string.permission_required)
            )
        },
        text = {
            val textContentDesc = stringResource(id = R.string.location_permission_rationale)
            Text(
                modifier = Modifier.semantics { contentDescription = textContentDesc },
                text = stringResource(id = R.string.location_permission_rationale)
            )
        },
        modifier = modifier
    )
}
