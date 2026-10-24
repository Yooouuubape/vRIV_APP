package com.example.ui.screens.business

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvToggle
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateEventScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onEventCreated: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) } // 1: e1.png, 2: e2.png, 3: e3.png

    val sampleCovers = listOf(
        R.drawable.techno to "Техно",
        R.drawable.rooftop to "Инди",
        R.drawable.standup to "Стендап",
        R.drawable.wine to "Винный вечер",
        R.drawable.gallery to "Арт",
        R.drawable.jazz to "Джаз"
    )

    var selectedCover by remember { mutableIntStateOf(sampleCovers[0].first) }
    var title by remember { mutableStateOf("Deep House Night @ Loft 13") }
    var description by remember { mutableStateOf("Ночь глубокого и мелодичного хауса на крыше Loft 13. Спецсеты от резидентов и авторское коктейльное меню.") }
    var category by remember { mutableStateOf("Вечеринки") }
    var genresText by remember { mutableStateOf("Хаус, Дип-хаус, Мелодик") }

    var dateDisplay by remember { mutableStateOf("Сб 31 окт") }
    var timeDisplay by remember { mutableStateOf("22:00 – 05:00") }
    var priceText by remember { mutableStateOf("30") }
    var ageLimit by remember { mutableStateOf("18+") }
    var allowCompany by remember { mutableStateOf(true) }

    var isSuccessModalVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Step progress header matching e1.png-e3.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            VryvIconButton(
                icon = VryvIcons.Back,
                onClick = {
                    if (step > 1) step-- else onBackClick()
                }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..3).forEach { s ->
                    Box(
                        modifier = Modifier
                            .size(if (s == step) 24.dp else 10.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (s <= step) VryvLime else VryvCardElevated)
                    )
                }
            }

            Text(
                text = "Шаг $step из 3",
                color = VryvTextSecondary,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            when (step) {
                1 -> {
                    // Step 1: e1.png
                    Text(
                        text = "1 · Что за событие?",
                        color = VryvTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "ВЫБЕРИТЕ АФИШУ (4:5)",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(sampleCovers) { (res, label) ->
                            val isSelected = res == selectedCover
                            Box(
                                modifier = Modifier
                                    .size(84.dp, 106.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(
                                        width = if (isSelected) 2.5.dp else 1.dp,
                                        color = if (isSelected) VryvLime else VryvLine,
                                        shape = RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedCover = res }
                            ) {
                                Image(
                                    painter = painterResource(id = res),
                                    contentDescription = label,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "НАЗВАНИЕ МЕРОПРИЯТИЯ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvCardElevated,
                            unfocusedContainerColor = VryvCardElevated,
                            focusedBorderColor = VryvLime,
                            unfocusedBorderColor = VryvLine,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_event_title")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VryvInputField(
                        value = category,
                        onValueChange = { category = it },
                        label = "Категория"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VryvInputField(
                        value = genresText,
                        onValueChange = { genresText = it },
                        label = "Жанры (теги для рекомендаций)"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VryvInputField(
                        value = description,
                        onValueChange = { description = it },
                        label = "Описание",
                        singleLine = false,
                        minLines = 3
                    )
                }

                2 -> {
                    // Step 2: e2.png
                    Text(
                        text = "2 · Когда, где и условия",
                        color = VryvTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VryvInputField(
                            value = dateDisplay,
                            onValueChange = { dateDisplay = it },
                            label = "Дата",
                            modifier = Modifier.weight(1f)
                        )
                        VryvInputField(
                            value = timeDisplay,
                            onValueChange = { timeDisplay = it },
                            label = "Время",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        VryvInputField(
                            value = priceText,
                            onValueChange = { priceText = it },
                            label = "Цена (BYN, 0 = бесплатно)",
                            modifier = Modifier.weight(1f)
                        )
                        VryvInputField(
                            value = ageLimit,
                            onValueChange = { ageLimit = it },
                            label = "Возраст",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Разрешить поиск компании",
                                color = VryvTextPrimary,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Гости смогут собирать группы на событие",
                                color = VryvTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        VryvToggle(
                            checked = allowCompany,
                            onCheckedChange = { allowCompany = it }
                        )
                    }
                }

                3 -> {
                    // Step 3: e3.png
                    Text(
                        text = "3 · Предпросмотр и публикация",
                        color = VryvTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(22.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(22.dp))
                    ) {
                        Image(
                            painter = painterResource(id = selectedCover),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0x880B0B0F))
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(18.dp)
                        ) {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$dateDisplay · $timeDisplay · Loft 13",
                                color = VryvLime,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (priceText == "0" || priceText.isBlank()) "Бесплатно" else "$priceText BYN",
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Чек-лист перед публикацией:", color = VryvTextPrimary, fontSize = 13.5.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(VryvIcons.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Афиша соответствует формату 4:5", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(VryvIcons.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Указаны корректные дата и время", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(VryvIcons.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Заведение Loft 13 верифицировано", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom action buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (step < 3) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCardElevated)
                        .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
                        .clickable {
                            val genres = genresText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            val price = priceText.toDoubleOrNull() ?: 0.0
                            repository.createBusinessEvent(
                                title = title.ifBlank { "Без названия" },
                                description = description,
                                category = category,
                                genres = genres,
                                dateDisplay = dateDisplay,
                                timeDisplay = timeDisplay,
                                priceByn = price,
                                ageLimit = ageLimit,
                                coverRes = selectedCover,
                                isDraft = true
                            )
                            onEventCreated(title)
                        }
                        .testTag("create_event_save_draft_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Сохранить как черновик",
                        color = VryvTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvLime)
                        .clickable { step++ }
                        .testTag("create_event_next_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Продолжить",
                        color = Color(0xFF0B0B0F),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvLime)
                        .clickable {
                            val genres = genresText.split(",").map { it.trim() }.filter { it.isNotBlank() }
                            val price = priceText.toDoubleOrNull() ?: 0.0
                            repository.createBusinessEvent(
                                title = title,
                                description = description,
                                category = category,
                                genres = genres,
                                dateDisplay = dateDisplay,
                                timeDisplay = timeDisplay,
                                priceByn = price,
                                ageLimit = ageLimit,
                                coverRes = selectedCover,
                                isDraft = false
                            )
                            isSuccessModalVisible = true
                        }
                        .testTag("create_event_next_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Отправить на модерацию",
                        color = Color(0xFF0B0B0F),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

    // Success Moderation dialog (e4.png)
    if (isSuccessModalVisible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xDD0B0B0F)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLime.copy(alpha = 0.4f), RoundedCornerShape(24.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(VryvLime.copy(alpha = 0.16f))
                        .border(1.5.dp, VryvLime, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Check,
                        contentDescription = null,
                        tint = VryvLime,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Отправлено на модерацию",
                    color = VryvTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Мероприятие появится в афише и свайпах после автоматической проверки (обычно до 15 минут).",
                    color = VryvTextSecondary,
                    fontSize = 13.5.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(22.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvLime)
                        .clickable {
                            isSuccessModalVisible = false
                            onEventCreated(title)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "В кабинет заведения",
                        color = Color(0xFF0B0B0F),
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
