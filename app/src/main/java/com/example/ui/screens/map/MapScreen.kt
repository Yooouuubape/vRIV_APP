package com.example.ui.screens.map

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvIconButton
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
fun MapScreen(
    repository: VryvRepository,
    onEventClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val events by repository.events.collectAsState()
    var selectedEventId by remember { mutableStateOf("e_techno") }
    var isRouteActive by remember { mutableStateOf(false) }
    var selectedTimeFilter by remember { mutableStateOf("Сейчас") }

    val timeFilters = listOf("Сейчас", "Через час", "Сегодня вечером", "Вся неделя")
    val selectedEvent = events.find { it.id == selectedEventId } ?: events.first()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF10131C))
    ) {
        // Dark stylized Minsk map background matching mapBg() in s2-mvp.js
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Grid streets
            val gridColor = Color(0xFF1A1F2E)
            for (i in 1..6) {
                val y = h * (i / 7f)
                drawLine(color = gridColor, start = Offset(0f, y), end = Offset(w, y), strokeWidth = 2.dp.toPx())
            }
            for (i in 1..4) {
                val x = w * (i / 5f)
                drawLine(color = gridColor, start = Offset(x, 0f), end = Offset(x, h), strokeWidth = 2.dp.toPx())
            }

            // Svisloch river curve
            val riverPath = Path().apply {
                moveTo(-20f, h * 0.25f)
                cubicTo(
                    w * 0.3f, h * 0.34f,
                    w * 0.55f, h * 0.45f,
                    w * 1.05f, h * 0.62f
                )
            }
            drawPath(
                path = riverPath,
                color = Color(0xFF142238),
                style = Stroke(width = 28.dp.toPx(), cap = StrokeCap.Round)
            )

            // Main avenues
            val avenuePath = Path().apply {
                moveTo(-20f, h * 0.68f)
                quadraticBezierTo(w * 0.48f, h * 0.48f, w * 1.05f, h * 0.20f)
            }
            drawPath(
                path = avenuePath,
                color = Color(0xFF232838),
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )

            // Dashed pink route path when route mode is active (matching mapRoute.png)
            if (isRouteActive) {
                val routePath = Path().apply {
                    moveTo(w * 0.49f, h * 0.49f)
                    cubicTo(
                        w * 0.42f, h * 0.43f,
                        w * 0.35f, h * 0.35f,
                        w * 0.28f, h * 0.26f
                    )
                }
                drawPath(
                    path = routePath,
                    color = VryvPink,
                    style = Stroke(
                        width = 4.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 12f), 0f)
                    )
                )
            }
        }

        // Top Bar: either "Маршрут" (mapRoute.png) or "Прямо сейчас" + filter chips (mapNow.png)
        if (isRouteActive) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VryvBg.copy(alpha = 0.85f))
            ) {
                VryvSubScreenHeader(
                    title = "Маршрут",
                    onBackClick = { isRouteActive = false },
                    rightContent = {
                        Text(
                            text = "Открыть в Картах",
                            color = VryvPink,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                repository.showToast("Открываем маршрут в Яндекс / Google Картах")
                            }
                        )
                    }
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Прямо сейчас",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                    VryvIconButton(
                        icon = VryvIcons.Search,
                        onClick = { onEventClick(selectedEvent.id) }
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(timeFilters) { filter ->
                        VryvChip(
                            text = filter,
                            selected = selectedTimeFilter == filter,
                            onClick = { selectedTimeFilter = filter }
                        )
                    }
                }
            }
        }

        // Map Pins & Clusters matching mapNow.png & mapRoute.png
        Box(modifier = Modifier.fillMaxSize()) {
            // Photo pin 1 (top-left: Techno Room)
            Box(
                modifier = Modifier
                    .offset(x = 84.dp, y = 175.dp)
                    .size(44.dp)
                    .shadow(12.dp, RoundedCornerShape(14.dp))
                    .clip(RoundedCornerShape(14.dp))
                    .border(2.5.dp, if (isRouteActive) VryvPink else Color.White, RoundedCornerShape(14.dp))
                    .clickable {
                        selectedEventId = "e_techno"
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.techno),
                    contentDescription = "Techno Room",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (!isRouteActive) {
                // Cluster 1: "7 / техно"
                MapClusterPin(
                    count = "7",
                    label = "техно",
                    modifier = Modifier
                        .offset(x = 185.dp, y = 245.dp)
                        .clickable { selectedEventId = "e_techno" }
                )

                // Cluster 2: "4 / бары"
                MapClusterPin(
                    count = "4",
                    label = "бары",
                    modifier = Modifier
                        .offset(x = 64.dp, y = 370.dp)
                        .clickable { selectedEventId = "e_wine" }
                )

                // Photo pin 2 (Jazz)
                Box(
                    modifier = Modifier
                        .offset(x = 270.dp, y = 385.dp)
                        .size(44.dp)
                        .shadow(12.dp, RoundedCornerShape(14.dp))
                        .clip(RoundedCornerShape(14.dp))
                        .border(2.5.dp, Color.White, RoundedCornerShape(14.dp))
                        .clickable { selectedEventId = "e_jazz" }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.jazz),
                        contentDescription = "Jazz",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Cluster 3: "12 / смешанные"
                MapClusterPin(
                    count = "12",
                    label = "смешанные",
                    modifier = Modifier
                        .offset(x = 150.dp, y = 465.dp)
                        .clickable { selectedEventId = "e_rooftop" }
                )
            }

            // User location lime dot with halo matching .you in s2-mvp.js
            Box(
                modifier = Modifier
                    .offset(x = 175.dp, y = 325.dp)
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(Color(0x38C7FF28)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(VryvLime)
                        .border(3.dp, Color.White, CircleShape)
                )
            }
        }

        // Bottom Floating Card matching mapNow.png or mapRoute.png
        if (isRouteActive) {
            // mapRoute.png bottom card
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 18.dp)
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(24.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(VryvPink),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Route,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "15 мин пешком · 1,1 км",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${selectedEvent.venueName} · ${selectedEvent.venueAddress}",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(VryvPink)
                            .clickable {
                                repository.showToast("Открываем маршрут в Навигаторе")
                            }
                            .testTag("map_route_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Открыть в Картах",
                            color = Color.White,
                            fontFamily = InterFontFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                            .clickable {
                                repository.showToast("Ссылка на маршрут скопирована для друзей")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Позвать с собой",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        } else {
            // mapNow.png bottom card
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 18.dp)
                    .fillMaxWidth()
                    .shadow(20.dp, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(24.dp))
                    .clickable { onEventClick(selectedEvent.id) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = selectedEvent.coverRes),
                    contentDescription = selectedEvent.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = selectedEvent.title,
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${selectedEvent.distanceKm} · идёт сейчас · до 01:00",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 12.5.sp,
                        maxLines = 1
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(VryvPink)
                        .clickable { isRouteActive = true }
                        .padding(horizontal = 14.dp)
                        .testTag("map_route_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = VryvIcons.Route,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "Маршрут",
                            color = Color.White,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MapClusterPin(
    count: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .size(54.dp)
            .shadow(14.dp, CircleShape)
            .clip(CircleShape)
            .background(VryvPink)
            .border(3.dp, Color(0x40FFFFFF), CircleShape),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = count,
            color = Color.White,
            fontFamily = InterFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 15.sp
        )
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontFamily = InterFontFamily,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 9.sp
        )
    }
}
