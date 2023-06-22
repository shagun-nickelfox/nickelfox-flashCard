package com.example.flashcardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.flashcardapp.ui.theme.FlashCardAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val model by viewModels<FlashCardViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashCardAppTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    val categoryList = listOf("Wine", "Food", "Beverage", "Events", "Special")
                    /*val categoryList = listOf("Wine", "Food", "Beverage", "Events", "Special")
                    var categorySelected by remember { mutableStateOf(categoryList[0]) }
                    val foodCards = remember(categorySelected) {
                        mutableStateListOf<FlashCard>()
                    }

                    categoryMap[categorySelected]?.let { list ->
                        foodCards.addAll(list.mapIndexed { index, item ->
                            item.isSelected = (index == list.lastIndex);item
                        })
                        Log.d("FoodCardsList",foodCards.toList().size.toString())
                    }

                    allTabs.onEach {
                        it.isSelected = (it.text == categorySelected)
                    }
*/
                    /* val categoryMap: HashMap<String, List<FlashCard>> = selectedTrapeziums
                         .groupBy { it.category }
                         .mapValues { (_, items) -> items }
                         .toMap(HashMap())
                     val categoryList = listOf("Wine", "Food", "Beverage", "Events", "Special")

                     val foodCards = remember { mutableStateListOf<FlashCard>() }
                     foodCards.clear()
                     categoryMap[categoryList[0]]?.let { foodCards.addAll(it) }
                     val selectedTrapezium = remember { mutableStateListOf(0) }*/
                    FlashCardsAndTabs(
                        viewModels(),
                        model.flashCards,
                        model.allTabs,
                        onSwipe = { card ->
                           //model.flashCards.value.removeLastOrNull()
                            model.flashCards.removeLastOrNull()
                            if (model.flashCards.size == 0) {
                                lifecycleScope.launch(Dispatchers.Main) {
                                    delay(100)
                                    val index = categoryList.indexOf(card.category)
                                    //categorySelected = categoryList[index + 1]
                                    model.setCategory(categoryList[index + 1])
                                }
                            } else {
                                model.flashCards[model.flashCards.lastIndex] =
                                    model.flashCards[model.flashCards.lastIndex].copy(
                                        isSelected = true
                                    )
                            }

                            /* val index = foodCards.indexOfFirst { it.id == card.id }
                             foodCards.remove(card)
                             if (index == 0) {
                                 val categoryIndex = categoryList.indexOf(card.category)
                                 if (categoryIndex != -1) {
                                     foodCards.clear()
                                     categoryMap[categoryList[categoryIndex + 1]]?.let {
                                         foodCards.addAll(
                                             it
                                         )
                                     }
                                     selectedTrapezium.clear()
                                     selectedTrapezium.add(categoryIndex + 1)
                                 }
                             } else {
                                 foodCards[index - 1] = foodCards[index - 1].copy(isSelected = true)
                             }*/
                        }
                    ) { category ->
                        lifecycleScope.launch(Dispatchers.Main) {
                            delay(100)
                            model.setCategory(category)
                            //categorySelected = category
                        }
                        /*foodCards.clear()
                        val categoryIndex = categoryList.indexOf(category)
                        categoryMap[categoryList[categoryIndex]]?.let {
                            foodCards.addAll(
                                it
                            )
                        }*/
                    }
                }
            }
        }
    }
}
