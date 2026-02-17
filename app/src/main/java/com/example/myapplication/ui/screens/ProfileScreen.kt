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
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
fun ProfilesScreen(profiles: List<UserProfile>, onProfileClick: (UserProfile) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("Профили авторов", color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Открывай профиль и смотри все видео конкретного автора", color = Color.Gray)
        }
        items(profiles) { profile ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onProfileClick(profile) }
                    .background(Card, RoundedCornerShape(16.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(58.dp).background(Color(0xFF3A4258), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(profile.creator.avatarEmoji, color = Color.White)
                }
                Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                    Text(profile.creator.name, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(profile.creator.nickname, color = Color.Gray)
                    Text(profile.specialty, color = Color.LightGray, maxLines = 1)
                }
                Text("${profile.videos.size} видео", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileDetailScreen(profile: UserProfile, onVideoClick: (String) -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("← Назад", color = Color.Gray, modifier = Modifier.clickable { onBack() })
        Spacer(modifier = Modifier.height(12.dp))
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
                Text(profile.creator.name, color = Color.White, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(profile.creator.nickname, color = Color.Gray)
                Text(profile.location, color = Color.LightGray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Stat(profile.videos.size.toString(), "Рецептов")
            Stat(profile.creator.followers, "Подписчики")
            Stat(profile.location, "Город")
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(profile.specialty, color = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Видео автора", color = Color.White, fontWeight = FontWeight.Bold)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(profile.videos) { recipe ->
                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .clickable { onVideoClick(recipe.id) }
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
