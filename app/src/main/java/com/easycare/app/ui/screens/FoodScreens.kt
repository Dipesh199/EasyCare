package com.easycare.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.easycare.app.model.Meal
import com.easycare.app.ui.components.LargeActionButton
import com.easycare.app.ui.components.SimpleTopBar
import com.easycare.app.ui.theme.AppSpacing

@Composable
fun FoodScreen(
    meals: List<Meal>,
    onBack: () -> Unit,
    onSelect: (Meal) -> Unit,
    onSpeak: (String) -> Unit,
) {
    LaunchedEffect(Unit) { onSpeak("Please choose your meal.") }
    Scaffold(topBar = { SimpleTopBar("Choose Your Meal", onBack) }) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(AppSpacing.large),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.medium),
        ) {
            items(meals, key = { it.id }) { meal ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Column(Modifier.padding(AppSpacing.large)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Restaurant, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.padding(6.dp))
                            Text(meal.name, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                            Text("€${meal.priceEuros}", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(Modifier.height(AppSpacing.small))
                        Text(meal.description, style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(AppSpacing.medium))
                        LargeActionButton("Select", { onSelect(meal) })
                    }
                }
            }
        }
    }
}

@Composable
fun OrderSuccessScreen(onHome: () -> Unit, onSpeak: (String) -> Unit) {
    LaunchedEffect(Unit) { onSpeak("Your food request has been sent.") }
    Scaffold { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).padding(AppSpacing.large),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(Icons.Default.CheckCircle, "Order request sent", modifier = Modifier.size(88.dp), tint = MaterialTheme.colorScheme.primary)
            Text("Order Request Sent", style = MaterialTheme.typography.headlineLarge, textAlign = TextAlign.Center)
            Spacer(Modifier.height(AppSpacing.large))
            Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)) {
                Column(Modifier.padding(AppSpacing.large), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Order status", style = MaterialTheme.typography.bodyLarge)
                    Text("Preparing", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(AppSpacing.extraLarge))
            LargeActionButton("Return Home", onHome)
        }
    }
}
