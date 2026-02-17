package com.example.myapplication.ui.screens

import android.net.Uri
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.theme.Card

@Composable
fun VideoDetailScreen(recipe: RecipeVideo, onBack: () -> Unit) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(recipe.videoAsset)))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }
    var liked by remember { mutableStateOf(false) }
    var likes by remember { mutableStateOf(recipe.likes) }
    var showComments by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("← Назад", color = Color.Gray, modifier = Modifier.clickable { onBack() })
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(Color.Black, RoundedCornerShape(18.dp))
            ) {
                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            this.player = player
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        item {
            Text(recipe.title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(recipe.caption, color = Color.LightGray)
            Text(recipe.fullDescription, color = Color.Gray)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {
                        liked = !liked
                        likes += if (liked) 1 else -1
                    },
                    label = { Text("$likes") },
                    leadingIcon = {
                        Icon(
                            Icons.Rounded.Favorite,
                            contentDescription = null,
                            tint = if (liked) Color.Red else Color.White
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(containerColor = Card, labelColor = Color.White)
                )
                AssistChip(
                    onClick = { showComments = true },
                    label = { Text("${recipe.commentsCount} комментариев") },
                    leadingIcon = { Icon(Icons.Rounded.ChatBubble, contentDescription = null) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = Card, labelColor = Color.White)
                )
            }
        }
        item {
            Text("Ингредиенты", color = Color.White, fontWeight = FontWeight.Bold)
            recipe.ingredients.forEach { ingredient ->
                Text("• $ingredient", color = Color.LightGray)
            }
        }
        item {
            Text("Шаги", color = Color.White, fontWeight = FontWeight.Bold)
        }
        itemsIndexed(recipe.steps) { index, step ->
            Text("${index + 1}. $step", color = Color.LightGray)
        }
        item {
            Text("Порции: ${recipe.servings} • Сложность: ${recipe.difficulty} • Калории: ${recipe.calories}", color = Color.Gray)
        }
    }

    if (showComments) {
        AlertDialog(
            onDismissRequest = { showComments = false },
            title = { Text("Комментарии") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    recipe.comments.forEach {
                        Text("${it.author}: ${it.text} ❤️ ${it.likes}")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showComments = false }) {
                    Text("Закрыть")
                }
            }
        )
    }
}
