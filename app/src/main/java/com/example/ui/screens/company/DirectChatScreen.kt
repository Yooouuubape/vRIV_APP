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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ChatMessage
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
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
    val threads by repository.directThreads.collectAsState()
    val thread = threads.find { it.userId == userId } ?: threads.first()

    var messageText by remember { mutableStateOf("") }
    var showBlockSheet by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header matching lchat.png / blocked.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VryvIconButton(
                icon = VryvIcons.Back,
                onClick = onBackClick
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = thread.userAvatarRes),
                contentDescription = thread.userName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .alpha(if (thread.isBlocked) 0.5f else 1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = thread.userName.substringBefore(","),
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (thread.isBlocked) "Заблокирована" else "${thread.userHandle} · в сети",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp
                )
            }

            IconButton(
                onClick = { showBlockSheet = true },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VryvCardElevated)
                    .testTag("direct_chat_more_button")
            ) {
                Icon(
                    imageVector = VryvIcons.More,
                    contentDescription = "Ещё",
                    tint = VryvTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        HorizontalDivider(color = VryvLine, thickness = 1.dp)

        if (thread.isBlocked) {
            // Blocked state matching blocked.png 1-to-1
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(VryvCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Ban,
                        contentDescription = null,
                        tint = VryvPink,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${thread.userName.substringBefore(",")} заблокирована",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Она не видит твой профиль и не может писать. Прежние сообщения скрыты.",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                VryvButton(
                    text = "Разблокировать",
                    style = VryvButtonStyle.Secondary,
                    onClick = { repository.toggleBlockDirectUser(thread.userId) }
                )
            }
        } else {
            // Normal chat messages matching lchat.png
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = "Сегодня · Совпадение по событию «Techno Room»",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                }

                items(thread.messages, key = { it.id }) { msg ->
                    DirectChatBubbleRef(message = msg)
                }
            }

            // Input bar matching lchat.png
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(VryvBg)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clip(RoundedCornerShape(22.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(22.dp))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp
                        ),
                        cursorBrush = SolidColor(VryvPink),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("direct_chat_input"),
                        decorationBox = { innerTextField ->
                            if (messageText.isEmpty()) {
                                Text(
                                    text = "Написать ${thread.userName.substringBefore(",")}...",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            repository.sendDirectMessage(thread.userId, messageText)
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(VryvPink)
                        .testTag("direct_chat_send_btn")
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

    // Block / Report Sheet matching block.png 1-to-1
    if (showBlockSheet) {
        VryvFloatingBottomSheet(
            onDismiss = { showBlockSheet = false }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 14.dp)
                ) {
                    Image(
                        painter = painterResource(id = thread.userAvatarRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = thread.userName.substringBefore(","),
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = thread.userHandle,
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp
                        )
                    }
                }

                VryvGroupList {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showBlockSheet = false
                                onReportClick("user", thread.userName)
                            }
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
                            Icon(
                                imageVector = VryvIcons.Flag,
                                contentDescription = null,
                                tint = VryvTextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Пожаловаться",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Icon(
                            imageVector = VryvIcons.ChevronRight,
                            contentDescription = null,
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    HorizontalDivider(color = VryvLine, thickness = 1.dp)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                repository.toggleBlockDirectUser(thread.userId)
                                showBlockSheet = false
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x2EFF1D8E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Ban,
                                contentDescription = null,
                                tint = VryvPink,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (thread.isBlocked) "Разблокировать" else "Заблокировать",
                                color = VryvPink,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "Не сможет писать",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                VryvButton(
                    text = "Отмена",
                    style = VryvButtonStyle.Secondary,
                    onClick = { showBlockSheet = false }
                )
            }
        }
    }
}

@Composable
private fun DirectChatBubbleRef(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.78f)
                .clip(RoundedCornerShape(18.dp))
                .background(if (message.isMe) VryvPink else VryvCardElevated)
                .padding(horizontal = 13.dp, vertical = 10.dp)
        ) {
            Text(
                text = message.text,
                color = if (message.isMe) Color.White else VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                lineHeight = 19.sp
            )
        }
    }
}
