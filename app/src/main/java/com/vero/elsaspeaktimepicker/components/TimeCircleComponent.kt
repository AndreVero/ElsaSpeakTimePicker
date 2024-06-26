package com.vero.elsaspeaktimepicker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vero.elsaspeaktimepicker.TimeItem
import com.vero.elsaspeaktimepicker.degreesToRadians
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

// Adjust degrees to start item drawing from top
private const val SELECTED_ANGLE_DEGREES = 180f

@Composable
fun TimeCircleComponent(
    items: List<TimeItem>,
    itemSize: Dp,
    onItemPicked: (TimeItem) -> Unit,
    size: Dp,
    state: TimePickerState = rememberTimePickerState(),
    content: @Composable (TimeItem) -> Unit,
    itemPadding: Dp = 30.dp,
    modifier: Modifier = Modifier,
) {
    Layout(
        modifier = modifier.drag(state, onItemPicked = onItemPicked),
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

        // Clear selected item
        state.selectedItem = SelectedItem()
        val changeAngle = state.angle.toDouble() - SELECTED_ANGLE_DEGREES

        layout(
            width = sizeInPx,
            height = sizeInPx,
        ) {
            placeables.forEachIndexed { index, placeable ->
                // Calculate angle of each item
                val itemAngle = changeAngle.degreesToRadians() + angleStep * index.toDouble()


                // Convert angles to degrees
                val itemAngleDegrees = (itemAngle * (180f / PI.toFloat())).toFloat()
                // Get the distance from top position to current item
                val distanceToSelectedItem = itemAngleDegrees.mod(360f) - SELECTED_ANGLE_DEGREES

                // Find the closest item
                if (abs(distanceToSelectedItem) < abs(state.selectedItem.angle)) {
                    state.selectedItem = SelectedItem(distanceToSelectedItem, items[index])
                }

                // Get coordinates relative to the circle center with paddings
                val offset = getCoordinates(
                    radius = radius.toDouble(),
                    angle = itemAngle,
                    paddings = paddingInPx
                )

                placeable.placeRelative(
                    x = offset.x.roundToInt(),
                    y = offset.y.roundToInt(),
                )
            }
        }
    }
}

private fun getCoordinates(angle: Double, radius: Double, paddings: Float): Offset {
    val radiusWithPaddings = radius - paddings
    val x = radiusWithPaddings * sin(angle)
    val y = radiusWithPaddings * cos(angle)
    return Offset(
        // Adding radius is necessary to shift the origin from the center of the circle
        x = x.toFloat() + radius.toFloat(),
        y = y.toFloat() + radius.toFloat(),
    )
}

@Composable
private fun rememberTimePickerState(): TimePickerState = remember {
    TimePickerState()
}