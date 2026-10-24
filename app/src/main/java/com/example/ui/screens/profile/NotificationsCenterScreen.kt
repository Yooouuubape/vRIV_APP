package com.example.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.NotificationType
import com.example.data.VryvRepository
import com.example.ui.components.VryvAvatarStack
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun NotificationsCenterScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val notifications by repository.notifications.collectAsState()
    var showIncomingInviteSheet by remember { mutableStateOf(false) }

    val inviteNotif = notifications.firstOrNull { it.type == NotificationType.COMPANY_INVITE }
    val friendNotif = notifications.firstOrNull { it.type == NotificationType.FRIEND_REQUEST }
    val reminderNotif = notifications.firstOrNull { it.type == NotificationType.EVENT_REMINDER }
    val changeNotif = notifications.firstOrNull { it.type == NotificationType.EVENT_CHANGED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header matching notifCenter.png 1-to-1
        VryvSubScreenHeader(
            title = "Уведомления",
            onBackClick = onBackClick,
            rightContent = {
                Text(
                    text = "Прочитать всё",
                    color = VryvPink,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { repository.markAllNotificationsRead() }
                        .testTag("mark_all_read_button")
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Приглашения · 2 (matching notifCenter.png)
            Text(
                text = "Приглашения · 2",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            VryvGroupList {
                // Row 1: Алина зовёт в компанию «Идём на Techno Room»
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showIncomingInviteSheet = true }
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .testTag("notification_item_${inviteNotif?.id ?: "n_1"}"),
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.av1),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Алина зовёт в компанию «Идём на Techno Room»",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 19.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Сегодня · 23:00 · 2 друга идут",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(VryvPink)
                                    .clickable {
                                        if (inviteNotif != null) repository.respondToNotification(inviteNotif.id, true)
                                        repository.showToast("Вы присоединились к компании Алины!")
                                    }
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Пойду", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.Bold)
                            }
                            Box(
                                modifier = Modifier
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(VryvCardElevated)
                                    .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                                    .clickable {
                                        if (inviteNotif != null) repository.respondToNotification(inviteNotif.id, false)
                                        repository.showToast("Приглашение отклонено")
                                    }
                                    .padding(horizontal = 14.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Не смогу", color = VryvTextPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                HorizontalDivider(color = VryvLine, thickness = 1.dp)

                // Row 2: Вика позвала в компанию «Идём на Techno Room»
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showIncomingInviteSheet = true }
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.av5),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Вика позвала в компанию «Идём на Techno Room»",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "Открытая · 3 из 5 мест",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Вступить",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            repository.showToast("Вы вступили в компанию")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Сообщения
            Text(
                text = "Сообщения",
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
                        .clickable { repository.showToast("Открываем диалог с Димой") }
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.av4),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Дима",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Передал тебе билет, проверь",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(VryvPink)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Заявки в друзья
            Text(
                text = "Заявки в друзья",
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
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                        .testTag("notification_item_${friendNotif?.id ?: "n_2"}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.av3),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Катя · @katya.ink",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "12 общих друзей",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(
                            modifier = Modifier
                                .height(34.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(VryvCardElevated)
                                .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                                .clickable {
                                    if (friendNotif != null) repository.respondToNotification(friendNotif.id, false)
                                    repository.showToast("Заявка отклонена")
                                }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Отклонить", color = VryvTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Box(
                            modifier = Modifier
                                .height(34.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(VryvPink)
                                .clickable {
                                    if (friendNotif != null) repository.respondToNotification(friendNotif.id, true)
                                    repository.showToast("Катя добавлена в друзья!")
                                }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Принять", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Напоминания
            Text(
                text = "Напоминания",
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
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                        .testTag("notification_item_${reminderNotif?.id ?: "n_4"}"),
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
                            imageVector = VryvIcons.Clock,
                            contentDescription = null,
                            tint = VryvTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Techno Room: Ночь 3 · сегодня 23:00",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Через 3 часа · Re:Public",
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Изменения мероприятий
            Text(
                text = "Изменения мероприятий",
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
                        .clickable { onEventClick("e_rooftop") }
                        .padding(horizontal = 16.dp, vertical = 13.dp)
                        .testTag("notification_item_${changeNotif?.id ?: "n_3"}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x2EFF1D8E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Alert,
                            contentDescription = null,
                            tint = VryvPink,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "«Северный Свет» перенесено",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Новая дата: Сб 24 · 22:00",
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

                HorizontalDivider(color = VryvLine, thickness = 1.dp)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEventClick("e_rooftop") }
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0x2EFF1D8E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Alert,
                            contentDescription = null,
                            tint = VryvPink,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "«Rooftop Sunset Party» отменено",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "План удалён из календаря",
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
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Incoming Invite Bottom Sheet matching invIn.png 1-to-1
    if (showIncomingInviteSheet) {
        VryvFloatingBottomSheet(
            onDismiss = { showIncomingInviteSheet = false }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
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
                    Column {
                        Text(
                            text = "Алина зовёт тебя",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "@alina.tech · только что",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp
                        )
                    }
                }

                // Event preview card inside invIn.png
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .clip(RoundedCornerShape(22.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.techno),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x990B0B0F))
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "Techno Room: Ночь 3",
                            color = Color.White,
                            fontFamily = InterFontFamily,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Сегодня · 23:00 · Re:Public · 30 BYN",
                            color = Color(0xFFDCDCE6),
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    VryvAvatarStack(
                        avatars = listOf(R.drawable.av1, R.drawable.av4),
                        avatarSize = 26.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "В компании: Алина, Дима · сбор в 22:30 у входа",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                VryvButton(
                    text = "Пойду вместе",
                    style = VryvButtonStyle.PrimaryPink,
                    onClick = {
                        showIncomingInviteSheet = false
                        repository.showToast("Вы присоединились к компании Алины!")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                VryvButton(
                    text = "Не смогу",
                    style = VryvButtonStyle.Secondary,
                    onClick = { showIncomingInviteSheet = false }
                )
            }
        }
    }
}
