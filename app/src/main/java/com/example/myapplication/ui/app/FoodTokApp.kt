package com.example.myapplication.ui.app

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Comment
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.FakeRecipeRepository
import com.example.myapplication.domain.GetAppDataUseCase
import com.example.myapplication.model.RecipeComment
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.screens.CommentsScreen
import com.example.myapplication.ui.screens.CreateRecipeScreen
import com.example.myapplication.ui.screens.DiscoverScreen
import com.example.myapplication.ui.screens.FeedScreen
import com.example.myapplication.ui.screens.MyProfileScreen
import com.example.myapplication.ui.screens.VideoDetailScreen
import com.example.myapplication.ui.theme.Night

private enum class AppTab { Feed, Discover, Create, Comments, Profile }

@Composable
fun FoodTokApp() {
    val appData = remember { GetAppDataUseCase(FakeRecipeRepository()).invoke() }
    var selectedTab by remember { mutableStateOf(AppTab.Feed) }
    var selectedVideo by remember { mutableStateOf<RecipeVideo?>(null) }
    val commentsByRecipe = remember { mutableStateMapOf<String, List<RecipeComment>>() }

    Scaffold(
        containerColor = Night,
        bottomBar = {
            if (selectedVideo == null) {
                BottomBar(selectedTab = selectedTab, onTabSelected = { selectedTab = it })
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Night)
        ) {
            if (selectedVideo != null) {
                VideoDetailScreen(
                    recipe = selectedVideo!!,
                    comments = commentsByRecipe[selectedVideo!!.id] ?: selectedVideo!!.comments,
                    onBack = { selectedVideo = null },
                    onCommentAdd = { comment ->
                        val current = commentsByRecipe[selectedVideo!!.id] ?: selectedVideo!!.comments
                        commentsByRecipe[selectedVideo!!.id] = current + comment
                    }
                )
            } else {
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tabTransition"
                ) { tab ->
                    when (tab) {
                        AppTab.Feed -> FeedScreen(appData.feed, onVideoClick = { selectedVideo = it })
                        AppTab.Discover -> DiscoverScreen(appData.categories, appData.feed, onVideoClick = { selectedVideo = it })
                        AppTab.Create -> CreateRecipeScreen()
                        AppTab.Comments -> CommentsScreen(
                            feed = appData.feed,
                            commentsByRecipe = commentsByRecipe,
                            onOpenRecipe = { selectedVideo = it },
                            onCommentAdd = { recipeId, comment ->
                                val recipe = appData.feed.first { it.id == recipeId }
                                val current = commentsByRecipe[recipeId] ?: recipe.comments
                                commentsByRecipe[recipeId] = current + comment
                            }
                        )

                        AppTab.Profile -> MyProfileScreen(
                            profile = appData.profiles.first(),
                            onVideoClick = { id -> selectedVideo = appData.feed.firstOrNull { it.id == id } }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomBar(selectedTab: AppTab, onTabSelected: (AppTab) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.9f))
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomItem("Лента", Icons.Rounded.Home, selectedTab == AppTab.Feed) { onTabSelected(AppTab.Feed) }
        BottomItem("Поиск", Icons.Rounded.Explore, selectedTab == AppTab.Discover) { onTabSelected(AppTab.Discover) }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .padding(8.dp)
                .clickable { onTabSelected(AppTab.Create) }
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        BottomItem("Комменты", Icons.AutoMirrored.Rounded.Comment, selectedTab == AppTab.Comments) { onTabSelected(AppTab.Comments) }
        BottomItem("Профиль", Icons.Rounded.Person, selectedTab == AppTab.Profile) { onTabSelected(AppTab.Profile) }
    }
}

@Composable
private fun BottomItem(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(4.dp)
            .clickable(onClick = onClick)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (selected) MaterialTheme.colorScheme.primary else Color.Gray
        )
        Text(
            text = title,
            color = if (selected) Color.White else Color.Gray,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
