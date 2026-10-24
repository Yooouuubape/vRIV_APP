package com.example.ui.screens.business

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun BusinessAnalyticsScreen(
    repository: VryvRepository,
    modifier: Modifier = Modifier
) {
    val venue by repository.businessVenue.collectAsState()
    val allEvents by repository.events.collectAsState()

    // STRICT ISOLATION: Only calculate analytics for this venue!
    val venueEvents = remember(allEvents, venue.id) {
        allEvents.filter { it.venueId == venue.id }
    }

    val totalViews = venueEvents.sumOf { it.viewsCount }
    val totalGoing = venueEvents.sumOf { it.goingCount }
    val totalTickets = venueEvents.sumOf { it.ticketClicksCount }
    val totalSaves = venueEvents.count { it.isBookmarked } * 24 + 48

    val conversionRate = if (totalViews > 0) String.format("%.1f%%", (totalGoing.toFloat() / totalViews.toFloat()) * 100f) else "0.0%"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Аудитория и воронка",
                color = VryvTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Показатели площадки «${venue.name}»",
                color = VryvTextSecondary,
                fontSize = 13.sp
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Conversion Funnel
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(VryvCard)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Воронка интереса событий площадки",
                        color = VryvTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    FunnelStep(label = "1. Просмотры в ленте", value = "$totalViews", percent = 1.0f, color = Color(0xFF6A6A76))
                    FunnelStep(label = "2. Сохранения в закладки", value = "$totalSaves", percent = 0.55f, color = VryvPink)
                    FunnelStep(label = "3. Намерение «Иду»", value = "$totalGoing", percent = 0.35f, color = VryvLime)
                    FunnelStep(label = "4. Переходы к билетам", value = "$totalTickets", percent = 0.22f, color = Color(0xFF00E5FF))

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Конверсия из просмотра в намерение посетить: $conversionRate",
                        color = VryvLime,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Events breakdown
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(VryvCard)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Эффективность по событиям (${venueEvents.size})",
                        color = VryvTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (venueEvents.isEmpty()) {
                        Text(text = "У заведения пока нет созданных событий", color = VryvTextSecondary, fontSize = 13.sp)
                    } else {
                        venueEvents.forEach { ev ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = ev.title, color = VryvTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                    Text(text = "${ev.dateDisplay} · ${ev.status.label}", color = VryvTextSecondary, fontSize = 12.sp)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "${ev.goingCount} идут", color = VryvLime, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    Text(text = "${ev.viewsCount} просм.", color = VryvTextMuted, fontSize = 11.5.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FunnelStep(label: String, value: String, percent: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = VryvTextSecondary, fontSize = 13.sp)
            Text(text = value, color = VryvTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(VryvBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(percent)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}
