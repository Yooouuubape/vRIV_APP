package com.example.ui.screens.swipes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipesScreen(
    repository: VryvRepository,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val allEvents by repository.events.collectAsState()

    // Filter only published and planned events (no drafts, moderation, completed or cancelled)
    val events = remember(allEvents) {
        allEvents.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    // Record pair of (index, wasBookmarkedDuringThisSwipe)
    val history = remember { mutableStateListOf<Pair<Int, Boolean>>() }

    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    val currentEvent = events.getOrNull(currentIndex)

    fun swipeRight() {
        if (currentEvent == null) return
        scope.launch {
            offsetX.animateTo(1000f, tween(250))
            val wasAlreadyBookmarked = currentEvent.isBookmarked
            if (!wasAlreadyBookmarked) {
                repository.toggleSaveEvent(currentEvent.id)
            }
            history.add(Pair(currentIndex, !wasAlreadyBookmarked))
            currentIndex++
            offsetX.snapTo(0f)
        }
    }

    fun swipeLeft() {
        if (currentEvent == null) return
        scope.launch {
            offsetX.animateTo(-1000f, tween(250))
            history.add(Pair(currentIndex, false))
            currentIndex++
            offsetX.snapTo(0f)
        }
    }

    fun undo() {
        if (history.isNotEmpty()) {
            val (prevIndex, wasSavedBySwipe) = history.removeAt(history.lastIndex)
            currentIndex = prevIndex
            val ev = events.getOrNull(prevIndex)
            if (ev != null && wasSavedBySwipe && ev.isBookmarked) {
                repository.toggleSaveEvent(ev.id)
            }
            scope.launch {
                offsetX.snapTo(0f)
            }
            repository.showToast("Предыдущая карточка восстановлена")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .padding(top = 12.dp, bottom = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Swipes Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Свайпы",
                    color = VryvTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Свайпай вправо чтобы сохранить",
                    color = VryvTextSecondary,
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = { undo() },
                enabled = history.isNotEmpty(),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VryvCard)
                    .testTag("swipes_undo_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "Отмена свайпа",
                    tint = if (history.isNotEmpty()) VryvLime else VryvTextMuted,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Main Card Stack or Empty
        if (currentEvent != null) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            ) {
                // Next background card preview
                val nextEvent = events.getOrNull(currentIndex + 1)
                if (nextEvent != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 14.dp, start = 8.dp, end = 8.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(VryvCard)
                    ) {
                        Image(
                            painter = painterResource(id = nextEvent.coverRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x990B0B0F))
                        )
                    }
                }

                // Foreground Interactive Card
                val rotation = (offsetX.value / 40f).coerceIn(-18f, 18f)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        .rotate(rotation)
                        .clip(RoundedCornerShape(28.dp))
                        .background(VryvCard)
                        .clickable { onEventClick(currentEvent.id) }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (offsetX.value > 250f) {
                                        swipeRight()
                                    } else if (offsetX.value < -250f) {
                                        swipeLeft()
                                    } else {
                                        scope.launch { offsetX.animateTo(0f) }
                                    }
                                },
                                onHorizontalDrag = { _, dragAmount ->
                                    scope.launch {
                                        offsetX.snapTo(offsetX.value + dragAmount)
                                    }
                                }
                            )
                        }
                        .testTag("swipe_card_${currentEvent.id}")
                ) {
                    Image(
                        painter = painterResource(id = currentEvent.coverRes),
                        contentDescription = currentEvent.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x66000000),
                                        Color(0xF00B0B0F)
                                    ),
                                    startY = 300f
                                )
                            )
                    )

                    // Swipe stamps
                    if (offsetX.value > 80f) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvLime.copy(alpha = 0.9f))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(text = "ИДУ / В ЗАКЛАДКИ", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    } else if (offsetX.value < -80f) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvPink.copy(alpha = 0.9f))
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(text = "ПРОПУСТИТЬ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    // Card Info Bottom
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(VryvPink)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentEvent.category.uppercase(),
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentEvent.title,
                            color = VryvTextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 26.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${currentEvent.dateDisplay} · ${currentEvent.timeDisplay}",
                            color = VryvLime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "${currentEvent.venueName} · ${currentEvent.address}",
                            color = VryvTextSecondary,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentEvent.priceString,
                                color = VryvTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "•",
                                color = VryvTextMuted
                            )
                            Text(
                                text = "${currentEvent.goingCount} идут",
                                color = VryvTextSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Control Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dislike button (Left)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(VryvCardElevated)
                        .clickable { swipeLeft() }
                        .testTag("swipes_dislike_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Пропустить",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Undo Button (Center)
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(VryvCard)
                        .clickable { undo() }
                        .testTag("swipes_center_undo_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay,
                        contentDescription = "Отменить",
                        tint = if (history.isNotEmpty()) VryvLime else VryvTextMuted,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Like / Save button (Right)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(VryvPink)
                        .clickable { swipeRight() }
                        .testTag("swipes_like_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Сохранить",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        } else {
            // Empty state when stack is exhausted
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Style,
                        contentDescription = null,
                        tint = VryvPink,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ты просмотрел все события!",
                        color = VryvTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Новые вечеринки и концерты появляются каждый день. Загляни в афишу или начни сначала.",
                        color = VryvTextSecondary,
                        fontSize = 13.5.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvLime)
                            .clickable {
                                currentIndex = 0
                                history.clear()
                                repository.showToast("Карточки сброшены. Приятного выбора!")
                            }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .testTag("swipes_restart_btn")
                    ) {
                        Text(
                            text = "Начать сначала",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
