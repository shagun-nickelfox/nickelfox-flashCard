package com.example.flashcardapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.flashcardapp.ui.theme.FlashCardAppTheme
import com.example.flashcardapp.ui.theme.beverageTabColor
import com.example.flashcardapp.ui.theme.eventTabColor
import com.example.flashcardapp.ui.theme.foodTabColor
import com.example.flashcardapp.ui.theme.specialTabColor
import com.example.flashcardapp.ui.theme.wineTabColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val selectedTrapeziums = listOf(
                FlashCard(
                    R.drawable.ic_launcher_background,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf("Orange", "Lemons", "Apples", "Grapes"),
                    foodTabColor,
                    false,
                    "Food"
                ),
                FlashCard(
                    R.drawable.ic_launcher_foreground,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf(
                        "Orange", "Lemons", "Apples", "Grapes"
                    ),
                    foodTabColor,
                    false,
                    "Food"
                ),
                FlashCard(
                    R.drawable.bg_wine2,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf(
                        "Orange", "Lemons", "Apples", "Grapes"
                    ),
                    foodTabColor,
                    true,
                    "Food"
                ),
                FlashCard(
                    R.drawable.bg_cup,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf(
                        "Oranges", "Lemon", "Apple", "Grapes"
                    ),
                    wineTabColor,
                    false,
                    "Wine"
                ),
                FlashCard(
                    R.drawable.bg_wine,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf(
                        "Orange", "Lemons", "Apple", "Grapes"
                    ),
                    wineTabColor,
                    false,
                    "Wine"
                ),
                FlashCard(
                    R.drawable.bg_wine2,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf(
                        "Orange", "Lemon", "Apples", "Grapes"
                    ),
                    wineTabColor,
                    false,
                    "Wine"
                ),

                FlashCard(
                    R.drawable.bg_wine2,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf(
                        "Orange", "Lemon", "Apples", "Grapes"
                    ),
                    beverageTabColor,
                    false,
                    "Beverage"
                ),
                FlashCard(
                    R.drawable.bg_wine2,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf(
                        "Orange", "Lemon", "Apples", "Grapes"
                    ),
                    beverageTabColor,
                    false,
                    "Beverage"
                ),
                FlashCard(
                    R.drawable.bg_wine2,
                    "Which grape varietal makes Burgundy’s most famous red wine?",
                    listOf(
                        "Orange", "Lemon", "Apples", "Grapes"
                    ),
                    beverageTabColor,
                    false,
                    "Beverage"
                )
            )
            val allTabs = listOf(
                TrapeziumItem("Wine", R.drawable.ic_wine, wineTabColor),
                TrapeziumItem("Food", R.drawable.ic_food, foodTabColor),
                TrapeziumItem("Beverage", R.drawable.ic_beverage, beverageTabColor),
                TrapeziumItem("Events", R.drawable.ic_events, eventTabColor),
                TrapeziumItem("Special", R.drawable.ic_special, specialTabColor)
            )

            val categoryMap: HashMap<String, List<FlashCard>> = selectedTrapeziums
                .groupBy { it.category }
                .mapValues { (_, items) -> items }
                .toMap(HashMap())


            val flashCards = remember { mutableStateListOf<FlashCard>() }
            FlashCardAppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    val categoryList = listOf("Wine", "Food", "Beverage", "Events", "Special")
                    var categorySelected by remember { mutableStateOf(categoryList[0]) }
                    allTabs.onEach {
                        it.isSelected = (it.text == categorySelected)
                    }

                    flashCards.clear()

                    categoryMap[categorySelected]?.let { list ->
                        flashCards.addAll(list.mapIndexed { index, item ->
                            item.isSelected = (index == list.lastIndex);item
                        })
                    }

                    val nextCard = remember { mutableStateOf(flashCards[0]) }
                    //NextCategoryBox(flashCards[0])
                    FlashCardsAndTabs(
                        nextCard,
                        flashCards,
                        allTabs,
                        onSwipe = { card ->
                            flashCards.removeLastOrNull()
                            if (flashCards.size == 0) {
                                lifecycleScope.launch(Dispatchers.Main) {
                                    //delay(100)
                                    val index = categoryList.indexOf(card.category)
                                    categorySelected = if (index == -1) {
                                        categoryList[0]
                                    } else {
                                        categoryList[index + 1]
                                    }
                                }
                            } else if (flashCards.size == 1) {
                                flashCards[flashCards.lastIndex] =
                                    flashCards[flashCards.lastIndex].copy(isSelected = true)
                                val index = categoryList.indexOf(card.category)
                                card.lastCard = true
                                if (index != -1) {
                                    val list = categoryMap[categoryList[index + 1]]
                                    if (list != null)
                                        nextCard.value = list[list.size - 1]
                                }
                            } else {
                                flashCards[flashCards.lastIndex] =
                                    flashCards[flashCards.lastIndex].copy(isSelected = true)
                            }
                        },
                    ) { category ->
                        lifecycleScope.launch {
                            Log.d("CategoryIndex", category)
                            categorySelected = category
                        }
                    }
                }
            }
        }
    }
}

