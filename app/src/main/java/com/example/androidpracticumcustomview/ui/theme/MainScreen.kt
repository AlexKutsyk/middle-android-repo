package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidpracticumcustomview.R

/*
Задание:
Реализуйте необходимые компоненты.
*/

@Composable
fun MainScreen() {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {

            CustomContainerCompose(
                firstChild = { alpha, offsetY ->
                    SetText(alpha.value, -offsetY.value.dp, R.string.first_title, Color.Blue)
                },
                secondChild = { alpha, offsetY ->
                    SetText(alpha.value, offsetY.value.dp, R.string.second_title, Color.Green)
                }
            )
        }
    }
}

@Composable
fun SetText(alpha: Float, offsetY: Dp, textRes: Int, color: Color) {
    Text(
        text = stringResource(textRes),
        fontSize = 24.sp,
        color = color,
        modifier = Modifier
            .offset(x = 0.dp, y = offsetY)
            .alpha(alpha = alpha)
    )
}