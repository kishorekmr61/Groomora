package com.groomora.app

import androidx.compose.runtime.Composable
import com.groomora.design.GroomoraTheme
import com.groomora.feature.home.HomeScreen

@Composable
fun App() {
    GroomoraTheme {
        HomeScreen()
    }
}
