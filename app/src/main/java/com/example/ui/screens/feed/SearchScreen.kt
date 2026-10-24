package com.example.ui.screens.feed

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.EventStatus
import com.example.data.VryvRepository
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvSegmentedControl
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun SearchScreen(
    repository: VryvRepository,
    onEventClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val events by repository.events.collectAsState()
    val venues by repository.allVenues.collectAsState()

    var query by remember { mutableStateOf("техно") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Списком, 1 = На карте / Места
    var activeFilterChip by remember { mutableStateOf("Клубы") }

    val filterChips = listOf("Клубы", "Сегодня", "До 50 BYN", "До 2 км", "Ещё")

    val matchedEvents = remember(events, query) {
        val published = events.filter { it.status == EventStatus.PUBLISHED || it.status == EventStatus.PLANNED }
        if (query.isBlank() || query.equals("техно", ignoreCase = true)) {
            published
        } else {
            published.filter { ev ->
                ev.title.contains(query, ignoreCase = true) ||
                    ev.venueName.contains(query, ignoreCase = true) ||
                    ev.venueAddress.contains(query, ignoreCase = true) ||
                    ev.category.contains(query, ignoreCase = true) ||
                    ev.genres.any { it.contains(query, ignoreCase = true) }
            }
        }
    }

    val matchedVenues = remember(venues, query) {
        if (query.isBlank() || query.equals("техно", ignoreCase = true)) {
            venues
        } else {
            venues.filter { v ->
                v.name.contains(query, ignoreCase = true) ||
                    v.address.contains(query, ignoreCase = true) ||
                    v.category.contains(query, ignoreCase = true) ||
                    v.genres.any { it.contains(query, ignoreCase = true) }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header matching search.png 1-to-1
        VryvSubScreenHeader(
            title = "Поиск",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Search bar with focused border and Clear X button matching search.png
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(VryvCard)
                    .border(1.5.dp, VryvPink, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = VryvIcons.Search,
                    contentDescription = null,
                    tint = VryvTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                BasicTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 15.sp
                    ),
                    cursorBrush = SolidColor(VryvPink),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("search_input"),
                    decorationBox = { innerTextField ->
                        if (query.isEmpty()) {
                            Text(
                                text = "Событие, место, жанр...",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                )
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = VryvIcons.Close,
                        contentDescription = "Очистить",
                        tint = VryvTextSecondary,
                        modifier = Modifier
                            .size(16.dp)
                            .clickable { query = "" }
                    )
                }
            }
        }

        // Filter chips row with dropdown chevrons matching search.png
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterChips) { chip ->
                val isSel = activeFilterChip == chip
                VryvChip(
                    text = chip,
                    selected = isSel,
                    onClick = { activeFilterChip = chip },
                    trailingContent = if (chip != "Ещё") {
                        {
                            Icon(
                                imageVector = VryvIcons.ChevronDown,
                                contentDescription = null,
                                tint = if (isSel) VryvTextPrimary else VryvTextSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    } else null
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            // Segmented control matching search.png (Списком / Места)
            VryvSegmentedControl(
                options = listOf("Списком", "Места"),
                selectedIndex = selectedTab,
                onSelect = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (selectedTab == 0) {
                    "Нашли ${matchedEvents.size} событий · сортировка: ближе сначала"
                } else {
                    "Нашли ${matchedVenues.size} заведений · Минск"
                },
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 12.5.sp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        if (selectedTab == 0) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
            ) {
                items(matchedEvents, key = { it.id }) { event ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEventClick(event.id) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = event.coverRes),
                            contentDescription = event.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = event.title,
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${event.dateDisplay} · ${event.timeDisplay.substringBefore("–").trim()}",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${event.priceString} · ${event.distanceKm}",
                                color = VryvLime,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            imageVector = VryvIcons.ChevronRight,
                            contentDescription = null,
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
            ) {
                items(matchedVenues, key = { it.id }) { venue ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val firstVenueEvent = events.firstOrNull { it.venueId == venue.id } ?: events.first()
                                onEventClick(firstVenueEvent.id)
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = venue.coverRes),
                            contentDescription = venue.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = venue.name,
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${venue.category} · ${venue.address}",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp
                            )
                            Text(
                                text = venue.genres.joinToString(" · "),
                                color = VryvLime,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            imageVector = VryvIcons.ChevronRight,
                            contentDescription = null,
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                }
            }
        }
    }
}
