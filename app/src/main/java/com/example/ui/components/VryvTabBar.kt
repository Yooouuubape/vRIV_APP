package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink

enum class UserTab(val label: String, val icon: ImageVector, val tag: String) {
    FEED("Афиша", VryvIcons.Home, "tab_feed"),
    SWIPES("Свайпы", VryvIcons.Cards, "tab_swipes"),
    MAP("Карта", VryvIcons.Map, "tab_map"),
    COMPANY("Компания", VryvIcons.Users, "tab_company"),
    PROFILE("Профиль", VryvIcons.User, "tab_profile")
}

enum class BusinessTab(val label: String, val icon: ImageVector, val tag: String) {
    OVERVIEW("Обзор", VryvIcons.Grid, "tab_biz_overview"),
    EVENTS("События", VryvIcons.Cal, "tab_biz_events"),
    AUDIENCE("Аудитория", VryvIcons.Chart, "tab_biz_audience"),
    MESSAGES("Сообщения", VryvIcons.Chat, "tab_biz_messages"),
    SETTINGS("Настройки", VryvIcons.Gear, "tab_biz_settings")
}

@Composable
fun VryvUserTabBar(
    selectedTab: UserTab,
    onTabSelected: (UserTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        VryvBg.copy(alpha = 0.85f),
                        VryvBg
                    )
                )
            )
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xD91E1E25))
                .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(32.dp))
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val tint = if (isSelected) VryvPink else Color(0xFF9696A3)
                Column(
                    modifier = Modifier
                        .width(68.dp)
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(if (isSelected) Color(0xFF28202D) else Color.Transparent)
                        .clickable { onTabSelected(tab) }
                        .testTag(tab.tag),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = tint,
                        modifier = Modifier.size(23.dp)
                    )
                    Text(
                        text = tab.label,
                        color = tint,
                        fontFamily = InterFontFamily,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.05).sp,
                        modifier = Modifier.padding(top = 3.dp)
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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        VryvBg.copy(alpha = 0.85f),
                        VryvBg
                    )
                )
            )
            .navigationBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xD91E1E25))
                .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(32.dp))
                .padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BusinessTab.values().forEach { tab ->
                val isSelected = tab == selectedTab
                val tint = if (isSelected) VryvLime else Color(0xFF7C7C8A)
                Box(
                    modifier = Modifier
                        .width(66.dp)
                        .height(54.dp)
                        .clip(RoundedCornerShape(27.dp))
                        .background(if (isSelected) Color(0x1FC7FF28) else Color.Transparent)
                        .clickable { onTabSelected(tab) }
                        .testTag(tab.tag),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = tint,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = tab.label,
                            color = tint,
                            fontFamily = InterFontFamily,
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = (-0.05).sp,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                    if (tab == BusinessTab.MESSAGES && hasUnreadMessages) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-12).dp, y = 7.dp)
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(VryvLime)
                        )
                    }
                }
            }
        }
    }
}
