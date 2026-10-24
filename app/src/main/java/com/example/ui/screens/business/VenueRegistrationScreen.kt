package com.example.ui.screens.business

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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VenueApplication
import com.example.data.VenueApplicationStatus
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvOptionCard
import com.example.ui.components.VryvStatusPill
import com.example.ui.components.VryvStatusStyle
import com.example.ui.components.VryvSubScreenHeader
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
    var showClaimSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .verticalScroll(rememberScrollState())
    ) {
        VryvSubScreenHeader(
            title = "ВРЫВ для заведений",
            onBackClick = onBackClick
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            // Hero Banner matching biz.png
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLime.copy(alpha = 0.35f), RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column {
                    VryvStatusPill("Минск · Кабинет площадки", VryvStatusStyle.Lime)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "ВРЫВ для бизнеса",
                        color = VryvTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.6).sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Подключайте площадки Минска, публикуйте события в афишу и свайпы, отслеживайте воронку «Пойду» и общайтесь с гостями.",
                        color = VryvTextSecondary,
                        fontSize = 13.5.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // If user already has an approved venue, allow jumping to cabinet
            if (currentUser.venueId != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(VryvLime.copy(alpha = 0.14f))
                        .border(1.dp, VryvLime, RoundedCornerShape(18.dp))
                        .clickable { onOpenCabinet() }
                        .padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(VryvLime.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(VryvIcons.Store, contentDescription = null, tint = VryvLime, modifier = Modifier.size(22.dp))
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Кабинет заведения активен", color = VryvLime, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Перейти к управлению событиями и аналитике", color = VryvTextSecondary, fontSize = 13.sp)
                        }
                        Icon(VryvIcons.ChevronRight, contentDescription = null, tint = VryvLime, modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Active Application Status card matching vstat.png
            if (userApp != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Заявка: ${userApp.venueName}",
                                color = VryvTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            VryvStatusPill(
                                text = userApp.status.label,
                                style = when (userApp.status) {
                                    VenueApplicationStatus.APPROVED -> VryvStatusStyle.Lime
                                    VenueApplicationStatus.ON_MODERATION -> VryvStatusStyle.Warning
                                    VenueApplicationStatus.NEEDS_INFO -> VryvStatusStyle.Neutral
                                    VenueApplicationStatus.REJECTED -> VryvStatusStyle.Danger
                                    VenueApplicationStatus.DRAFT -> VryvStatusStyle.Neutral
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "${userApp.category} · ${userApp.address}",
                            color = VryvTextSecondary,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Timeline steps matching vstat.png
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Отправлена", "Проверка", "Решение").forEachIndexed { idx, stepLabel ->
                                val active = idx <= 1 || userApp.status == VenueApplicationStatus.APPROVED
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(4.dp)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (active) VryvLime else VryvCardElevated)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = stepLabel,
                                        color = if (active) VryvTextPrimary else VryvTextMuted,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Demo moderator simulation tools (preserved for interactive testing)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvBg)
                                .border(1.dp, VryvLine, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "Симуляция модерации (для тестирования):",
                                    color = VryvLime,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(VryvLime.copy(alpha = 0.18f))
                                            .clickable {
                                                repository.simulateModeratorApplicationDecision(userApp.id, true)
                                            }
                                            .padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                        Text(text = "Одобрить заявку", color = VryvLime, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(VryvPink.copy(alpha = 0.18f))
                                            .clickable {
                                                repository.simulateModeratorApplicationDecision(userApp.id, false)
                                            }
                                            .padding(horizontal = 12.dp, vertical = 7.dp)
                                    ) {
                                        Text(text = "Отклонить заявку", color = VryvPink, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Action 1: Add new venue (biz.png)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                    .clickable { onOpenRegistrationWizard() }
                    .padding(18.dp)
                    .testTag("biz_register_venue_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvLime.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(VryvIcons.Plus, contentDescription = null, tint = VryvLime, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Добавить новое заведение", color = VryvTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "4 простых шага регистрации бара, клуба или площадки", color = VryvTextSecondary, fontSize = 13.sp)
                    }
                    Icon(VryvIcons.ChevronRight, contentDescription = null, tint = VryvTextMuted, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action 2: Claim existing venue (biz.png & claim.png)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                    .clickable {
                        repository.showToast("Подача заявки на существующее заведение отправлена на проверку")
                        showClaimSheet = true
                    }
                    .padding(18.dp)
                    .testTag("biz_claim_venue_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvPink.copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(VryvIcons.Store, contentDescription = null, tint = VryvPink, modifier = Modifier.size(22.dp))
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Заявка на существующее заведение", color = VryvTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Подтвердите права управления площадкой, которая уже есть в афише", color = VryvTextSecondary, fontSize = 13.sp)
                    }
                    Icon(VryvIcons.ChevronRight, contentDescription = null, tint = VryvTextMuted, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // Claim existing venue bottom sheet (claim.png)
    if (showClaimSheet) {
        var searchVenue by remember { mutableStateOf("Modul") }
        VryvFloatingBottomSheet(
            onDismiss = { showClaimSheet = false }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Найти вашу площадку",
                    color = VryvTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Выберите заведение из каталога Минска, чтобы запросить доступ владельца",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                VryvInputField(
                    value = searchVenue,
                    onValueChange = { searchVenue = it },
                    placeholder = "Название или адрес...",
                    leadingIcon = VryvIcons.Search
                )
                Spacer(modifier = Modifier.height(12.dp))
                listOf(
                    "Modul Art Platform" to "ул. Октябрьская, 16/3 · Клуб",
                    "Свободы, 4" to "пл. Свободы, 4 · Винный бар",
                    "Хулиган" to "ул. Октябрьская, 16 · Бар"
                ).forEach { (vTitle, vSub) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                            .clickable {
                                showClaimSheet = false
                                repository.showToast("Заявка на «$vTitle» отправлена")
                            }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(vTitle, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                            Text(vSub, color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        VryvStatusPill("Выбрать", VryvStatusStyle.Lime)
                    }
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
    var step by remember { mutableIntStateOf(1) } // 1: v1.png, 2: v2.png, 3: v3.png, 4: v4.png

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
    val verificationMethods = listOf(
        "Выписка из ЕГР / Договор аренды" to "Загрузите фото или PDF документа",
        "Подтверждение через рабочий Email/домен" to "Мгновенный код на корпоративную почту",
        "Звонок администратора" to "Модератор свяжется в течение 30 минут"
    )

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
            // Header + 4-step progress matching v1.png-v4.png
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VryvIconButton(
                    icon = VryvIcons.Back,
                    onClick = {
                        if (step > 1) step -= 1 else onBackClick()
                    }
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (1..4).forEach { s ->
                        Box(
                            modifier = Modifier
                                .size(if (s == step) 26.dp else 10.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (s <= step) VryvLime else VryvCardElevated)
                        )
                    }
                }

                Text(
                    text = "Шаг $step из 4",
                    color = VryvTextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            when (step) {
                1 -> {
                    Text(
                        text = "О заведении",
                        color = VryvTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.6).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Основная информация о вашей локации в Минске",
                        color = VryvTextSecondary,
                        fontSize = 13.5.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "НАЗВАНИЕ ЗАВЕДЕНИЯ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = venueName,
                        onValueChange = { venueName = it; errorMessage = null },
                        placeholder = { Text("Например, Hide Club", color = VryvTextMuted, fontSize = 14.sp) },
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
                            .testTag("venue_name_input")
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "КАТЕГОРИЯ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSel = category == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(if (isSel) VryvLime else VryvCardElevated)
                                    .border(1.dp, if (isSel) VryvLime else VryvLine, RoundedCornerShape(999.dp))
                                    .clickable { category = cat }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSel) Color(0xFF0B0B0F) else VryvTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "АДРЕС В МИНСКЕ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        leadingIcon = { Icon(VryvIcons.Pin, contentDescription = null, tint = VryvLime, modifier = Modifier.size(18.dp)) },
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
                            .testTag("venue_address_input")
                    )
                }

                2 -> {
                    Text(
                        text = "Атмосфера и график",
                        color = VryvTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.6).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Музыкальные направления и особенности заведения",
                        color = VryvTextSecondary,
                        fontSize = 13.5.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "МУЗЫКАЛЬНЫЕ НАПРАВЛЕНИЯ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allGenres.forEach { g ->
                            val isSel = selectedGenres.contains(g)
                            VryvChip(
                                text = g,
                                selected = isSel,
                                onClick = {
                                    if (isSel) selectedGenres.remove(g) else selectedGenres.add(g)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "ОСОБЕННОСТИ ЗАВЕДЕНИЯ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allFeatures.forEach { f ->
                            val isSel = selectedFeatures.contains(f)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(if (isSel) VryvLime else VryvCardElevated)
                                    .border(1.dp, if (isSel) VryvLime else VryvLine, RoundedCornerShape(999.dp))
                                    .clickable {
                                        if (isSel) selectedFeatures.remove(f) else selectedFeatures.add(f)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = f,
                                    color = if (isSel) Color(0xFF0B0B0F) else VryvTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    VryvInputField(
                        value = schedule,
                        onValueChange = { schedule = it },
                        label = "График работы",
                        leadingIcon = VryvIcons.Clock
                    )
                }

                3 -> {
                    Text(
                        text = "Контакты заведения",
                        color = VryvTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.6).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Для связи модерации и будущих гостей",
                        color = VryvTextSecondary,
                        fontSize = 13.5.sp
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    VryvInputField(value = contactName, onValueChange = { contactName = it }, label = "Контактное лицо")
                    Spacer(modifier = Modifier.height(12.dp))
                    VryvInputField(value = email, onValueChange = { email = it }, label = "Рабочий Email")
                    Spacer(modifier = Modifier.height(12.dp))
                    VryvInputField(value = phone, onValueChange = { phone = it }, label = "Телефон администратора")
                    Spacer(modifier = Modifier.height(12.dp))
                    VryvInputField(value = telegram, onValueChange = { telegram = it }, label = "Telegram заведения")
                }

                4 -> {
                    Text(
                        text = "Подтверждение",
                        color = VryvTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.6).sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Проверьте данные перед отправкой заявки на модерацию",
                        color = VryvTextSecondary,
                        fontSize = 13.5.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = venueName.ifBlank { "Моё заведение" },
                                color = VryvTextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = "$category · $address", color = VryvLime, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "График: $schedule", color = VryvTextSecondary, fontSize = 12.5.sp)
                            Text(text = "Контакты: $contactName, $phone, $telegram", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "СПОСОБ ВЕРИФИКАЦИИ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    verificationMethods.forEach { (method, sub) ->
                        val isSel = verificationMethod == method
                        VryvOptionCard(
                            title = method,
                            subtitle = sub,
                            selected = isSel,
                            onClick = { verificationMethod = method }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
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
                .clip(RoundedCornerShape(16.dp))
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
                color = Color(0xFF0B0B0F),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
