package com.example.ui.screens.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvGroupRow
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvOtpRow
import com.example.ui.components.VryvSegmentedControl
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.components.VryvTag
import com.example.ui.components.VryvTagVariant
import com.example.ui.components.VryvToggle
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvDanger
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun SettingsScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by repository.currentUser.collectAsState()
    val allUsers by repository.registeredUsers.collectAsState()

    // Sub-screens matching s2-user.js:
    // mail.png, mailCode.png, mailDone.png, pass.png, passOk.png, sessions.png, notif.png, priv.png, perms.png, delacc.png, delaccOk.png
    var activeSubScreen by remember { mutableStateOf<String?>(null) }

    // Mail flow state
    var newEmail by remember { mutableStateOf("m.kovalev@work.by") }
    var emailPassword by remember { mutableStateOf("12345678") }
    var emailOtp by remember { mutableStateOf("2410") }

    // Password flow state
    var currentPass by remember { mutableStateOf("12345678") }
    var nextPass by remember { mutableStateOf("NewPass2026") }
    var confirmNextPass by remember { mutableStateOf("NewPass2026") }

    // Delete account state
    var deletePass by remember { mutableStateOf("12345678") }

    // Notifications state (notif.png)
    var notifFriends by remember { mutableStateOf(true) }
    var notifInvites by remember { mutableStateOf(true) }
    var notifReminders by remember { mutableStateOf(true) }
    var notifDigest by remember { mutableStateOf(true) }
    var notifNewPlaces by remember { mutableStateOf(false) }
    var notifEmailDigest by remember { mutableStateOf(false) }

    // Privacy state (priv.png)
    var plansVisIdx by remember { mutableIntStateOf(1) } // Друзья
    var savedVisIdx by remember { mutableIntStateOf(2) } // Только я
    var findMeByNick by remember { mutableStateOf(true) }
    var showInCompanySearch by remember { mutableStateOf(true) }

    // Sub-screen 1: Смена почты (mail.png -> mailCode.png -> mailDone.png)
    if (activeSubScreen == "mail" || activeSubScreen == "mailCode" || activeSubScreen == "mailDone") {
        Column(modifier = modifier.fillMaxSize().background(VryvBg)) {
            if (activeSubScreen != "mailDone") {
                VryvSubScreenHeader(
                    title = if (activeSubScreen == "mail") "Смена почты" else "Код подтверждения",
                    onBackClick = {
                        activeSubScreen = if (activeSubScreen == "mailCode") "mail" else null
                    }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = if (activeSubScreen == "mailDone") Arrangement.SpaceBetween else Arrangement.Top
            ) {
                when (activeSubScreen) {
                    "mail" -> {
                        VryvInputField(
                            value = user.email,
                            onValueChange = {},
                            label = "Текущая почта"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        VryvInputField(
                            value = newEmail,
                            onValueChange = { newEmail = it },
                            label = "Новая почта"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        VryvInputField(
                            value = emailPassword,
                            onValueChange = { emailPassword = it },
                            label = "Текущий пароль для подтверждения",
                            isPassword = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Отправим 6-значный код на новый адрес. Старая почта перестанет работать после подтверждения.",
                            color = VryvTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                        Spacer(modifier = Modifier.height(22.dp))
                        VryvButton(
                            text = "Отправить код",
                            style = VryvButtonStyle.PrimaryPink,
                            onClick = { activeSubScreen = "mailCode" }
                        )
                    }
                    "mailCode" -> {
                        Text(
                            text = "Отправили 6-значный код на $newEmail",
                            color = VryvTextSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        VryvOtpRow(
                            code = emailOtp,
                            onCodeChange = { emailOtp = it },
                            cellCount = 6
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Не пришло письмо?", color = VryvTextSecondary, fontSize = 13.sp)
                            Text("Отправить снова · 0:38", color = VryvTextMuted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        VryvButton(
                            text = "Подтвердить почту",
                            style = VryvButtonStyle.PrimaryPink,
                            onClick = { activeSubScreen = "mailDone" }
                        )
                    }
                    "mailDone" -> {
                        Spacer(modifier = Modifier.height(24.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(Color(0x2EC7FF28)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(VryvIcons.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(34.dp))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Почта обновлена", color = VryvTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Новый адрес $newEmail привязан к аккаунту. На старую почту отправлено уведомление.",
                                color = VryvTextSecondary,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                lineHeight = 20.sp
                            )
                        }
                        VryvButton(
                            text = "Вернуться в настройки",
                            style = VryvButtonStyle.PrimaryPink,
                            onClick = { activeSubScreen = null }
                        )
                    }
                }
            }
        }
        return
    }

    // Sub-screen 2: Изменить пароль (pass.png -> passOk.png)
    if (activeSubScreen == "pass" || activeSubScreen == "passOk") {
        Column(modifier = modifier.fillMaxSize().background(VryvBg)) {
            if (activeSubScreen == "pass") {
                VryvSubScreenHeader(
                    title = "Изменить пароль",
                    onBackClick = { activeSubScreen = null }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = if (activeSubScreen == "passOk") Arrangement.SpaceBetween else Arrangement.Top
            ) {
                if (activeSubScreen == "pass") {
                    VryvInputField(
                        value = currentPass,
                        onValueChange = { currentPass = it },
                        label = "Текущий пароль",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    VryvInputField(
                        value = nextPass,
                        onValueChange = { nextPass = it },
                        label = "Новый пароль",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        repeat(4) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(VryvLime)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    VryvInputField(
                        value = confirmNextPass,
                        onValueChange = { confirmNextPass = it },
                        label = "Повторите новый пароль",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "После смены пароля все сессии на других устройствах будут завершены.",
                        color = VryvTextSecondary,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    VryvButton(
                        text = "Обновить пароль",
                        style = VryvButtonStyle.PrimaryPink,
                        onClick = { activeSubScreen = "passOk" }
                    )
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0x2EC7FF28)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(VryvIcons.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(34.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Пароль изменён", color = VryvTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Другие устройства разлогинены. Текущая сессия на этом iPhone остаётся активной.",
                            color = VryvTextSecondary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                    VryvButton(
                        text = "Готово",
                        style = VryvButtonStyle.PrimaryPink,
                        onClick = { activeSubScreen = null }
                    )
                }
            }
        }
        return
    }

    // Sub-screen 3: Активные сессии (sessions.png)
    if (activeSubScreen == "sessions") {
        Column(modifier = modifier.fillMaxSize().background(VryvBg)) {
            VryvSubScreenHeader(
                title = "Активные сессии",
                onBackClick = { activeSubScreen = null }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "Это устройство",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvGroupList {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x2EC7FF28)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(VryvIcons.Phone, contentDescription = null, tint = VryvLime, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("iPhone 15 Pro · Минск", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("ВРЫВ iOS 1.0 · сейчас онлайн", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                        VryvTag("Текущая", VryvTagVariant.LIME)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Другие устройства",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvGroupList {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(VryvCardElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(VryvIcons.Globe, contentDescription = null, tint = VryvTextPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("MacBook Air · Chrome", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Минск · 2 дня назад", color = VryvTextSecondary, fontSize = 12.5.sp)
                        }
                        Text(
                            text = "Выйти",
                            color = VryvPink,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable { repository.showToast("Сессия MacBook Air завершена") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                VryvButton(
                    text = "Выйти со всех других устройств",
                    style = VryvButtonStyle.Danger,
                    onClick = {
                        repository.showToast("Все другие сессии завершены")
                        activeSubScreen = null
                    }
                )
            }
        }
        return
    }

    // Sub-screen 4: Уведомления (notif.png)
    if (activeSubScreen == "notif") {
        Column(modifier = modifier.fillMaxSize().background(VryvBg)) {
            VryvSubScreenHeader(
                title = "Уведомления",
                onBackClick = { activeSubScreen = null }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text("События и друзья", color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                VryvGroupList {
                    SettingsToggleRowRef("Друзья идут на событие", "Когда друг отмечает «Собираюсь»", notifFriends) { notifFriends = it }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    SettingsToggleRowRef("Приглашения", "Когда друг зовёт с собой", notifInvites) { notifInvites = it }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    SettingsToggleRowRef("Напоминания о планах", "За 3 часа до начала события", notifReminders) { notifReminders = it }
                }

                Spacer(modifier = Modifier.height(18.dp))
                Text("Рекомендации", color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                VryvGroupList {
                    SettingsToggleRowRef("Подборка на выходные", "По четвергам в 18:00", notifDigest) { notifDigest = it }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    SettingsToggleRowRef("Новые места в городе", "Раз в неделю", notifNewPlaces) { notifNewPlaces = it }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    SettingsToggleRowRef("Дайджест на почту", "Еженедельное письмо", notifEmailDigest) { notifEmailDigest = it }
                }
            }
        }
        return
    }

    // Sub-screen 5: Конфиденциальность (priv.png)
    if (activeSubScreen == "priv") {
        Column(modifier = modifier.fillMaxSize().background(VryvBg)) {
            VryvSubScreenHeader(
                title = "Конфиденциальность",
                onBackClick = { activeSubScreen = null }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text("Кто видит мои планы", color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                VryvSegmentedControl(
                    options = listOf("Все", "Друзья", "Только я"),
                    selectedIndex = plansVisIdx,
                    onSelect = { plansVisIdx = it }
                )

                Spacer(modifier = Modifier.height(18.dp))
                Text("Кто видит сохранённые события", color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                VryvSegmentedControl(
                    options = listOf("Все", "Друзья", "Только я"),
                    selectedIndex = savedVisIdx,
                    onSelect = { savedVisIdx = it }
                )

                Spacer(modifier = Modifier.height(18.dp))
                Text("Обнаружение", color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                VryvGroupList {
                    SettingsToggleRowRef("Находить меня по @никнейму", "В поиске пользователей", findMeByNick) { findMeByNick = it }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    SettingsToggleRowRef("Показывать в «Кто ещё идёт»", "На странице мероприятия для друзей", showInCompanySearch) { showInCompanySearch = it }
                }

                Spacer(modifier = Modifier.height(18.dp))
                VryvGroupList {
                    VryvGroupRow(
                        title = "Заблокированные пользователи",
                        trailingValue = "2",
                        showDivider = false,
                        onClick = { repository.showToast("Список заблокированных") }
                    )
                }
            }
        }
        return
    }

    // Sub-screen 6: Разрешения (perms.png)
    if (activeSubScreen == "perms") {
        Column(modifier = modifier.fillMaxSize().background(VryvBg)) {
            VryvSubScreenHeader(
                title = "Разрешения",
                onBackClick = { activeSubScreen = null }
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "ВРЫВ запрашивает доступ только к тому, что нужно для работы функций.",
                    color = VryvTextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 14.dp)
                )
                VryvGroupList {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(Color(0x2EC7FF28)), contentAlignment = Alignment.Center) {
                            Icon(VryvIcons.Pin, contentDescription = null, tint = VryvLime, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Геолокация", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("События рядом и расстояние до места", color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        VryvTag("При использовании", VryvTagVariant.LIME)
                    }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(Color(0x2EC7FF28)), contentAlignment = Alignment.Center) {
                            Icon(VryvIcons.Bell, contentDescription = null, tint = VryvLime, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Push-уведомления", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Приглашения друзей и напоминания", color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        VryvTag("Разрешено", VryvTagVariant.LIME)
                    }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(VryvCardElevated), contentAlignment = Alignment.Center) {
                            Icon(VryvIcons.Camera, contentDescription = null, tint = VryvTextPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Фото и камера", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Аватар профиля и обложки событий", color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        VryvTag("По запросу", VryvTagVariant.NEUTRAL)
                    }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(VryvCardElevated), contentAlignment = Alignment.Center) {
                            Icon(VryvIcons.Calendar, contentDescription = null, tint = VryvTextPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Календарь", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Экспорт планов в системный календарь", color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        VryvTag("Выключено", VryvTagVariant.NEUTRAL)
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                VryvButton(
                    text = "Открыть системные настройки",
                    style = VryvButtonStyle.Secondary,
                    onClick = { repository.showToast("Открываем системные настройки") }
                )
            }
        }
        return
    }

    // Sub-screen 7: Удаление аккаунта (delacc.png -> delaccOk.png)
    if (activeSubScreen == "delacc" || activeSubScreen == "delaccOk") {
        Column(modifier = modifier.fillMaxSize().background(VryvBg)) {
            if (activeSubScreen == "delacc") {
                VryvSubScreenHeader(
                    title = "Удаление аккаунта",
                    onBackClick = { activeSubScreen = null }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalArrangement = if (activeSubScreen == "delaccOk") Arrangement.SpaceBetween else Arrangement.Top
            ) {
                if (activeSubScreen == "delacc") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0x1AFF1D8E))
                            .border(1.dp, Color(0x59FF1D8E), RoundedCornerShape(16.dp))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(Color(0x2EFF1D8E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(VryvIcons.Alert, contentDescription = null, tint = VryvPink, modifier = Modifier.size(18.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Что произойдёт", color = VryvTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Профиль @${user.username.removePrefix("@")}, сохранённые события, история посещений и список друзей будут скрыты и удалены через 30 дней. Если передумаешь — просто войди в аккаунт в течение месяца.",
                                color = VryvTextSecondary,
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    VryvInputField(
                        value = deletePass,
                        onValueChange = { deletePass = it },
                        label = "Введите пароль для подтверждения",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    VryvButton(
                        text = "Удалить аккаунт",
                        style = VryvButtonStyle.Danger,
                        onClick = { activeSubScreen = "delaccOk" }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    VryvButton(
                        text = "Оставить аккаунт",
                        style = VryvButtonStyle.Secondary,
                        onClick = { activeSubScreen = null }
                    )
                } else {
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(72.dp).clip(CircleShape).background(Color(0x2EFF1D8E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(VryvIcons.Trash, contentDescription = null, tint = VryvPink, modifier = Modifier.size(32.dp))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Запрос принят", color = VryvTextPrimary, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Аккаунт @${user.username.removePrefix("@")} отключён и будет окончательно удалён 23 ноября 2026. Чтобы восстановить — просто войди до этой даты.",
                            color = VryvTextSecondary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                    VryvButton(
                        text = "На главный экран",
                        style = VryvButtonStyle.Secondary,
                        onClick = {
                            repository.deleteCurrentAccount()
                            onLogoutClick()
                        }
                    )
                }
            }
        }
        return
    }

    // Main Settings Screen matching settings.png 1-to-1
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvSubScreenHeader(
            title = "Настройки",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top profile row matching settings.png
            item {
                VryvGroupList {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBackClick() }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = user.avatarRes),
                            contentDescription = user.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = user.name,
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "@${user.username.removePrefix("@")} · Личный профиль",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp
                            )
                        }
                        Icon(
                            imageVector = VryvIcons.ChevronRight,
                            contentDescription = null,
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Group 1: Аккаунт
            item {
                Text(
                    text = "Аккаунт",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvGroupList {
                    VryvGroupRow(
                        icon = VryvIcons.Mail,
                        title = "Email",
                        subtitle = user.email,
                        trailingValue = "Подтверждён",
                        trailingColor = VryvLime,
                        showDivider = true,
                        onClick = { activeSubScreen = "mail" }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Lock,
                        title = "Пароль",
                        subtitle = "Обновлён 2 недели назад",
                        showDivider = true,
                        onClick = { activeSubScreen = "pass" }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Globe,
                        title = "Google",
                        subtitle = "Вход в один тап",
                        trailingValue = "Подключён",
                        trailingColor = VryvLime,
                        showDivider = true,
                        onClick = { repository.showToast("Аккаунт Google подключён") }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Phone,
                        title = "Активные сессии",
                        subtitle = "2 устройства",
                        showDivider = false,
                        onClick = { activeSubScreen = "sessions" }
                    )
                }
            }

            // Group 2: Приложение
            item {
                Text(
                    text = "Приложение",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvGroupList {
                    VryvGroupRow(
                        icon = VryvIcons.Bell,
                        title = "Уведомления",
                        subtitle = "События, друзья, дайджест",
                        showDivider = true,
                        onClick = { activeSubScreen = "notif" }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Eye,
                        title = "Конфиденциальность",
                        subtitle = "Кто видит мои планы: Друзья",
                        showDivider = true,
                        onClick = { activeSubScreen = "priv" }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Shield,
                        title = "Разрешения",
                        subtitle = "Геолокация, пуши, камера",
                        showDivider = true,
                        onClick = { activeSubScreen = "perms" }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Sliders,
                        title = "Мои интересы",
                        subtitle = "6 категорий · до 50 BYN",
                        showDivider = false,
                        onClick = { onBackClick() }
                    )
                }
            }

            // Group 3: Бизнес
            item {
                Text(
                    text = "Бизнес",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvGroupList {
                    VryvGroupRow(
                        icon = VryvIcons.Store,
                        iconTint = VryvLime,
                        title = "ВРЫВ для бизнеса",
                        subtitle = "Заведения и публикация событий",
                        trailingValue = "Loft 13",
                        showDivider = false,
                        onClick = {
                            repository.setBusinessMode(true)
                            onBackClick()
                        }
                    )
                }
            }

            // Demo Account Switcher
            item {
                Text(
                    text = "Демо-аккаунты",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvGroupList {
                    allUsers.forEachIndexed { idx, acc ->
                        val isCurrent = acc.id == user.id
                        VryvGroupRow(
                            icon = VryvIcons.User,
                            iconTint = if (isCurrent) VryvPink else VryvTextPrimary,
                            title = acc.name,
                            subtitle = "${acc.email} · ${if (acc.venueId != null) "Владелец Loft 13" else "Гость"}",
                            trailingValue = if (isCurrent) "Активен" else "Войти",
                            trailingColor = if (isCurrent) VryvLime else VryvPink,
                            showDivider = idx < allUsers.lastIndex,
                            onClick = {
                                if (!isCurrent) repository.switchDemoAccount(acc.id)
                            }
                        )
                    }
                }
            }

            // Group 4: Выйти / Удалить аккаунт matching settings.png
            item {
                VryvGroupList {
                    Box(modifier = Modifier.testTag("logout_button")) {
                        VryvGroupRow(
                            icon = VryvIcons.Logout,
                            title = "Выйти из аккаунта",
                            showDivider = true,
                            onClick = onLogoutClick
                        )
                    }
                    Box(modifier = Modifier.testTag("delete_account_button")) {
                        VryvGroupRow(
                            icon = VryvIcons.Trash,
                            iconTint = VryvPink,
                            title = "Удалить аккаунт",
                            isDanger = true,
                            showDivider = false,
                            onClick = { activeSubScreen = "delacc" }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(36.dp))
            }
        }
    }
}

@Composable
private fun SettingsToggleRowRef(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 12.5.sp
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        VryvToggle(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
