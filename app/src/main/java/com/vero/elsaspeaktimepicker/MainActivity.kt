package com.vero.elsaspeaktimepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatSpringSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vero.elsaspeaktimepicker.components.SunMoonComponent
import com.vero.elsaspeaktimepicker.components.TimeCircleComponent
import com.vero.elsaspeaktimepicker.ui.theme.BackgroundColor
import com.vero.elsaspeaktimepicker.ui.theme.CircleAccentColor
import com.vero.elsaspeaktimepicker.ui.theme.CircleBackgroundColor
import com.vero.elsaspeaktimepicker.ui.theme.ElsaSpeakTimePickerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ElsaSpeakTimePickerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val itemSize = 50.dp
                    val size = 1100.dp
                    val times = remember { getTimes() }
                    val defaultSpringSpec = remember {
                        FloatSpringSpec(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessVeryLow,
                        )
                    }
                    var currentItem by remember {
                         mutableStateOf(times.first())
                    }

                    val rotationAnimation = remember {
                        Animatable(180f)
                    }
                    val scope = rememberCoroutineScope()

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundColor)
                    ) {
                        SunMoonComponent(
                            rotation = rotationAnimation.value,
                            modifier = Modifier
                                .size(height = 150.dp, width = 100.dp)
                                .offset(y = 165.dp)
                                .align(Alignment.Center)
                        )
                        TimeCircleComponent(
                            items = times,
                            itemSize = itemSize,
                            onItemPicked = {
                                currentItem = it
                                scope.launch {
                                    rotationAnimation.animateTo(
                                        if (it.isDayTime) 0f else 180f,
                                        defaultSpringSpec
                                    )
                                }
                            },
                            size = size,
                            content = { item ->
                                val colorAnimation by animateColorAsState(
                                    targetValue = if (item == currentItem) CircleAccentColor else CircleBackgroundColor,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioNoBouncy,
                                        stiffness = Spring.StiffnessLow,
                                    )
                                )

                                Box(
                                    modifier = Modifier
                                        .size(itemSize)
                                        .clip(CircleShape)
                                        .background(colorAnimation)
                                        .border(1.dp, CircleAccentColor, CircleShape)
                                ) {
                                    Text(
                                        text = item.time,
                                        modifier = Modifier.align(Alignment.Center),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(size)
                                .offset(y = 750.dp)
                                .align(Alignment.BottomCenter)
                                .clip(CircleShape)
                                .background(CircleBackgroundColor)
                        )
                    }
                }
            }
        }
    }
}