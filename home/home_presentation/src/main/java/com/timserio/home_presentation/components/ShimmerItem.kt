package com.timserio.home_presentation.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import com.timserio.core.R
import com.timserio.core_ui.ui.theme.SolunarLoadingDark
import com.timserio.core_ui.ui.theme.SolunarLoadingLight

private const val INSPECTION_LABEL = "inspection label"

@Composable
fun ShimmerItem(
    isLoading: Boolean,
    contentAfterLoading: @Composable () -> Unit,
    modifier: Modifier,
    height: Dp,
    width: Dp? = null
) {
    if (isLoading) {
        val loadingContentDesc = stringResource(id = R.string.loading)
        Row(
            modifier = modifier
                .semantics {
                    contentDescription = loadingContentDesc
                },
            horizontalArrangement = Arrangement.Center
        ) {
            val boxModifier = width?.let {
                Modifier
                    .width(it)
                    .height(height)
                    .shimmerEffect()
            } ?: run {
                Modifier
                    .fillMaxWidth()
                    .height(height)
                    .shimmerEffect()
            }
            Box(boxModifier)
        }
    } else {
        contentAfterLoading()
    }
}

private fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition(label = INSPECTION_LABEL)
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(tween(1000)),
        label = INSPECTION_LABEL
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                SolunarLoadingLight,
                SolunarLoadingDark,
                SolunarLoadingLight
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}
