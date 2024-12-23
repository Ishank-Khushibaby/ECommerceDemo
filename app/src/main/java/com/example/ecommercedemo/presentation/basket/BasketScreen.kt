package com.example.ecommercedemo.presentation.basket

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.Placeholder
import com.example.ecommercedemo.R
import com.example.ecommercedemo.data.model.ProductsItem
import com.example.ecommercedemo.presentation.common.SIDE_EFFECTS_KEY
import com.example.ecommercedemo.presentation.common.components.LoadingAnimation
import com.example.ecommercedemo.presentation.home.HomeContract
import com.example.ecommercedemo.ui.theme.DarkLightPreviews
import com.example.ecommercedemo.ui.theme.ECommerceDemoTheme
import com.example.ecommercedemo.ui.theme.orange
import com.example.ecommercedemo.ui.theme.orangeLight
import com.example.ecommercedemo.ui.theme.text_background
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@Composable
fun BasketScreen(
    state: HomeContract.HomeState,
    onEvent: (event: HomeContract.HomeEvent) -> Unit,
    effectFlow: Flow<HomeContract.Effect>?,
    onNavigate: (navigationEffect: HomeContract.Effect.Navigation) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is HomeContract.Effect.Navigation -> {
                    onNavigate(effect)
                }

                is HomeContract.Effect.OnApiError -> {
                    snackbarHostState.showSnackbar(
                        message = effect.message
                    )
                }

                else -> {}
            }
        }?.collect()
    }
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(orange) // Replace with your header background color
                    .padding(16.dp)
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(
                        onClick = { onNavigate(HomeContract.Effect.Navigation.NavigateToBack) },
                        modifier = Modifier
                            .background(Color.White, shape = CircleShape)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowLeft,
                                contentDescription = null
                            )
                            Text(text = stringResource(R.string.go_back), color = Color.Black)
                        }
                    }
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(R.string.my_basket),
                        fontSize = 24.sp,
                        lineHeight = 32.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.White
                    )
                }
            }

        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().weight(1f),
                ) {
                    Text(
                        text = stringResource(R.string.total), fontSize = 18.sp, fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "₦ 60,000",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2B1E72),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Button(
                    onClick = { /* Handle checkout */ },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = orange),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = stringResource(R.string.checkout), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LazyColumn {
                items(state.selectedProducts){
                    BasketItemRow(item = it)
                }
            }
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            LoadingAnimation()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun BasketItemRow(item: ProductsItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = item.image,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            text = "₦ ${item.price}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2B1E72)
        )
    }
}

@DarkLightPreviews
@Composable
fun BasketPreview() {
    ECommerceDemoTheme {
        BasketScreen(
            state = HomeContract.HomeState(),
            onEvent = {},
            effectFlow = null,
            onNavigate = {}
        )
    }
}