package com.vero.elsaspeaktimepicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vero.elsaspeaktimepicker.components.TimeCircleComponent
import com.vero.elsaspeaktimepicker.ui.theme.BackgroundColor
import com.vero.elsaspeaktimepicker.ui.theme.CirceBackgroundColor
import com.vero.elsaspeaktimepicker.ui.theme.ElsaSpeakTimePickerTheme

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

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundColor)
                    ) {
                        TimeCircleComponent(
                            items = getTimes(),
                            itemSize = itemSize,
                            onItemPicked = {},
                            size = size,
                            content = { item ->
                                Box(
                                    modifier = Modifier
                                        .size(itemSize)
                                        .background(CirceBackgroundColor)
                                        .clip(CircleShape)
                                        .border(1.dp, Color.White, CircleShape)
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
                                .background(CirceBackgroundColor)
                        )
                    }
                }
            }
        }
    }
}