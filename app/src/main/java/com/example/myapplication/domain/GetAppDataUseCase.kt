package com.example.myapplication.domain

import com.example.myapplication.data.RecipeRepository
import com.example.myapplication.model.Category
import com.example.myapplication.model.InboxItem
import com.example.myapplication.model.RecipeVideo

data class AppData(
    val feed: List<RecipeVideo>,
    val categories: List<Category>,
    val inbox: List<InboxItem>
)

class GetAppDataUseCase(private val repository: RecipeRepository) {
    operator fun invoke(): AppData = AppData(
        feed = repository.getFeed(),
        categories = repository.getCategories(),
        inbox = repository.getInbox()
    )
}
