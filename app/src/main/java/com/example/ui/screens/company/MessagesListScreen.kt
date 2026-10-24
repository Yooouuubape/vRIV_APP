package com.example.ui.screens.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DirectChatThread
import com.example.data.VryvRepository
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
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
    val threads by repository.directThreads.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredThreads = remember(threads, searchQuery) {
        if (searchQuery.isBlank()) threads
        else threads.filter {
            it.userName.contains(searchQuery, ignoreCase = true) ||
                it.userHandle.contains(searchQuery, ignoreCase = true) ||
                it.lastMessage.contains(searchQuery, ignoreCase = true)
        }
    }

    val unreadThreads = filteredThreads.filter { it.unreadCount > 0 }
    val earlierThreads = filteredThreads.filter { it.unreadCount == 0 }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header matching msgs.png
        VryvSubScreenHeader(
            title = "Сообщения",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Search bar matching msgs.png
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .clip(RoundedCornerShape(23.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(23.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = VryvIcons.Search,
                    contentDescription = null,
                    tint = VryvTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp
                    ),
                    cursorBrush = SolidColor(VryvPink),
                    modifier = Modifier.weight(1f),
                    decorationBox = { inner ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Поиск диалогов",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 14.sp
                            )
                        }
                        inner()
                    }
                )
            }

            if (unreadThreads.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Непрочитанные · ${unreadThreads.size}",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvGroupList {
                    unreadThreads.forEachIndexed { idx, thread ->
                        DirectThreadRowRef(
                            thread = thread,
                            isUnread = true,
                            onClick = { onOpenDirectChat(thread.userId) }
                        )
                        if (idx < unreadThreads.lastIndex) {
                            HorizontalDivider(color = VryvLine, thickness = 1.dp)
                        }
                    }
                }
            }

            if (earlierThreads.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ранее",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvGroupList {
                    earlierThreads.forEachIndexed { idx, thread ->
                        DirectThreadRowRef(
                            thread = thread,
                            isUnread = false,
                            onClick = { onOpenDirectChat(thread.userId) }
                        )
                        if (idx < earlierThreads.lastIndex) {
                            HorizontalDivider(color = VryvLine, thickness = 1.dp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun DirectThreadRowRef(
    thread: DirectChatThread,
    isUnread: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 13.dp)
            .testTag("direct_thread_${thread.userId}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = thread.userAvatarRes),
            contentDescription = thread.userName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = thread.userName,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if (thread.isBlocked) "Пользователь заблокирован" else thread.lastMessage,
                color = if (isUnread) VryvTextPrimary else VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = thread.lastTime,
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 11.5.sp
            )
            if (isUnread) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(VryvPink)
                )
            }
        }
    }
}
