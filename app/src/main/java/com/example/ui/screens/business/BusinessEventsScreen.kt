package com.example.ui.screens.business

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvInputBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
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
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Мероприятия ${venue.name}",
                        color = VryvTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Только события вашей площадки (${venueEvents.size})",
                        color = VryvTextSecondary,
                        fontSize = 12.sp
                    )
                }

                IconButton(
                    onClick = onCreateEventClick,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(VryvLime)
                        .testTag("biz_add_event_header_btn")
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Создать событие",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Status Filter Tabs
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) VryvLime else VryvCard)
                            .clickable { selectedFilter = filter }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.Black else VryvTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
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
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvCardElevated)
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
                    contentPadding = PaddingValues(16.dp),
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
                }
            }
        }

        // FAB
        FloatingActionButton(
            onClick = onCreateEventClick,
            containerColor = VryvLime,
            contentColor = Color.Black,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("biz_fab_create_event")
        ) {
            Icon(Icons.Default.Add, contentDescription = "Создать мероприятие")
        }
    }

    // Cancel Event Bottom Sheet
    if (eventToCancel != null) {
        ModalBottomSheet(
            onDismissRequest = { eventToCancel = null },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VryvCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Отмена мероприятия",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "«${eventToCancel!!.title}» будет снято с афиши, а гостям с билетами и планами будет отправлено уведомление.",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Причина отмены", color = VryvTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = cancelReason,
                    onValueChange = { cancelReason = it },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VryvInputBg,
                        unfocusedContainerColor = VryvInputBg,
                        focusedBorderColor = VryvPink,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = VryvTextPrimary,
                        unfocusedTextColor = VryvTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("cancel_reason_input")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvPink)
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

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Reschedule Event Bottom Sheet
    if (eventToReschedule != null) {
        ModalBottomSheet(
            onDismissRequest = { eventToReschedule = null },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VryvCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Перенос мероприятия",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Измените дату для «${eventToReschedule!!.title}». Уведомление появится в планах гостей.",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Новая дата", color = VryvTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = newRescheduleDate,
                    onValueChange = { newRescheduleDate = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(text = "Новое время", color = VryvTextSecondary, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = newRescheduleTime,
                    onValueChange = { newRescheduleTime = it },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
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
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
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
            .clip(RoundedCornerShape(18.dp))
            .background(VryvCard)
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
                        .size(76.dp)
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
                                    Icons.Default.MoreVert,
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
                                    leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = VryvLime) },
                                    onClick = {
                                        showMenu = false
                                        onDuplicateClick()
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Перенести дату", color = VryvTextPrimary) },
                                    leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = VryvLime) },
                                    onClick = {
                                        showMenu = false
                                        onRescheduleClick()
                                    }
                                )
                                if (event.status != EventStatus.CANCELLED) {
                                    DropdownMenuItem(
                                        text = { Text("Отменить событие", color = VryvPink) },
                                        leadingIcon = { Icon(Icons.Default.Cancel, contentDescription = null, tint = VryvPink) },
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
                        fontSize = 15.sp,
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

            // If event is in MODERATION, show demo moderation approval helper
            if (event.status == EventStatus.MODERATION) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x22C7FF28))
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
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}
