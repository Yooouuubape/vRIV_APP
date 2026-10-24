package com.example.ui.screens.map

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
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
import androidx.compose.ui.graphics.Color
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
import com.example.data.VryvEvent
import com.example.data.VryvRepository
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

    // Exclude cancelled, completed, draft, moderation events
    val activeEvents = remember(allEvents) {
        allEvents.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
    }

    var selectedTimeFilter by remember { mutableStateOf("Все") }
    val timeFilters = listOf("Все", "Прямо сейчас", "Сегодня вечер", "Завтра")

    // Filter map markers according to selected time
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
        // Dark Map Canvas
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val mapWidth = maxWidth
            val mapHeight = maxHeight

            Image(
                painter = painterResource(id = R.drawable.minsk_dark),
                contentDescription = "Карта Минска (Демо)",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Event Pins on map
            visibleEvents.forEach { event ->
                val isSelected = event.id == selectedEvent?.id
                val pinX = (event.mapX * mapWidth.value).dp
                val pinY = (event.mapY * mapHeight.value).dp

                Box(
                    modifier = Modifier
                        .offset(x = pinX - 22.dp, y = pinY - 22.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) VryvPink else VryvCard.copy(alpha = 0.9f))
                        .border(
                            width = if (isSelected) 2.5.dp else 1.5.dp,
                            color = if (isSelected) Color.White else VryvLime,
                            shape = CircleShape
                        )
                        .clickable { selectedEventId = event.id }
                        .testTag("map_pin_${event.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = event.title,
                        tint = if (isSelected) Color.White else VryvLime,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Top Header Controls
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp)
        ) {
            // Time filter pills
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xDD1A1A20))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                timeFilters.forEach { filter ->
                    val isSelected = filter == selectedTimeFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) VryvPink else Color.Transparent)
                            .clickable {
                                selectedTimeFilter = filter
                                selectedEventId = null
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = filter,
                            color = if (isSelected) Color.White else VryvTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Демонстрационная карта событий Минска · точек: ${visibleEvents.size}",
                color = VryvTextMuted,
                fontSize = 11.sp,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        // Bottom Selected Event Card Preview
        if (selectedEvent != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                    .clickable { onEventClick(selectedEvent.id) }
                    .padding(14.dp)
                    .testTag("map_event_preview")
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = selectedEvent.coverRes),
                            contentDescription = selectedEvent.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(68.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = selectedEvent.title,
                                color = VryvTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${selectedEvent.dateDisplay} · ${selectedEvent.timeDisplay}",
                                color = VryvPink,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${selectedEvent.venueName} · ${selectedEvent.priceString}",
                                color = VryvTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(VryvLime.copy(alpha = 0.15f))
                                .clickable { showRouteSheet = true }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("map_route_action_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Directions,
                                contentDescription = null,
                                tint = VryvLime,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Маршрут",
                                color = VryvLime,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { onEventClick(selectedEvent.id) }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Подробнее",
                                color = VryvTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = VryvTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Route Sheet (mapRoute)
    if (showRouteSheet && selectedEvent != null) {
        ModalBottomSheet(
            onDismissRequest = { showRouteSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VryvCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Маршрут: ${selectedEvent.venueName}",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = selectedEvent.address,
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Steps
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvBg)
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
                            Text("1", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Двигайтесь в сторону исторического центра / Немиги",
                            color = VryvTextPrimary,
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
                            Text("2", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Прибытие по адресу: ${selectedEvent.address}",
                            color = VryvTextPrimary,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvLime)
                        .clickable {
                            showRouteSheet = false
                            try {
                                val geoUri = Uri.parse("geo:0,0?q=${Uri.encode(selectedEvent.address)}")
                                val intent = Intent(Intent.ACTION_VIEW, geoUri)
                                context.startActivity(intent)
                            } catch (_: Exception) {
                                repository.showToast("Адрес: ${selectedEvent.address}")
                            }
                        }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Открыть на карте устройства",
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
