package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.RecipeComment
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.theme.Card

@Composable
fun CommentsScreen(
    feed: List<RecipeVideo>,
    commentsByRecipe: Map<String, List<RecipeComment>>,
    onOpenRecipe: (RecipeVideo) -> Unit,
    onCommentAdd: (String, RecipeComment) -> Unit
) {
    var selectedRecipeId by remember { mutableStateOf(feed.firstOrNull()?.id.orEmpty()) }
    var draft by remember { mutableStateOf("") }
    val selectedRecipe = feed.firstOrNull { it.id == selectedRecipeId } ?: return
    val comments = commentsByRecipe[selectedRecipe.id] ?: selectedRecipe.comments

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Комментарии", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
        Text("Выбери рецепт и общайся с аудиторией", color = Color.Gray)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text("Рецепты", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
            items(feed.take(6)) { recipe ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selectedRecipeId == recipe.id) recipe.accent.copy(alpha = 0.28f) else Card,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedRecipeId = recipe.id }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(recipe.title, color = Color.White, modifier = Modifier.weight(1f))
                    Text("Открыть", color = Color(0xFFFFB74D), modifier = Modifier.clickable { onOpenRecipe(recipe) })
                }
            }
            item {
                Text("Лента комментариев", color = Color.White, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 4.dp))
            }
            items(comments) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Card, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Text(it.author, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(it.text, color = Color.LightGray, modifier = Modifier.padding(top = 4.dp))
                    Text("❤️ ${it.likes}", color = Color.Gray, modifier = Modifier.padding(top = 6.dp))
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = draft,
                onValueChange = { draft = it },
                label = { Text("Напиши комментарий") },
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                if (draft.isNotBlank()) {
                    onCommentAdd(
                        selectedRecipe.id,
                        RecipeComment(
                            id = "${selectedRecipe.id}-new-${System.currentTimeMillis()}",
                            author = "@you",
                            text = draft,
                            likes = 0
                        )
                    )
                    draft = ""
                }
            }) {
                Icon(Icons.Rounded.Send, contentDescription = null, tint = Color.White)
            }
        }
    }
}
