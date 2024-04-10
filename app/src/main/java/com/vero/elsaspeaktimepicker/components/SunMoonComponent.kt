package com.vero.elsaspeaktimepicker.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vero.elsaspeaktimepicker.R

@Composable
fun SunMoonComponent(
    rotation: Float,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(id = R.drawable.icon_house),
            contentDescription = "House",
            Modifier
                .align(Alignment.Center)
                .size(60.dp),
            tint = Color.Gray
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(rotation)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon_sun),
                contentDescription = "Sun",
                Modifier.align(Alignment.TopStart),
                tint = Color.Yellow
            )
            Icon(
                painter = painterResource(id = R.drawable.icon_moon),
                contentDescription = "Moon",
                Modifier
                    .align(Alignment.BottomStart)
                    .rotate(180f),
                tint = Color.White
            )
        }
    }
}