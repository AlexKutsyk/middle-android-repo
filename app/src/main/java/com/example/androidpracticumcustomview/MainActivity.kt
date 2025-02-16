package com.example.androidpracticumcustomview

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.androidpracticumcustomview.ui.theme.CustomContainer
import com.example.androidpracticumcustomview.ui.theme.MainScreen

/*
Задание:
Реализуйте необходимые компоненты.
*/

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        Раскомментируйте нужный вариант
         */
        startXmlPracticum() // «традиционный» android (XML)
//        setContent { // Jetpack Compose
//            MainScreen()
//        }
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this)
        setContentView(customContainer)

        val firstView = TextView(this).apply {
            this.setTextView(
                R.string.first_title,
                R.color.purple_700
            )
            customContainer.addView(this@apply)
        }

        val secondView = TextView(this).apply {
            this.setTextView(
                R.string.second_title,
                R.color.teal_700
            )
        }

        // Добавление второго элемента через некоторое время
        Handler(Looper.getMainLooper()).postDelayed({
            customContainer.addView(secondView)
        }, 2000)
    }

    private fun TextView.setTextView(
        textRes: Int,
        colorRes: Int
    ) {
        with(this) {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setText(textRes)
            textSize = 24f
            setTextColor(resources.getColor(colorRes, theme))
            alpha = 0f
        }
    }
}