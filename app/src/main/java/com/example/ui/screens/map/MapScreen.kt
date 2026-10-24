package com.example.ui.screens.map

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonVariant
import com.example.ui.components.VryvChip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    repository: VryvRepository,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val allEvents by repository.events.collectAsState()

    val activeEvents = remember(allEvents) {
        allEvents.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
    }

    var selectedTimeFilter by remember { mutableStateOf("Все") }
    val timeFilters = listOf("Все", "Прямо сейчас", "Сегодня вечер", "Завтра")

    val visibleEvents = remember(activeEvents, selectedTimeFilter) {
        when (selectedTimeFilter) {
            "Прямо сейчас" -> activeEvents.filter { it.dateCategory == "TODAY" && (it.timeDisplay.contains("18:") || it.timeDisplay.contains("20:") || it.timeDisplay.contains("12:")) }
            "Сегодня вечер" -> activeEvents.filter { it.dateCategory == "TODAY" }
            "Завтра" -> activeEvents.filter { it.dateCategory == "TOMORROW" || it.dateCategory == "WEEKEND" }
            else -> activeEvents
        }
    }

    var selectedEventId by remember { mutableStateOf<String?>(visibleEvents.firstOrNull()?.id) }
    val selectedEvent = visibleEvents.find { it.id == selectedEventId } ?: visibleEvents.firstOrNull()

    var showRouteSheet by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Dark Map Canvas matching screen_03, mapNow, mapRoute
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val mapWidth = maxWidth
            val mapHeight = maxHeight

            Image(
                painter = painterResource(id = R.drawable.minsk_dark),
                contentDescription = "Карта Минска (Демо)",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient scrims at top and bottom of map
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color(0xD90B0B0F),
                            0.20f to Color.Transparent,
                            0.72f to Color.Transparent,
                            1.0f to Color(0xF50B0B0F)
                        )
                    )
            )

            // Dashed pink route path on map when selectedEvent is present
            if (selectedEvent != null) {
                val destXRatio = selectedEvent.mapX
                val destYRatio = selectedEvent.mapY
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val startX = size.width * 0.48f
                    val startY = size.height * 0.54f
                    val endX = size.width * destXRatio
                    val endY = size.height * destYRatio
                    val path = Path().apply {
                        moveTo(startX, startY)
                        quadraticBezierTo(
                            (startX + endX) / 2f + 40f,
                            (startY + endY) / 2f - 30f,
                            endX,
                            endY
                        )
                    }
                    drawPath(
                        path = path,
                        color = Color(0x4DFF1D8E),
                        style = Stroke(width = 24f, cap = StrokeCap.Round)
                    )
                    drawPath(
                        path = path,
                        color = Color(0xFFFF1D8E),
                        style = Stroke(
                            width = 9f,
                            cap = StrokeCap.Round,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(22f, 16f), 0f)
                        )
                    )
                }
            }

            // User Location Dot (18dp #C7FF28 with 48dp halo)
            Box(
                modifier = Modifier
                    .offset(x = (mapWidth * 0.48f) - 24.dp, y = (mapHeight * 0.54f) - 24.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0x38C7FF28)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(VryvLime)
                        .border(3.dp, VryvBg, CircleShape)
                )
            }

            // Cluster bubble (52dp #FF1D8E circle)
            Box(
                modifier = Modifier
                    .offset(x = (mapWidth * 0.25f) - 26.dp, y = (mapHeight * 0.34f) - 26.dp)
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(VryvPink)
                    .border(3.dp, Color(0x40FFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${visibleEvents.size}",
                    color = Color.White,
                    fontFamily = InterFontFamily,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Event Photo Pins on map (44x44dp r14)
            visibleEvents.forEach { event ->
                val isSelected = event.id == selectedEvent?.id
                val pinX = (event.mapX * mapWidth.value).dp
                val pinY = (event.mapY * mapHeight.value).dp

                Column(
                    modifier = Modifier
                        .offset(x = pinX - 24.dp, y = pinY - 24.dp)
                        .clickable { selectedEventId = event.id }
                        .testTag("map_pin_${event.id}"),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (isSelected) 48.dp else 44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvCard)
                            .border(
                                width = 2.5.dp,
                                color = if (isSelected) VryvPink else Color.White,
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        Image(
                            painter = painterResource(id = event.coverRes),
                            contentDescription = event.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(VryvPink)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = event.priceString,
                                color = Color.White,
                                fontFamily = InterFontFamily,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Top Header Controls matching screen_03 & mapNow
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(23.dp))
                        .background(Color(0xD91A1A20))
                        .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(23.dp))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = VryvIcons.Search,
                        contentDescription = null,
                        tint = VryvTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Прямо сейчас · ${visibleEvents.size} событий рядом",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xD91A1A20))
                        .border(1.dp, Color(0x14FFFFFF), CircleShape)
                        .clickable { repository.showToast("Показаны точки Минска (${visibleEvents.size})") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Sliders,
                        contentDescription = null,
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Time filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                timeFilters.forEach { filter ->
                    val isSelected = filter == selectedTimeFilter
                    VryvChip(
                        text = filter,
                        selected = isSelected,
                        onClick = {
                            selectedTimeFilter = filter
                            selectedEventId = null
                        },
                        leadingDotColor = if (filter == "Прямо сейчас") VryvLime else null
                    )
                }
            }
        }

        // Bottom Selected Event Card Preview (screen_03 & mapNow)
        if (selectedEvent != null) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 96.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xF01C1C23))
                    .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(24.dp))
                    .clickable { onEventClick(selectedEvent.id) }
                    .padding(14.dp)
                    .testTag("map_event_preview"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = selectedEvent.coverRes),
                    contentDescription = selectedEvent.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(84.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        VryvTag(
                            text = "Через 40 мин",
                            variant = VryvTagVariant.LIME
                        )
                        Text(
                            text = "900 м · 11 мин",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 11.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = selectedEvent.title,
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "${selectedEvent.venueName} · ${selectedEvent.address}",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 12.5.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedEvent.priceString,
                            color = if (selectedEvent.priceByn == 0.0) VryvLime else VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(17.dp))
                                    .background(VryvPink)
                                    .clickable { showRouteSheet = true }
                                    .padding(horizontal = 14.dp)
                                    .testTag("map_route_action_button"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Маршрут",
                                    color = Color.White,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Route Sheet matching mapRoute.png 1-to-1
    if (showRouteSheet && selectedEvent != null) {
        ModalBottomSheet(
            onDismissRequest = { showRouteSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF17171C),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF3A3A44))
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "15 мин пешком · 1,1 км",
                            color = VryvLime,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${selectedEvent.venueName} · ${selectedEvent.address}",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${selectedEvent.title} · Начало в ${selectedEvent.timeDisplay}",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Route steps
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCard)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(VryvPink),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("1", color = Color.White, fontFamily = InterFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Двигайтесь вдоль Немиги / Зыбицкой в сторону центра",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(VryvLime),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("2", color = Color.Black, fontFamily = InterFontFamily, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Прибытие по адресу: ${selectedEvent.address}",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    VryvButton(
                        text = "Открыть в Картах",
                        onClick = {
                            showRouteSheet = false
                            try {
                                val geoUri = Uri.parse("geo:0,0?q=${Uri.encode(selectedEvent.address)}")
                                val intent = Intent(Intent.ACTION_VIEW, geoUri)
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                repository.showToast("Адрес: ${selectedEvent.address}")
                            }
                        },
                        variant = VryvButtonVariant.PRIMARY,
                        modifier = Modifier.weight(1.2f)
                    )
                    VryvButton(
                        text = "Подробнее",
                        onClick = {
                            showRouteSheet = false
                            onEventClick(selectedEvent.id)
                        },
                        variant = VryvButtonVariant.SECONDARY,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
