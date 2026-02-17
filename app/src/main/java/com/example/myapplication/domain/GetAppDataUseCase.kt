package com.example.myapplication.domain

import com.example.myapplication.data.RecipeRepository
import com.example.myapplication.model.Category
import com.example.myapplication.model.InboxItem
import com.example.myapplication.model.RecipeVideo
import com.example.myapplication.model.UserProfile

data class AppData(
    val feed: List<RecipeVideo>,
    val categories: List<Category>,
    val inbox: List<InboxItem>,
    val profiles: List<UserProfile>
)

class GetAppDataUseCase(private val repository: RecipeRepository) {
    operator fun invoke(): AppData = AppData(
        feed = repository.getFeed(),
        categories = repository.getCategories(),
        inbox = repository.getInbox(),
        profiles = repository.getProfiles()
    )
}
