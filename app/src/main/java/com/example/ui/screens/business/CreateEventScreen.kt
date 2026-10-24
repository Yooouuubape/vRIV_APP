package com.example.ui.screens.business

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvInputBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
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
    var step by remember { mutableIntStateOf(1) } // 1: Что, 2: Условия, 3: Предпросмотр

    val sampleCovers = listOf(
        R.drawable.techno to "Техно",
        R.drawable.rooftop to "Лофт",
        R.drawable.concert to "Концерт",
        R.drawable.jazz to "Джаз",
        R.drawable.dj to "Диджей",
        R.drawable.standup to "Стендап"
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
        // Step progress header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (step > 1) step-- else onBackClick()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = VryvTextPrimary
                )
            }

            // Step dots
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                (1..3).forEach { s ->
                    Box(
                        modifier = Modifier
                            .size(if (s == step) 20.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (s <= step) VryvLime else VryvCard)
                    )
                }
            }

            Text(
                text = "Шаг $step из 3",
                color = VryvTextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(end = 12.dp)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            when (step) {
                1 -> {
                    // Step 1: Что
                    Text(
                        text = "1 · Что за событие?",
                        color = VryvTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Выбери афишу", color = VryvTextSecondary, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(sampleCovers) { (res, label) ->
                            val isSelected = res == selectedCover
                            Box(
                                modifier = Modifier
                                    .size(80.dp, 100.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) VryvLime else Color.Transparent,
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

                    Text(text = "Название мероприятия", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvInputBg,
                            unfocusedContainerColor = VryvInputBg,
                            focusedBorderColor = VryvLime,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_event_title")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Категория", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvInputBg,
                            unfocusedContainerColor = VryvInputBg,
                            focusedBorderColor = VryvLime,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Жанры (теги для рекомендаций через запятую)", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = genresText,
                        onValueChange = { genresText = it },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvInputBg,
                            unfocusedContainerColor = VryvInputBg,
                            focusedBorderColor = VryvLime,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Описание", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvInputBg,
                            unfocusedContainerColor = VryvInputBg,
                            focusedBorderColor = VryvLime,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                2 -> {
                    // Step 2: Условия
                    Text(
                        text = "2 · Когда, где и условия",
                        color = VryvTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Дата", color = VryvTextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = dateDisplay,
                                onValueChange = { dateDisplay = it },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = VryvInputBg,
                                    unfocusedContainerColor = VryvInputBg,
                                    focusedBorderColor = VryvLime,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = VryvTextPrimary,
                                    unfocusedTextColor = VryvTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Время", color = VryvTextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = timeDisplay,
                                onValueChange = { timeDisplay = it },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = VryvInputBg,
                                    unfocusedContainerColor = VryvInputBg,
                                    focusedBorderColor = VryvLime,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = VryvTextPrimary,
                                    unfocusedTextColor = VryvTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Цена (BYN, 0 = бесплатно)", color = VryvTextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = priceText,
                                onValueChange = { priceText = it },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = VryvInputBg,
                                    unfocusedContainerColor = VryvInputBg,
                                    focusedBorderColor = VryvLime,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = VryvTextPrimary,
                                    unfocusedTextColor = VryvTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Возраст", color = VryvTextSecondary, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = ageLimit,
                                onValueChange = { ageLimit = it },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = VryvInputBg,
                                    unfocusedContainerColor = VryvInputBg,
                                    focusedBorderColor = VryvLime,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = VryvTextPrimary,
                                    unfocusedTextColor = VryvTextPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(VryvInputBg)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Разрешить поиск компании",
                                color = VryvTextPrimary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Гости смогут собирать группы",
                                color = VryvTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                        Switch(
                            checked = allowCompany,
                            onCheckedChange = { allowCompany = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.Black,
                                checkedTrackColor = VryvLime
                            )
                        )
                    }
                }

                3 -> {
                    // Step 3: Предпросмотр и чек-лист
                    Text(
                        text = "3 · Предпросмотр и публикация",
                        color = VryvTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Preview Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(VryvCard)
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
                                .background(Color(0x77000000))
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = title,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
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
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvCard)
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Чек-лист перед публикацией:", color = VryvTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Афиша соответствует формату 4:5", color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Указаны корректные дата и время", color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Заведение Loft 13 верифицировано", color = VryvTextSecondary, fontSize = 12.sp)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (step < 3) {
                // Secondary action: Save Draft
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvCardElevated)
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
                        .padding(vertical = 12.dp)
                        .testTag("create_event_save_draft_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Сохранить как черновик",
                        color = VryvTextSecondary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Primary action: Next step
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvLime)
                        .clickable { step++ }
                        .padding(vertical = 14.dp)
                        .testTag("create_event_next_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Продолжить",
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                // Step 3: Send to moderation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
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
                        .padding(vertical = 14.dp)
                        .testTag("create_event_next_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Отправить на модерацию",
                        color = Color.Black,
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
                .background(Color(0xDD000000)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(VryvCard)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = VryvLime,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Отправлено на модерацию",
                    color = VryvTextPrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Мероприятие появится в афише и свайпах после автоматической проверки (обычно до 15 минут).",
                    color = VryvTextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VryvLime)
                        .clickable {
                            isSuccessModalVisible = false
                            onEventCreated(title)
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "В кабинет заведения",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
