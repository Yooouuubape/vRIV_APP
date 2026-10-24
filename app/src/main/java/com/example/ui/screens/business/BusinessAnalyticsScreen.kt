package com.example.ui.screens.business

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvIcons
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
fun BusinessAnalyticsScreen(
    repository: VryvRepository,
    modifier: Modifier = Modifier
) {
    val venue by repository.businessVenue.collectAsState()
    val allEvents by repository.events.collectAsState()
    var selectedPeriodChip by remember { mutableStateOf("7 дней") }

    // STRICT ISOLATION: Only calculate analytics for this venue!
    val venueEvents = remember(allEvents, venue.id) {
        allEvents.filter { it.venueId == venue.id }
    }

    val totalViews = venueEvents.sumOf { it.viewsCount }
    val totalGoing = venueEvents.sumOf { it.goingCount }
    val totalSaves = venueEvents.count { it.isBookmarked } * 120 + 612

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header matching d4.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Аудитория",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.6).sp
            )
            Text(
                text = "Экспорт",
                color = VryvPink,
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chips = listOf("7 дней", "30 дней", "90 дней", "Все события")
            items(chips) { ch ->
                val isSel = selectedPeriodChip == ch
                VryvChip(
                    text = ch,
                    selected = isSel,
                    onClick = { selectedPeriodChip = ch },
                    trailingContent = if (ch == "Все события") {
                        {
                            Icon(
                                imageVector = VryvIcons.ChevronDown,
                                contentDescription = null,
                                tint = VryvTextSecondary,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    } else null
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Воронка от показа до «Иду» matching d4.png 1-to-1
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Воронка от показа до «Иду»",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    FunnelStep(label = "Показы в ленте", value = "18 400", percent = 1.0f, color = VryvPink)
                    FunnelStep(label = "Открыли карточку", value = "$totalViews · 26%", percent = 0.56f, color = VryvPink)
                    FunnelStep(label = "Сохранили", value = "$totalSaves · 12,7%", percent = 0.28f, color = VryvPink)
                    FunnelStep(label = "Нажали «Собираюсь»", value = "$totalGoing · 3,8%", percent = 0.14f, color = VryvLime)
                }
            }

            // 2. Просмотры по дням bar chart matching d4.png 1-to-1
            item {
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
                            text = "Просмотры по дням",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "+18% к прошлой нед.",
                            color = VryvLime,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    val chartData = listOf(
                        Triple("Пн", 0.38f, false),
                        Triple("Вт", 0.44f, false),
                        Triple("Ср", 0.52f, false),
                        Triple("Чт", 0.68f, false),
                        Triple("Пт", 0.96f, true),
                        Triple("Сб", 1.00f, true),
                        Triple("Вс", 0.62f, false)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        chartData.forEach { (day, ratio, isPeak) ->
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height((82 * ratio).dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isPeak) VryvPink else VryvCardElevated)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = day,
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // 3. Портрет гостей (2x2 Grid) matching d4.png 1-to-1
            item {
                Text(
                    text = "Портрет гостей",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PortraitBox(
                            title = "22–28 лет",
                            subtitle = "58% аудитории",
                            modifier = Modifier.weight(1f)
                        )
                        PortraitBox(
                            title = "Электроника, Бары",
                            subtitle = "Главные интересы",
                            titleSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        PortraitBox(
                            title = "Пт–Сб, 20–23:00",
                            subtitle = "Пик активности",
                            titleSize = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                        PortraitBox(
                            title = "30–50 BYN",
                            subtitle = "Комфортный чек",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Events breakdown for this venue
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Эффективность по событиям (${venueEvents.size})",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    venueEvents.forEachIndexed { idx, ev ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = ev.title, color = VryvTextPrimary, fontSize = 14.5.sp, fontWeight = FontWeight.SemiBold)
                                Text(text = "${ev.dateDisplay} · ${ev.status.label}", color = VryvTextSecondary, fontSize = 12.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "${ev.goingCount} идут", color = VryvLime, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                                Text(text = "${ev.viewsCount} просм.", color = VryvTextMuted, fontSize = 11.5.sp)
                            }
                        }
                        if (idx < venueEvents.lastIndex) {
                            HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

@Composable
private fun PortraitBox(
    title: String,
    subtitle: String,
    titleSize: androidx.compose.ui.unit.TextUnit = 20.sp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Text(
            text = title,
            color = VryvTextPrimary,
            fontFamily = InterFontFamily,
            fontSize = titleSize,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            color = VryvTextSecondary,
            fontFamily = InterFontFamily,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun FunnelStep(label: String, value: String, percent: Float, color: Color) {
    Column(modifier = Modifier.padding(vertical = 5.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = VryvTextSecondary, fontFamily = InterFontFamily, fontSize = 13.sp)
            Text(text = value, color = VryvTextPrimary, fontFamily = InterFontFamily, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(5.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(VryvCardElevated)
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
