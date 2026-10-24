package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary

enum class UserTab(val label: String, val icon: ImageVector, val tag: String) {
    FEED("Афиша", Icons.Default.Home, "tab_feed"),
    SWIPES("Свайпы", Icons.Default.Style, "tab_swipes"),
    MAP("Карта", Icons.Default.Map, "tab_map"),
    COMPANY("Компания", Icons.Default.Group, "tab_company"),
    PROFILE("Профиль", Icons.Default.Person, "tab_profile")
}

enum class BusinessTab(val label: String, val icon: ImageVector, val tag: String) {
    OVERVIEW("Обзор", Icons.Default.Dashboard, "tab_biz_overview"),
    EVENTS("События", Icons.Default.CalendarMonth, "tab_biz_events"),
    AUDIENCE("Аудитория", Icons.Default.Analytics, "tab_biz_audience"),
    MESSAGES("Сообщения", Icons.Default.Chat, "tab_biz_messages"),
    SETTINGS("Настройки", Icons.Default.Settings, "tab_biz_settings")
}

@Composable
fun VryvUserTabBar(
    selectedTab: UserTab,
    onTabSelected: (UserTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(VryvBg)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val tint = if (isSelected) VryvPink else VryvTextMuted
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 6.dp)
                        .testTag(tab.tag),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = tab.label,
                        color = tint,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun VryvBusinessTabBar(
    selectedTab: BusinessTab,
    onTabSelected: (BusinessTab) -> Unit,
    hasUnreadMessages: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(VryvBg)
            .navigationBarsPadding()
    ) {
        HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BusinessTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val tint = if (isSelected) VryvLime else VryvTextMuted
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(CircleShape)
                        .clickable { onTabSelected(tab) }
                        .padding(vertical = 6.dp)
                        .testTag(tab.tag),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = tint,
                            modifier = Modifier.size(24.dp)
                        )
                        if (tab == BusinessTab.MESSAGES && hasUnreadMessages) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(VryvLime)
                                    .align(Alignment.TopEnd)
                            )
                        }
                    }
                    Text(
                        text = tab.label,
                        color = tint,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}
