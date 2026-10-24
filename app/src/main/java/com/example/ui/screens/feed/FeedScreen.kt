package com.example.ui.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.EventStatus
import com.example.data.VryvEvent
import com.example.data.VryvRepository
import com.example.ui.components.EventCardFeatured
import com.example.ui.components.EventCardGrid
import com.example.ui.components.VryvMainHeader
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
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
    val user by repository.currentUser.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    // Active public events only (Exclude Drafts, Moderation, Cancelled, Completed)
    val publicActiveEvents = remember(events) {
        events.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
    }

    var selectedCategory by remember { mutableStateOf("Все") }
    val categories = listOf("Все", "Вечеринки", "Концерты", "Стендап", "Бары", "Выставки")

    var showFilterSheet by remember { mutableStateOf(false) }

    // Applied filters
    var appliedDateFilter by remember { mutableStateOf("Все") } // "Все", "Сегодня", "Завтра", "Выходные"
    var appliedBudgetFilter by remember { mutableStateOf("Все") } // "Все", "Бесплатно", "До 30 BYN", "До 50 BYN"

    // Temporary filters in bottom sheet
    var tempDateFilter by remember { mutableStateOf("Все") }
    var tempBudgetFilter by remember { mutableStateOf("Все") }

    val filteredEvents = remember(publicActiveEvents, selectedCategory, appliedDateFilter, appliedBudgetFilter) {
        publicActiveEvents.filter { ev ->
            val matchCategory = selectedCategory == "Все" || ev.category.equals(selectedCategory, ignoreCase = true)
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

    // Personalized "Для тебя" based on user's interests from onboarding/preferences!
    val forYouEvents = remember(publicActiveEvents, user.interests) {
        publicActiveEvents.filter { ev ->
            ev.isBookmarked || ev.genres.any { g -> user.interests.any { ui -> ui.equals(g, ignoreCase = true) } }
        }.ifEmpty { publicActiveEvents.take(4) }
    }

    val freeEvents = remember(publicActiveEvents) {
        publicActiveEvents.filter { it.priceByn == 0.0 }
    }

    val upcomingWeekendEvents = remember(publicActiveEvents) {
        publicActiveEvents.filter { it.dateCategory == "WEEKEND" || it.dateDisplay.contains("Сб") || it.dateDisplay.contains("Вс") }
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
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Category chips bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(end = 8.dp)
                    ) {
                        items(categories) { category ->
                            val isSelected = category == selectedCategory
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) VryvPink else VryvCard)
                                    .clickable { selectedCategory = category }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                                    .testTag("category_chip_$category")
                            ) {
                                Text(
                                    text = category,
                                    color = if (isSelected) Color.White else VryvTextSecondary,
                                    fontSize = 13.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Filter Button
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (appliedDateFilter != "Все" || appliedBudgetFilter != "Все") VryvPink else VryvCard)
                            .clickable {
                                tempDateFilter = appliedDateFilter
                                tempBudgetFilter = appliedBudgetFilter
                                showFilterSheet = true
                            }
                            .testTag("feed_filter_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Фильтры",
                            tint = if (appliedDateFilter != "Все" || appliedBudgetFilter != "Все") Color.White else VryvTextPrimary,
                            modifier = Modifier.size(18.dp)
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
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Фильтры: $appliedDateFilter · $appliedBudgetFilter (${filteredEvents.size})",
                            color = VryvLime,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Сбросить",
                            color = VryvPink,
                            fontSize = 12.sp,
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

            // 1. Featured Big Card
            if (filteredEvents.isNotEmpty()) {
                item {
                    val featured = filteredEvents.first()
                    Box(modifier = Modifier.padding(16.dp)) {
                        EventCardFeatured(
                            event = featured,
                            onClick = { onEventClick(featured.id) },
                            onSaveToggle = { repository.toggleSaveEvent(featured.id) }
                        )
                    }
                }
            }

            // 2. Personalization: "Для тебя" (Based on user.interests)
            item {
                SectionHeader(
                    title = "Для тебя",
                    subtitle = "По вкусам: ${user.interests.take(3).joinToString(", ")}",
                    onSeeAll = {}
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(forYouEvents, key = { it.id }) { event ->
                        EventCardGrid(
                            event = event,
                            onClick = { onEventClick(event.id) },
                            onSaveToggle = { repository.toggleSaveEvent(event.id) }
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // 3. Spontaneous Map Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x33C7FF28))
                        .clickable { onOpenMap() }
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(VryvLime),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NearMe,
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Прямо сейчас в городе",
                                    color = VryvTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Смотри вечеринки и бары на карте Минска",
                                    color = VryvTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            tint = VryvLime
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // 4. Weekend selection
            if (upcomingWeekendEvents.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Планы на выходные",
                        subtitle = "Суббота и воскресенье",
                        onSeeAll = {}
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(upcomingWeekendEvents, key = { it.id }) { event ->
                            EventCardGrid(
                                event = event,
                                onClick = { onEventClick(event.id) },
                                onSaveToggle = { repository.toggleSaveEvent(event.id) }
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(20.dp)) }

            // 5. Free events
            if (freeEvents.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "Вход свободный",
                        subtitle = "Выставки, маркеты и бесплатные тусовки",
                        onSeeAll = {}
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(freeEvents, key = { it.id }) { event ->
                            EventCardGrid(
                                event = event,
                                onClick = { onEventClick(event.id) },
                                onSaveToggle = { repository.toggleSaveEvent(event.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Filter Bottom Sheet (Functional and persistent)
    if (showFilterSheet) {
        val previewCount = remember(publicActiveEvents, selectedCategory, tempDateFilter, tempBudgetFilter) {
            publicActiveEvents.count { ev ->
                val matchCat = selectedCategory == "Все" || ev.category.equals(selectedCategory, ignoreCase = true)
                val matchD = when (tempDateFilter) {
                    "Сегодня" -> ev.dateCategory == "TODAY" || ev.dateDisplay.contains("Сегодня")
                    "Завтра" -> ev.dateCategory == "TOMORROW" || ev.dateDisplay.contains("Завтра")
                    "Выходные" -> ev.dateCategory == "WEEKEND" || ev.dateDisplay.contains("Сб") || ev.dateDisplay.contains("Вс")
                    else -> true
                }
                val matchB = when (tempBudgetFilter) {
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
            sheetState = rememberModalBottomSheetState(),
            containerColor = VryvCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Фильтры афиши",
                        color = VryvTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Сбросить",
                        color = VryvPink,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            tempDateFilter = "Все"
                            tempBudgetFilter = "Все"
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Дата события",
                    color = VryvTextSecondary,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Все", "Сегодня", "Завтра", "Выходные").forEach { d ->
                        val isSel = tempDateFilter == d
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) VryvPink else VryvBg)
                                .clickable { tempDateFilter = d }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(text = d, color = if (isSel) Color.White else VryvTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Бюджет",
                    color = VryvTextSecondary,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Все", "Бесплатно", "До 30 BYN", "До 50 BYN").forEach { b ->
                        val isSel = tempBudgetFilter == b
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSel) VryvLime else VryvBg)
                                .clickable { tempBudgetFilter = b }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(text = b, color = if (isSel) Color.Black else VryvTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(26.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvPink)
                        .clickable {
                            appliedDateFilter = tempDateFilter
                            appliedBudgetFilter = tempBudgetFilter
                            showFilterSheet = false
                            repository.showToast("Найдено мероприятий: $previewCount")
                        }
                        .testTag("apply_filters_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Показать события ($previewCount)",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    subtitle: String? = null,
    onSeeAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                color = VryvTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = VryvTextSecondary,
                    fontSize = 12.sp
                )
            }
        }
    }
}
