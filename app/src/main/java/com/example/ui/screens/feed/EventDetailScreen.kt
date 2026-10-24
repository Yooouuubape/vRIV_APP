package com.example.ui.screens.feed

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.StatusBadge
import com.example.ui.components.VryvAvatarStack
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonVariant
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: String,
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onFindCompanyClick: (String) -> Unit,
    onOpenRouteClick: (String) -> Unit,
    onInviteFriendsClick: (String) -> Unit,
    onReportClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val events by repository.events.collectAsState()
    val event = events.find { it.id == eventId } ?: events.first()

    var showTicketSheet by remember { mutableStateOf(false) }
    var showWaitlistSheet by remember { mutableStateOf(false) }

    val singularCategoryTag = when (event.category) {
        "Концерты" -> "Концерт"
        "Вечеринки" -> "Вечеринка"
        "Бары" -> "Бар"
        "Выставки" -> "Выставка"
        else -> event.category
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 104.dp)
        ) {
            // Hero Image matching Screenshots 4 & 5 1-to-1
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Image(
                    painter = painterResource(id = event.coverRes),
                    contentDescription = event.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color(0x4D0B0B0F),
                                0.35f to Color(0x1A0B0B0F),
                                0.72f to Color(0xBF0B0B0F),
                                1.0f to VryvBg
                            )
                        )
                )

                // Top Floating Circular Glass Buttons (Back on left, Share + Heart on right)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VryvIconButton(
                        icon = VryvIcons.Back,
                        onClick = onBackClick,
                        isGlass = true,
                        testTag = "detail_back_button"
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        VryvIconButton(
                            icon = VryvIcons.Share,
                            onClick = { repository.showToast("Ссылка на мероприятие скопирована") },
                            isGlass = true
                        )
                        VryvIconButton(
                            icon = if (event.isBookmarked) VryvIcons.HeartFill else VryvIcons.Heart,
                            onClick = { repository.toggleSaveEvent(event.id) },
                            isGlass = true,
                            tint = if (event.isBookmarked) VryvPink else VryvTextPrimary,
                            testTag = "detail_bookmark_button"
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Tags Row: Pink Category Pill + Dark Genre Pill + Dark Age Limit Pill (Screenshots 4 & 5)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (event.status != EventStatus.PUBLISHED) {
                        StatusBadge(status = event.status)
                    } else {
                        Box(
                            modifier = Modifier
                                .height(28.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(VryvPink)
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = singularCategoryTag,
                                color = Color.White,
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    val secondaryGenre = event.genres.firstOrNull() ?: "Инди-рок"
                    Box(
                        modifier = Modifier
                            .height(28.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = secondaryGenre,
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(28.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = event.ageLimit,
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Event Title ("Северный Свет: осенний сет")
                Text(
                    text = event.title,
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.7).sp,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Special Status Banners (if cancelled or rescheduled)
                if (event.status == EventStatus.CANCELLED) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x24FF5A5A))
                            .border(1.dp, Color(0x59FF5A5A), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Событие отменено организатором",
                                color = Color(0xFFFF8C8C),
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.cancellationReason
                                    ?: "По техническим причинам мероприятие не состоится. Деньги за предпродажу вернутся автоматически.",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                } else if (event.status == EventStatus.RESCHEDULED) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x24FFC42E))
                            .border(1.dp, Color(0x59FFC42E), RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Новая дата: ${event.dateDisplay}",
                                color = Color(0xFFFFD35A),
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Перенесено с ${event.oldDateDisplay ?: "прежней даты"}. Все записи действительны.",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp
                            )
                        }
                    }
                }

                // 3-Row Unified Info Card matching Screenshots 4 & 5 1-to-1
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Row 1: Date & Doors/Time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(Color(0xFF26262E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Cal,
                                contentDescription = null,
                                tint = VryvTextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.fullDateTitle.ifBlank { event.dateDisplay },
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = event.doorsInfo.ifBlank { "${event.timeDisplay} · двери за 30 минут" },
                                color = VryvTextMuted,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp
                            )
                        }
                    }

                    HorizontalDivider(color = VryvLine, thickness = 1.dp)

                    // Row 2: Venue + Address/Distance + Pink "Маршрут" link
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(Color(0xFF26262E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Pin,
                                contentDescription = null,
                                tint = VryvTextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.venueName,
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${event.address} · ${event.distanceText}",
                                color = VryvTextMuted,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp
                            )
                        }
                        Text(
                            text = "Маршрут",
                            color = VryvPink,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable { onOpenRouteClick(event.id) }
                                .padding(start = 8.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }

                    HorizontalDivider(color = VryvLine, thickness = 1.dp)

                    // Row 3: Price + Tickets left (clickable to open ticket modal)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showTicketSheet = true }
                            .testTag("buy_ticket_button"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(11.dp))
                                .background(Color(0xFF26262E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Ticket,
                                contentDescription = null,
                                tint = VryvTextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.priceString,
                                color = if (event.priceByn == 0.0) VryvLime else VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = event.ticketsLeftText,
                                color = VryvTextMuted,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // "Участники" section matching Screenshots 4 & 5 1-to-1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Участники",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.4).sp
                    )
                    Text(
                        text = "Все ${event.goingCount}",
                        color = VryvTextMuted,
                        fontFamily = InterFontFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { onInviteFriendsClick(event.id) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onInviteFriendsClick(event.id) },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    VryvAvatarStack(
                        avatarResList = listOf(
                            R.drawable.av1,
                            R.drawable.av2,
                            R.drawable.av3,
                            R.drawable.av4
                        ),
                        size = 36.dp,
                        extraCountText = "+8"
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Идут ${event.friendsGoingCount} друзей",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "и ${event.groupsLookingCount} группы ищут компанию",
                            color = VryvTextMuted,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // "О событии" section matching Screenshot 5 1-to-1
                Text(
                    text = "О событии",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.4).sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    color = VryvTextMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))

                // Subtle Report link
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onReportClick("event", event.title) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = VryvIcons.Flag,
                        contentDescription = null,
                        tint = VryvTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Пожаловаться на мероприятие",
                        color = VryvTextMuted,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Sticky Bottom Bar matching Screenshots 4 & 5:
        // Left: Pink pill with Check + "Иду"
        // Right: Dark pill with Users + "Ищу компанию"
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.28f to Color(0xF50B0B0F),
                        1.0f to VryvBg
                    )
                )
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Left button: Pink "Иду"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .background(VryvPink)
                    .clickable { repository.toggleGoingEvent(event.id) }
                    .testTag("action_going_${event.id}"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = VryvIcons.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Иду",
                        color = Color.White,
                        fontFamily = InterFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Right button: Dark "Ищу компанию"
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .clip(RoundedCornerShape(27.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(27.dp))
                    .clickable { onFindCompanyClick(event.id) }
                    .testTag("action_find_company"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = VryvIcons.Users,
                        contentDescription = null,
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Ищу компанию",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Waitlist Sheet
    if (showWaitlistSheet) {
        ModalBottomSheet(
            onDismissRequest = { showWaitlistSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF17171C),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color(0x24C7FF28))
                        .border(1.dp, Color(0x59C7FF28), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Bell,
                        contentDescription = null,
                        tint = VryvLime,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Ты в листе ожидания",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Номер в очереди: #4. Мы сразу пришлём пуш-уведомление, если кто-то отменит запись.",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.5.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                VryvButton(
                    text = "Понятно",
                    onClick = { showWaitlistSheet = false },
                    variant = VryvButtonVariant.PRIMARY
                )
            }
        }
    }

    // Electronic Ticket Sheet
    if (showTicketSheet) {
        ModalBottomSheet(
            onDismissRequest = { showTicketSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF17171C),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Электронный билет",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = event.title,
                    color = VryvPink,
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${event.dateDisplay} · ${event.timeDisplay}",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "QR-код билета",
                        tint = Color.Black,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "VRYV-MSQ-2026-${event.id.hashCode().toString().takeLast(6)}",
                    color = VryvTextMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))
                VryvButton(
                    text = "Сохранить билет",
                    onClick = {
                        showTicketSheet = false
                        repository.showToast("Билет сохранен в Apple Wallet / Google Pay")
                    },
                    variant = VryvButtonVariant.LIME
                )
            }
        }
    }
}
