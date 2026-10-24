package com.example.ui.screens.feed

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonVariant
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvSegmentedControl
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
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
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Все") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Списком / События, 1: Места

    val filterChips = listOf("Все", "Сегодня", "На выходных", "Техно", "Стендап", "Бесплатно", "Бары")

    val searchResults = remember(query, selectedFilter, events) {
        events.filter { event ->
            val matchesQuery = query.isBlank() ||
                event.title.contains(query, ignoreCase = true) ||
                event.venueName.contains(query, ignoreCase = true) ||
                event.description.contains(query, ignoreCase = true) ||
                event.genres.any { it.contains(query, ignoreCase = true) }

            val matchesFilter = when (selectedFilter) {
                "Сегодня" -> event.dateDisplay.contains("Сегодня", ignoreCase = true)
                "На выходных" -> event.dateDisplay.contains("Сб") || event.dateDisplay.contains("Вс")
                "Техно" -> event.genres.any { it.contains("Техно", ignoreCase = true) }
                "Стендап" -> event.category.contains("Стендап", ignoreCase = true)
                "Бесплатно" -> event.priceByn == 0.0
                "Бары" -> event.category.contains("Бары", ignoreCase = true)
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Search Header matching search.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VryvIconButton(
                icon = VryvIcons.Back,
                onClick = onBackClick,
                testTag = "search_back_button"
            )

            BasicTextField(
                value = query,
                onValueChange = { query = it },
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = VryvTextPrimary
                ),
                cursorBrush = SolidColor(VryvPink),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("search_input_field"),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(24.dp))
                            .background(VryvCard)
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
                        Box(modifier = Modifier.weight(1f)) {
                            if (query.isEmpty()) {
                                Text(
                                    text = "Поиск: техно, стендап, Loft 13...",
                                    color = VryvTextMuted,
                                    fontFamily = InterFontFamily,
                                    fontSize = 14.5.sp
                                )
                            }
                            innerTextField()
                        }
                        if (query.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2C2C35))
                                    .clickable { query = "" },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = VryvIcons.Close,
                                    contentDescription = "Очистить",
                                    tint = VryvTextSecondary,
                                    modifier = Modifier.size(13.dp)
                                )
                            }
                        }
                    }
                }
            )
        }

        // Quick Filters row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filterChips) { filter ->
                val isSelected = filter == selectedFilter
                VryvChip(
                    text = filter,
                    selected = isSelected,
                    onClick = { selectedFilter = filter },
                    trailingChevron = filter in listOf("Сегодня", "На выходных")
                )
            }
        }

        // Results Count + Segmented Control
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Найдено событий: ${searchResults.size}",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                if (query.isNotEmpty() || selectedFilter != "Все") {
                    Text(
                        text = "Сбросить",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .clickable {
                                query = ""
                                selectedFilter = "Все"
                            }
                    )
                }
            }

            VryvSegmentedControl(
                options = listOf("События", "Места"),
                selectedIndex = selectedTab,
                onOptionSelected = { selectedTab = it },
                modifier = Modifier.width(164.dp)
            )
        }

        if (searchResults.isEmpty()) {
            // searchEmpty.png
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(VryvCard),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Search,
                            contentDescription = null,
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ничего не нашлось",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "В эти даты по запросу событий нет. Попробуй сбросить фильтр даты или посмотреть похожие жанры.",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 13.5.sp,
                        lineHeight = 19.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    VryvButton(
                        text = "Сбросить фильтры",
                        onClick = {
                            query = ""
                            selectedFilter = "Все"
                        },
                        variant = VryvButtonVariant.SECONDARY
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (selectedTab == 0) {
                    item {
                        VryvGroupList {
                            searchResults.forEachIndexed { index, event ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onEventClick(event.id) }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
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
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "${event.dateDisplay}, ${event.timeDisplay} · ${event.venueName}",
                                            color = VryvTextSecondary,
                                            fontFamily = InterFontFamily,
                                            fontSize = 12.5.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = event.priceString,
                                            color = if (event.priceByn == 0.0) VryvLime else VryvPink,
                                            fontFamily = InterFontFamily,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${event.goingCount} идут",
                                            color = VryvTextSecondary,
                                            fontFamily = InterFontFamily,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                                if (index < searchResults.lastIndex) {
                                    HorizontalDivider(
                                        color = VryvLine,
                                        thickness = 0.5.dp,
                                        modifier = Modifier.padding(start = 84.dp)
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // searchVenues.png — Venues tab
                    val venues = listOf(
                        Triple("Loft 13", "Бар и площадка · Зыбицкая, 6 · 6 событий", R.drawable.rooftop),
                        Triple("Клуб «Модуль»", "Электронный клуб · Октябрьская, 16 · 4 события", R.drawable.techno),
                        Triple("StandUp Hall", "Комедийный клуб · Интернациональная, 20 · 5 событий", R.drawable.standup),
                        Triple("Цех 16", "Выставочное пространство · Октябрьская, 16к3", R.drawable.gallery)
                    )
                    item {
                        VryvGroupList {
                            venues.forEachIndexed { idx, (vName, vSub, vImg) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            val firstEv = searchResults.firstOrNull()
                                            if (firstEv != null) onEventClick(firstEv.id)
                                        }
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = vImg),
                                        contentDescription = vName,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = vName,
                                            color = VryvTextPrimary,
                                            fontFamily = InterFontFamily,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = vSub,
                                            color = VryvTextSecondary,
                                            fontFamily = InterFontFamily,
                                            fontSize = 12.5.sp
                                        )
                                    }
                                    Icon(
                                        imageVector = VryvIcons.ChevRight,
                                        contentDescription = null,
                                        tint = Color(0xFF5A5A66),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                if (idx < venues.lastIndex) {
                                    HorizontalDivider(
                                        color = VryvLine,
                                        thickness = 0.5.dp,
                                        modifier = Modifier.padding(start = 80.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
