package com.example.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.EventCardGrid
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

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

    val savedEvents = remember(events) { events.filter { it.isBookmarked } }
    val plannedEvents = remember(events) { events.filter { it.isGoing } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSwitchRoleClick() }
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .testTag("role_switcher_header")
            ) {
                Text(
                    text = "@${user.username}",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ExpandMore,
                    contentDescription = "Сменить режим",
                    tint = VryvTextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(
                    onClick = { repository.showToast("Ссылка на твой профиль скопирована") },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(VryvCard)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Поделиться",
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onSettingsClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(VryvCard)
                        .testTag("profile_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Настройки",
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // User Header Info
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Image(
                            painter = painterResource(id = user.avatarRes),
                            contentDescription = user.name,
                            modifier = Modifier
                                .size(78.dp)
                                .clip(CircleShape)
                                .border(2.5.dp, VryvPink, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = user.name,
                                color = VryvTextPrimary,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Place,
                                    contentDescription = null,
                                    tint = VryvPink,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = user.city,
                                    color = VryvTextSecondary,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user.bio,
                        color = VryvTextPrimary,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Cards Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(count = "${companions.count { it.isFriend }}", label = "Друзей", modifier = Modifier.weight(1f))
                        StatCard(count = "${savedEvents.size}", label = "Сохранено", modifier = Modifier.weight(1f))
                        StatCard(count = "${plannedEvents.size}", label = "В планах", modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Edit Profile Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvCard)
                                .clickable { onEditProfileClick() }
                                .padding(horizontal = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Редактировать профиль",
                                color = VryvTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Business Cabinet Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(VryvCardElevated)
                            .clickable { repository.setBusinessMode(true) }
                            .padding(14.dp)
                            .testTag("banner_vryv_business")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(VryvLime.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Store,
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
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Управление заведением Loft 13",
                                        color = VryvTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = VryvTextMuted
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Segmented Tabs: Планы, Сохранённое, Друзья
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvCard)
                            .padding(4.dp)
                    ) {
                        sections.forEach { sec ->
                            val isSelected = sec == selectedSection
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color(0xFF2E2E38) else Color.Transparent)
                                    .clickable { selectedSection = sec }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = sec,
                                    color = if (isSelected) Color.White else VryvTextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
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
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                                Text(
                                    text = "Сохранено: ${savedEvents.size} событий",
                                    color = VryvTextSecondary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }
                        }
                        items(savedEvents) { event ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                                PlannedEventRow(event = event, onClick = { onEventClick(event.id) })
                            }
                        }
                    }
                }

                "Друзья" -> {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                            Text(
                                text = "Мои друзья · ${companions.size}",
                                color = VryvTextSecondary,
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
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(VryvCard)
                                .clickable { onOpenDirectChat(friend.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = friend.avatarRes),
                                contentDescription = friend.name,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = friend.name, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Text(text = "@${friend.username} · ${friend.sharedTags.joinToString()}", color = VryvTextSecondary, fontSize = 12.sp, maxLines = 1)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(VryvCardElevated)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(text = "Чат", color = VryvPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(count: String, label: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(VryvCard)
            .padding(vertical = 12.dp, horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = count, color = VryvTextPrimary, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = label, color = VryvTextSecondary, fontSize = 12.sp)
        }
    }
}

@Composable
fun PlansCalendarSection(
    events: List<com.example.data.VryvEvent>,
    onEventClick: (String) -> Unit
) {
    val plannedList = listOf(
        Triple(events.find { it.id == "e_rooftop" } ?: events[0], "Собираюсь", VryvPink),
        Triple(events.find { it.id == "e_concert" } ?: events[1], "Собираюсь", VryvPink),
        Triple(events.find { it.id == "e_standup" } ?: events[2], "Отменил", Color(0xFF6A6A76)),
        Triple(events.find { it.id == "e_jazz" } ?: events[3], "Посетил", VryvLime)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(VryvCard)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Октябрь 2026", color = VryvTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LegendDot(color = VryvPink, label = "Иду")
                LegendDot(color = VryvLime, label = "Был")
                LegendDot(color = Color(0xFF6A6A76), label = "Отмена")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekdays strip (19 to 25 Oct)
        val days = listOf(
            Triple("Пн", "19", VryvLime),
            Triple("Вт", "20", Color.Transparent),
            Triple("Ср", "21", Color(0xFF6A6A76)),
            Triple("Чт", "22", Color.Transparent),
            Triple("Пт", "23", Color.Transparent),
            Triple("Сб", "24", VryvPink),
            Triple("Вс", "25", Color.Transparent)
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
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isToday) VryvPink else Color.Transparent),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = weekday,
                        color = if (isToday) Color.White.copy(alpha = 0.8f) else VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = dayNum,
                        color = if (isToday) Color.White else VryvTextPrimary,
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

        // Planned events list
        plannedList.forEach { (ev, statusLabel, statusColor) ->
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
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = ev.title,
                        color = VryvTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (statusLabel == "Отменил") TextDecoration.LineThrough else null,
                        maxLines = 1
                    )
                    Text(
                        text = "${ev.dateDisplay} · ${ev.venueName}",
                        color = VryvTextSecondary,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusLabel,
                        color = statusColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
        Text(text = label, color = VryvTextSecondary, fontSize = 11.sp)
    }
}

@Composable
fun PlannedEventRow(event: com.example.data.VryvEvent, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(VryvCard)
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
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = event.title, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(text = "${event.dateDisplay} · ${event.venueName}", color = VryvTextSecondary, fontSize = 12.sp)
        }
        Text(text = event.priceString, color = VryvPink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}
