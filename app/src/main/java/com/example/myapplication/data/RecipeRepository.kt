package com.example.myapplication.data

import androidx.compose.ui.graphics.Color
import com.example.myapplication.model.Category
import com.example.myapplication.model.Creator
import com.example.myapplication.model.InboxItem
import com.example.myapplication.model.RecipeVideo

interface RecipeRepository {
    fun getFeed(): List<RecipeVideo>
    fun getCategories(): List<Category>
    fun getInbox(): List<InboxItem>
}

class FakeRecipeRepository : RecipeRepository {
    override fun getFeed(): List<RecipeVideo> {
        val chefNika = Creator("c1", "Ника", "@nika.cooks", "120K")
        val chefArtem = Creator("c2", "Артём", "@airfryer.art", "78K")
        return listOf(
            RecipeVideo(
                id = "r1",
                title = "Лосось терияки за 12 минут",
                caption = "Сочный лосось + рис + огурец = идеальный dinner box 🍱",
                cookTime = "12 мин",
                difficulty = "Легко",
                likes = "24.8K",
                comments = "942",
                saves = "11.3K",
                ingredients = listOf("Лосось", "Соус терияки", "Рис", "Огурец", "Кунжут"),
                steps = listOf("Обжарь лосось 3-4 минуты", "Добавь соус", "Сервируй с рисом"),
                creator = chefNika,
                accent = Color(0xFFFF7043)
            ),
            RecipeVideo(
                id = "r2",
                title = "Паста one-pot с грибами",
                caption = "Все в одной сковороде. Меньше посуды — больше кайфа 🍝",
                cookTime = "15 мин",
                difficulty = "Средне",
                likes = "14.2K",
                comments = "512",
                saves = "6.1K",
                ingredients = listOf("Паста", "Сливки", "Шампиньоны", "Пармезан", "Чеснок"),
                steps = listOf("Обжарь грибы", "Добавь пасту и воду", "Вмешай сливки и сыр"),
                creator = chefArtem,
                accent = Color(0xFF8BC34A)
            )
        )
    }

    override fun getCategories(): List<Category> = listOf(
        Category("1", "Быстрые", "⚡"),
        Category("2", "ЗОЖ", "🥗"),
        Category("3", "Десерты", "🍰"),
        Category("4", "Street food", "🌮"),
        Category("5", "Веган", "🌱")
    )

    override fun getInbox(): List<InboxItem> = listOf(
        InboxItem("n1", "Chef Nika лайкнула ваш рецепт", "Снимите еще ролик про завтрак", "2м"),
        InboxItem("n2", "Новый комментарий", "Какой рис вы использовали?", "18м"),
        InboxItem("n3", "Подписки", "@airfryer.art выложил новый рилс", "1ч")
    )
}
