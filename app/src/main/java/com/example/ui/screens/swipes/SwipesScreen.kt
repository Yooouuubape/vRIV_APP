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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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

    // Order swipe deck so "Katrin Velvet b2b Nox" (e_katrin) is first, matching Screenshot 3 1-to-1
    val events = remember(allEvents) {
        val active = allEvents.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
        val katrin = active.find { it.id == "e_katrin" }
        if (katrin != null) {
            listOf(katrin) + active.filter { it.id != "e_katrin" }
        } else {
            active
        }
    }

    // "Завтра" (index 1) is selected by default in Screenshot 3
    var selectedSegment by remember { mutableIntStateOf(1) }
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
        // Header matching Screenshot 3 ("Свайпы" + Undo & Sliders buttons)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Свайпы",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
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
            // Card stack container
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
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

                // Foreground swipe card matching Screenshot 3 1-to-1
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

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0.0f to Color(0x260B0B0F),
                                    0.35f to Color(0x140B0B0F),
                                    0.62f to Color(0x990B0B0F),
                                    0.85f to Color(0xEB0B0B0F),
                                    1.0f to Color(0xFA0B0B0F)
                                )
                            )
                    )

                    // Top row: Lime "Совпадение 92%" pill on left + "1 / 24" pill on right
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .height(28.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(VryvLime)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Совпадение ${currentEvent.matchPercent}%",
                                color = VryvBg,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.2).sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(28.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0x9E0B0B0F))
                                .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(14.dp))
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${currentIndex + 1} / 24",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Bottom card content matching Screenshot 3 1-to-1
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        // Genre + Age dark translucent pills ("Хаус", "Мелодик-техно", "18+")
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            val tags = (currentEvent.genres.take(2) + listOf(currentEvent.ageLimit)).distinct()
                            tags.forEach { tagText ->
                                Box(
                                    modifier = Modifier
                                        .height(26.dp)
                                        .clip(RoundedCornerShape(13.dp))
                                        .background(Color(0x991A1A22))
                                        .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(13.dp))
                                        .padding(horizontal = 11.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tagText,
                                        color = VryvTextPrimary,
                                        fontFamily = InterFontFamily,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentEvent.title,
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.8).sp,
                            lineHeight = 31.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Clock + Date/Time & Pin + Venue · Address
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = VryvIcons.Clock,
                                    contentDescription = null,
                                    tint = VryvTextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = currentEvent.pillTimeText,
                                    color = Color(0xFFD0D0DA),
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = VryvIcons.Pin,
                                    contentDescription = null,
                                    tint = VryvTextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${currentEvent.venueName} · ${currentEvent.address}",
                                    color = Color(0xFFD0D0DA),
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        HorizontalDivider(
                            color = Color(0x1FFFFFFF),
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        // Footer row: Friend avatars + "Идут 2 друга" on left, "от 35 BYN" on right
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
                                    avatarResList = listOf(R.drawable.av1, R.drawable.av2),
                                    size = 26.dp
                                )
                                val friendsCount = currentEvent.friendsGoingCount.coerceAtLeast(2)
                                Text(
                                    text = "Идут $friendsCount друга",
                                    color = Color(0xFFD0D0DA),
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Text(
                                text = currentEvent.priceString,
                                color = if (currentEvent.priceByn == 0.0) VryvLime else VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.3).sp
                            )
                        }
                    }
                }
            }

            // 3 Circular Action Buttons BELOW the card on the dark background (Screenshot 3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left: 64dp Pass (X)
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(VryvCard)
                        .border(1.dp, VryvLine, CircleShape)
                        .clickable { swipeLeft() }
                        .testTag("swipes_dislike_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Close,
                        contentDescription = "Пропустить",
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Center: 52dp Bookmark / Undo
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(VryvCard)
                        .border(1.dp, VryvLine, CircleShape)
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
                        imageVector = VryvIcons.Bookmark,
                        contentDescription = "В закладки",
                        tint = if (currentEvent.isBookmarked) VryvPink else VryvTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Right: 64dp Like (Pink circle with filled white heart)
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(VryvPink)
                        .clickable { swipeRight() }
                        .testTag("swipes_like_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.HeartFill,
                        contentDescription = "Нравится",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
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
