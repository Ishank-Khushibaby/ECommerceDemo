package com.example.ecommercedemo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecommercedemo.presentation.basket.BasketScreen
import com.example.ecommercedemo.presentation.common.Screen
import com.example.ecommercedemo.presentation.home.HomeContract
import com.example.ecommercedemo.presentation.home.HomeScreen
import com.example.ecommercedemo.presentation.home.HomeViewModel
import com.example.ecommercedemo.presentation.utils.Utils.sharedViewModel
import com.example.ecommercedemo.ui.theme.ECommerceDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ECommerceDemoTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.HomeScreen.route
                    ) {
                        composable(route = Screen.Home.HomeScreen.route) {
                            val viewModel = it.sharedViewModel<HomeViewModel>(navController)
                            LaunchedEffect(Unit) {
                                viewModel.getProducts()
                            }
                            HomeScreen(
                                state = viewModel.state.value,
                                effectFlow = viewModel.effect,
                                onEvent = { event ->
                                    viewModel.onEvent(event)
                                },
                                onNavigate = { navigationEffect ->
                                    when (navigationEffect) {
                                        HomeContract.Effect.Navigation.NavigateToBucket -> navController.navigate(
                                            Screen.Home.Bucket.route
                                        )
                                        else->{}
                                    }
                                }
                            )
                        }
                        composable(route = Screen.Home.Bucket.route) {
                            val viewModel = it.sharedViewModel<HomeViewModel>(navController)
                            BasketScreen(
                                state = viewModel.state.value,
                                effectFlow = viewModel.effect,
                                onEvent = { event ->
                                    viewModel.onEvent(event)
                                },
                                onNavigate = { navigationEffect ->
                                    when (navigationEffect) {
                                        HomeContract.Effect.Navigation.NavigateToBack-> navController.navigateUp()
                                        else -> {}
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}