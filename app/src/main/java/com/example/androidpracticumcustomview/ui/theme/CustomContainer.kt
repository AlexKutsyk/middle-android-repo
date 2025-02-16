package com.example.androidpracticumcustomview.ui.theme

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.example.androidpracticumcustomview.R
import kotlin.math.pow

/*
Задание:
Реализуйте необходимые компоненты;
Создайте проверку что дочерних элементов не более 2-х;
Предусмотрите обработку ошибок рендера дочерних элементов.
Задание по желанию:
Предусмотрите параметризацию длительности анимации.
 */

class CustomContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    init {
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var maxWidth = 0
        var maxHeight = 0

        repeat(childCount) { index ->
            val child = getChildAt(index)
            measureChild(child, widthMeasureSpec, heightMeasureSpec)

            maxWidth = maxOf(maxWidth, child.measuredWidth)
            maxHeight = maxOf(maxHeight, child.measuredHeight)
        }

        val resolveWidth = resolveSize(maxWidth, widthMeasureSpec)
        val resolveHeight = resolveSize(maxHeight, heightMeasureSpec)

        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        try {
            val childCount = childCount
            val child = getChildAt(childCount - 1)

            val childWidth = child.measuredWidth
            val childHeight = child.measuredHeight

            val childLeft = width / 2 - childWidth / 2
            val childTop = height / 2 - childHeight / 2
            val childRight = childLeft + childWidth
            val childBottom = childTop + childHeight

            child.layout(childLeft, childTop, childRight, childBottom)

            animateView(child, childCount)
        } catch (e: Throwable) {
            Log.e(TAG, "Rendering error: ${e.message}")
        }
    }

    override fun addView(child: View) {
        if (childCount <= 2) {
            super.addView(child)
        } else {
            throw IllegalStateException(context.getString(R.string.error_message))
        }
    }

    private fun animateView(view: View, childCount: Int) {

        val translation = (height / 2 - view.measuredHeight) * (-1L).toDouble()
            .pow(childCount.toDouble())

        view.animate()
            .alpha(1f)
            .setDuration(2000)
            .start()

        view.animate()
            .translationY(translation.toFloat())
            .setDuration(5000)
            .start()
    }

    companion object {
        private const val TAG = "CustomContainer"
    }
}