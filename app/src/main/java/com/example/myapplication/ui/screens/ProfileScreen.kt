package com.example.myapplication.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.theme.Card

@Composable
fun ProfileScreen(feed: List<RecipeVideo>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(74.dp)
                    .background(Color(0xFF3A4258), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text("Alex Cook", color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("@alex.foodtok", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Stat("54", "Рецепта")
            Stat("92K", "Подписчики")
            Stat("1.8M", "Лайки")
        }

        Spacer(modifier = Modifier.height(18.dp))
        Text("Мои ролики", color = Color.White, fontWeight = FontWeight.Bold)

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(feed + feed) { recipe ->
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .background(recipe.accent.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                        .padding(8.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(recipe.title, color = Color.White, maxLines = 2, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@Composable
private fun Stat(value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Card, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(value, color = Color.White, fontWeight = FontWeight.Bold)
        Text(label, color = Color.Gray, style = MaterialTheme.typography.labelSmall)
    }
}
