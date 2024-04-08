package com.vero.elsaspeaktimepicker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vero.elsaspeaktimepicker.TimeItem
import com.vero.elsaspeaktimepicker.degreesToRadians
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun TimeCircleComponent(
    items: List<TimeItem>,
    itemSize: Dp,
    onItemPicked: (TimeItem) -> Unit,
    size: Dp,
    content: @Composable (TimeItem) -> Unit,
    itemPadding: Dp = 30.dp,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier,
        content = {
            // Draw each item with appropriate information
            repeat(items.size) { index ->
                Box(modifier = Modifier.size(itemSize)) {
                    content(items[index])
                }
            }
        }
    ) { measurables, constraints ->

        val paddingInPx = itemPadding.toPx()

        val placeables = measurables.map { measurable -> measurable.measure(constraints) }
        val sizeInPx = size.toPx().toInt()

        // We need to remove item size because item will be position not in circle but at the edge
        val availableSpace = sizeInPx - itemSize.toPx()

        val radius = (availableSpace / 2.0).roundToInt()

        //Calculate step between each item
        val angleStep = (360 / items.size.toDouble()).degreesToRadians()

        layout(
            width = sizeInPx,
            height = sizeInPx,
        ) {
            placeables.forEachIndexed { index, placeable ->
                // Calculate angle of each item
                val itemAngle = angleStep * index.toDouble()

                // Get coordinates relative to the circle center with paddings
                val offset = getCoordinates(
                    width = radius.toDouble() - paddingInPx,
                    height = radius.toDouble() - paddingInPx,
                    angle = itemAngle,
                )

                placeable.placeRelative(
                    x = offset.x.roundToInt() + radius,
                    y = offset.y.roundToInt() + radius,
                )
            }
        }
    }
}

private fun getCoordinates(width: Double, height: Double, angle: Double): Offset {
    val x = width * sin(angle)
    val y = height * cos(angle)
    return Offset(
        x = x.toFloat(),
        y = y.toFloat(),
    )
}