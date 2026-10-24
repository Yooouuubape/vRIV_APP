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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.NotificationItem
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary
import com.example.ui.theme.VryvWarning

@Composable
fun NotificationsCenterScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val notifications by repository.notifications.collectAsState()
    var selectedNotifForInvite by remember { mutableStateOf<NotificationItem?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Top Header matching notifCenter.png
        VryvSubScreenHeader(
            title = "Уведомления",
            onBackClick = onBackClick,
            rightContent = {
                if (notifications.any { !it.isRead }) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { repository.markAllNotificationsRead() }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                            .testTag("mark_all_read_button")
                    ) {
                        Text(
                            text = "Прочитать все",
                            color = VryvPink,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        )

        if (notifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = VryvIcons.Bell,
                        contentDescription = null,
                        tint = VryvTextMuted,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Нет новых уведомлений",
                        color = VryvTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "СЕГОДНЯ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                    )
                }
                items(notifications) { notif ->
                    NotificationRowItem(
                        item = notif,
                        onClick = {
                            if (notif.type == "invite") {
                                selectedNotifForInvite = notif
                            } else if (notif.relatedEventId != null) {
                                onEventClick(notif.relatedEventId)
                            }
                        }
                    )
                }
            }
        }
    }

    // Sheet for Incoming Invitation (invIn.png)
    if (selectedNotifForInvite != null) {
        val notif = selectedNotifForInvite!!
        VryvFloatingBottomSheet(
            onDismiss = { selectedNotifForInvite = null }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.av2),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Илья Кравцов приглашает вас",
                            color = VryvTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "«Го сегодня на RAVE в Modul!»",
                            color = VryvTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Event preview card inside invIn sheet
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCardElevated)
                        .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.techno),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = notif.title,
                            color = VryvTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = notif.subtitle,
                            color = VryvTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VryvButton(
                        text = "Не смогу",
                        style = VryvButtonStyle.SecondaryDark,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            repository.respondToInvite(notif.id, accept = false)
                            selectedNotifForInvite = null
                        }
                    )
                    VryvButton(
                        text = "Пойду!",
                        style = VryvButtonStyle.PrimaryPink,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            repository.respondToInvite(notif.id, accept = true)
                            selectedNotifForInvite = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationRowItem(
    item: NotificationItem,
    onClick: () -> Unit
) {
    val (icon, tint) = when (item.type) {
        "invite" -> VryvIcons.Flame to VryvPink
        "message" -> VryvIcons.Msg to VryvLime
        "group" -> VryvIcons.Users to VryvPink
        "event_change" -> VryvIcons.Clock to VryvWarning
        else -> VryvIcons.Check to VryvLime
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(if (item.isRead) VryvCard else VryvCardElevated)
            .border(
                width = 1.dp,
                color = if (item.isRead) VryvLine else VryvPink.copy(alpha = 0.35f),
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(tint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(20.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title,
                    color = VryvTextPrimary,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f, fill = false)
                )
                if (!item.isRead) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(VryvPink)
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = item.subtitle, color = VryvTextSecondary, fontSize = 12.5.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))
        Text(text = item.time, color = VryvTextMuted, fontSize = 11.sp)
    }
}
