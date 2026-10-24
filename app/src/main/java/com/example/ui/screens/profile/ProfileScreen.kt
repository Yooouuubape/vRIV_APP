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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvOptionCard
import com.example.ui.components.VryvSegmentedControl
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.components.VryvTag
import com.example.ui.components.VryvTagVariant
import com.example.ui.components.VryvToggle
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalLayoutApi::class)
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
    val friends by repository.friends.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Планы (prof.png), 1: Сохранённое (saved.png), 2: Друзья (friends.png)
    var selectedDayIndex by remember { mutableIntStateOf(5) } // Сб 24 highlighted
    var savedSortChip by remember { mutableStateOf("По дате") }
    var friendSearchQuery by remember { mutableStateOf("") }
    var showInterestsScreen by remember { mutableStateOf(false) }

    val savedEvents = remember(events) { events.filter { it.isBookmarked } }
    val pendingRequests = remember(friends) { friends.filter { it.isRequest } }
    val acceptedFriends = remember(friends, friendSearchQuery) {
        friends.filter { !it.isRequest && (friendSearchQuery.isBlank() || it.name.contains(friendSearchQuery, ignoreCase = true) || it.username.contains(friendSearchQuery, ignoreCase = true)) }
    }

    // Sub-screen: Мои интересы (recs.png in s2-user.js)
    if (showInterestsScreen) {
        val selectedTags = remember { mutableStateListOf("Электроника", "Техно", "Стендап", "Выставки", "Винные дегустации", "Бары") }
        val allTags = listOf("Электроника", "Техно", "Стендап", "Выставки", "Винные дегустации", "Бары", "Концерты", "Джаз", "Кинопоказы", "Квизы", "Настолки", "Маркеты")
        var selectedBudget by remember { mutableStateOf("До 50 BYN") }
        var hideKids by remember { mutableStateOf(true) }
        var hideOnline by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
        ) {
            VryvSubScreenHeader(
                title = "Мои интересы",
                onBackClick = { showInterestsScreen = false },
                rightContent = {
                    Text(
                        text = "Сбросить",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { selectedTags.clear() }
                    )
                }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "По этим тегам строится подборка «Для вас» и свайп-режим. Изменения применятся сразу.",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Категории · выбрано ${selectedTags.size}",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allTags.forEach { tag ->
                        val isSel = selectedTags.contains(tag)
                        VryvChip(
                            text = tag,
                            selected = isSel,
                            isBig = true,
                            onClick = {
                                if (isSel) selectedTags.remove(tag) else selectedTags.add(tag)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Бюджет",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Бесплатно", "До 30 BYN", "До 50 BYN", "Любой").forEach { b ->
                        VryvChip(
                            text = b,
                            selected = selectedBudget == b,
                            onClick = { selectedBudget = b }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Что скрывать",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                VryvGroupList {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { hideKids = !hideKids }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Детские события 0+", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Утренники и семейные шоу", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                        VryvToggle(checked = hideKids, onCheckedChange = { hideKids = it })
                    }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { hideOnline = !hideOnline }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Онлайн-трансляции", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Показывать только офлайн в городе", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                        VryvToggle(checked = hideOnline, onCheckedChange = { hideOnline = it })
                    }
                }
            }
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                VryvButton(
                    text = "Обновить рекомендации",
                    style = VryvButtonStyle.PrimaryPink,
                    onClick = {
                        repository.showToast("Рекомендации обновлены")
                        showInterestsScreen = false
                    }
                )
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        // Top Header: @username + chevD on left, Share + Gear on right (matching prof.png)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onSwitchRoleClick() }
                        .padding(vertical = 4.dp, horizontal = 4.dp)
                        .testTag("role_switcher_header")
                ) {
                    Text(
                        text = "@${user.username.removePrefix("@")}",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = VryvIcons.ChevronDown,
                        contentDescription = "Сменить профиль",
                        tint = VryvTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    VryvIconButton(
                        icon = VryvIcons.Share,
                        onClick = { repository.showToast("Ссылка на профиль @${user.username.removePrefix("@")} скопирована") }
                    )
                    Box(modifier = Modifier.testTag("settings_button")) {
                        VryvIconButton(
                            icon = VryvIcons.Gear,
                            onClick = onSettingsClick
                        )
                    }
                }
            }
        }

        // Profile Bio & Stats Card matching prof.png 1-to-1
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Avatar 84.dp with pink double ring + Name + Minsk pin
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, VryvPink, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = user.avatarRes),
                            contentDescription = user.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(84.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.name,
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.6).sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = VryvIcons.Pin,
                                contentDescription = null,
                                tint = VryvPink,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = user.city,
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
                    color = Color(0xFFD0D0DC),
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3 Stat Cards: 86 Друзей / 17 Сохранено / 48 Посетил
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProfileStatCardRef(
                        value = "${user.friendsCount}",
                        label = "Друзей",
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = 2 }
                    )
                    ProfileStatCardRef(
                        value = "17",
                        label = "Сохранено",
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = 1 }
                    )
                    ProfileStatCardRef(
                        value = "${user.attendedCount}",
                        label = "Посетил",
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = 0 }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Редактировать профиль + Sliders button (matching prof.png)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                            .clickable { onEditProfileClick() }
                            .testTag("edit_profile_button"),
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

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, CircleShape)
                            .clickable { showInterestsScreen = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Sliders,
                            contentDescription = "Мои интересы",
                            tint = VryvTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // "ВРЫВ для бизнеса" Card matching prof.png 1-to-1
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .clickable { repository.setBusinessMode(true) }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("business_mode_entry_card"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(VryvCardElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Store,
                            contentDescription = null,
                            tint = VryvLime,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ВРЫВ для бизнеса",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Заведение или свои события",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp
                        )
                    }
                    Icon(
                        imageVector = VryvIcons.ChevronRight,
                        contentDescription = null,
                        tint = VryvTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Segmented Control: Планы / Сохранённое / Друзья
                VryvSegmentedControl(
                    options = listOf("Планы", "Сохранённое", "Друзья"),
                    selectedIndex = selectedTab,
                    onSelect = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }
        }

        // Tab Content
        when (selectedTab) {
            0 -> {
                // TAB 0: ПЛАНЫ (prof.png)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(22.dp))
                            .padding(14.dp)
                    ) {
                        // Calendar Header: Октябрь 2026 + Legend (Иду / Был / Отменил)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Октябрь 2026",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                LegendDotItem(color = VryvPink, label = "Иду")
                                LegendDotItem(color = VryvLime, label = "Был")
                                LegendDotItem(color = Color(0xFF56565F), label = "Отменил")
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 7-day strip: Пн 19 .. Вс 25
                        val days = listOf(
                            Triple("Пн", "19", Color.Transparent),
                            Triple("Вт", "20", VryvLime),
                            Triple("Ср", "21", Color(0xFF56565F)),
                            Triple("Чт", "22", Color.Transparent),
                            Triple("Пт", "23", VryvPink),
                            Triple("Сб", "24", Color.White),
                            Triple("Вс", "25", Color.Transparent)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            days.forEachIndexed { idx, (dayLabel, dateNum, dotColor) ->
                                val isSel = idx == selectedDayIndex
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSel) VryvPink else VryvCardElevated)
                                        .clickable { selectedDayIndex = idx }
                                        .padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = dayLabel,
                                        color = if (isSel) Color.White else VryvTextSecondary,
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = dateNum,
                                        color = if (isSel) Color.White else VryvTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(dotColor)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 4 Plan items inside the calendar card matching prof.png 1-to-1
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            PlanRowCardRef(
                                coverRes = R.drawable.rooftop,
                                title = "Rooftop Sunset Party",
                                subtitle = "Сб, 24 окт · 19:00 · Верхний город",
                                badgeText = "Собираюсь",
                                badgeVariant = VryvTagVariant.PINK,
                                isCancelled = false,
                                onClick = { onEventClick("e_rooftop") }
                            )
                            PlanRowCardRef(
                                coverRes = R.drawable.concert,
                                title = "Северный Свет",
                                subtitle = "Пт, 23 окт · 20:00 · Prime Hall",
                                badgeText = "Собираюсь",
                                badgeVariant = VryvTagVariant.PINK,
                                isCancelled = false,
                                onClick = { onEventClick("e_techno") }
                            )
                            PlanRowCardRef(
                                coverRes = R.drawable.standup,
                                title = "Открытый микрофон",
                                subtitle = "Ср, 21 окт · передумал идти",
                                badgeText = "Отменил",
                                badgeVariant = VryvTagVariant.NEUTRAL,
                                isCancelled = true,
                                onClick = { onEventClick("e_standup") }
                            )
                            PlanRowCardRef(
                                coverRes = R.drawable.jazz,
                                title = "Jazz Session",
                                subtitle = "Вт, 20 окт · клуб «Граффити»",
                                badgeText = "Посетил",
                                badgeVariant = VryvTagVariant.LIME,
                                isCancelled = false,
                                onClick = { onEventClick("e_jazz") }
                            )
                        }
                    }

                    // Bottom privacy row matching prof.png
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = VryvIcons.Lock,
                            contentDescription = null,
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Мои планы видят: только друзья",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Изменить",
                            color = VryvPink,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onEditProfileClick() }
                        )
                    }
                }
            }

            1 -> {
                // TAB 1: СОХРАНЁННОЕ (saved.png)
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val chips = listOf("По дате", "По цене", "Категория", "Архив")
                        items(chips) { ch ->
                            val isSel = savedSortChip == ch
                            VryvChip(
                                text = ch,
                                selected = isSel,
                                onClick = { savedSortChip = ch },
                                trailingContent = if (ch == "По дате") {
                                    {
                                        Icon(
                                            imageVector = VryvIcons.ChevronDown,
                                            contentDescription = null,
                                            tint = if (isSel) VryvTextPrimary else VryvTextSecondary,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                } else null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // 2-column grid of saved cards matching saved.png
                val displaySaved = if (savedEvents.isNotEmpty()) savedEvents else events.take(4)
                val rows = displaySaved.chunked(2)
                items(rows) { rowPair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowPair.forEach { ev ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(180.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { onEventClick(ev.id) }
                            ) {
                                Image(
                                    painter = painterResource(id = ev.coverRes),
                                    contentDescription = ev.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color(0x880B0B0F))
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(10.dp)
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(VryvPink)
                                        .clickable { repository.toggleBookmark(ev.id) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = VryvIcons.BookmarkFilled,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(12.dp)
                                ) {
                                    Text(
                                        text = ev.title,
                                        color = Color.White,
                                        fontFamily = InterFontFamily,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${ev.dateDisplay} · ${ev.priceString}",
                                        color = Color(0xFFC8C8D4),
                                        fontFamily = InterFontFamily,
                                        fontSize = 11.5.sp
                                    )
                                }
                            }
                        }
                        if (rowPair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            2 -> {
                // TAB 2: ДРУЗЬЯ (friends.png)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        // Search bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .clip(RoundedCornerShape(23.dp))
                                .background(VryvCard)
                                .border(1.dp, VryvLine, RoundedCornerShape(23.dp))
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(VryvIcons.Search, contentDescription = null, tint = VryvTextSecondary, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            BasicTextField(
                                value = friendSearchQuery,
                                onValueChange = { friendSearchQuery = it },
                                singleLine = true,
                                textStyle = TextStyle(color = VryvTextPrimary, fontFamily = InterFontFamily, fontSize = 14.sp),
                                cursorBrush = SolidColor(VryvPink),
                                modifier = Modifier.weight(1f),
                                decorationBox = { inner ->
                                    if (friendSearchQuery.isEmpty()) {
                                        Text("Найти по имени или @username", color = VryvTextSecondary, fontSize = 14.sp)
                                    }
                                    inner()
                                }
                            )
                        }

                        if (pendingRequests.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Заявки · ${pendingRequests.size}",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            VryvGroupList {
                                pendingRequests.forEachIndexed { idx, req ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = req.avatarRes),
                                            contentDescription = req.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(CircleShape)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(req.name, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                            Text("@${req.username.removePrefix("@")} · ${req.mutualEvents} общих друзей", color = VryvTextSecondary, fontSize = 12.sp)
                                        }
                                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .height(34.dp)
                                                    .clip(RoundedCornerShape(999.dp))
                                                    .background(VryvPink)
                                                    .clickable { repository.acceptFriendRequest(req.id) }
                                                    .padding(horizontal = 12.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("Принять", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .size(34.dp)
                                                    .clip(CircleShape)
                                                    .background(VryvCardElevated)
                                                    .clickable { repository.declineFriendRequest(req.id) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(VryvIcons.Close, contentDescription = "Отклонить", tint = VryvTextSecondary, modifier = Modifier.size(15.dp))
                                            }
                                        }
                                    }
                                    if (idx < pendingRequests.lastIndex) {
                                        HorizontalDivider(color = VryvLine, thickness = 1.dp)
                                    }
                                }
                            }
                        }

                        // Приглашения (matching friends.png)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Приглашения",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        VryvGroupList {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onEventClick("e_techno") }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.av1),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Алина зовёт на «Северный Свет»", color = VryvTextPrimary, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold)
                                    Text("Пт, 23 окт · 20:00 · Prime Hall", color = VryvTextSecondary, fontSize = 12.5.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .height(34.dp)
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(VryvPink)
                                        .clickable { repository.showToast("Вы приняли приглашение Алины") }
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(VryvIcons.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        // Мои друзья · 86
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Мои друзья · ${user.friendsCount}",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        VryvGroupList {
                            acceptedFriends.forEachIndexed { idx, friend ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onOpenDirectChat(friend.id) }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = friend.avatarRes),
                                        contentDescription = friend.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(friend.name, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                        Text(
                                            text = friend.statusText.ifBlank { "@${friend.username.removePrefix("@")}" },
                                            color = VryvTextSecondary,
                                            fontSize = 12.5.sp
                                        )
                                    }
                                    Icon(
                                        imageVector = VryvIcons.Message,
                                        contentDescription = "Написать",
                                        tint = VryvTextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                if (idx < acceptedFriends.lastIndex) {
                                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatCardRef(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
            .padding(vertical = 11.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
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

@Composable
private fun LegendDotItem(color: Color, label: String) {
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
private fun PlanRowCardRef(
    coverRes: Int,
    title: String,
    subtitle: String,
    badgeText: String,
    badgeVariant: VryvTagVariant,
    isCancelled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(VryvCardElevated)
            .clickable { onClick() }
            .padding(10.dp)
            .alpha(if (isCancelled) 0.55f else 1f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = coverRes),
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textDecoration = if (isCancelled) TextDecoration.LineThrough else TextDecoration.None
            )
            Text(
                text = subtitle,
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 11.5.sp
            )
        }
        VryvTag(
            text = badgeText,
            variant = badgeVariant
        )
    }
}
