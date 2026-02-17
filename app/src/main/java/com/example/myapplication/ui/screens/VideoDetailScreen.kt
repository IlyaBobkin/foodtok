package com.example.myapplication.ui.screens

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.myapplication.model.RecipeComment
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.theme.Card

@Composable
fun VideoDetailScreen(
    recipe: RecipeVideo,
    comments: List<RecipeComment>,
    onBack: () -> Unit,
    onCommentAdd: (RecipeComment) -> Unit
) {
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
    var saved by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(true) }
    var commentDraft by remember { mutableStateOf("") }
    val likeScale by animateFloatAsState(targetValue = if (liked) 1.25f else 1f, label = "detailLike")

    DisposableEffect(Unit) { onDispose { player.release() } }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = this@VideoDetailScreen.player
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.background(Color.Black.copy(alpha = 0.45f), CircleShape).clickable { onBack() }.padding(8.dp)) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
            }
            Text(recipe.creator.nickname, color = Color.White, fontWeight = FontWeight.Bold)
            Text("HD", color = Color.White)
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ActionButton(icon = Icons.Rounded.Favorite, label = "${recipe.likes + if (liked) 1 else 0}", tint = if (liked) Color.Red else Color.White, modifier = Modifier.scale(likeScale)) {
                liked = !liked
            }
            ActionButton(icon = Icons.Rounded.ChatBubble, label = comments.size.toString()) { showDetails = true }
            ActionButton(icon = Icons.Rounded.Bookmark, label = if (saved) "Сохранено" else recipe.saves.toString()) { saved = !saved }
        }

        AnimatedVisibility(
            visible = showDetails,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(430.dp)
                    .navigationBarsPadding()
                    .background(Color(0xFF14161C), RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp))
                    .padding(16.dp)
            ) {
                Text(recipe.title, color = Color.White, fontWeight = FontWeight.Bold)
                Text(recipe.caption, color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
                Text(recipe.fullDescription, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 10.dp)) {
                    AssistChip(onClick = {}, label = { Text("${recipe.cookTime} • ${recipe.difficulty}") }, colors = AssistChipDefaults.assistChipColors(containerColor = Card, labelColor = Color.White))
                    AssistChip(onClick = {}, label = { Text("${recipe.calories} • ${recipe.servings} порц.") }, colors = AssistChipDefaults.assistChipColors(containerColor = Card, labelColor = Color.White))
                }

                LazyColumn(modifier = Modifier.weight(1f).padding(top = 10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { Text("Ингредиенты", color = Color.White, fontWeight = FontWeight.SemiBold) }
                    items(recipe.ingredients) { Text("• $it", color = Color.LightGray) }
                    item { Text("Шаги приготовления", color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp)) }
                    items(recipe.steps) { step -> Text(step, color = Color.LightGray) }
                    item { Text("Комментарии", color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 8.dp)) }
                    items(comments.takeLast(5)) { c -> Text("${c.author}: ${c.text}", color = Color.LightGray) }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = commentDraft,
                        onValueChange = { commentDraft = it },
                        modifier = Modifier.weight(1f),
                        label = { Text("Оставить комментарий") }
                    )
                    Box(modifier = Modifier.background(Card, CircleShape).clickable {
                        if (commentDraft.isNotBlank()) {
                            onCommentAdd(
                                RecipeComment(
                                    id = "${recipe.id}-new-${System.currentTimeMillis()}",
                                    author = "@you",
                                    text = commentDraft,
                                    likes = 0
                                )
                            )
                            commentDraft = ""
                        }
                    }.padding(10.dp)) {
                        Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text("Скрыть детали", color = Color.Gray, modifier = Modifier.align(Alignment.CenterHorizontally).clickable { showDetails = false })
            }
        }

        if (!showDetails) {
            Text(
                "Показать рецепт",
                color = Color.White,
                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp).clickable { showDetails = true }
            )
        }
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    tint: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                .clickable { onClick() }
                .padding(10.dp)
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = modifier)
        }
        Text(label, color = Color.White)
    }
}
