/*
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
}*/
