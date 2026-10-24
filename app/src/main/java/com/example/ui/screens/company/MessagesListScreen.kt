package com.example.ui.screens.company

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DirectDialog
import com.example.data.VryvRepository
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun MessagesListScreen(
    repository: VryvRepository,
    onOpenDirectChat: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dialogs by repository.directDialogs.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredDialogs = remember(dialogs, searchQuery) {
        if (searchQuery.isBlank()) dialogs
        else dialogs.filter {
            it.otherUser.name.contains(searchQuery, ignoreCase = true) ||
            it.lastMessage.contains(searchQuery, ignoreCase = true)
        }
    }

    val unreadDialogs = filteredDialogs.filter { it.unreadCount > 0 }
    val earlierDialogs = filteredDialogs.filter { it.unreadCount == 0 }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvSubScreenHeader(
            title = "Сообщения",
            onBackClick = onBackClick
        )

        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
            VryvInputField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Поиск по диалогам...",
                leadingIcon = VryvIcons.Search
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
        ) {
            if (unreadDialogs.isNotEmpty()) {
                item {
                    Text(
                        text = "НЕПРОЧИТАННЫЕ · ${unreadDialogs.size}",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                }
                items(unreadDialogs) { dialog ->
                    DialogRowItem(
                        dialog = dialog,
                        onClick = { onOpenDirectChat(dialog.otherUser.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (earlierDialogs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "РАНЕЕ",
                        color = VryvTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                    )
                }
                items(earlierDialogs) { dialog ->
                    DialogRowItem(
                        dialog = dialog,
                        onClick = { onOpenDirectChat(dialog.otherUser.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DialogRowItem(
    dialog: DirectDialog,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(VryvCard)
            .border(
                width = 1.dp,
                color = if (dialog.unreadCount > 0) VryvPink.copy(alpha = 0.35f) else VryvLine,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box {
            Image(
                painter = painterResource(id = dialog.otherUser.avatarRes),
                contentDescription = dialog.otherUser.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )
            if (dialog.otherUser.isOnline) {
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .clip(CircleShape)
                        .background(VryvLime)
                        .border(2.dp, VryvCard, CircleShape)
                        .align(Alignment.BottomEnd)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dialog.otherUser.name,
                    color = VryvTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dialog.time,
                    color = VryvTextMuted,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = dialog.lastMessage,
                color = if (dialog.unreadCount > 0) VryvTextPrimary else VryvTextSecondary,
                fontSize = 13.sp,
                fontWeight = if (dialog.unreadCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (dialog.unreadCount > 0) {
            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(VryvPink),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${dialog.unreadCount}",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
