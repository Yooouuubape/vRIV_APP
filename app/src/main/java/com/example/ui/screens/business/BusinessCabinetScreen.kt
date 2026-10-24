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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
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
        // Business Header
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
                    .clickable { onSwitchToPersonalClick() }
                    .padding(vertical = 4.dp, horizontal = 6.dp)
                    .testTag("biz_header_role_switch")
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(VryvPink),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "13",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = venue.name,
                            color = VryvTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Сменить профиль",
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(VryvLime)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Подтверждено · ${venue.category}",
                            color = VryvTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(VryvCard)
                    .clickable { onSwitchToPersonalClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = VryvTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Личный", color = VryvTextSecondary, fontSize = 11.sp)
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Period switcher (Сегодня / 7 дней / 30 дней)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VryvCard)
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
                                .padding(vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = p,
                                color = if (isSelected) Color.Black else VryvTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // 6 Metric Cards
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

            // Nearest Event Card
            item {
                Text(
                    text = "Ближайшее мероприятие",
                    color = VryvTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCard)
                        .clickable { onEventClick(nearestEvent.id) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = nearestEvent.coverRes),
                        contentDescription = nearestEvent.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = nearestEvent.title,
                            color = VryvTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${nearestEvent.dateDisplay} · ${nearestEvent.timeDisplay}",
                            color = VryvLime,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${nearestEvent.goingCount} планируют пойти",
                            color = VryvTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = VryvTextMuted
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Quick Action: Add Event
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvLime)
                        .clickable { onCreateEventClick() }
                        .padding(vertical = 14.dp)
                        .testTag("biz_create_event_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Создать мероприятие",
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
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
            .clip(RoundedCornerShape(14.dp))
            .background(VryvCard)
            .padding(14.dp)
    ) {
        Column {
            Text(text = label, color = VryvTextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = value, color = VryvTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = dynamic, color = VryvLime, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
