package com.example.ui.screens.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.components.GoogleMulticolorIcon
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvGroupRow
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvSegmentedControl
import com.example.ui.components.VryvStatusPill
import com.example.ui.components.VryvStatusStyle
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.components.VryvToggle
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

private enum class SettingsSubPage {
    MAIN,
    CHANGE_PASS,
    SESSIONS,
    NOTIFICATIONS,
    PRIVACY,
    PERMISSIONS
}

@Composable
fun SettingsScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by repository.currentUser.collectAsState()
    var subPage by remember { mutableStateOf(SettingsSubPage.MAIN) }
    var showDeleteConfirmSheet by remember { mutableStateOf(false) }

    when (subPage) {
        SettingsSubPage.CHANGE_PASS -> {
            ChangePasswordSubScreen(
                repository = repository,
                onBack = { subPage = SettingsSubPage.MAIN }
            )
            return
        }
        SettingsSubPage.SESSIONS -> {
            SessionsSubScreen(
                repository = repository,
                onBack = { subPage = SettingsSubPage.MAIN }
            )
            return
        }
        SettingsSubPage.NOTIFICATIONS -> {
            NotificationsSettingsSubScreen(
                onBack = { subPage = SettingsSubPage.MAIN }
            )
            return
        }
        SettingsSubPage.PRIVACY -> {
            PrivacySettingsSubScreen(
                userPrivacy = user.privacyPlansVisibility,
                onUpdatePrivacy = { repository.updateUserProfile(user.name, user.bio, it) },
                onBack = { subPage = SettingsSubPage.MAIN }
            )
            return
        }
        SettingsSubPage.PERMISSIONS -> {
            PermissionsSettingsSubScreen(
                onBack = { subPage = SettingsSubPage.MAIN }
            )
            return
        }
        SettingsSubPage.MAIN -> Unit
    }

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
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Section 1: АККАУНТ И БЕЗОПАСНОСТЬ (settings.png)
            item {
                SectionLabel("АККАУНТ И БЕЗОПАСНОСТЬ")
                VryvGroupList {
                    VryvGroupRow(
                        icon = VryvIcons.Mail,
                        title = "Почта",
                        subtitle = "m.veres@gmail.com · Подтверждена",
                        showDivider = true,
                        onClick = { repository.showToast("Почта подтверждена") }
                    )
                    // Google row with multicolor icon
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { repository.showToast("Аккаунт Google подключён") }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(VryvCardElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            GoogleMulticolorIcon(size = 18.dp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Google", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "Подключён", color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        VryvStatusPill(text = "Активен", style = VryvStatusStyle.Lime)
                    }
                    HorizontalDivider(color = VryvLine, thickness = 1.dp)
                    VryvGroupRow(
                        icon = VryvIcons.Lock,
                        title = "Сменить пароль",
                        subtitle = "Обновлён 2 мес. назад",
                        showDivider = true,
                        onClick = { subPage = SettingsSubPage.CHANGE_PASS }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Globe,
                        title = "Активные сессии",
                        subtitle = "2 устройства",
                        showDivider = false,
                        onClick = { subPage = SettingsSubPage.SESSIONS }
                    )
                }
            }

            // Section 2: ПРИЛОЖЕНИЕ (settings.png)
            item {
                SectionLabel("ПРИЛОЖЕНИЕ")
                VryvGroupList {
                    VryvGroupRow(
                        icon = VryvIcons.Bell,
                        title = "Уведомления",
                        subtitle = "События, друзья, компании",
                        showDivider = true,
                        onClick = { subPage = SettingsSubPage.NOTIFICATIONS }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Shield,
                        title = "Приватность",
                        subtitle = "Планы видят: ${user.privacyPlansVisibility}",
                        showDivider = true,
                        onClick = { subPage = SettingsSubPage.PRIVACY }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Pin,
                        title = "Разрешения",
                        subtitle = "Геолокация, пуши, камера",
                        showDivider = false,
                        onClick = { subPage = SettingsSubPage.PERMISSIONS }
                    )
                }
            }

            // Section 3: ДЕМО-РЕЖИМ И ПЕРЕКЛЮЧЕНИЕ ПОЛЬЗОВАТЕЛЕЙ (preserved for testability & interactive switching)
            item {
                SectionLabel("ДЕМО-РЕЖИМ И ПЕРЕКЛЮЧЕНИЕ ПОЛЬЗОВАТЕЛЕЙ")
                VryvGroupList {
                    VryvGroupRow(
                        icon = VryvIcons.User,
                        title = "Войти как Максим Салуков",
                        subtitle = "Владелец Loft 13 (u_max)",
                        showDivider = true,
                        onClick = { repository.switchAccount("u_max") }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.User,
                        title = "Войти как Алина",
                        subtitle = "Посетитель, вечеринки и выставки (u_alina)",
                        showDivider = true,
                        onClick = { repository.switchAccount("u_alina") }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.User,
                        title = "Войти как Артём",
                        subtitle = "Посетитель, стендап и музыка (u_artem)",
                        showDivider = true,
                        onClick = { repository.switchAccount("u_artem") }
                    )
                    VryvGroupRow(
                        icon = VryvIcons.Spark,
                        title = "Сбросить демо-данные",
                        subtitle = "Восстановить события, заявки и чаты",
                        showDivider = false,
                        onClick = { repository.resetToDefaults() }
                    )
                }
            }

            // Section 4: Actions (Выйти из аккаунта / Удалить аккаунт) matching settings.png
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
                            .clickable { onLogoutClick() }
                            .testTag("logout_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Выйти из аккаунта",
                            color = VryvTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { showDeleteConfirmSheet = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Удалить аккаунт",
                            color = VryvDanger,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Delete account confirmation sheet (delacc.png)
    if (showDeleteConfirmSheet) {
        VryvFloatingBottomSheet(onDismiss = { showDeleteConfirmSheet = false }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(VryvDanger.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Alert,
                        contentDescription = null,
                        tint = VryvDanger,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Удалить аккаунт?",
                    color = VryvTextPrimary,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Все планы, избранное, чаты и компании будут удалены. В течение 14 дней аккаунт можно восстановить по почте.",
                    color = VryvTextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(18.dp))
                VryvButton(
                    text = "Удалить аккаунт",
                    style = VryvButtonStyle.Danger,
                    onClick = {
                        showDeleteConfirmSheet = false
                        onLogoutClick()
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                VryvButton(
                    text = "Отмена",
                    style = VryvButtonStyle.SecondaryDark,
                    onClick = { showDeleteConfirmSheet = false }
                )
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        color = VryvTextSecondary,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun ChangePasswordSubScreen(
    repository: VryvRepository,
    onBack: () -> Unit
) {
    var currentPass by remember { mutableStateOf("••••••••••") }
    var newPass by remember { mutableStateOf("NewMinsk2026!") }
    var repeatPass by remember { mutableStateOf("NewMinsk2026!") }
    var savedSuccess by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvSubScreenHeader(
            title = "Сменить пароль",
            onBackClick = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            if (!savedSuccess) {
                VryvInputField(
                    value = currentPass,
                    onValueChange = { currentPass = it },
                    label = "Текущий пароль",
                    trailingIcon = VryvIcons.Eye
                )
                Spacer(modifier = Modifier.height(14.dp))
                VryvInputField(
                    value = newPass,
                    onValueChange = { newPass = it },
                    label = "Новый пароль",
                    trailingIcon = VryvIcons.Eye
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
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
                    value = repeatPass,
                    onValueChange = { repeatPass = it },
                    label = "Повторите новый пароль",
                    trailingIcon = VryvIcons.Check
                )
                Spacer(modifier = Modifier.weight(1f))
                VryvButton(
                    text = "Обновить пароль",
                    style = VryvButtonStyle.PrimaryPink,
                    onClick = {
                        savedSuccess = true
                        repository.showToast("Пароль успешно обновлён")
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                // passOk.png state
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(VryvLime.copy(alpha = 0.15f))
                            .border(1.5.dp, VryvLime, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Check,
                            contentDescription = null,
                            tint = VryvLime,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = "Пароль обновлён",
                        color = VryvTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Все остальные устройства останутся в аккаунте, пока вы не завершите их сессии.",
                        color = VryvTextSecondary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    VryvButton(
                        text = "Вернуться в настройки",
                        style = VryvButtonStyle.PrimaryPink,
                        onClick = onBack
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionsSubScreen(
    repository: VryvRepository,
    onBack: () -> Unit
) {
    var otherSessionsTerminated by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvSubScreenHeader(
            title = "Активные сессии",
            onBackClick = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            SectionLabel("ЭТО УСТРОЙСТВО")
            VryvGroupList {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(VryvLime.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(VryvIcons.Check, contentDescription = null, tint = VryvLime, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("iPhone 15 Pro · ВРЫВ iOS", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text("Минск · Сейчас онлайн", color = VryvTextSecondary, fontSize = 12.sp)
                    }
                    VryvStatusPill("Текущая", VryvStatusStyle.Lime)
                }
            }

            if (!otherSessionsTerminated) {
                Spacer(modifier = Modifier.height(18.dp))
                SectionLabel("ДРУГИЕ УСТРОЙСТВА")
                VryvGroupList {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(VryvCardElevated),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(VryvIcons.Globe, contentDescription = null, tint = VryvTextPrimary, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("MacBook Air · Chrome", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text("Минск · Вчера в 21:08", color = VryvTextSecondary, fontSize = 12.sp)
                        }
                        Text(
                            text = "Выйти",
                            color = VryvDanger,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.clickable {
                                otherSessionsTerminated = true
                                repository.showToast("Сессия завершена")
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                VryvButton(
                    text = "Выйти на всех других устройствах",
                    style = VryvButtonStyle.SecondaryDark,
                    onClick = {
                        otherSessionsTerminated = true
                        repository.showToast("Все другие сессии завершены")
                    }
                )
            }
        }
    }
}

@Composable
private fun NotificationsSettingsSubScreen(onBack: () -> Unit) {
    var remindGoing by remember { mutableStateOf(true) }
    var eventChanges by remember { mutableStateOf(true) }
    var friendInvites by remember { mutableStateOf(true) }
    var companyRequests by remember { mutableStateOf(true) }
    var chatMessages by remember { mutableStateOf(true) }
    var digest by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvSubScreenHeader(
            title = "Уведомления",
            onBackClick = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            SectionLabel("СОБЫТИЯ")
            VryvGroupList {
                ToggleSettingRow("Напоминания о «Пойду»", "За 3 часа до начала", remindGoing) { remindGoing = it }
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                ToggleSettingRow("Изменения и отмены", "Перенос времени, смена площадки", eventChanges) { eventChanges = it }
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                ToggleSettingRow("Дайджест выходных", "По четвергам в 18:00", digest) { digest = it }
            }
            Spacer(modifier = Modifier.height(18.dp))
            SectionLabel("ЛЮДИ И КОМПАНИИ")
            VryvGroupList {
                ToggleSettingRow("Приглашения от друзей", "Когда вас зовут на событие", friendInvites) { friendInvites = it }
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                ToggleSettingRow("Заявки в компанию", "Новые отклики и ответы", companyRequests) { companyRequests = it }
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                ToggleSettingRow("Сообщения в чатах", "Личные и групповые чаты", chatMessages) { chatMessages = it }
            }
        }
    }
}

@Composable
private fun PrivacySettingsSubScreen(
    userPrivacy: String,
    onUpdatePrivacy: (String) -> Unit,
    onBack: () -> Unit
) {
    val options = listOf("Все", "Друзья", "Только я")
    var selectedPlansIdx by remember { mutableStateOf(options.indexOf(userPrivacy).coerceAtLeast(0)) }
    var showInEventParticipants by remember { mutableStateOf(true) }
    var allowDirectMessages by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvSubScreenHeader(
            title = "Приватность",
            onBackClick = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            SectionLabel("КТО ВИДИТ МОИ ПЛАНЫ «ПОЙДУ»")
            VryvSegmentedControl(
                options = options,
                selectedIndex = selectedPlansIdx,
                onSelect = {
                    selectedPlansIdx = it
                    onUpdatePrivacy(options[it])
                }
            )
            Spacer(modifier = Modifier.height(18.dp))
            SectionLabel("ПРОФИЛЬ И ЧАТЫ")
            VryvGroupList {
                ToggleSettingRow("Показывать меня в списке участников", "На странице события", showInEventParticipants) { showInEventParticipants = it }
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                ToggleSettingRow("Личные сообщения", "Разрешать писать участникам компаний", allowDirectMessages) { allowDirectMessages = it }
            }
        }
    }
}

@Composable
private fun PermissionsSettingsSubScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvSubScreenHeader(
            title = "Разрешения",
            onBackClick = onBack
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            SectionLabel("СИСТЕМНЫЕ ДОСТУПЫ iOS / ANDROID")
            VryvGroupList {
                PermissionRow(VryvIcons.Pin, "Геолокация", "Для карты и событий рядом", "При использовании", VryvStatusStyle.Lime)
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                PermissionRow(VryvIcons.Bell, "Push-уведомления", "Напоминания, приглашения, чаты", "Разрешено", VryvStatusStyle.Lime)
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                PermissionRow(VryvIcons.Camera, "Фото и камера", "Аватар и обложки событий", "Только выбранные", VryvStatusStyle.Neutral)
            }
        }
    }
}

@Composable
private fun ToggleSettingRow(
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
            Text(text = title, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = VryvTextSecondary, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        VryvToggle(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun PermissionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    status: String,
    style: VryvStatusStyle
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(VryvCardElevated),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = VryvTextPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = VryvTextSecondary, fontSize = 12.sp)
        }
        VryvStatusPill(status, style)
    }
}
