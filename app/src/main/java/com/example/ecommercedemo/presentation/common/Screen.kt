package com.example.ecommercedemo.presentation.common


const val SIDE_EFFECTS_KEY = "effect_key"
sealed class Screen(val route: String) {
    data object Home : Screen("home") {
        data object HomeScreen : Screen("home_screen")
        data object Bucket : Screen("bucket")
    }
}