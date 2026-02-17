package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.ChatBubble
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.theme.Card
import com.example.myapplication.ui.theme.TextSecondary

@Composable
fun RecipeMockVideoCard(recipe: RecipeVideo, modifier: Modifier = Modifier, onClick: (() -> Unit)? = null) {
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Box(
        modifier = modifier
            .then(clickableModifier)
            .fillMaxWidth()
            .height(560.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(recipe.accent.copy(alpha = 0.4f), Color.Black)
                ),
                shape = RoundedCornerShape(26.dp)
            )
            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(26.dp))
            .padding(18.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.PlayArrow, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Text("Смотреть", color = Color.White, style = MaterialTheme.typography.labelMedium)
            }
        }

        Column(modifier = Modifier.align(Alignment.BottomStart)) {
            Text(text = recipe.creator.nickname, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = recipe.title,
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = recipe.caption, color = TextSecondary)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatChip(text = formatCount(recipe.likes), icon = Icons.Rounded.Favorite)
                StatChip(text = formatCount(recipe.commentsCount), icon = Icons.Rounded.ChatBubble)
                StatChip(text = formatCount(recipe.saves), icon = Icons.Rounded.Bookmark)
            }
        }

        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    .border(1.dp, Color.White.copy(alpha = 0.35f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(recipe.creator.avatarEmoji, color = Color.White, fontWeight = FontWeight.Black)
            }
            Text(recipe.creator.followers, color = Color.White, style = MaterialTheme.typography.labelMedium)
        }
    }
}

private fun formatCount(value: Int): String = if (value >= 1000) "%.1fK".format(value / 1000f) else value.toString()

@Composable
private fun StatChip(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    AssistChip(
        onClick = {},
        label = { Text(text) },
        leadingIcon = { Icon(icon, null, modifier = Modifier.size(16.dp)) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Card,
            labelColor = Color.White,
            leadingIconContentColor = Color.White
        )
    )
}

@Composable
fun MetaPill(time: String, difficulty: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        AssistChip(
            onClick = {},
            label = { Text(time) },
            leadingIcon = { Icon(Icons.Rounded.AccessTime, null, modifier = Modifier.size(16.dp)) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = Card,
                labelColor = Color.White,
                leadingIconContentColor = Color.White
            )
        )
        AssistChip(
            onClick = {},
            label = { Text(difficulty) },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = Card,
                labelColor = Color.White
            )
        )
    }
}
