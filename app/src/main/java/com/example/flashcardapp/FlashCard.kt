package com.example.flashcardapp

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.example.flashcardapp.ui.CardFace
import com.example.flashcardapp.ui.FlipCard
import com.example.flashcardapp.ui.theme.statusBorderColor
import com.example.flashcardapp.ui.theme.statusColor
import com.wajahatkarim.flippable.FlippableController
import java.util.UUID
import kotlin.math.roundToInt

@Composable
fun FlashCardsAndTabs(
    card: MutableState<FlashCard>,
    flashCard: List<FlashCard>,
    selectedTrapeziums: List<TrapeziumItem>,
    onSwipe: (FlashCard) -> Unit,
    onCategoryChanged: (String) -> Unit,
) {
    val changeAlpha = animateFloatAsState(
        targetValue = if (card.value.lastCard) 1f else 0.4f,
        animationSpec = tween(400)
    )
    Box(
        modifier = Modifier
            .clickable(enabled = false) {}
            .height((LocalConfiguration.current.screenHeightDp - LocalConfiguration.current.screenHeightDp * 0.2).dp)
            .padding(start = 36.dp, end = 45.dp)
            .border(
                width = 8.dp,
                color = card.value.color,
                shape = RoundedCornerShape(25.dp)
            )
            .alpha(changeAlpha.value)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val painter = painterResource(card.value.image)
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
            text = card.value.question,
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
    Row(modifier = Modifier.padding(start = 36.dp, end = 8.dp)) {
        Box(
            modifier = Modifier
                .weight(1f)
        )
        {
            flashCard.ToFlashCardItems(onSwipe)
        }
        TrapeziumShapeList(onCategoryChanged, selectedTrapeziums)
    }
}

@Composable
fun NextCategoryBox(card: FlashCard) {
    Box(
        modifier = Modifier
            .padding(start = 36.dp, end = 8.dp)
            .clickable(enabled = false) {}
            .alpha(0.2f)
            .border(
                width = 8.dp,
                color = card.color,
                shape = RoundedCornerShape(25.dp)
            ),
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
}

@Composable
fun List<FlashCard>.ToFlashCardItems(
    onSwipe: (FlashCard) -> Unit,
) {
    this.forEachIndexed { i, fc ->
        FlashCardItem(
            CardFace.Front,
            card = fc,
            offset = (size.minus(i + 1) * 40).dp,
            alpha = 0.25f
        ) { card ->
            onSwipe(card)
        }
    }
}

@OptIn(ExperimentalSwipeableCardApi::class, ExperimentalMaterialApi::class)
@Composable
fun FlashCardItem(
    cardFace: CardFace,
    card: FlashCard,
    offset: Dp,
    alpha: Float,
    onSwipe: (FlashCard) -> Unit,
) {
    val offset = animateDpAsState(
        targetValue = if (card.isSelected) 0.dp else offset,
        animationSpec = tween(400)
    )
    val alphaX = animateFloatAsState(
        targetValue = if (card.isSelected) 1f else alpha,
        animationSpec = tween(400)
    )

    var cardFace by remember(card) {
        mutableStateOf(cardFace)
    }

    val screenDensity = LocalConfiguration.current.densityDpi.div(160F)
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var flippableController = remember(card.id) { FlippableController() }
    /*val state = remember(card.id) {
        val a = SwipeableCardState(maxWidth, maxHeight)
        a
    }*/

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height((LocalConfiguration.current.screenHeightDp - LocalConfiguration.current.screenHeightDp * 0.2).dp)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .offset(y = offset.value)
            .pointerInput(card) {
                detectVerticalDragGestures(
                    onDragStart = {
                    },
                    onDragEnd = { onSwipe(card) },
                    onVerticalDrag = { change, amount ->
                        change.consume()
                        offsetX += amount
                        offsetY += amount
                    })
            }
            .graphicsLayer(
                translationX = offsetX,
                translationY = offsetY,
                rotationZ = offsetX / 10,
            )
            /*.pointerInput(card) {
                detectVerticalDragGestures(
                    onDragStart = { },
                    onDragEnd = {
                        val coercedOffset = Offset(offsetX, offsetY)
                            .coerceIn(
                                listOf(Direction.Left, Direction.Down, Direction.Right),
                                maxHeight = maxHeight,
                                maxWidth = maxWidth
                            )

                        *//*if (hasNotTravelledEnough(maxWidth, maxHeight, coercedOffset)) {
                            //if swipe cancel
                        } else {
                            val horizontalTravel = abs(offsetX)
                            val verticalTravel = abs(offsetY)

                            if (horizontalTravel > verticalTravel) {
                                if (offsetX > 0) {
                                    state.swipe(Direction.Right)
                                    onSwiped(Direction.Right)
                                } else {
                                    state.swipe(Direction.Left)
                                    onSwiped(Direction.Left)
                                }
                            } else {
                                if (state.offset.targetValue.y < 0) {
                                    state.swipe(Direction.Up)
                                    onSwiped(Direction.Up)
                                } else {
                                    state.swipe(Direction.Down)
                                    onSwiped(Direction.Down)
                                }
                            }
                        }*//*
                        onSwipe(card)
                    },
                    onDragCancel = { },
                    onVerticalDrag = { change, dragAmount ->
                        val newValue = Offset(
                            x = dragAmount.coerceIn(-maxWidth, maxWidth),
                            y = dragAmount.coerceIn(-maxHeight, maxHeight)
                        )
                        if (change.positionChange() != Offset.Zero) change.consume()
                        offsetX = newValue.x
                        offsetY = newValue.y
                    }
                )
            }
            .graphicsLayer(
                translationX = offsetX,
                translationY = offsetY,
                rotationZ = (offsetX / 60).coerceIn(-40f, 40f),
            )*/
            /*.swipableCard(
                state = state,
                onSwiped = {
                    onSwipe(card)
                },
                onSwipeCancel = {
                    println("The swiping was cancelled")
                }
            )*/
            .alpha(alphaX.value)
    ) {
        FlipCard(
            cardFace = cardFace,
            onClick = { cardFace = cardFace.next },
            modifier = Modifier
                .fillMaxSize(),
            front = {
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
            back = {
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
        )
        /* Flippable(
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

             flipController = flippableController,
             flipDurationMs = 800,
             flipAnimationType = FlipAnimationType.HORIZONTAL_CLOCKWISE,
             flipOnTouch = card.isSelected,
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
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        border = BorderStroke(1.dp, statusBorderColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            if (correctAnswer) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = text,
                        modifier = Modifier
                            .align(Alignment.Center),
                        style = TextStyle(fontSize = 14.sp),
                        color = contentColor
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = text,
                        modifier = Modifier
                            .align(Alignment.Center),
                        style = TextStyle(fontSize = 14.sp),
                        color = contentColor
                    )
                }
            }
        }

        if (correctAnswer) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier.wrapContentSize()
                ) {
                    Image(
                        painter = painterResource(imageResId),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 20.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
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
    var lastCard: Boolean = false,
    val id: String = UUID.randomUUID().toString(),
)

data class FlashCards(
    val list:FlashCard,
    val direction: String = "Center"
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
            .wrapContentHeight()
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







