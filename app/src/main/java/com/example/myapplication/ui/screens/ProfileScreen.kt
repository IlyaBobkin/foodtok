package com.example.myapplication.ui.screens

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
import com.example.myapplication.model.UserProfile
import com.example.myapplication.ui.theme.Card

@Composable
fun MyProfileScreen(profile: UserProfile, onVideoClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(74.dp)
                    .background(Color(0xFF3A4258), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(profile.creator.avatarEmoji, color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text("Мой профиль", color = Color.Gray)
                Text(profile.creator.name, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(profile.creator.nickname, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Stat(profile.videos.size.toString(), "Мои видео")
            Stat(profile.creator.followers, "Подписчики")
            Stat("4.9", "Рейтинг")
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(profile.specialty, color = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Мои рецепты", color = Color.White, fontWeight = FontWeight.Bold)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(profile.videos) { recipe ->
                Box(
                    modifier = Modifier
                        .height(170.dp)
                        .clickable { onVideoClick(recipe.id) }
                        .background(recipe.accent.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                        .padding(10.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(recipe.title, color = Color.White, maxLines = 2, style = MaterialTheme.typography.labelMedium)
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
