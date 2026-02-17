package com.example.myapplication.ui.app

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
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.myapplication.data.FakeRecipeRepository
import com.example.myapplication.domain.GetAppDataUseCase
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.model.UserProfile
import com.example.myapplication.ui.screens.CreateRecipeScreen
import com.example.myapplication.ui.screens.DiscoverScreen
import com.example.myapplication.ui.screens.FeedScreen
import com.example.myapplication.ui.screens.InboxScreen
import com.example.myapplication.ui.screens.ProfileDetailScreen
import com.example.myapplication.ui.screens.ProfilesScreen
import com.example.myapplication.ui.screens.VideoDetailScreen
import com.example.myapplication.ui.theme.Night

private enum class AppTab { Feed, Discover, Create, Inbox, Profile }

@Composable
fun FoodTokApp() {
    val appData = remember { GetAppDataUseCase(FakeRecipeRepository()).invoke() }
    var selectedTab by remember { mutableStateOf(AppTab.Feed) }
    var selectedVideo by remember { mutableStateOf<RecipeVideo?>(null) }
    var selectedProfile by remember { mutableStateOf<UserProfile?>(null) }

    Scaffold(
        containerColor = Night,
        bottomBar = {
            if (selectedVideo == null) {
                BottomBar(selectedTab = selectedTab, onTabSelected = {
                    selectedProfile = null
                    selectedTab = it
                })
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Night)
        ) {
            when {
                selectedVideo != null -> VideoDetailScreen(recipe = selectedVideo!!, onBack = { selectedVideo = null })
                selectedTab == AppTab.Feed -> FeedScreen(appData.feed, onVideoClick = { selectedVideo = it })
                selectedTab == AppTab.Discover -> DiscoverScreen(appData.categories, appData.feed, onVideoClick = { selectedVideo = it })
                selectedTab == AppTab.Create -> CreateRecipeScreen()
                selectedTab == AppTab.Inbox -> InboxScreen(appData.inbox)
                selectedTab == AppTab.Profile && selectedProfile != null -> ProfileDetailScreen(
                    profile = selectedProfile!!,
                    onVideoClick = { id -> selectedVideo = appData.feed.firstOrNull { it.id == id } },
                    onBack = { selectedProfile = null }
                )

                else -> ProfilesScreen(appData.profiles, onProfileClick = { selectedProfile = it })
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
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        BottomItem("Inbox", Icons.Rounded.Send, selectedTab == AppTab.Inbox) { onTabSelected(AppTab.Inbox) }
        BottomItem("Профили", Icons.Rounded.Person, selectedTab == AppTab.Profile) { onTabSelected(AppTab.Profile) }
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
            tint = if (selected) Color.White else Color.Gray
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
