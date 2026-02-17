package com.example.myapplication.model

import androidx.compose.ui.graphics.Color

data class Creator(
    val id: String,
    val name: String,
    val nickname: String,
    val followers: String
)

data class RecipeVideo(
    val id: String,
    val title: String,
    val caption: String,
    val cookTime: String,
    val difficulty: String,
    val likes: String,
    val comments: String,
    val saves: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val creator: Creator,
    val accent: Color
)

data class Category(
    val id: String,
    val name: String,
    val emoji: String
)

data class InboxItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val time: String
)
