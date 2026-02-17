package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.model.InboxItem
import com.example.myapplication.ui.theme.Card

@Composable
fun InboxScreen(items: List<InboxItem>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Inbox", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
        LazyColumn(
            contentPadding = PaddingValues(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items) { item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Card, RoundedCornerShape(16.dp))
                        .padding(14.dp)
                ) {
                    Text(item.title, color = Color.White, fontWeight = FontWeight.SemiBold)
                    Text(item.subtitle, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
                    Text(item.time, color = Color(0xFFFF8A65), modifier = Modifier.padding(top = 6.dp))
                }
            }
        }
    }
}
