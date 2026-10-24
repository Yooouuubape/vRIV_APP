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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvTag
import com.example.ui.components.VryvTagVariant
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

@Composable
fun BusinessCabinetScreen(
    repository: VryvRepository,
    onCreateEventClick: () -> Unit,
    onSwitchToPersonalClick: () -> Unit,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val venue by repository.businessVenue.collectAsState()
    val events by repository.events.collectAsState()
    val period by repository.businessMetricsPeriod.collectAsState()
    val metricsMap by repository.businessMetricsMap.collectAsState()

    val currentMetrics = metricsMap[period] ?: metricsMap["7 дней"] ?: metricsMap["Сегодня"]!!
    val venueEvents = events.filter { it.venueId == venue.id }
    val activeEvents = venueEvents.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Business Header matching d1.png 1-to-1
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
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSwitchToPersonalClick() }
                    .padding(vertical = 4.dp, horizontal = 4.dp)
                    .testTag("biz_header_role_switch")
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rooftop),
                    contentDescription = venue.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = venue.name,
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = VryvIcons.ChevronDown,
                    contentDescription = "Сменить профиль",
                    tint = VryvTextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                VryvTag(
                    text = "Подтверждено",
                    variant = VryvTagVariant.LIME
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box {
                    VryvIconButton(
                        icon = VryvIcons.Bell,
                        onClick = { repository.showToast("Новые уведомления площадки") }
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(VryvPink)
                    )
                }
                VryvIconButton(
                    icon = VryvIcons.Gear,
                    onClick = onSwitchToPersonalClick
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp)
        ) {
            // Period switcher chips (7 дней / 30 дней / Сегодня) matching d1.png
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("7 дней", "30 дней", "Сегодня").forEach { p ->
                        VryvChip(
                            text = p,
                            selected = p == period,
                            onClick = { repository.setMetricsPeriod(p) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 6 Metric Cards (2x3 Grid) matching d1.png 1-to-1
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            label = "Просмотры",
                            value = "${currentMetrics.views}",
                            dynamic = "+18%",
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Сохранения",
                            value = "${currentMetrics.saves}",
                            dynamic = "+24%",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            label = "Идут",
                            value = "${currentMetrics.going}",
                            dynamic = "+9%",
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Переходы к билетам",
                            value = "${currentMetrics.ticketClicks}",
                            dynamic = "+12%",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            label = "Конверсия в «Иду»",
                            value = currentMetrics.conversionRate,
                            dynamic = "+0.4 п.п.",
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Новые подписчики",
                            value = "${currentMetrics.newGuests}",
                            dynamic = "+15%",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Two action buttons: "+ Создать событие" (Pink) & "Все события" (Secondary) matching d1.png
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(VryvPink)
                            .clickable { onCreateEventClick() }
                            .testTag("biz_create_event_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = VryvIcons.Plus,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Создать событие",
                                color = Color.White,
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                            .clickable {
                                val firstId = activeEvents.firstOrNull()?.id ?: events.first().id
                                onEventClick(firstId)
                            }
                            .padding(horizontal = 18.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Все события",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Section: Активные события matching d1.png
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Активные события · ${activeEvents.size}",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Все",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    activeEvents.forEach { ev ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp))
                                .background(VryvCard)
                                .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                                .clickable { onEventClick(ev.id) }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = ev.coverRes),
                                contentDescription = ev.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(14.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = ev.title,
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${ev.dateDisplay} · ${ev.timeDisplay.substringBefore("–").trim()} · ${ev.priceString}",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp
                                )
                                Text(
                                    text = "${ev.viewsCount} просмотров · ${ev.goingCount} идут",
                                    color = VryvLime,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            VryvTag(
                                text = "Активно",
                                variant = VryvTagVariant.LIME
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Section: Вопросы гостей · 2 новых matching d1.png
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Вопросы гостей · 2 новых",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Все",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                VryvGroupList {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 13.dp),
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
                            Text(
                                text = "Алина · по «Rooftop Sunset»",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Столики нужно бронировать заранее?",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp
                            )
                        }
                        Text(
                            text = "12 мин",
                            color = VryvPink,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    HorizontalDivider(color = VryvLine, thickness = 1.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.av4),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Дима · по «Северный Свет»",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "До скольки работает вход?",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp
                            )
                        }
                        Text(
                            text = "1 ч",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun MetricCard(
    label: String,
    value: String,
    dynamic: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = value,
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = dynamic,
                    color = VryvLime,
                    fontFamily = InterFontFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 12.sp
            )
        }
    }
}
