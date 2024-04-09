package com.vero.elsaspeaktimepicker.components

import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.Velocity
import com.vero.elsaspeaktimepicker.TimeItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

fun Modifier.drag(
    state: TimePickerState,
    onItemPicked: (TimeItem) -> Unit,
    magnitudeFactor: Float = 0.25f
) = pointerInput(Unit) {

    val center = Offset(x = this.size.width / 2f, this.size.height / 2f)

    val decay = splineBasedDecay<Float>(this)

    coroutineScope {
        while (true) {
            var startedAngle = 0f

            val pointerInput = awaitPointerEventScope {
                // Catch the down event
                val pointer = awaitFirstDown()

                // Calculate the angle where user started dragging and convert to degrees
                startedAngle = -atan2(
                    center.x - pointer.position.x,
                    center.y - pointer.position.y,
                ) * (180f / PI.toFloat()).mod(360f)
                pointer
            }

            // Stop previous animation
            state.stop()

            val tracker = VelocityTracker()
            var changeAngle = 0f

            awaitPointerEventScope {
                // Catch drag event
                drag(pointerInput.id) { change ->
                    // Calculate the angle after user drag event and convert to degrees
                    changeAngle = -atan2(
                        center.x - change.position.x,
                        center.y - change.position.y,
                    ) * (180f / PI.toFloat()).mod(360f)

                    launch {
                        // Change the current angle (later will be added to each item angle)
                        state.snapTo((state.oldAngle + (startedAngle - changeAngle).mod(360f)))
                    }
                    // Pass the info about changes to the VelocityTracker for later calculations
                    tracker.addPosition(change.uptimeMillis, change.position)
                    if (change.positionChange() != Offset.Zero) change.consume()
                }

                // Get magnitude of velocity and multiply by factor (to decrease the speed)
                var velocity = tracker.calculateVelocity().getMagnitudeOfLinearVelocity() * magnitudeFactor

                // Calculate the fling side (left or right)
                val difference = startedAngle - changeAngle
                velocity = if (difference > 0)
                    velocity else -velocity

                // Calculate new angle according to the velocity
                val targetAngle = decay.calculateTargetValue(
                    state.angle,
                    velocity,
                )

                launch {
                    // Animate items to the new angle with velocity
                    state.decayTo(
                        angle = targetAngle,
                        velocity = velocity,
                    )
                }
            }
            // In the end save the old angle for further calculations
            state.oldAngle = state.angle.mod(360f)
        }
    }
}

// This is used to determine the speed of the user's drag gesture
private fun Velocity.getMagnitudeOfLinearVelocity(): Float {
    return sqrt(this.x.pow(2) + this.y.pow(2))
}