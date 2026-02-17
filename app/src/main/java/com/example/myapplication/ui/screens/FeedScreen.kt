package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.components.MetaPill
import com.example.myapplication.ui.components.RecipeMockVideoCard

@Composable
fun FeedScreen(feed: List<RecipeVideo>, onVideoClick: (RecipeVideo) -> Unit) {
    val liked = remember { mutableStateMapOf<String, Boolean>() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Text(
                    text = "FoodTok Pro",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "Вертикальный видеопоток с реакциями, лайками и быстрым открытием рецепта",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        items(feed, key = { it.id }) { recipe ->
            val isLiked = liked[recipe.id] == true
            val likeScale by animateFloatAsState(targetValue = if (isLiked) 1.2f else 1f, label = "likeScale")

            Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                RecipeMockVideoCard(
                    recipe = recipe,
                    modifier = Modifier.fillMaxWidth().height(640.dp),
                    onClick = { onVideoClick(recipe) }
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.35f), CircleShape)
                            .clickable { liked[recipe.id] = !isLiked }
                            .padding(10.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Favorite,
                            contentDescription = null,
                            tint = if (isLiked) Color.Red else Color.White,
                            modifier = Modifier.scale(likeScale)
                        )
                    }
                    AnimatedVisibility(
                        visible = isLiked,
                        enter = scaleIn(),
                        exit = scaleOut()
                    ) {
                        Text("Лайк!", color = Color.White)
                    }
                }
            }
            Row(modifier = Modifier.padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MetaPill(time = recipe.cookTime, difficulty = recipe.difficulty)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
