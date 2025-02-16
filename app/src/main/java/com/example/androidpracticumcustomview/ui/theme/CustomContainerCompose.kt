package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
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
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?
) {
    // Блок создания и инициализации переменных
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    val targetOffsetY = remember { mutableFloatStateOf(0f) }
    val heightScreen = remember { mutableFloatStateOf(0f) }
    val heightChild = remember { mutableFloatStateOf(0f) }
    val errorMessage = stringResource(R.string.error_message)

    val childrenCount = listOfNotNull(firstChild, secondChild).size
    if (childrenCount > 2) {
        throw IllegalStateException(errorMessage)
    }

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 2000)
            )
        }
        launch {
            targetOffsetY.floatValue = heightScreen.floatValue / 2 - heightChild.floatValue
            offsetY.animateTo(
                targetValue = targetOffsetY.floatValue,
                animationSpec = tween(durationMillis = 5000)
            )
        }
    }

    // Основной контейнер
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .onSizeChanged { intSize ->
                heightScreen.floatValue = intSize.height.toFloat()
            }
    ) {

        firstChild?.let {
            Box(
                modifier = Modifier
                    .onSizeChanged { intSize ->
                        heightChild.floatValue = intSize.height.toFloat()
                    }
                    .graphicsLayer {
                        this.alpha = alpha.value
                        translationY = -offsetY.value
                    }
            ) {
                it()
            }
        }
        secondChild?.let {
            Box(
                Modifier
                    .onSizeChanged { intSize ->
                        heightChild.floatValue = intSize.height.toFloat()
                    }
                    .graphicsLayer {
                        this.alpha = alpha.value
                        translationY = offsetY.value
                    }
            ) {
                it()
            }
        }
    }
}