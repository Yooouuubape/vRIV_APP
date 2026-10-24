package com.example.ui.screens.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.EventCardFeatured
import com.example.ui.components.EventCardHero
import com.example.ui.components.VryvAvatarStack
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
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
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
    val user by repository.currentUser.collectAsState()
    val notifications by repository.notifications.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    val publicActiveEvents = remember(events) {
        events.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
    }

    var selectedCategory by remember { mutableStateOf("Все") }
    val categories = listOf("Все", "Сегодня", "Выходные", "Вечеринки", "Концерты", "Стендап", "Бары", "Выставки", "Бесплатно")

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
                "Все" -> true
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
            contentPadding = PaddingValues(bottom = 104.dp)
        ) {
            // Category chips bar + Filter icon button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 2.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(end = 8.dp)
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

            // 1. Hero Featured Card (screen_01 & homeP)
            if (filteredEvents.isNotEmpty()) {
                item {
                    val featured = filteredEvents.first()
                    SectionHeader(
                        title = "Подобрано под тебя",
                        subtitle = "По вкусам: ${user.interests.take(3).joinToString(", ")}",
                        actionText = "Твои интересы",
                        onSeeAll = {}
                    )
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
                        EventCardHero(
                            event = featured,
                            onClick = { onEventClick(featured.id) },
                            onSaveToggle = { repository.toggleSaveEvent(featured.id) }
                        )
                    }
                }
            }

            // 2. "Для тебя" / "Сегодня вечером" Horizontal Carousel
            item {
                Spacer(modifier = Modifier.height(10.dp))
                SectionHeader(
                    title = "Для тебя",
                    actionText = "Все ${forYouEvents.size}",
                    onSeeAll = {}
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
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

            // 3. "Твои друзья собираются" Card (homeP.png) + Map Banner
            item {
                Spacer(modifier = Modifier.height(14.dp))
                SectionHeader(
                    title = "Твои друзья собираются",
                    actionText = "Все",
                    onSeeAll = {}
                )
                val friendEvent = publicActiveEvents.getOrNull(1) ?: publicActiveEvents.firstOrNull()
                if (friendEvent != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                            .clickable { onEventClick(friendEvent.id) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.wine),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(58.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Дегустация натуральных вин",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Сб, 25 окт · 19:00 · Винный бар «Соболь»",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = 6.dp)
                            ) {
                                VryvAvatarStack(
                                    avatarResList = listOf(R.drawable.av1, R.drawable.av3, R.drawable.av4),
                                    size = 20.dp
                                )
                                Text(
                                    text = "Аня, Влад и ещё 2 идут",
                                    color = VryvPink,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        VryvButton(
                            text = if (friendEvent.isGoing) "✓ Иду" else "Иду",
                            onClick = { repository.toggleGoingEvent(friendEvent.id) },
                            variant = if (friendEvent.isGoing) VryvButtonVariant.SECONDARY else VryvButtonVariant.PRIMARY,
                            isSmall = true,
                            modifier = Modifier.width(76.dp)
                        )
                    }
                }
            }

            // 4. Spontaneous Map Banner
            item {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x1EC7FF28))
                        .border(1.dp, Color(0x47C7FF28), RoundedCornerShape(20.dp))
                        .clickable { onOpenMap() }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(VryvLime),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Pin,
                                contentDescription = null,
                                tint = VryvBg,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Прямо сейчас в городе",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Смотри вечеринки и бары на карте Минска",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = VryvIcons.ChevRight,
                        contentDescription = null,
                        tint = VryvLime,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // 5. Weekend selection ("Планы на выходные")
            if (upcomingWeekendEvents.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(
                        title = "Планы на выходные",
                        actionText = "Все",
                        onSeeAll = {}
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(upcomingWeekendEvents, key = { "weekend_${it.id}" }) { event ->
                            EventCardFeatured(
                                event = event,
                                onClick = { onEventClick(event.id) },
                                onSaveToggle = { repository.toggleSaveEvent(event.id) }
                            )
                        }
                    }
                }
            }

            // 6. Free events ("Бесплатно на выходных")
            if (freeEvents.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(
                        title = "Бесплатно на выходных",
                        actionText = "Все ${freeEvents.size}",
                        onSeeAll = {}
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(freeEvents, key = { "free_${it.id}" }) { event ->
                            EventCardFeatured(
                                event = event,
                                onClick = { onEventClick(event.id) },
                                onSaveToggle = { repository.toggleSaveEvent(event.id) }
                            )
                        }
                    }
                }
            }

            // 7. "Популярно рядом" compact rows (screen_01)
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader(
                    title = "Популярно рядом",
                    actionText = "На карте",
                    onSeeAll = onOpenMap
                )
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    filteredEvents.take(4).forEach { event ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(VryvCard)
                                .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                                .clickable { onEventClick(event.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = event.coverRes),
                                contentDescription = event.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = event.title,
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${event.dateDisplay}, ${event.timeDisplay} · ${event.venueName}",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 3.dp)
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(top = 6.dp)
                                ) {
                                    Text(
                                        text = event.priceString,
                                        color = if (event.priceByn == 0.0) VryvLime else VryvPink,
                                        fontFamily = InterFontFamily,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "· ${event.goingCount} идут",
                                        color = VryvTextSecondary,
                                        fontFamily = InterFontFamily,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (event.isBookmarked) VryvPink else Color(0xFF25252D))
                                    .clickable { repository.toggleSaveEvent(event.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = VryvIcons.Save,
                                    contentDescription = null,
                                    tint = if (event.isBookmarked) Color.White else VryvTextSecondary,
                                    modifier = Modifier.size(17.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Filter Bottom Sheet matching filter.png 1-to-1
    if (showFilterSheet) {
        val previewCount = remember(publicActiveEvents, selectedCategory, tempDateFilter, tempBudgetFilter, onlyFreeToggle) {
            publicActiveEvents.count { ev ->
                val matchCat = selectedCategory == "Все" || ev.category.equals(selectedCategory, ignoreCase = true)
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
    subtitle: String? = null,
    actionText: String? = null,
    onSeeAll: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.5.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
        if (actionText != null) {
            Text(
                text = actionText,
                color = VryvPink,
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onSeeAll() }
            )
        }
    }
}
