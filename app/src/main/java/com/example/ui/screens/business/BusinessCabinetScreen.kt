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
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvStatusPill
import com.example.ui.components.VryvStatusStyle
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

    val currentMetrics = metricsMap[period] ?: metricsMap["Сегодня"]!!
    val venueEvents = events.filter { it.venueId == venue.id }
    val nearestEvent = venueEvents.firstOrNull { it.status == EventStatus.PUBLISHED } ?: venueEvents.firstOrNull() ?: events.first()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Business Header matching d1.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSwitchToPersonalClick() }
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .testTag("biz_header_role_switch")
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VryvLime.copy(alpha = 0.18f))
                        .border(1.dp, VryvLime.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "13",
                        color = VryvLime,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = venue.name,
                            color = VryvTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = VryvIcons.ChevronDown,
                            contentDescription = "Сменить профиль",
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(VryvLime)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Подтверждено · ${venue.category}",
                            color = VryvTextSecondary,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(VryvCardElevated)
                    .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                    .clickable { onSwitchToPersonalClick() }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = VryvIcons.User,
                        contentDescription = null,
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "Личный",
                        color = VryvTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Period switcher (Сегодня / 7 дней / 30 дней) matching d1.png
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                        .padding(4.dp)
                ) {
                    listOf("Сегодня", "7 дней", "30 дней").forEach { p ->
                        val isSelected = p == period
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) VryvLime else Color.Transparent)
                                .clickable { repository.setMetricsPeriod(p) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = p,
                                color = if (isSelected) Color(0xFF0B0B0F) else VryvTextSecondary,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 6 Metric Cards (2x3 Grid) matching d1.png
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
                            dynamic = "+12%",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            label = "Намерение «Иду»*",
                            value = "${currentMetrics.going}",
                            dynamic = "+24%",
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Клики по билетам",
                            value = "${currentMetrics.ticketClicks}",
                            dynamic = "+8%",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        MetricCard(
                            label = "Конверсия",
                            value = currentMetrics.conversionRate,
                            dynamic = "+1.4%",
                            modifier = Modifier.weight(1f)
                        )
                        MetricCard(
                            label = "Новые гости",
                            value = "${currentMetrics.newGuests}",
                            dynamic = "+15%",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Text(
                    text = "*«Иду» фиксирует интерес пользователя, а не фактический визит на мероприятие.",
                    color = VryvTextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Nearest Event Card matching d1.png
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ближайшее мероприятие",
                        color = VryvTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    VryvStatusPill("Опубликовано", VryvStatusStyle.Lime)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                        .clickable { onEventClick(nearestEvent.id) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = nearestEvent.coverRes),
                        contentDescription = nearestEvent.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nearestEvent.title,
                            color = VryvTextPrimary,
                            fontSize = 15.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${nearestEvent.dateDisplay} · ${nearestEvent.timeDisplay}",
                            color = VryvLime,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${nearestEvent.goingCount} планируют пойти · ${nearestEvent.viewsCount} просм.",
                            color = VryvTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = VryvIcons.ChevronRight,
                        contentDescription = null,
                        tint = VryvTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Quick Action: Add Event (preserving testTag("biz_create_event_btn"))
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvLime)
                        .clickable { onCreateEventClick() }
                        .testTag("biz_create_event_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = VryvIcons.Plus,
                            contentDescription = null,
                            tint = Color(0xFF0B0B0F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Создать мероприятие",
                            color = Color(0xFF0B0B0F),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
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
            Text(text = label, color = VryvTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = value,
                    color = VryvTextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(VryvLime.copy(alpha = 0.14f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(text = dynamic, color = VryvLime, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
