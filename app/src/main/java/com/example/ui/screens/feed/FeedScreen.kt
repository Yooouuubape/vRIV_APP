package com.example.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.EventCardFeatured
import com.example.ui.components.EventCardHero
import com.example.ui.components.EventCardLiveRow
import com.example.ui.components.EventCardWide
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonVariant
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvGroupRow
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvMainHeader
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FeedScreen(
    repository: VryvRepository,
    onEventClick: (String) -> Unit,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val events by repository.events.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    val publicActiveEvents = remember(events) {
        events.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
    }

    var selectedCategory by remember { mutableStateOf("Для тебя") }
    val categories = listOf(
        "Для тебя",
        "Вечеринки",
        "Концерты",
        "Стендап",
        "Бары",
        "Выставки",
        "Бесплатно",
        "Сегодня",
        "Выходные",
        "Все"
    )

    var showFilterSheet by remember { mutableStateOf(false) }

    var appliedDateFilter by remember { mutableStateOf("Все") }
    var appliedBudgetFilter by remember { mutableStateOf("Все") }

    var tempDateFilter by remember { mutableStateOf("Все") }
    var tempBudgetFilter by remember { mutableStateOf("Все") }
    val tempFilterCategories = remember { mutableStateListOf("Концерты", "Вечеринки", "Стендап") }
    var onlyFreeToggle by remember { mutableStateOf(false) }
    var freeSpotsToggle by remember { mutableStateOf(true) }

    val filteredEvents = remember(publicActiveEvents, selectedCategory, appliedDateFilter, appliedBudgetFilter) {
        publicActiveEvents.filter { ev ->
            val matchCategory = when (selectedCategory) {
                "Для тебя", "Все" -> true
                "Сегодня" -> ev.dateCategory == "TODAY" || ev.dateDisplay.contains("Сегодня")
                "Выходные" -> ev.dateCategory == "WEEKEND" || ev.dateDisplay.contains("Сб") || ev.dateDisplay.contains("Вс")
                "Бесплатно" -> ev.priceByn == 0.0
                else -> ev.category.equals(selectedCategory, ignoreCase = true)
            }
            val matchDate = when (appliedDateFilter) {
                "Сегодня" -> ev.dateCategory == "TODAY" || ev.dateDisplay.contains("Сегодня")
                "Завтра" -> ev.dateCategory == "TOMORROW" || ev.dateDisplay.contains("Завтра")
                "Выходные" -> ev.dateCategory == "WEEKEND" || ev.dateDisplay.contains("Сб") || ev.dateDisplay.contains("Вс")
                else -> true
            }
            val matchBudget = when (appliedBudgetFilter) {
                "Бесплатно" -> ev.priceByn == 0.0
                "До 30 BYN" -> ev.priceByn <= 30.0
                "До 50 BYN" -> ev.priceByn <= 50.0
                else -> true
            }
            matchCategory && matchDate && matchBudget
        }
    }

    // "Для тебя" portrait carousel order matching Screenshots 1 & 2:
    // 1. Jazz Session в «Хулигане» (e_jazz)
    // 2. Открытый микрофон (e_standup_open)
    // 3. «Тихий свет» (e_gallery)
    val forYouEvents = remember(filteredEvents, publicActiveEvents) {
        val preferredIds = listOf("e_jazz", "e_standup_open", "e_gallery")
        val preferred = preferredIds.mapNotNull { id ->
            filteredEvents.find { it.id == id } ?: publicActiveEvents.find { it.id == id }
        }
        val rest = filteredEvents.filter { ev -> ev.id != "e_techno" && preferred.none { it.id == ev.id } }
        (preferred + rest).ifEmpty { publicActiveEvents.take(3) }
    }

    // "Прямо сейчас" live event matching Screenshot 2: Фуд-маркет на Октябрьской (e_foodmarket)
    val liveNowEvent = remember(publicActiveEvents) {
        publicActiveEvents.find { it.id == "e_foodmarket" } ?: publicActiveEvents.firstOrNull()
    }

    // "На выходных" wide landscape carousel matching Screenshot 2:
    // 1. Rooftop Sunset Party (e_rooftop)
    // 2. Северный Свет: осенний сет (e_concert)
    val weekendWideEvents = remember(publicActiveEvents) {
        val preferredIds = listOf("e_rooftop", "e_concert", "e_underground", "e_standup")
        val preferred = preferredIds.mapNotNull { id -> publicActiveEvents.find { it.id == id } }
        preferred.ifEmpty {
            publicActiveEvents.filter { it.dateCategory == "WEEKEND" || it.dateDisplay.contains("Сб") || it.dateDisplay.contains("Вс") }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvMainHeader(
            unreadNotificationsCount = unreadCount,
            onOpenSearch = onOpenSearch,
            onOpenNotifications = onOpenNotifications
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 108.dp)
        ) {
            // Date subtitle + "Куда сегодня?" display title (Screenshot 1)
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 12.dp)
                ) {
                    Text(
                        text = "Четверг, 22 октября",
                        color = VryvTextMuted,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Куда сегодня?",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1.1).sp,
                        lineHeight = 36.sp
                    )
                }
            }

            // Category chips bar (Screenshot 1)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(start = 20.dp, end = 8.dp)
                    ) {
                        items(categories) { category ->
                            val isSelected = category == selectedCategory
                            VryvChip(
                                text = category,
                                selected = isSelected,
                                onClick = { selectedCategory = category },
                                testTag = "category_chip_$category"
                            )
                        }
                    }

                    Box(modifier = Modifier.padding(end = 20.dp)) {
                        VryvIconButton(
                            icon = VryvIcons.Sliders,
                            onClick = {
                                tempDateFilter = appliedDateFilter
                                tempBudgetFilter = appliedBudgetFilter
                                showFilterSheet = true
                            },
                            tint = if (appliedDateFilter != "Все" || appliedBudgetFilter != "Все") VryvPink else VryvTextPrimary,
                            hasUnreadDot = appliedDateFilter != "Все" || appliedBudgetFilter != "Все",
                            testTag = "feed_filter_btn"
                        )
                    }
                }
            }

            // Active filters indicator banner
            if (appliedDateFilter != "Все" || appliedBudgetFilter != "Все") {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Фильтры: $appliedDateFilter · $appliedBudgetFilter (${filteredEvents.size})",
                            color = VryvLime,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Сбросить",
                            color = VryvPink,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable {
                                appliedDateFilter = "Все"
                                appliedBudgetFilter = "Все"
                                repository.showToast("Фильтры сброшены")
                            }
                        )
                    }
                }
            }

            // 1. Hero Featured Card (Screenshot 1: "Techno Room: Ночь 3")
            if (filteredEvents.isNotEmpty()) {
                item {
                    val featured = filteredEvents.first()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        EventCardHero(
                            event = featured,
                            onClick = { onEventClick(featured.id) },
                            onSaveToggle = { repository.toggleSaveEvent(featured.id) }
                        )
                    }
                }
            }

            // 2. "Для тебя" Horizontal Carousel of Portrait Cards (Screenshots 1 & 2)
            item {
                Spacer(modifier = Modifier.height(22.dp))
                SectionHeader(
                    title = "Для тебя",
                    actionText = "Все",
                    onSeeAll = { selectedCategory = "Для тебя" }
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(forYouEvents, key = { "foryou_${it.id}" }) { event ->
                        EventCardFeatured(
                            event = event,
                            onClick = { onEventClick(event.id) },
                            onSaveToggle = { repository.toggleSaveEvent(event.id) }
                        )
                    }
                }
            }

            // 3. "Прямо сейчас" with lime dot + Horizontal Live Row Card (Screenshot 2)
            if (liveNowEvent != null) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    SectionHeader(
                        title = "Прямо сейчас",
                        showLiveDot = true,
                        actionText = "На карте",
                        onSeeAll = onOpenMap
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        EventCardLiveRow(
                            event = liveNowEvent,
                            onClick = { onEventClick(liveNowEvent.id) }
                        )
                    }
                }
            }

            // 4. "На выходных" Horizontal Carousel of Wide Landscape Cards (Screenshot 2)
            if (weekendWideEvents.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    SectionHeader(
                        title = "На выходных",
                        actionText = "Все",
                        onSeeAll = { selectedCategory = "Выходные" }
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(weekendWideEvents, key = { "weekend_${it.id}" }) { event ->
                            EventCardWide(
                                event = event,
                                onClick = { onEventClick(event.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Filter Bottom Sheet
    if (showFilterSheet) {
        val previewCount = remember(publicActiveEvents, selectedCategory, tempDateFilter, tempBudgetFilter, onlyFreeToggle) {
            publicActiveEvents.count { ev ->
                val matchCat = selectedCategory == "Для тебя" || selectedCategory == "Все" || ev.category.equals(selectedCategory, ignoreCase = true)
                val matchD = when (tempDateFilter) {
                    "Сегодня" -> ev.dateCategory == "TODAY" || ev.dateDisplay.contains("Сегодня")
                    "Завтра" -> ev.dateCategory == "TOMORROW" || ev.dateDisplay.contains("Завтра")
                    "Выходные" -> ev.dateCategory == "WEEKEND" || ev.dateDisplay.contains("Сб") || ev.dateDisplay.contains("Вс")
                    else -> true
                }
                val effectiveBudget = if (onlyFreeToggle) "Бесплатно" else tempBudgetFilter
                val matchB = when (effectiveBudget) {
                    "Бесплатно" -> ev.priceByn == 0.0
                    "До 30 BYN" -> ev.priceByn <= 30.0
                    "До 50 BYN" -> ev.priceByn <= 50.0
                    else -> true
                }
                matchCat && matchD && matchB
            }
        }

        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF17171C),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF3A3A44))
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Фильтры",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Сбросить",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            tempDateFilter = "Все"
                            tempBudgetFilter = "Все"
                            onlyFreeToggle = false
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Когда",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Все", "Сегодня", "Завтра", "Выходные").forEach { d ->
                        VryvChip(
                            text = d,
                            selected = tempDateFilter == d,
                            onClick = { tempDateFilter = d }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Категории",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Концерты", "Вечеринки", "Бары", "Стендап", "Выставки", "Театр", "Кино", "Лекции").forEach { c ->
                        val isSel = tempFilterCategories.contains(c)
                        VryvChip(
                            text = c,
                            selected = isSel,
                            onClick = {
                                if (isSel) tempFilterCategories.remove(c) else tempFilterCategories.add(c)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Цена",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (tempBudgetFilter == "Все") "Любая" else tempBudgetFilter,
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Все", "Бесплатно", "До 30 BYN", "До 50 BYN").forEach { b ->
                        VryvChip(
                            text = b,
                            selected = tempBudgetFilter == b,
                            onClick = { tempBudgetFilter = b }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                VryvGroupList {
                    VryvGroupRow(
                        title = "Только бесплатные",
                        toggleState = onlyFreeToggle,
                        onToggleChange = {
                            onlyFreeToggle = it
                            if (it) tempBudgetFilter = "Бесплатно"
                        }
                    )
                    VryvGroupRow(
                        title = "Свободные места сейчас",
                        toggleState = freeSpotsToggle,
                        onToggleChange = { freeSpotsToggle = it },
                        showDivider = false
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                VryvButton(
                    text = "Показать события ($previewCount)",
                    onClick = {
                        appliedDateFilter = tempDateFilter
                        appliedBudgetFilter = if (onlyFreeToggle) "Бесплатно" else tempBudgetFilter
                        showFilterSheet = false
                        repository.showToast("Найдено мероприятий: $previewCount")
                    },
                    variant = VryvButtonVariant.PRIMARY,
                    testTag = "apply_filters_btn"
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    showLiveDot: Boolean = false,
    actionText: String? = null,
    onSeeAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (showLiveDot) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(VryvLime)
                )
            }
            Text(
                text = title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            )
        }
        if (actionText != null) {
            Text(
                text = actionText,
                color = VryvTextMuted,
                fontFamily = InterFontFamily,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onSeeAll() }
            )
        }
    }
}
