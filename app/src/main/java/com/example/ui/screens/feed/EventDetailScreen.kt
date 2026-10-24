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
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvTag
import com.example.ui.components.VryvTagVariant
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp)
        ) {
            // Hero Image matching screen_04_карточка_события.png
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
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
                                0.0f to Color(0x660B0B0F),
                                0.35f to Color(0x260B0B0F),
                                0.80f to Color(0xD90B0B0F),
                                1.0f to VryvBg
                            )
                        )
                )

                // Top Glass Buttons
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
                            icon = VryvIcons.Save,
                            onClick = { repository.toggleSaveEvent(event.id) },
                            isGlass = true,
                            tint = if (event.isBookmarked) VryvPink else VryvTextPrimary,
                            testTag = "detail_bookmark_button"
                        )
                    }
                }

                // Bottom of Hero: Tags + Title
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (event.status != EventStatus.PUBLISHED) {
                            StatusBadge(status = event.status)
                        } else {
                            VryvTag(text = event.category, variant = VryvTagVariant.PINK)
                        }
                        VryvTag(text = event.ageLimit, variant = VryvTagVariant.DARK)
                        VryvTag(text = "По твоим интересам", variant = VryvTagVariant.DARK)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = event.title,
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.7).sp,
                        lineHeight = 30.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Special Status Banners (evCancel.png, evMoved.png, evDone.png, evFull.png)
                if (event.status == EventStatus.CANCELLED) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
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
                                text = event.cancellationReason ?: "По техническим причинам мероприятие не состоится. Деньги за предпродажу вернутся автоматически.",
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
                            .padding(bottom = 12.dp)
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

                // 3-Row Unified Info Card (Date/Time, Venue + Маршрут, Price + Билет)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Row 1: Date & Time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF26262F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Cal,
                                contentDescription = null,
                                tint = VryvPink,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${event.dateDisplay} · ${event.timeDisplay}",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Сбор гостей за 30 минут",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp
                            )
                        }
                    }

                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)

                    // Row 2: Venue & Route
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF26262F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Pin,
                                contentDescription = null,
                                tint = VryvPink,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.venueName,
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${event.address} · 1,1 км от тебя",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(Color(0xFF26262F))
                                .clickable { onOpenRouteClick(event.id) }
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Маршрут",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)

                    // Row 3: Price & Ticket
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF26262F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Ticket,
                                contentDescription = null,
                                tint = VryvPink,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Вход · ${event.priceString}",
                                color = if (event.priceByn == 0.0) VryvLime else VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "На входе или предпродажа",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(VryvLime)
                                .clickable { showTicketSheet = true }
                                .padding(horizontal = 12.dp)
                                .testTag("buy_ticket_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (event.priceByn == 0.0) "Регистрация" else "Купить билет",
                                color = VryvBg,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Participants Card matching screen_04
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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
                            text = "Участники",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Все ${event.goingCount} >",
                            color = VryvPink,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { onInviteFriendsClick(event.id) }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
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
                            size = 34.dp,
                            extraCountText = "+${(event.goingCount - 4).coerceAtLeast(12)}"
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "4 твоих друга идут: Аня, Влад, Кирилл, Даша",
                                color = Color(0xFFD0D0DC),
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp,
                                lineHeight = 17.sp
                            )
                            Text(
                                text = "12 человек ищут компанию",
                                color = VryvPink,
                                fontFamily = InterFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        VryvButton(
                            text = "Пригласить друзей",
                            onClick = { onInviteFriendsClick(event.id) },
                            variant = VryvButtonVariant.SECONDARY,
                            isSmall = true,
                            modifier = Modifier.weight(1f)
                        )
                        VryvButton(
                            text = "Лист ожидания",
                            onClick = { showWaitlistSheet = true },
                            variant = VryvButtonVariant.SECONDARY,
                            isSmall = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description Section
                Text(
                    text = "О событии",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = event.description,
                    color = Color(0xFFC6C6D2),
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    event.genres.forEach { genre ->
                        VryvChip(
                            text = genre,
                            selected = false,
                            onClick = {}
                        )
                    }
                    VryvChip(
                        text = event.ageLimit,
                        selected = false,
                        onClick = {}
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(8.dp))

                // Report button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onReportClick("event", event.title) }
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = VryvIcons.Flag,
                        contentDescription = null,
                        tint = VryvPink,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Пожаловаться на мероприятие",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Sticky Bottom Bar matching screen_04_карточка_события.png
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.25f to Color(0xF50B0B0F),
                        1.0f to VryvBg
                    )
                )
                .border(0.5.dp, VryvLine)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VryvButton(
                text = if (event.isGoing) "✓ Иду (${event.goingCount})" else "Я пойду (${event.goingCount})",
                onClick = { repository.toggleGoingEvent(event.id) },
                variant = VryvButtonVariant.PRIMARY,
                modifier = Modifier.weight(1f),
                testTag = "action_going_${event.id}"
            )
            VryvButton(
                text = "Ищу компанию",
                onClick = { onFindCompanyClick(event.id) },
                variant = VryvButtonVariant.SECONDARY,
                leadingIcon = {
                    Icon(
                        imageVector = VryvIcons.Users,
                        contentDescription = null,
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier.weight(1.1f),
                testTag = "action_find_company"
            )
        }
    }

    // Waitlist Sheet (waitOk.png)
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
