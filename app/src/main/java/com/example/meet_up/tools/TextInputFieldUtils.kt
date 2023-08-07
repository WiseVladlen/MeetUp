package com.example.meet_up.tools

import android.view.Window
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

fun AppCompatEditText.showKeyboard(window: Window) {
    requestFocus()

    WindowCompat.getInsetsController(window, this).show(WindowInsetsCompat.Type.ime())
}