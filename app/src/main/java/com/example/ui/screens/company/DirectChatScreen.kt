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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
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
fun DirectChatScreen(
    userId: String,
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onReportClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by repository.currentUser.collectAsState()
    val companions by repository.companions.collectAsState()
    val dialogs by repository.directDialogs.collectAsState()

    val companion = companions.find { it.id == userId }
    val dialog = dialogs.find { it.otherUser.id == userId }
    val isBlocked = companion?.isBlocked == true || currentUser.blockedUserIds.contains(userId)

    var textInput by remember { mutableStateOf("") }
    var showActionSheet by remember { mutableStateOf(false) }

    val quickReplies = listOf("Пойдём!", "Кто ещё идёт?", "Во сколько встречаемся?")

    if (companion == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Пользователь не найден", color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            VryvButton(
                text = "Вернуться назад",
                style = VryvButtonStyle.PrimaryPink,
                onClick = onBackClick
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header matching lchat.png / blocked.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(VryvBg)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VryvIconButton(
                icon = VryvIcons.Back,
                onClick = onBackClick
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box {
                Image(
                    painter = painterResource(id = companion.avatarRes),
                    contentDescription = companion.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                if (!isBlocked && companion.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(VryvLime)
                            .border(2.dp, VryvBg, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = companion.name,
                    color = VryvTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBlocked) "Заблокирован" else "@${companion.username} · в сети",
                    color = if (isBlocked) VryvDanger else VryvTextSecondary,
                    fontSize = 12.sp
                )
            }

            VryvIconButton(
                icon = VryvIcons.More,
                onClick = { showActionSheet = true }
            )
        }

        // Messages list
        val messages = dialog?.messages ?: emptyList()

        if (messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Начните диалог с ${companion.name}",
                    color = VryvTextSecondary,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages, key = { it.id }) { msg ->
                    val isMyMsg = msg.isMe || msg.senderId == currentUser.id
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = if (isMyMsg) 48.dp else 0.dp,
                                end = if (isMyMsg) 0.dp else 48.dp
                            ),
                        contentAlignment = if (isMyMsg) Alignment.CenterEnd else Alignment.CenterStart
                    ) {
                        Column(
                            horizontalAlignment = if (isMyMsg) Alignment.End else Alignment.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 18.dp,
                                            topEnd = 18.dp,
                                            bottomStart = if (isMyMsg) 18.dp else 4.dp,
                                            bottomEnd = if (isMyMsg) 4.dp else 18.dp
                                        )
                                    )
                                    .background(if (isMyMsg) VryvPink else VryvCard)
                                    .border(
                                        width = if (isMyMsg) 0.dp else 1.dp,
                                        color = if (isMyMsg) Color.Transparent else VryvLine,
                                        shape = RoundedCornerShape(
                                            topStart = 18.dp,
                                            topEnd = 18.dp,
                                            bottomStart = if (isMyMsg) 18.dp else 4.dp,
                                            bottomEnd = if (isMyMsg) 4.dp else 18.dp
                                        )
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = msg.text,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = msg.time,
                                color = VryvTextMuted,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Blocked banner (blocked.png) or Quick replies + Input Bar (lchat.png)
        if (isBlocked) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(VryvDanger.copy(alpha = 0.14f))
                    .border(1.dp, VryvDanger.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Пользователь заблокирован. Отправка сообщений недоступна.",
                    color = VryvDanger,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(quickReplies) { reply ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                            .clickable {
                                val dialogId = dialog?.id ?: "dm_${companion.id}"
                                repository.sendDirectMessage(dialogId, reply)
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = reply,
                            color = VryvTextPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VryvBg)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    placeholder = { Text("Сообщение...", color = VryvTextMuted, fontSize = 14.sp) },
                    shape = RoundedCornerShape(999.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = VryvCardElevated,
                        unfocusedContainerColor = VryvCardElevated,
                        focusedBorderColor = VryvPink,
                        unfocusedBorderColor = VryvLine,
                        focusedTextColor = VryvTextPrimary,
                        unfocusedTextColor = VryvTextPrimary
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_input_field")
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (textInput.isNotBlank()) {
                            val dialogId = dialog?.id ?: "dm_${companion.id}"
                            repository.sendDirectMessage(dialogId, textInput)
                            textInput = ""
                        }
                    },
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(VryvPink)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = VryvIcons.Send,
                        contentDescription = "Отправить",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Action sheet matching block.png
    if (showActionSheet) {
        VryvFloatingBottomSheet(
            onDismiss = { showActionSheet = false }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Действия с чатом",
                    color = VryvTextPrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            showActionSheet = false
                            onReportClick("Пользователь", companion.name)
                        }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(VryvIcons.Flag, contentDescription = null, tint = VryvTextPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Пожаловаться на пользователя", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            showActionSheet = false
                            repository.blockUser(companion.id)
                        }
                        .padding(vertical = 12.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(VryvIcons.Shield, contentDescription = null, tint = VryvDanger, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "Заблокировать собеседника", color = VryvDanger, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
