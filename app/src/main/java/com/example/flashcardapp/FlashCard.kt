package com.example.flashcardapp

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.SwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.example.flashcardapp.ui.theme.statusBorderColor
import com.example.flashcardapp.ui.theme.statusColor
import com.wajahatkarim.flippable.FlipAnimationType
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import java.util.UUID

@Composable
fun FlashCardsAndTabs(
    model: Lazy<FlashCardViewModel>,
    flashCard: List<FlashCard>,
    selectedTrapeziums: List<TrapeziumItem>,
    onSwipe: (FlashCard) -> Unit,
    onCategoryChanged: (String) -> Unit,
) {
    Row(modifier = Modifier.padding(start = 36.dp, end = 8.dp)) {
        Box(modifier = Modifier.weight(1f))
        {
            model.value.flashCards.ToFlashCardItems(onSwipe)
            //flashCard.ToFlashCardItems(onSwipe)
        }
        TrapeziumShapeList(onCategoryChanged, selectedTrapeziums)
    }
}

@Composable
fun List<FlashCard>.ToFlashCardItems(onSwipe: (FlashCard) -> Unit) {
    Log.d("CardList", this.toList().joinToString())
    this.forEachIndexed { i, fc ->
        FlashCardItem(
            card = fc,
            offset = (size.minus(i + 1) * 40).dp,
            alpha = 0.1f
        ) { card ->
            onSwipe(card)
        }
    }
}

@OptIn(ExperimentalSwipeableCardApi::class)
@Composable
fun FlashCardItem(
    card: FlashCard,
    offset: Dp,
    alpha: Float,
    onSwipe: (FlashCard) -> Unit,
) {
    val offsetY = animateDpAsState(
        targetValue = if (card.isSelected) 0.dp else offset,
        animationSpec = tween(400)
    )
    val alphaX = animateFloatAsState(
        targetValue = if (card.isSelected) 1f else alpha,
        animationSpec = tween(400)
    )
    val screenDensity = LocalConfiguration.current.densityDpi.div(160F)

    val maxWidth = LocalConfiguration.current.screenWidthDp.dp.value.times(screenDensity)
    val maxHeight = LocalConfiguration.current.screenHeightDp.dp.value.times(screenDensity)

    val state = remember(card.id) {
        val a = SwipeableCardState(maxWidth, maxHeight)
        a
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp - LocalConfiguration.current.screenHeightDp * 0.2).dp)
            .offset(y = offsetY.value)
            .clickable { onSwipe(card) }
            /*.swipableCard(
                state = state,
                onSwiped = {
                    onSwipe(card)
                },
                onSwipeCancel = {
                    println("The swiping was cancelled")
                }
            )*/
            .alpha(alphaX.value),
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 8.dp,
                    color = card.color,
                    shape = RoundedCornerShape(25.dp)
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val painter = painterResource(card.image)
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillHeight
            )

            Text(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp),
                fontSize = 36.sp,
                text = "Flash Cards",
                color = Color.White
            )

            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(start = 16.dp, end = 16.dp),
                fontSize = 24.sp,
                lineHeight = 40.sp,
                text = card.question,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            Text(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp),
                fontSize = 16.sp,
                text = "Tap to flip",
                textDecoration = TextDecoration.Underline,
                color = Color.Yellow
            )
        }
        /*Flippable(
            frontSide = {
                Box(
                    modifier = Modifier
                        .border(
                            width = 8.dp,
                            color = card.color,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val painter = painterResource(card.image)
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                    )

                    Text(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 40.dp),
                        fontSize = 36.sp,
                        text = "Flash Cards",
                        color = Color.White
                    )

                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(start = 16.dp, end = 16.dp),
                        fontSize = 24.sp,
                        lineHeight = 40.sp,
                        text = card.question,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 40.dp),
                        fontSize = 16.sp,
                        text = "Tap to flip",
                        textDecoration = TextDecoration.Underline,
                        color = Color.Yellow
                    )
                }
            },

            backSide = {
                Box(
                    modifier = Modifier
                        .border(
                            width = 8.dp,
                            color = card.color,
                            shape = RoundedCornerShape(25.dp)
                        )
                        .fillMaxSize()
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    val painter = painterResource(card.image)
                    Image(
                        modifier = Modifier
                            .fillMaxSize(),
                        painter = painter,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                    )
                    Column {
                        Column {
                            repeat(4) {
                                RoundedCornerChip(
                                    correctAnswer = it == 0,
                                    contentColor = if (0 == it) Color.Black else Color.White,
                                    imageResId = R.drawable.cooper_credit,
                                    backgroundColor = if (0 == it) statusColor else if (3 == it) Color.Red else Color.Transparent,
                                    text = card.answers[it]
                                )
                            }
                        }
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 55.dp),
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 14.sp,
                            text = "Tap to flip",
                            textDecoration = TextDecoration.Underline,
                            color = statusBorderColor
                        )
                    }
                }
            },

            flipController = rememberFlipController(),
            flipDurationMs = 800,
            flipAnimationType = FlipAnimationType.HORIZONTAL_CLOCKWISE,
            flipOnTouch = card.isSelected
        )*/
    }
}

@Composable
fun RoundedCornerChip(
    correctAnswer: Boolean,
    contentColor: Color,
    imageResId: Int,
    backgroundColor: Color,
    text: String,
) {
    val shape = RoundedCornerShape(16)
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(60.dp)
            .background(backgroundColor)
            .border(shape = shape, width = 1.dp, color = statusBorderColor)
    ) {
        if (correctAnswer) {
            Row {
                Text(
                    text = text,
                    modifier = Modifier.weight(0.1f),
                    style = TextStyle(fontSize = 14.sp),
                    color = contentColor
                )
                Image(
                    painter = painterResource(imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .wrapContentSize()
                        .weight(1f),
                    contentScale = ContentScale.FillBounds
                )
            }
        } else {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                style = TextStyle(fontSize = 14.sp),
                color = contentColor
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

data class FlashCard(
    val image: Int,
    val question: String,
    val answers: List<String>,
    val color: Color,
    var isSelected: Boolean,
    val category: String,
    val id: String = UUID.randomUUID().toString(),
)

@Composable
fun TrapeziumShapeList(
    onCategoryChanged: (String) -> Unit,
    allTabs: List<TrapeziumItem>,
) {
    Column(
        modifier = Modifier
            .padding(top = 50.dp)
    ) {
        allTabs.forEachIndexed { index, item ->
            TrapeziumShapeItem(
                item = item,
                isSelected = item.isSelected,
                onCategoryChanged = {
                    onCategoryChanged(it)
                }
                /*item = item,
                isSelected = selectedTrapeziums.contains(index),
                onCategoryChanged = {
                    if (!selectedTrapeziums.contains(index)) {
                        selectedTrapeziums.clear()
                        selectedTrapeziums.add(index)
                    }
                    onCategoryChanged(it)
                }*/
            )
        }
    }
}

@Composable
fun TrapeziumShapeItem(
    item: TrapeziumItem,
    isSelected: Boolean,
    onCategoryChanged: (String) -> Unit,
) {
    val height = animateDpAsState(
        targetValue = if (isSelected) 135.dp else 70.dp,
        animationSpec = tween(600)
    )
    Box(
        modifier = Modifier
            .clickable(onClick = { onCategoryChanged(item.text) })
            .width(36.dp)
            .height(height = height.value)
    ) {
        DrawTrapezium(item.color)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(item.image),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
            if (isSelected) {
                Column(
                    modifier = Modifier
                        .rotate(90f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        modifier = Modifier,
                        style = TextStyle(
                            fontSize = 10.sp,
                            color = Color.White
                        ),
                        text = item.text
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}

data class TrapeziumItem(
    val text: String,
    val image: Int,
    val color: Color,
    var isSelected: Boolean = false,
)

@Composable
fun DrawTrapezium(color: Color) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(
                RoundedCornerShape(
                    topStart = CornerSize(0.dp),
                    topEnd = CornerSize(30.dp),
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(30.dp)
                )
            )
            .background(color)
    )
    /*Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val path = Path().apply {
            moveTo(size.width, size.height * 3 / 4)
            lineTo(size.width, size.height / 4)
            lineTo(0f, 0f)
            lineTo(0f, size.height)
            close()
        }

        drawPath(
            path = path,
            color = color
        )
    }*/
}







