package com.example.myapplication.model

import androidx.compose.ui.graphics.Color

data class Creator(
    val id: String,
    val name: String,
    val nickname: String,
    val followers: String,
    val avatarEmoji: String,
    val bio: String
)

data class RecipeComment(
    val id: String,
    val author: String,
    val text: String,
    val likes: Int
)

data class RecipeVideo(
    val id: String,
    val title: String,
    val caption: String,
    val fullDescription: String,
    val cookTime: String,
    val difficulty: String,
    val servings: String,
    val calories: String,
    val likes: Int,
    val commentsCount: Int,
    val saves: Int,
    val ingredients: List<String>,
    val steps: List<String>,
    val tags: List<String>,
    val comments: List<RecipeComment>,
    val creator: Creator,
    val videoAsset: String,
    val accent: Color
)

data class UserProfile(
    val creator: Creator,
    val location: String,
    val specialty: String,
    val videos: List<RecipeVideo>
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
