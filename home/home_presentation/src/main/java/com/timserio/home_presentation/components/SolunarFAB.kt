package com.timserio.home_presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.timserio.core_ui.ui.theme.DarkBlue
import com.timserio.core_ui.ui.theme.TransparentWhite
import com.timserio.core_ui.ui.theme.shapeScheme

private const val ALPHA = "alpha"
private const val FAB_SCALE = "fab_scale"
private const val TEXT_SHADOW = "text_shadow"
private const val TEXT_SHADOW_ALPHA = "text_shadow_alpha"
private const val TRANSITION = "transition"

enum class FloatingActionButtonState {
    EXPANDED,
    COLLAPSED
}

enum class Identifier {
    GET_CURRENT_LOCATION
}

data class MiniFAB(
    @DrawableRes
    val icon: Int,
    val label: String,
    val id: Identifier
)

@Composable
fun SolunarFAB(
    fabState: FloatingActionButtonState,
    onFabStateChanged: (FloatingActionButtonState) -> Unit,
    items: List<MiniFAB>,
    onGetCurrentLocationClicked: () -> Unit
) {
    val transition = updateTransition(targetState = fabState, label = TRANSITION)
    val fabScale by transition.animateFloat(label = FAB_SCALE) {
        if (it == FloatingActionButtonState.EXPANDED) 60f else 0f
    }
    val alpha by transition.animateFloat(
        label = ALPHA,
        transitionSpec = { tween(durationMillis = 50) }
    ) {
        if (it == FloatingActionButtonState.EXPANDED) 1f else 0f
    }
    val textShadow by transition.animateDp(
        label = TEXT_SHADOW,
        transitionSpec = { tween(durationMillis = 50) }
    ) {
        if (it == FloatingActionButtonState.EXPANDED) 2.dp else 0.dp
    }

    Column(horizontalAlignment = Alignment.End) {
        if (fabState == FloatingActionButtonState.EXPANDED) {
            items.forEach {
                MiniFloatingActionButton(
                    miniFab = it,
                    onMiniFabClicked = {
                        onGetCurrentLocationClicked()
                        onFabStateChanged(FloatingActionButtonState.COLLAPSED)
                    },
                    alpha = alpha,
                    textShadow = textShadow,
                    fabScale = fabScale
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }
        FloatingActionButton(
            onClick = {
                onFabStateChanged(
                    if (fabState == FloatingActionButtonState.EXPANDED) {
                        FloatingActionButtonState.COLLAPSED
                    } else FloatingActionButtonState.EXPANDED
                )
            },
            shape = MaterialTheme.shapeScheme.medium
        ) {
            if (fabState == FloatingActionButtonState.EXPANDED) {
                Icon(Icons.Rounded.Close, tint = Color.White, contentDescription = null)
            } else Icon(Icons.Rounded.Edit, tint = Color.White, contentDescription = null)
        }
    }
}

@Composable
private fun MiniFloatingActionButton(
    miniFab: MiniFAB,
    alpha: Float,
    textShadow: Dp,
    showLabel: Boolean = false,
    fabScale: Float,
    onMiniFabClicked: (MiniFAB) -> Unit
) {
    val fabColor = MaterialTheme.colorScheme.primaryContainer
    val shadow = Color.Black.copy(0.5f)
    val painter = painterResource(id = miniFab.icon)
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (showLabel) {
            Text(
                text = miniFab.label,
                color = DarkBlue,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha,
                            animationSpec = tween(50),
                            label = TEXT_SHADOW_ALPHA
                        ).value
                    )
                    .shadow(elevation = textShadow, shape = MaterialTheme.shapeScheme.small)
                    .background(color = TransparentWhite, shape = MaterialTheme.shapeScheme.small)
                    .padding(start = 6.dp, end = 6.dp)
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
        Canvas(
            modifier = Modifier
                .size(32.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    onClick = { onMiniFabClicked.invoke(miniFab) },
                    indication = rememberRipple(
                        bounded = false,
                        radius = 20.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
        ) {
            translate(left = -15f) {
                drawCircle(
                    color = shadow,
                    radius = fabScale,
                    center = Offset(
                        center.x + 2f,
                        center.y + 2f
                    )
                )

                drawCircle(
                    color = fabColor,
                    radius = fabScale
                )

                with(painter) {
                    translate(left = 18f, top = 18f) {
                        draw(size = Size(20.dp.toPx(), 20.dp.toPx()), alpha = alpha)
                    }
                }
            }
        }
    }
}
