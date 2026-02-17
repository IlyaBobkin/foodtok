package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.Category
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.theme.Card

private val curatedGroups = listOf(
    "Для завтрака" to listOf("завтрак", "brunch"),
    "Романтический ужин" to listOf("ужин", "десерт", "мясо"),
    "Мексиканская кухня" to listOf("мексиканская", "streetfood", "тако"),
    "Фит и ЗОЖ" to listOf("пп", "mealprep", "вегетарианское")
)

@Composable
fun DiscoverScreen(categories: List<Category>, feed: List<RecipeVideo>, onVideoClick: (RecipeVideo) -> Unit) {
    var query by remember { mutableStateOf("") }
    val filtered = feed.filter {
        val q = query.trim().lowercase()
        q.isBlank() || it.title.lowercase().contains(q) || it.tags.any { tag -> tag.lowercase().contains(q) }
    }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 12.dp)) {
        item {
            Text(
                text = "Discover",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                label = { Text("Поиск рецептов, тегов, кухонь") }
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(categories) { category ->
                    Box(
                        modifier = Modifier
                            .background(Card, RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text("${category.emoji} ${category.name}", color = Color.White)
                    }
                }
            }
        }

        items(curatedGroups) { group ->
            val groupItems = filtered.filter { recipe ->
                group.second.any { tag -> recipe.tags.any { it.contains(tag, ignoreCase = true) } || recipe.title.contains(tag, ignoreCase = true) }
            }
            if (groupItems.isNotEmpty()) {
                Text(
                    text = group.first,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(360.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(groupItems.take(4)) { recipe ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(170.dp)
                                .clickable { onVideoClick(recipe) }
                                .background(recipe.accent.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(recipe.title, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
