package com.example.ui.screens.profile

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.components.EventCardGrid
import com.example.ui.components.VryvBadgeStyle
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonVariant
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvOptionCard
import com.example.ui.components.VryvSegmentedControl
import com.example.ui.components.VryvStatusPill
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    repository: VryvRepository,
    onEditProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onEventClick: (String) -> Unit,
    onSwitchRoleClick: () -> Unit,
    onOpenDirectChat: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val user by repository.currentUser.collectAsState()
    val events by repository.events.collectAsState()
    val companions by repository.companions.collectAsState()

    var selectedSection by remember { mutableStateOf("Планы") }
    val sections = listOf("Планы", "Сохранённое", "Друзья")

    var showSortSheet by remember { mutableStateOf(false) }
    var selectedSort by remember { mutableStateOf("По дате проведения") }
    var showInterestsSheet by remember { mutableStateOf(false) }

    val savedEvents = remember(events, selectedSort) {
        val base = events.filter { it.isBookmarked }
        when (selectedSort) {
            "Сначала дешевле" -> base.sortedBy { it.priceByn }
            else -> base
        }
    }
    val plannedEvents = remember(events) { events.filter { it.isGoing } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Top Header matching prof.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSwitchRoleClick() }
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .testTag("role_switcher_header")
            ) {
                Text(
                    text = "@${user.username}",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = VryvIcons.ChevDown,
                    contentDescription = "Сменить режим",
                    tint = VryvTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VryvIconButton(
                    icon = VryvIcons.Share,
                    onClick = { repository.showToast("Ссылка на твой профиль скопирована") },
                    contentDescription = "Поделиться"
                )
                VryvIconButton(
                    icon = VryvIcons.Gear,
                    onClick = onSettingsClick,
                    contentDescription = "Настройки",
                    testTag = "profile_settings_button"
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 104.dp)
        ) {
            // User Header Info (prof.png)
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = user.avatarRes),
                            contentDescription = user.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                                .border(2.5.dp, VryvPink, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = user.name,
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = VryvIcons.Pin,
                                    contentDescription = null,
                                    tint = VryvPink,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${user.city} · На ВРЫВе с 2025",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user.bio,
                        color = Color(0xFFD4D4DE),
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Interests chips
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        user.interests.take(4).forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(VryvCard)
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag,
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Stats Cards Row: 86 Друзей / 17 Сохранено / 48 Посетил
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(count = "${companions.count { it.isFriend }}", label = "Друзей", modifier = Modifier.weight(1f))
                        StatCard(count = "${savedEvents.size}", label = "Сохранено", modifier = Modifier.weight(1f))
                        StatCard(count = "${plannedEvents.size}", label = "В планах", modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Edit Profile Button + Interests Sliders Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .clip(RoundedCornerShape(21.dp))
                                .background(VryvCardElevated)
                                .clickable { onEditProfileClick() }
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Редактировать профиль",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        VryvIconButton(
                            icon = VryvIcons.Sliders,
                            onClick = { showInterestsSheet = true },
                            contentDescription = "Мои интересы"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Business Cabinet Banner matching prof.png
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color(0x1AC7FF28))
                            .border(1.dp, Color(0x47C7FF28), RoundedCornerShape(18.dp))
                            .clickable { repository.setBusinessMode(true) }
                            .padding(14.dp)
                            .testTag("banner_vryv_business"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0x2EC7FF28)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = VryvIcons.Store,
                                    contentDescription = null,
                                    tint = VryvLime,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "ВРЫВ для бизнеса",
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Управление заведением Loft 13",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Icon(
                            imageVector = VryvIcons.ChevRight,
                            contentDescription = null,
                            tint = VryvLime,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Segmented Tabs: Планы, Сохранённое, Друзья
                    VryvSegmentedControl(
                        options = sections,
                        selectedIndex = sections.indexOf(selectedSection).coerceAtLeast(0),
                        onOptionSelected = { selectedSection = sections[it] }
                    )
                }
            }

            // Tab Content
            when (selectedSection) {
                "Планы" -> {
                    item {
                        PlansCalendarSection(
                            events = events,
                            onEventClick = onEventClick
                        )
                    }
                }

                "Сохранённое" -> {
                    if (savedEvents.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "В сохранённом пока ничего нет",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Сохранено: ${savedEvents.size} событий",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                VryvChip(
                                    text = selectedSort,
                                    selected = false,
                                    onClick = { showSortSheet = true },
                                    trailingChevron = true
                                )
                            }
                        }
                        // 2-column grid matching saved.png
                        val rows = savedEvents.chunked(2)
                        items(rows) { rowEvents ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                rowEvents.forEach { event ->
                                    EventCardGrid(
                                        event = event,
                                        onClick = { onEventClick(event.id) },
                                        onSaveToggle = { repository.toggleSaveEvent(event.id) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                if (rowEvents.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                "Друзья" -> {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
                            Text(
                                text = "Мои друзья · ${companions.size}",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                    items(companions) { friend ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 5.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(VryvCard)
                                .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                                .clickable { onOpenDirectChat(friend.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = friend.avatarRes),
                                contentDescription = friend.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = friend.name,
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "@${friend.username} · ${friend.sharedTags.joinToString()}",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .height(32.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(VryvCardElevated)
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Чат",
                                    color = VryvPink,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Sort Sheet (sort.png)
    if (showSortSheet) {
        val sortOptions = listOf(
            "По дате проведения" to "Сначала ближайшие события",
            "По дате сохранения" to "Сначала недавно добавленные",
            "Сначала дешевле" to "От бесплатных к более дорогим",
            "Поблизости" to "Ближе к твоему району"
        )
        ModalBottomSheet(
            onDismissRequest = { showSortSheet = false },
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
                Text(
                    text = "Сортировка сохранённого",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    sortOptions.forEach { (title, sub) ->
                        VryvOptionCard(
                            title = title,
                            subtitle = sub,
                            selected = selectedSort == title,
                            onClick = {
                                selectedSort = title
                                showSortSheet = false
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }

    // Interests Modal (recs.png)
    if (showInterestsSheet) {
        val allTags = listOf("Техно", "Хаус", "Электроника", "Стендап", "Выставки", "Бары", "Концерты", "Вино", "Кино", "Джаз", "Театр", "Квизы")
        val tempInterests = remember { mutableStateListOf<String>().apply { addAll(user.interests) } }
        ModalBottomSheet(
            onDismissRequest = { showInterestsSheet = false },
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
                Text(
                    text = "Мои интересы и рекомендации",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Отметь жанры и форматы — лента перестроится мгновенно",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allTags.forEach { t ->
                        val isSel = tempInterests.contains(t)
                        VryvChip(
                            text = t,
                            selected = isSel,
                            isBig = true,
                            onClick = {
                                if (isSel) tempInterests.remove(t) else tempInterests.add(t)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                VryvButton(
                    text = "Сохранить интересы (${tempInterests.size})",
                    onClick = {
                        repository.finishOnboarding(tempInterests.toList(), user.preferredBudget, user.preferredFormats)
                        showInterestsSheet = false
                        repository.showToast("Интересы обновлены")
                    },
                    variant = VryvButtonVariant.PRIMARY
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun StatCard(count: String, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
            .padding(vertical = 12.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = count,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 11.5.sp
            )
        }
    }
}

@Composable
fun PlansCalendarSection(
    events: List<com.example.data.VryvEvent>,
    onEventClick: (String) -> Unit
) {
    val plannedList = listOf(
        Triple(events.find { it.id == "e_rooftop" } ?: events[0], "Собираюсь", VryvBadgeStyle.PINK),
        Triple(events.find { it.id == "e_concert" } ?: events[1], "Собираюсь", VryvBadgeStyle.PINK),
        Triple(events.find { it.id == "e_standup" } ?: events[2], "Отменил", VryvBadgeStyle.MUTE),
        Triple(events.find { it.id == "e_jazz" } ?: events[3], "Посетил", VryvBadgeStyle.OK)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Октябрь 2026",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LegendDot(color = VryvPink, label = "Иду")
                LegendDot(color = VryvLime, label = "Был")
                LegendDot(color = Color(0xFF6A6A76), label = "Отмена")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        val days = listOf(
            Triple("Пн", "19", VryvLime),
            Triple("Вт", "20", Color.Transparent),
            Triple("Ср", "21", Color(0xFF6A6A76)),
            Triple("Чт", "22", Color.Transparent),
            Triple("Пт", "23", Color.Transparent),
            Triple("Сб", "24", VryvPink),
            Triple("Вс", "25", VryvPink)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { (weekday, dayNum, dotColor) ->
                val isToday = dayNum == "24"
                Column(
                    modifier = Modifier
                        .width(42.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isToday) VryvPink else Color(0xFF22222A)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = weekday,
                        color = if (isToday) Color.White.copy(alpha = 0.85f) else VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = dayNum,
                        color = if (isToday) Color.White else VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 3.dp)
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(if (isToday) Color.White else dotColor)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))
        HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(8.dp))

        plannedList.forEach { (ev, statusLabel, badgeStyle) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEventClick(ev.id) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = ev.coverRes),
                    contentDescription = ev.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ev.title,
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (statusLabel == "Отменил") TextDecoration.LineThrough else null,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${ev.dateDisplay}, ${ev.timeDisplay} · ${ev.venueName}",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                VryvStatusPill(
                    text = statusLabel,
                    style = badgeStyle
                )
            }
        }
    }
}

@Composable
fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            color = VryvTextSecondary,
            fontFamily = InterFontFamily,
            fontSize = 11.sp
        )
    }
}

@Composable
fun PlannedEventRow(event: com.example.data.VryvEvent, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = event.coverRes),
            contentDescription = event.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = "${event.dateDisplay} · ${event.venueName}",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 12.sp
            )
        }
        Text(
            text = event.priceString,
            color = VryvPink,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
