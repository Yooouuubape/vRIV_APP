package com.example.ui.screens.swipes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.VryvAvatarStack
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonVariant
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvSegmentedControl
import com.example.ui.components.VryvTag
import com.example.ui.components.VryvTagVariant
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
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

    val events = remember(allEvents) {
        allEvents.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
    }

    var selectedSegment by remember { mutableIntStateOf(0) }
    var currentIndex by remember { mutableIntStateOf(0) }
    val history = remember { mutableStateListOf<Pair<Int, Boolean>>() }

    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    val currentEvent = events.getOrNull(currentIndex)

    fun swipeRight() {
        if (currentEvent == null) return
        scope.launch {
            offsetX.animateTo(1000f, tween(220))
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
            offsetX.animateTo(-1000f, tween(220))
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
            .padding(top = 6.dp, bottom = 92.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header matching screen_02_свайп_подбор.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Подбор",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.7).sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VryvIconButton(
                    icon = VryvIcons.Undo,
                    onClick = { undo() },
                    tint = if (history.isNotEmpty()) VryvLime else VryvTextPrimary,
                    testTag = "swipes_undo_button"
                )
                VryvIconButton(
                    icon = VryvIcons.Sliders,
                    onClick = { repository.showToast("Фильтры подбора: все категории") }
                )
            }
        }

        // 3-segment control: Сегодня / Завтра / Выходные
        VryvSegmentedControl(
            options = listOf("Сегодня", "Завтра", "Выходные"),
            selectedIndex = selectedSegment,
            onOptionSelected = { selectedSegment = it },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        if (currentEvent != null) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                // Back card peeking behind
                val nextEvent = events.getOrNull(currentIndex + 1)
                if (nextEvent != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(28.dp))
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
                                .background(Color(0x880B0B0F))
                        )
                    }
                }

                // Foreground swipe card
                val rotation = (offsetX.value / 40f).coerceIn(-16f, 16f)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                        .rotate(rotation)
                        .clip(RoundedCornerShape(28.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(28.dp))
                        .clickable { onEventClick(currentEvent.id) }
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onDragEnd = {
                                    if (offsetX.value > 220f) {
                                        swipeRight()
                                    } else if (offsetX.value < -220f) {
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

                    // Gradient overlay matching prototype
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0.0f to Color(0x400B0B0F),
                                    0.22f to Color(0x0D0B0B0F),
                                    0.55f to Color(0x590B0B0F),
                                    0.85f to Color(0xF50B0B0F),
                                    1.0f to VryvBg
                                )
                            )
                    )

                    // Top tags + Counter pill (1 / 24)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            VryvTag(
                                text = currentEvent.category,
                                variant = VryvTagVariant.PINK
                            )
                            VryvTag(
                                text = "900 м",
                                variant = VryvTagVariant.DARK
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(28.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x9E14141A))
                                .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(14.dp))
                                .padding(horizontal = 11.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${currentIndex + 1} / ${events.size}",
                                color = Color(0xFFDADAE4),
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Bottom card info above the 3 circular buttons
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(start = 22.dp, end = 22.dp, bottom = 104.dp)
                    ) {
                        Text(
                            text = currentEvent.title,
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.6).sp,
                            lineHeight = 29.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "${currentEvent.dateDisplay}, ${currentEvent.timeDisplay} · ${currentEvent.venueName} · ${currentEvent.address}",
                            color = Color(0xFFD0D0DA),
                            fontFamily = InterFontFamily,
                            fontSize = 13.5.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentEvent.description,
                            color = Color(0xFFA8A8B6),
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            lineHeight = 18.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                VryvAvatarStack(
                                    avatarResList = listOf(R.drawable.av3, R.drawable.av4, R.drawable.av5),
                                    size = 24.dp
                                )
                                Text(
                                    text = "${currentEvent.goingCount} идут",
                                    color = Color(0xFFC8C8D4),
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp
                                )
                            }

                            Text(
                                text = currentEvent.priceString,
                                color = if (currentEvent.priceByn == 0.0) VryvLime else VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.4).sp
                            )
                        }
                    }

                    // Bottom 3 circular buttons inside card (66dp X, 52dp Save/Undo, 66dp Pink Heart)
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 22.dp),
                        horizontalArrangement = Arrangement.spacedBy(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: 66dp Pass (X)
                        Box(
                            modifier = Modifier
                                .size(66.dp)
                                .clip(CircleShape)
                                .background(Color(0xF022222B))
                                .border(1.dp, Color(0x1FFFFFFF), CircleShape)
                                .clickable { swipeLeft() }
                                .testTag("swipes_dislike_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Close,
                                contentDescription = "Пропустить",
                                tint = VryvTextPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Center: 52dp Bookmark / Undo
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color(0xEB1C1C23))
                                .border(1.dp, Color(0x14FFFFFF), CircleShape)
                                .clickable {
                                    if (history.isNotEmpty()) {
                                        undo()
                                    } else {
                                        repository.toggleSaveEvent(currentEvent.id)
                                    }
                                }
                                .testTag("swipes_center_undo_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Save,
                                contentDescription = "В закладки",
                                tint = if (currentEvent.isBookmarked) VryvPink else VryvTextPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Right: 66dp Like (Pink Heart)
                        Box(
                            modifier = Modifier
                                .size(66.dp)
                                .clip(CircleShape)
                                .background(VryvPink)
                                .clickable { swipeRight() }
                                .testTag("swipes_like_btn"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Heart,
                                contentDescription = "Интересно",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
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
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0x26FF1D8E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Spark,
                            contentDescription = null,
                            tint = VryvPink,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ты просмотрел все события!",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Новые вечеринки и концерты появляются каждый день. Загляни в афишу или начни сначала.",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 13.5.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    VryvButton(
                        text = "Начать сначала",
                        onClick = {
                            currentIndex = 0
                            history.clear()
                            repository.showToast("Карточки сброшены. Приятного выбора!")
                        },
                        variant = VryvButtonVariant.LIME,
                        testTag = "swipes_restart_btn"
                    )
                }
            }
        }
    }
}
