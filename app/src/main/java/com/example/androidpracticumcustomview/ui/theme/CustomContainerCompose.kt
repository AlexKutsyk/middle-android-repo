package com.example.androidpracticumcustomview.ui.theme

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.example.androidpracticumcustomview.R
import kotlinx.coroutines.launch

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */
@Composable
fun CustomContainerCompose(
    firstChild: @Composable ((
        alpha: Animatable<Float, AnimationVector1D>,
        offsetY: Animatable<Float, AnimationVector1D>
    ) -> Unit)?,
    secondChild: @Composable ((
        alpha: Animatable<Float, AnimationVector1D>,
        offsetY: Animatable<Float, AnimationVector1D>
    ) -> Unit)?
) {
    // Блок создания и инициализации переменных
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val targetOffsetY = remember { mutableFloatStateOf(0f) }
    val errorMessage = stringResource(R.string.error_message)

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 2000)
            )
        }
        launch {
            offsetY.animateTo(
                targetValue = targetOffsetY.floatValue,
                animationSpec = tween(durationMillis = 5000)
            )
        }
    }

    // Основной контейнер
    Box {
        Layout(content = {
            firstChild?.let { firstChild(alpha, offsetY) }
            secondChild?.let { secondChild(alpha, offsetY) }
        }) { measurables, constraints ->

            if (measurables.size > 2) throw IllegalStateException(errorMessage)

            val placeables = measurables.map { measurable ->
                measurable.measure(constraints)
            }

            val width = constraints.maxWidth
            val height = constraints.maxHeight

            layout(width, height) {
                placeables.forEach { placeable ->
                    try {
                        val position = IntOffset(
                            (width - placeable.width) / 2, (height - placeable.height) / 2
                        )
                        targetOffsetY.floatValue = (position.y / 2 - 2 * placeable.height).toFloat()
                        placeable.place(position.x, position.y)
                    } catch (e: Throwable) {
                        Log.e(TAG, "Rendering error: ${e.message}")
                    }
                }
            }
        }
    }
}

const val TAG = "CustomContainerCompose"