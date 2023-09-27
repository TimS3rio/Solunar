package com.timserio.home_presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.timserio.core.R

@Composable
fun DayRatingView(
    modifier: Modifier,
    heightWidth: Pair<Int, Int> = Pair(100, 100),
    isLoading: Boolean,
    value: Float,
    name: String
) {
    ShimmerItem(
        isLoading = isLoading,
        contentAfterLoading = { DayRatingView(value = value, name = name, modifier = modifier) },
        modifier = modifier,
        height = heightWidth.first.dp,
        width = heightWidth.second.dp
    )
}

@Composable
private fun DayRatingView(modifier: Modifier, heightWidth: Pair<Int, Int> = Pair(100, 100), value: Float, name: String) {
    val background = MaterialTheme.colorScheme.secondary
    val angleRatio = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = value) {
        angleRatio.animateTo(
            targetValue = value,
            animationSpec = tween(
                durationMillis = 300
            )
        )
    }

    Box(contentAlignment = Alignment.Center) {
        val tertiary = MaterialTheme.colorScheme.tertiary
        Canvas(
            modifier = Modifier
                .width(heightWidth.first.dp)
                .height(heightWidth.second.dp)
                .aspectRatio(1f),
        ) {
            drawArc(
                color = background,
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
                size = size,
                style = Stroke(
                    width = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
            drawArc(
                color = tertiary,
                startAngle = 270f,
                sweepAngle = 360f * angleRatio.value,
                useCenter = false,
                size = size,
                style = Stroke(
                    width = 8.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val dayRatingContentDesc = stringResource(id = R.string.day_rating)
            Text(
                text = name,
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.semantics {
                    contentDescription = dayRatingContentDesc
                }
            )
        }
    }
}
