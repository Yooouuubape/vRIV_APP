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
import com.example.R
import com.example.data.ChatMessage
import com.example.data.VryvRepository
import com.example.ui.components.VryvAvatarStack
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
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun GroupChatScreen(
    groupId: String,
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onReportClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val groups by repository.companyGroups.collectAsState()
    val group = groups.find { it.id == groupId } ?: groups.first()

    var messageText by remember { mutableStateOf("") }
    var showMembersSheet by remember { mutableStateOf(false) }

    val quickReplies = listOf("Отлично!", "Кто за баром после?", "Беру подругу")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header matching compChat.png 1-to-1
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
            VryvAvatarStack(
                avatars = group.memberAvatars.take(2),
                avatarSize = 22.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.title,
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "4 участника · сегодня",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                VryvIconButton(
                    icon = VryvIcons.Users,
                    onClick = { showMembersSheet = true }
                )
                VryvIconButton(
                    icon = VryvIcons.More,
                    onClick = { onReportClick("group", group.title) }
                )
            }
        }

        HorizontalDivider(color = VryvLine, thickness = 1.dp)

        // Messages list matching compChat.png
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Сегодня",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }

            items(group.messages, key = { it.id }) { msg ->
                GroupChatBubbleRef(message = msg)
            }

            item {
                Text(
                    text = "Катя присоединилась к компании",
                    color = VryvLime,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
        }

        // Quick reply chips matching compChat.png
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(quickReplies) { reply ->
                Box(
                    modifier = Modifier
                        .height(34.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(VryvCardElevated)
                        .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                        .clickable { repository.sendGroupMessage(group.id, reply) }
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = reply,
                        color = Color(0xFFD0D0DC),
                        fontFamily = InterFontFamily,
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Input Bar matching compChat.png
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
                        .testTag("group_chat_input"),
                    decorationBox = { innerTextField ->
                        if (messageText.isEmpty()) {
                            Text(
                                text = "Сообщение компании...",
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
                        repository.sendGroupMessage(group.id, messageText)
                        messageText = ""
                    }
                },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(VryvPink)
                    .testTag("group_chat_send_btn")
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

    // Members Sheet matching compMembers.png 1-to-1
    if (showMembersSheet) {
        VryvFloatingBottomSheet(
            onDismiss = { showMembersSheet = false }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Участники · 4 из 5",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                VryvGroupList {
                    val members = listOf(
                        Triple("Максим · создатель", "@max.vryv", R.drawable.av2),
                        Triple("Алина, 24", "@alina.tech", R.drawable.av1),
                        Triple("Дима, 25", "@dima.vinyl", R.drawable.av4),
                        Triple("Катя, 22", "@katya.ink · недавно", R.drawable.av3)
                    )
                    members.forEachIndexed { idx, (name, sub, avatar) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = avatar),
                                contentDescription = name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = name,
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = sub,
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp
                                )
                            }
                            if (idx > 0) {
                                Icon(
                                    imageVector = VryvIcons.ChevronRight,
                                    contentDescription = null,
                                    tint = VryvTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        if (idx < members.lastIndex) {
                            HorizontalDivider(color = VryvLine, thickness = 1.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                VryvButton(
                    text = "Пригласить друзей",
                    style = VryvButtonStyle.Secondary,
                    onClick = {
                        showMembersSheet = false
                        repository.showToast("Приглашение друзей")
                    }
                )
            }
        }
    }
}

@Composable
private fun GroupChatBubbleRef(message: ChatMessage) {
    if (message.isMe) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .clip(RoundedCornerShape(18.dp))
                    .background(VryvPink)
                    .padding(horizontal = 13.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 19.sp
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = message.senderAvatarRes),
                contentDescription = message.senderName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "${message.senderName} · ${message.time}",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(start = 6.dp, bottom = 3.dp)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(VryvCardElevated)
                        .padding(horizontal = 13.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = message.text,
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}
