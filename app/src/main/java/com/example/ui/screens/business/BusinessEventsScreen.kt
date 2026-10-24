package com.example.ui.screens.business

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EventStatus
import com.example.data.VryvEvent
import com.example.data.VryvRepository
import com.example.ui.components.StatusBadge
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvDanger
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun BusinessEventsScreen(
    repository: VryvRepository,
    onCreateEventClick: () -> Unit,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val venue by repository.businessVenue.collectAsState()
    val allEvents by repository.events.collectAsState()

    // STRICT ISOLATION: Only events belonging to THIS venue!
    val venueEvents = remember(allEvents, venue.id) {
        allEvents.filter { it.venueId == venue.id }
    }

    var selectedFilter by remember { mutableStateOf("Все") }
    val filters = listOf("Все", "Опубликованные", "На модерации", "Черновики", "Отменённые")

    var eventToCancel by remember { mutableStateOf<VryvEvent?>(null) }
    var cancelReason by remember { mutableStateOf("Технический перенос") }

    var eventToReschedule by remember { mutableStateOf<VryvEvent?>(null) }
    var newRescheduleDate by remember { mutableStateOf("Сб 7 ноя") }
    var newRescheduleTime by remember { mutableStateOf("20:00 – 03:00") }

    val filteredList = remember(venueEvents, selectedFilter) {
        when (selectedFilter) {
            "Опубликованные" -> venueEvents.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
            "На модерации" -> venueEvents.filter { it.status == EventStatus.MODERATION }
            "Черновики" -> venueEvents.filter { it.status == EventStatus.DRAFT }
            "Отменённые" -> venueEvents.filter { it.status == EventStatus.CANCELLED || it.status == EventStatus.COMPLETED }
            else -> venueEvents
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header matching d2.png
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Мероприятия ${venue.name}",
                        color = VryvTextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Только события вашей площадки (${venueEvents.size})",
                        color = VryvTextSecondary,
                        fontSize = 12.5.sp
                    )
                }

                IconButton(
                    onClick = onCreateEventClick,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(VryvLime)
                        .testTag("biz_add_event_header_btn")
                ) {
                    Icon(
                        imageVector = VryvIcons.Plus,
                        contentDescription = "Создать событие",
                        tint = Color(0xFF0B0B0F),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Status Filter Tabs matching d2.png
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (isSelected) VryvLime else VryvCardElevated)
                            .border(1.dp, if (isSelected) VryvLime else VryvLine, RoundedCornerShape(999.dp))
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color(0xFF0B0B0F) else VryvTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            // Events List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "В этой категории нет событий",
                            color = VryvTextSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(VryvCardElevated)
                                .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                                .clickable { onCreateEventClick() }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(text = "Создать мероприятие", color = VryvLime, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList, key = { it.id }) { event ->
                        BusinessEventRowItem(
                            event = event,
                            onClick = { onEventClick(event.id) },
                            onCancelClick = { eventToCancel = event },
                            onRescheduleClick = { eventToReschedule = event },
                            onDuplicateClick = { repository.duplicateBusinessEvent(event.id) },
                            onSimulateApproval = { repository.simulateEventApproval(event.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(90.dp))
                    }
                }
            }
        }

        // FAB (preserved testTag("biz_fab_create_event"))
        FloatingActionButton(
            onClick = onCreateEventClick,
            containerColor = VryvLime,
            contentColor = Color(0xFF0B0B0F),
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 90.dp)
                .testTag("biz_fab_create_event")
        ) {
            Icon(VryvIcons.Plus, contentDescription = "Создать мероприятие")
        }
    }

    // Cancel Event Bottom Sheet
    if (eventToCancel != null) {
        VryvFloatingBottomSheet(
            onDismiss = { eventToCancel = null }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Отмена мероприятия",
                    color = VryvTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "«${eventToCancel!!.title}» будет снято с афиши, а гостям с билетами и планами будет отправлено уведомление.",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "ПРИЧИНА ОТМЕНЫ",
                    color = VryvTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = cancelReason,
                    onValueChange = { cancelReason = it },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VryvCardElevated,
                        unfocusedContainerColor = VryvCardElevated,
                        focusedBorderColor = VryvPink,
                        unfocusedBorderColor = VryvLine,
                        focusedTextColor = VryvTextPrimary,
                        unfocusedTextColor = VryvTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("cancel_reason_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvDanger)
                        .clickable {
                            repository.cancelBusinessEvent(eventToCancel!!.id, cancelReason)
                            eventToCancel = null
                        }
                        .testTag("confirm_cancel_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Подтвердить отмену и уведомить гостей",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Reschedule Event Bottom Sheet
    if (eventToReschedule != null) {
        VryvFloatingBottomSheet(
            onDismiss = { eventToReschedule = null }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Перенос мероприятия",
                    color = VryvTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Измените дату для «${eventToReschedule!!.title}». Уведомление появится в планах гостей.",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                VryvInputField(
                    value = newRescheduleDate,
                    onValueChange = { newRescheduleDate = it },
                    label = "Новая дата"
                )

                Spacer(modifier = Modifier.height(12.dp))

                VryvInputField(
                    value = newRescheduleTime,
                    onValueChange = { newRescheduleTime = it },
                    label = "Новое время"
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvLime)
                        .clickable {
                            repository.rescheduleBusinessEvent(
                                eventToReschedule!!.id,
                                newRescheduleDate,
                                newRescheduleTime
                            )
                            eventToReschedule = null
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Сохранить новую дату",
                        color = Color(0xFF0B0B0F),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun BusinessEventRowItem(
    event: VryvEvent,
    onClick: () -> Unit,
    onCancelClick: () -> Unit,
    onRescheduleClick: () -> Unit,
    onDuplicateClick: () -> Unit,
    onSimulateApproval: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = event.coverRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(78.dp)
                        .clip(RoundedCornerShape(14.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(status = event.status)

                        Box {
                            IconButton(
                                onClick = { showMenu = true },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = VryvIcons.More,
                                    contentDescription = "Действия",
                                    tint = VryvTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = { showMenu = false },
                                modifier = Modifier.background(VryvCardElevated)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Дублировать", color = VryvTextPrimary) },
                                    leadingIcon = { Icon(VryvIcons.Copy, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp)) },
                                    onClick = {
                                        showMenu = false
                                        onDuplicateClick()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Перенести дату", color = VryvTextPrimary) },
                                    leadingIcon = { Icon(VryvIcons.Calendar, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp)) },
                                    onClick = {
                                        showMenu = false
                                        onRescheduleClick()
                                    }
                                )
                                if (event.status != EventStatus.CANCELLED) {
                                    DropdownMenuItem(
                                        text = { Text("Отменить событие", color = VryvPink) },
                                        leadingIcon = { Icon(VryvIcons.Close, contentDescription = null, tint = VryvPink, modifier = Modifier.size(16.dp)) },
                                        onClick = {
                                            showMenu = false
                                            onCancelClick()
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = event.title,
                        color = VryvTextPrimary,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${event.dateDisplay} · ${event.timeDisplay}",
                        color = VryvTextSecondary,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "${event.viewsCount} просм.",
                            color = VryvTextMuted,
                            fontSize = 11.5.sp
                        )
                        Text(
                            text = "${event.goingCount} идут",
                            color = VryvLime,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = event.priceString,
                            color = VryvTextSecondary,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }

            if (event.status == EventStatus.MODERATION) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(VryvLime.copy(alpha = 0.14f))
                        .clickable { onSimulateApproval() }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Демо: Одобрить событие модератором",
                            color = VryvLime,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(VryvIcons.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
