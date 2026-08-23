package com.groomora.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.groomora.design.*
import com.groomora.design.components.*

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel,
    onNavigateToShop: (String) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "My Favorites",
                onBack = onBack
            )
        }
    ) { padding ->
        if (state.isLoading) {
            GroomoraLoadingState()
        } else if (state.favoriteShops.isEmpty() && state.favoriteProfessionals.isEmpty()) {
            GroomoraEmptyState(
                title = "No favorites yet",
                message = "Save shops and professionals to find them easily.",
                icon = Icons.Default.Favorite,
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.favoriteShops.isNotEmpty()) {
                    item {
                        Text(
                            text = "Shops",
                            style = MaterialTheme.typography.titleLarge,
                            color = AppText
                        )
                    }
                    items(state.favoriteShops) { shop ->
                        ShopCard(
                            shop = shop,
                            onViewShop = { onNavigateToShop(shop.id) },
                            isFavorite = true,
                            onFavoriteToggle = { viewModel.onIntent(FavoritesIntent.ToggleShopFavorite(shop.id)) }
                        )
                    }
                }
            }
        }
    }
}
