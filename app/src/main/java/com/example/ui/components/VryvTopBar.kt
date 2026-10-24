package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary

@Composable
fun VryvMainHeader(
    unreadNotificationsCount: Int,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VryvBg)
            .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "ВРЫВ Логотип",
            contentScale = ContentScale.Fit,
            modifier = Modifier.height(42.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                    .padding(horizontal = 14.dp)
            ) {
                Icon(
                    imageVector = VryvIcons.Pin,
                    contentDescription = null,
                    tint = VryvPink,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Минск",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            VryvIconButton(
                icon = VryvIcons.Search,
                onClick = onOpenSearch,
                contentDescription = "Поиск",
                testTag = "search_button"
            )
            VryvIconButton(
                icon = VryvIcons.Bell,
                onClick = onOpenNotifications,
                contentDescription = "Уведомления",
                hasUnreadDot = unreadNotificationsCount > 0,
                testTag = "notifications_button"
            )
        }
    }
}

@Composable
fun VryvSubScreenHeader(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    centerTitle: Boolean = subtitle == null,
    rightAction: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(VryvBg)
            .padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        VryvIconButton(
            icon = VryvIcons.Back,
            onClick = onBackClick,
            contentDescription = "Назад",
            testTag = "back_button"
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            horizontalAlignment = if (centerTitle) Alignment.CenterHorizontally else Alignment.Start
        ) {
            Text(
                text = title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.2).sp,
                textAlign = if (centerTitle) TextAlign.Center else TextAlign.Start,
                maxLines = 1
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = VryvTextMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
        }

        if (rightAction != null) {
            rightAction()
        } else if (centerTitle) {
            Spacer(modifier = Modifier.width(40.dp))
        }
    }
}
