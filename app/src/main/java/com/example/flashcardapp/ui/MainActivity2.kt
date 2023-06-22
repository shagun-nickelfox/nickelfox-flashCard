package com.example.flashcardapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.flashcardapp.ui.theme.FlashCardAppTheme
import java.util.UUID

class MainActivity2 : ComponentActivity() {

    private val flashCardsList = mutableStateListOf<FlashCardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        flashCardsList.addAll(
            listOf(
                FlashCardItem(currentlyOnTop = true, color = Color.Red),
                FlashCardItem(currentlyOnTop = false, color = Color.Green),
                FlashCardItem(currentlyOnTop = false, color = Color.Blue)
            )
        )

        setContent {
            FlashCardAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FlashCards(flashCardsList.toList())
                }
            }
        }
    }

    @Composable
    fun FlashCards(list: List<FlashCardItem>) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(24.dp), contentAlignment = Alignment.Center
        ) {
            list.take(3).forEachIndexed { index, flashCardItem ->
                val bottomPadding = when (index) {
                    0 -> 0.dp
                    1 -> 50.dp
                    else -> 100.dp
                }
                val alpha = when (index) {
                    0 -> 0.1F
                    1 -> 0.3F
                    else -> 1F
                }
                FlashCard(flashCardItem, bottomPadding, alpha)
            }
        }
    }

    @Composable
    fun FlashCard(flashCardItem: FlashCardItem, bottomPadding: Dp, alpha: Float) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding)
                .pointerInput(Unit) {
                    detectTapGestures {
                        flashCardsList.remove(flashCardItem)
                    }
                }
                .background(
                    color = flashCardItem.color.copy(alpha = alpha),
                    shape = RoundedCornerShape(24.dp)
                )
        ) {}
    }

    @Preview(showBackground = true)
    @Composable
    fun FlashCardsPreview() {
        val flashCardsList = remember { mutableStateListOf<FlashCardItem>() }

        flashCardsList.clear()

        flashCardsList.addAll(
            listOf(
                FlashCardItem(currentlyOnTop = true, color = Color.Red),
                FlashCardItem(currentlyOnTop = false, color = Color.Green),
                FlashCardItem(currentlyOnTop = false, color = Color.Blue)
            )
        )

        FlashCards(list = flashCardsList)
    }
}

data class FlashCardItem(
    val id: String = UUID.randomUUID().toString(),
    val currentlyOnTop: Boolean = false,
    val color: Color
)