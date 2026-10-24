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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VenueApplication
import com.example.data.VenueApplicationStatus
import com.example.data.VryvRepository
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvInputBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun BusinessHubScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onOpenRegistrationWizard: () -> Unit,
    onOpenCabinet: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by repository.currentUser.collectAsState()
    val applications by repository.venueApplications.collectAsState()
    val userApp = applications.find { it.userId == currentUser.id }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = VryvTextPrimary)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "ВРЫВ для бизнеса",
            color = VryvTextPrimary,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Подключайте площадки Минска, публикуйте события и находите гостей",
            color = VryvTextSecondary,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // If user already has an approved venue, allow jumping to cabinet
        if (currentUser.venueId != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22C7FF28))
                    .border(1.dp, VryvLime, RoundedCornerShape(16.dp))
                    .clickable { onOpenCabinet() }
                    .padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(VryvLime.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Store, contentDescription = null, tint = VryvLime)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Кабинет заведения активен", color = VryvLime, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Перейти к управлению событиями и аналитике", color = VryvTextSecondary, fontSize = 13.sp)
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Active Application Status card
        if (userApp != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Заявка: ${userApp.venueName}", color = VryvTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    when (userApp.status) {
                                        VenueApplicationStatus.APPROVED -> Color(0x33C7FF28)
                                        VenueApplicationStatus.ON_MODERATION -> Color(0x33FFB300)
                                        VenueApplicationStatus.NEEDS_INFO -> Color(0x3300B0FF)
                                        VenueApplicationStatus.REJECTED -> Color(0x33FF1D8E)
                                        VenueApplicationStatus.DRAFT -> VryvCardElevated
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = userApp.status.label,
                                color = when (userApp.status) {
                                    VenueApplicationStatus.APPROVED -> VryvLime
                                    VenueApplicationStatus.ON_MODERATION -> Color(0xFFFFB300)
                                    VenueApplicationStatus.NEEDS_INFO -> Color(0xFF00B0FF)
                                    VenueApplicationStatus.REJECTED -> VryvPink
                                    VenueApplicationStatus.DRAFT -> VryvTextSecondary
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "${userApp.category} · ${userApp.address}", color = VryvTextSecondary, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(14.dp))

                    // Demo moderator simulation tools
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(VryvBg)
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = "Симуляция модерации (для тестирования):", color = VryvLime, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x33C7FF28))
                                        .clickable {
                                            repository.simulateModeratorApplicationDecision(userApp.id, true)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = "Одобрить заявку", color = VryvLime, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0x33FF1D8E))
                                        .clickable {
                                            repository.simulateModeratorApplicationDecision(userApp.id, false)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = "Отклонить заявку", color = VryvPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Action 1: Add new venue
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VryvCard)
                .clickable { onOpenRegistrationWizard() }
                .padding(20.dp)
                .testTag("biz_register_venue_btn")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(VryvPink.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = VryvPink)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Добавить заведение", color = VryvTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "4 простых шага регистрации вашего бара, клуба или площадки", color = VryvTextSecondary, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action 2: Claim existing venue
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(VryvCard)
                .clickable {
                    repository.showToast("Подача заявки на существующее заведение отправлена на проверку")
                }
                .padding(20.dp)
                .testTag("biz_claim_venue_btn")
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(VryvLime.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Store, contentDescription = null, tint = VryvLime)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Заявка на существующее заведение", color = VryvTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Подтвердите права управления площадкой, которая уже есть в афише", color = VryvTextSecondary, fontSize = 13.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VenueRegistrationWizard(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) } // 1: О заведении, 2: Атмосфера, 3: Контакты, 4: Подтверждение

    // Step 1 fields
    var venueName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Бар") }
    val categories = listOf("Бар", "Ночной клуб", "Кафе", "Ресторан", "Концертная площадка", "Арт-пространство")
    var city by remember { mutableStateOf("Минск") }
    var address by remember { mutableStateOf("ул. Октябрьская, 16") }

    // Step 2 fields
    val allGenres = listOf("Техно", "Хаус", "Джаз", "Рок", "Инди", "Хип-хоп", "Лаунж", "Поп")
    val selectedGenres = remember { mutableStateListOf("Хаус", "Лаунж") }
    val allFeatures = listOf("Летняя терраса", "Танцпол", "VIP-комнаты", "Авторская кухня", "Крафтовое пиво", "Кальяны")
    val selectedFeatures = remember { mutableStateListOf("Летняя терраса", "Танцпол") }
    var schedule by remember { mutableStateOf("Вт–Вс 18:00–04:00") }

    // Step 3 fields
    var contactName by remember { mutableStateOf("Максим Салуков") }
    var email by remember { mutableStateOf("contact@vryv.by") }
    var phone by remember { mutableStateOf("+375 (29) 123-45-67") }
    var telegram by remember { mutableStateOf("@minsk_venue") }
    var website by remember { mutableStateOf("https://vryv.by") }

    // Step 4 fields
    var verificationMethod by remember { mutableStateOf("Выписка из ЕГР / Договор аренды") }
    val verificationMethods = listOf("Выписка из ЕГР / Договор аренды", "Подтверждение через рабочий Email/домен", "Звонок администратора")

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            IconButton(onClick = {
                if (step > 1) step -= 1 else onBackClick()
            }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = VryvTextPrimary)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress indicator (4 steps)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (1..4).forEach { s ->
                        Box(
                            modifier = Modifier
                                .size(if (s == step) 24.dp else 10.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (s <= step) VryvLime else VryvCard)
                        )
                    }
                }
                Text(text = "Шаг $step из 4", color = VryvTextSecondary, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            when (step) {
                1 -> {
                    Text(text = "О заведении", color = VryvTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Основная информация о вашей локации", color = VryvTextSecondary, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Название заведения", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = venueName,
                        onValueChange = { venueName = it; errorMessage = null },
                        placeholder = { Text("Например, Hide Club", color = VryvTextMuted, fontSize = 14.sp) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvInputBg,
                            unfocusedContainerColor = VryvInputBg,
                            focusedBorderColor = VryvLime,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("venue_name_input")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Категория", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        categories.forEach { cat ->
                            val isSel = category == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) VryvLime else VryvCard)
                                    .clickable { category = cat }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(text = cat, color = if (isSel) Color.Black else VryvTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "Адрес в Минске", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = VryvLime) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvInputBg,
                            unfocusedContainerColor = VryvInputBg,
                            focusedBorderColor = VryvLime,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("venue_address_input")
                    )
                }

                2 -> {
                    Text(text = "Атмосфера и график", color = VryvTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Музыкальные направления и особенности заведения", color = VryvTextSecondary, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Музыкальные направления", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        allGenres.forEach { g ->
                            val isSel = selectedGenres.contains(g)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) VryvPink else VryvCard)
                                    .clickable {
                                        if (isSel) selectedGenres.remove(g) else selectedGenres.add(g)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(text = g, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Особенности заведения", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        allFeatures.forEach { f ->
                            val isSel = selectedFeatures.contains(f)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) VryvLime else VryvCard)
                                    .clickable {
                                        if (isSel) selectedFeatures.remove(f) else selectedFeatures.add(f)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(text = f, color = if (isSel) Color.Black else VryvTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "График работы", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = schedule,
                        onValueChange = { schedule = it },
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

                3 -> {
                    Text(text = "Контакты заведения", color = VryvTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Для связи модерации и будущих гостей", color = VryvTextSecondary, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(text = "Контактное лицо", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = contactName, onValueChange = { contactName = it }, modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Рабочий Email", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Телефон администратора", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = phone, onValueChange = { phone = it }, modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Telegram заведения", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(value = telegram, onValueChange = { telegram = it }, modifier = Modifier.fillMaxWidth())
                }

                4 -> {
                    Text(text = "Подтверждение", color = VryvTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Проверьте данные перед отправкой заявки на модерацию", color = VryvTextSecondary, fontSize = 13.sp)

                    Spacer(modifier = Modifier.height(20.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(VryvCard)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(text = venueName.ifBlank { "Моё заведение" }, color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            Text(text = "$category · $address", color = VryvTextSecondary, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "График: $schedule", color = VryvTextSecondary, fontSize = 12.5.sp)
                            Text(text = "Контакты: $contactName, $phone, $telegram", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Способ верификации", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    verificationMethods.forEach { method ->
                        val isSel = verificationMethod == method
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSel) Color(0x33C7FF28) else VryvCard)
                                .clickable { verificationMethod = method }
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = method, color = VryvTextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                            if (isSel) Icon(Icons.Default.Check, contentDescription = null, tint = VryvLime)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (errorMessage != null) {
            Text(text = errorMessage!!, color = VryvPink, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(VryvLime)
                .clickable {
                    if (step == 1 && venueName.isBlank()) {
                        errorMessage = "Введите название заведения"
                        return@clickable
                    }
                    if (step < 4) {
                        step += 1
                    } else {
                        val app = VenueApplication(
                            id = "vapp_${System.currentTimeMillis()}",
                            userId = repository.currentUser.value.id,
                            venueName = venueName.ifBlank { "Новое заведение" },
                            category = category,
                            city = city,
                            address = address,
                            genres = selectedGenres.toList(),
                            features = selectedFeatures.toList(),
                            schedule = schedule,
                            contactName = contactName,
                            email = email,
                            phone = phone,
                            telegram = telegram,
                            website = website,
                            verificationMethod = verificationMethod,
                            status = VenueApplicationStatus.ON_MODERATION
                        )
                        repository.submitVenueApplication(app)
                        onCompleted()
                    }
                }
                .testTag("venue_wizard_next_btn"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (step < 4) "Далее" else "Отправить заявку",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
