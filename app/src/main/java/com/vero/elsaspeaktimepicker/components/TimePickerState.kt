package com.vero.elsaspeaktimepicker.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import com.vero.elsaspeaktimepicker.TimeItem

class SelectedItem(
    // distance to the closest/selected item
    val angle: Float = 361f,
    // index of the selected item
    val item: TimeItem? = null,
)

class TimePickerState {

    // Angle changes
    private val _angle = Animatable(0f)
    val angle: Float
        get() = _angle.value

    // Saved angle after end of animation
    var oldAngle: Float = 0f

    // Currently selected item
    var selectedItem = SelectedItem()

    // Animation that we will use for spins
    private val decayAnimationSpec = FloatSpringSpec(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessVeryLow,
    )

    suspend fun stop() {
        _angle.stop()
    }

    suspend fun snapTo(angle: Float) {
        _angle.snapTo(angle)
    }

    suspend fun animateTo(angle: Float) {
        // Save the new old angle as this is the last step of animation
        oldAngle = angle
        _angle.animateTo(angle, decayAnimationSpec)
    }

    suspend fun decayTo(angle: Float, velocity: Float) {
        _angle.animateTo(
            targetValue = angle,
            initialVelocity = velocity,
            animationSpec = decayAnimationSpec,
        )
    }
}