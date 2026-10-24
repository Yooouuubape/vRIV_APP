package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = VryvTextPrimary
                )
            }
            Text(
                text = "Настройки",
                color = VryvTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Group 1: Аккаунт
            item {
                Text(
                    text = "Аккаунт и безопасность",
                    color = VryvTextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCard)
                ) {
                    SettingsRowItem(
                        icon = Icons.Default.Lock,
                        title = "Сменить пароль",
                        subtitle = "Последнее изменение 3 мес назад",
                        onClick = { repository.showToast("Функция смены пароля активна") }
                    )
                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                    SettingsRowItem(
                        icon = Icons.Default.Devices,
                        title = "Активные сессии",
                        subtitle = "iPhone 15 Pro, Минск (сейчас)",
                        onClick = { repository.showToast("1 активное устройство") }
                    )
                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                    SettingsRowItem(
                        icon = Icons.Default.Security,
                        title = "Конфиденциальность",
                        subtitle = "Видимость планов и данных",
                        onClick = { repository.showToast("Настройки приватности сохранены") }
                    )
                }
            }

            // Group 2: Уведомления
            item {
                Text(
                    text = "Уведомления",
                    color = VryvTextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCard)
                ) {
                    SettingsRowItem(
                        icon = Icons.Default.Notifications,
                        title = "Push-уведомления",
                        subtitle = "События, чаты, компании",
                        onClick = { repository.showToast("Уведомления включены") }
                    )
                }
            }

            // Group 3: Тестирование и состояние
            item {
                Text(
                    text = "Демо-режим и переключение пользователей",
                    color = VryvTextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCard)
                ) {
                    SettingsRowItem(
                        icon = Icons.Default.Person,
                        title = "Войти как Максим Салуков",
                        subtitle = "Владелец Loft 13 (u_max)",
                        onClick = { repository.switchAccount("u_max") },
                        iconColor = VryvLime
                    )
                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                    SettingsRowItem(
                        icon = Icons.Default.Person,
                        title = "Войти как Алина",
                        subtitle = "Посетитель, вечеринки и выставки (u_alina)",
                        onClick = { repository.switchAccount("u_alina") },
                        iconColor = VryvPink
                    )
                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                    SettingsRowItem(
                        icon = Icons.Default.Person,
                        title = "Войти как Артём",
                        subtitle = "Посетитель, стендап и музыка (u_artem)",
                        onClick = { repository.switchAccount("u_artem") },
                        iconColor = VryvLime
                    )
                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                    SettingsRowItem(
                        icon = Icons.Default.Refresh,
                        title = "Сбросить демо-данные",
                        subtitle = "Восстановить события, заявки, группы и чаты в исходное",
                        onClick = {
                            repository.resetToDefaults()
                        },
                        iconColor = VryvPink
                    )
                }
            }

            // Group 4: Выход
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x22FF1D8E))
                        .clickable { onLogoutClick() }
                        .padding(vertical = 14.dp)
                        .testTag("logout_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = VryvPink,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Выйти из аккаунта",
                            color = VryvPink,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    iconColor: Color = VryvPink
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, color = VryvTextSecondary, fontSize = 12.sp)
        }

        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = VryvTextMuted, modifier = Modifier.size(18.dp))
    }
}
