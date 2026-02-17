package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.ui.components.RecipeMockVideoCard

@Composable
fun FeedScreen(feed: List<RecipeVideo>, onVideoClick: (RecipeVideo) -> Unit, onOpenCreator: (String) -> Unit) {
    val liked = remember { mutableStateMapOf<String, Boolean>() }
    val pagerState = rememberPagerState(pageCount = { feed.size })

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        pageSpacing = 8.dp,
        beyondViewportPageCount = 1
    ) { page ->
        val recipe = feed[page]
        val isLiked = liked[recipe.id] == true
        val likeScale by animateFloatAsState(targetValue = if (isLiked) 1.2f else 1f, label = "likeScale")

        Box(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp, vertical = 6.dp)) {
            RecipeMockVideoCard(
                recipe = recipe,
                modifier = Modifier.fillMaxSize(),
                onClick = { onVideoClick(recipe) },
                onCreatorClick = { onOpenCreator(recipe.creator.id) }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.35f), CircleShape)
                        .clickable { liked[recipe.id] = !isLiked }
                        .padding(10.dp)
                ) {
                    Icon(
                        Icons.Rounded.Favorite,
                        contentDescription = null,
                        tint = if (isLiked) Color.Red else Color.White,
                        modifier = Modifier.scale(likeScale)
                    )
                }
                AnimatedVisibility(
                    visible = isLiked,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    Text("Лайк!", color = Color.White)
                }
            }
        }
    }
}
