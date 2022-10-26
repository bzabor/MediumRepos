package com.medium.viewpager.ui.theme

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WormPageIndicator(
    totalPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    indicatorSize: Dp = 6.dp,
    color: Color = Color.White,
    spacing: Dp = indicatorSize,
    selectedMultiplier: Int = 3
) {

    Log.d("ZZZ", "WormPageIndicator totalPages: $totalPages, currentpage: $currentPage")
    assert(
        value = currentPage in 0 until totalPages,
        lazyMessage = { "Current page index is out of range." }
    )

    val rowWidth = (indicatorSize * (selectedMultiplier + (totalPages - 1))) + (spacing * (totalPages - 1))

    Row(
        modifier = modifier
            .requiredWidth(rowWidth),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until totalPages) {
            val selected = i == currentPage

            val height = indicatorSize
            val width: Dp by animateDpAsState(
                if (selected) indicatorSize * selectedMultiplier else indicatorSize
            )

            Canvas(
                modifier = Modifier
                    .size(width, height),
                onDraw = {
                    drawRoundRect(
                        color = color,
                        cornerRadius = CornerRadius(height.toPx() / 2),
                        size = Size(width.toPx(), height.toPx())
                    )
                }
            )
        }
    }
}
