package com.example.flashcardapp

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.flashcardapp.ui.theme.beverageTabColor
import com.example.flashcardapp.ui.theme.eventTabColor
import com.example.flashcardapp.ui.theme.foodTabColor
import com.example.flashcardapp.ui.theme.specialTabColor
import com.example.flashcardapp.ui.theme.wineTabColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FlashCardViewModel(
) : ViewModel() {
    var selectedTrapeziums = mutableListOf<FlashCard>()
    var categoryMap: HashMap<String, List<FlashCard>>

    var allTabs = mutableStateListOf<TrapeziumItem>()

    val categoryList = listOf("Wine", "Food", "Beverage", "Events", "Special")

    var categorySelected = "Wine"

    var flashCards = mutableStateListOf<FlashCard>()
    //var flashCards = MutableStateFlow<MutableList<FlashCard>>(mutableListOf())

    init {
        allTabs.addAll(
            listOf(
                TrapeziumItem("Wine", R.drawable.ic_wine, wineTabColor),
                TrapeziumItem("Food", R.drawable.ic_food, foodTabColor),
                TrapeziumItem("Beverage", R.drawable.ic_beverage, beverageTabColor),
                TrapeziumItem("Events", R.drawable.ic_events, eventTabColor),
                TrapeziumItem("Special", R.drawable.ic_special, specialTabColor)
            )
        )
        selectedTrapeziums.addAll(
            listOf(
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
                    true,
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
        )

        categoryMap = selectedTrapeziums
            .groupBy { it.category }
            .mapValues { (_, items) -> items }
            .toMap(HashMap())

        categoryMap[categorySelected]?.let { list ->
            flashCards.addAll(list.mapIndexed { index, item ->
                item.isSelected = (index == list.lastIndex);item
            })
        }
    }

    fun setCategory(categorySelect: String) {
        categorySelected = categorySelect
        Log.d("FlashCards", flashCards.joinToString { it.isSelected.toString() + " " + it.id })
        flashCards.clear()
        categoryMap[categorySelected]?.let { list ->
            flashCards.addAll(list.mapIndexed { index, item ->
                item.isSelected = (index == list.lastIndex);item
            })
        }
        Log.d(
            "FlashCards",
            flashCards.joinToString { it.isSelected.toString() + " " + it.id })
        allTabs.onEach {
            it.isSelected = (it.text == categorySelected)
        }
    }
}