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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvStatusPill
import com.example.ui.components.VryvStatusStyle
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
fun GroupChatScreen(
    groupId: String,
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onReportClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by repository.currentUser.collectAsState()
    val groups by repository.groups.collectAsState()
    val group = groups.find { it.id == groupId }

    var textInput by remember { mutableStateOf("") }
    var showMembersSheet by remember { mutableStateOf(false) }

    val quickReplies = listOf("Во сколько встречаемся?", "Я на месте", "Кто ещё идёт?", "Встречаемся у входа")

    if (group == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Компания не найдена", color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            VryvButton(
                text = "Назад к компаниям",
                style = VryvButtonStyle.PrimaryPink,
                onClick = onBackClick
            )
        }
        return
    }

    val isMember = group.memberIds.contains(currentUser.id) || group.isUserJoined || group.creatorId == currentUser.id

    if (!group.isOpen && !isMember) {
        // compPage.png / compSent.png state for non-member of closed group
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(VryvPink.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(VryvIcons.Lock, contentDescription = null, tint = VryvPink, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (group.isUserPending) "Заявка отправлена" else "Это закрытая компания",
                color = VryvTextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Доступ к чату предоставляется только после одобрения заявки создателем (${group.creatorName}).",
                color = VryvTextSecondary,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(22.dp))
            VryvButton(
                text = if (group.isUserPending) "Ожидает подтверждения" else "Подать заявку на вступление",
                style = if (group.isUserPending) VryvButtonStyle.SecondaryDark else VryvButtonStyle.PrimaryPink,
                onClick = {
                    if (!group.isUserPending) repository.joinGroup(group.id)
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            VryvButton(
                text = "Назад",
                style = VryvButtonStyle.Ghost,
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
        // Chat Header matching compChat.png
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(VryvBg)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VryvCardElevated)
                    .border(1.dp, VryvLine, CircleShape)
                    .testTag("chat_back_button")
            ) {
                Icon(
                    imageVector = VryvIcons.Back,
                    contentDescription = "Назад",
                    tint = VryvTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (group.isOpen) VryvLime.copy(alpha = 0.16f) else VryvPink.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = VryvIcons.Users,
                    contentDescription = null,
                    tint = if (group.isOpen) VryvLime else VryvPink,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showMembersSheet = true }
            ) {
                Text(
                    text = group.title,
                    color = VryvTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(
                    text = "${group.currentMembersCount} участников · ${group.eventTitle}",
                    color = VryvTextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            VryvIconButton(
                icon = VryvIcons.Users,
                onClick = { showMembersSheet = true }
            )
        }

        // Pinned Meeting Info Banner (compChat.png)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(VryvCard)
                .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = VryvIcons.Pin,
                contentDescription = null,
                tint = VryvLime,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Сбор: ${group.meetingPlace} · в ${group.meetingTime}",
                color = VryvTextPrimary,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
        }

        // Messages list
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(group.chatMessages, key = { it.id }) { msg ->
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
                        if (!isMyMsg) {
                            Text(
                                text = msg.senderName,
                                color = VryvLime,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                            )
                        }
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

        // Quick reply pills
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
                        .clickable { repository.sendGroupMessage(group.id, reply) }
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

        // Input Bar matching compChat.png
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
                placeholder = { Text("Сообщение в компанию...", color = VryvTextMuted, fontSize = 14.sp) },
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
                        repository.sendGroupMessage(group.id, textInput)
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

    // Participants Bottom Sheet matching compMembers.png
    if (showMembersSheet) {
        VryvFloatingBottomSheet(
            onDismiss = { showMembersSheet = false }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Состав компании · ${group.currentMembersCount} из ${group.maxMembers}",
                    color = VryvTextPrimary,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))

                val sampleMembers = listOf(
                    Triple(R.drawable.av2, "Максим Салуков", if (group.creatorName.contains("Максим")) "Создатель компании" else "Участник"),
                    Triple(R.drawable.av1, "Алина", if (group.creatorName.contains("Алина")) "Создатель компании" else "@alina.tech"),
                    Triple(R.drawable.av3, "Артём", if (group.creatorName.contains("Артём")) "Создатель компании" else "@artem.minsk"),
                    Triple(R.drawable.av4, "Дарья", "@daria.v")
                )

                sampleMembers.take(group.currentMembersCount).forEach { (avatar, name, role) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = avatar),
                            contentDescription = name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = name, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = role, color = if (role.contains("Создатель")) VryvLime else VryvTextSecondary, fontSize = 12.sp)
                        }
                        if (role.contains("Создатель")) {
                            VryvStatusPill("Организатор", VryvStatusStyle.Lime)
                        }
                    }
                    HorizontalDivider(color = VryvLine, thickness = 0.5.dp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvDanger.copy(alpha = 0.14f))
                            .border(1.dp, VryvDanger.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                            .clickable {
                                repository.leaveGroup(group.id)
                                showMembersSheet = false
                                onBackClick()
                            }
                            .padding(vertical = 13.dp)
                            .testTag("group_leave_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Выйти из компании", color = VryvDanger, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                            .clickable {
                                showMembersSheet = false
                                onReportClick("Группа", group.title)
                            }
                            .padding(vertical = 13.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Пожаловаться", color = VryvTextSecondary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}
