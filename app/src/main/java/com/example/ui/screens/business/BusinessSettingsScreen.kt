package com.example.ui.screens.business

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvGroupRow
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvToggle
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun BusinessSettingsScreen(
    repository: VryvRepository,
    onSwitchToPersonalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val venue by repository.businessVenue.collectAsState()

    var name by remember { mutableStateOf(venue.name) }
    var address by remember { mutableStateOf(venue.address) }
    var schedule by remember { mutableStateOf(venue.schedule) }
    var phone by remember { mutableStateOf(venue.phone) }
    var telegram by remember { mutableStateOf(venue.telegram) }

    var showScheduleSheet by remember { mutableStateOf(false) }
    var showAddressSheet by remember { mutableStateOf(false) }
    var showNotifSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        Text(
            text = "Настройки заведения",
            color = VryvTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.5).sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quick settings group matching d6.png (vtime, vaddr, bnotif)
            item {
                Text(
                    text = "БЫСТРЫЕ НАСТРОЙКИ ПЛОЩАДКИ",
                    color = VryvTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )
                VryvGroupList {
                    VryvGroupRow(
                        icon = VryvIcons.Clock,
                        title = "Часы работы",
                        subtitle = schedule,
                        showDivider = true,
                        onClick = { showScheduleSheet = true }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Pin,
                        title = "Адрес и контакты",
                        subtitle = "$address · $telegram",
                        showDivider = true,
                        onClick = { showAddressSheet = true }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Bell,
                        title = "Уведомления бизнеса",
                        subtitle = "Модерация, вопросы гостей, отчёты",
                        showDivider = false,
                        onClick = { showNotifSheet = true }
                    )
                }
            }

            // Full Editable Venue Profile Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Профиль площадки",
                        color = VryvTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    VryvInputField(
                        value = name,
                        onValueChange = { name = it },
                        label = "Название"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VryvInputField(
                        value = address,
                        onValueChange = { address = it },
                        label = "Адрес"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VryvInputField(
                        value = schedule,
                        onValueChange = { schedule = it },
                        label = "График работы"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VryvInputField(
                        value = telegram,
                        onValueChange = { telegram = it },
                        label = "Telegram для гостей"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvLime)
                            .clickable {
                                repository.updateVenueInfo(name, address, schedule, phone, telegram)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Сохранить изменения",
                            color = Color(0xFF0B0B0F),
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Switch back to personal mode (preserving testTag("exit_business_mode_btn"))
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLime.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .clickable { onSwitchToPersonalClick() }
                        .testTag("exit_business_mode_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = VryvIcons.User,
                            contentDescription = null,
                            tint = VryvLime,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Выйти из бизнес-режима",
                            color = VryvLime,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }

    // vtime.png bottom sheet
    if (showScheduleSheet) {
        VryvFloatingBottomSheet(onDismiss = { showScheduleSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Часы работы площадки", color = VryvTextPrimary, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                VryvInputField(
                    value = schedule,
                    onValueChange = { schedule = it },
                    label = "Расписание по дням"
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvLime)
                        .clickable {
                            repository.updateVenueInfo(name, address, schedule, phone, telegram)
                            showScheduleSheet = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Сохранить расписание", color = Color(0xFF0B0B0F), fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // vaddr.png bottom sheet
    if (showAddressSheet) {
        VryvFloatingBottomSheet(onDismiss = { showAddressSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Адрес и контакты", color = VryvTextPrimary, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                VryvInputField(value = address, onValueChange = { address = it }, label = "Адрес в Минске")
                Spacer(modifier = Modifier.height(10.dp))
                VryvInputField(value = phone, onValueChange = { phone = it }, label = "Телефон")
                Spacer(modifier = Modifier.height(10.dp))
                VryvInputField(value = telegram, onValueChange = { telegram = it }, label = "Telegram")
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvLime)
                        .clickable {
                            repository.updateVenueInfo(name, address, schedule, phone, telegram)
                            showAddressSheet = false
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Сохранить контакты", color = Color(0xFF0B0B0F), fontSize = 14.5.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // bnotif.png bottom sheet
    if (showNotifSheet) {
        var modStatus by remember { mutableStateOf(true) }
        var guestMsgs by remember { mutableStateOf(true) }
        var weeklyReport by remember { mutableStateOf(true) }
        VryvFloatingBottomSheet(onDismiss = { showNotifSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Уведомления бизнеса", color = VryvTextPrimary, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                VryvGroupList {
                    BizNotifRow("Статус модерации событий", "Одобрение и комментарии", modStatus) { modStatus = it }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    BizNotifRow("Вопросы гостей", "Новые обращения в чат площадки", guestMsgs) { guestMsgs = it }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    BizNotifRow("Еженедельная сводка", "Просмотры и «Пойду» по понедельникам", weeklyReport) { weeklyReport = it }
                }
            }
        }
    }
}

@Composable
private fun BizNotifRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = VryvTextSecondary, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        VryvToggle(checked = checked, onCheckedChange = onCheckedChange)
    }
}
