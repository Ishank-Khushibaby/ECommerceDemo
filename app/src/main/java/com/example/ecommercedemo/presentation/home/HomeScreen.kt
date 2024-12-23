package com.example.ecommercedemo.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.example.ecommercedemo.ui.theme.DarkLightPreviews
import com.example.ecommercedemo.ui.theme.ECommerceDemoTheme
import com.example.ecommercedemo.ui.theme.orange
import com.example.ecommercedemo.ui.theme.orangeLight
import com.example.ecommercedemo.ui.theme.text_background
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
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
                else->{}
            }
        }?.collect()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(12.dp),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier,
                            painter = painterResource(id = R.drawable.ic_toogle),
                            contentDescription = "Toggle Icon",
                            tint = Color.Black
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                onNavigate(HomeContract.Effect.Navigation.NavigateToBucket)
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_basket),
                                contentDescription = "Basket Icon",
                                modifier = Modifier.size(32.dp),
                                tint = orange
                            )
                            Text(
                                text = stringResource(R.string.my_basket),
                                fontSize = 10.sp,
                                lineHeight = 15.sp,
                                fontWeight = FontWeight.W400
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = TextUnit(18f, TextUnitType.Sp)
                        )
                    ) {
                        append(stringResource(id = R.string.user))
                    }

                    append(
                        stringResource(R.string.home_description_for_user)
                    )
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.W400,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(text_background, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Search Icon",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        innerTextField()
                    }
                }
            )
            Text(
                text = stringResource(R.string.recommended_combo),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                modifier = Modifier,
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.products) { it ->
                    ComboCard(
                        it
                    ){item->
                        onEvent(HomeContract.HomeEvent.AddProductToBucket(item = item))
                    }
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
fun ComboCard(
    product: ProductsItem,
    addProduct: (ProductsItem)->Unit,
) {

    Column(
        modifier = Modifier
            .padding(4.dp)
            .shadow(2.dp, shape = RoundedCornerShape(16.dp), ambientColor = text_background)
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GlideImage(
            model = product.image,
            contentDescription = null,
        )
        Text(
            text = product.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "₦${product.price}",
                fontSize = 14.sp,
                color = orange
            )
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(orangeLight)
                    .clickable { addProduct(product) },
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add Icon",
                tint = orange
            )
        }
    }
}


@DarkLightPreviews
@Composable
fun HomePreview() {
    ECommerceDemoTheme {
        HomeScreen(
            state = HomeContract.HomeState(),
            onEvent = {},
            effectFlow = null,
            onNavigate = {}
        )
    }
}