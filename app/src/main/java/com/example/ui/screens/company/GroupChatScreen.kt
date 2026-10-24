package com.example.ui.screens.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ChatMessage
import com.example.data.VryvRepository
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvInputBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
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
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(VryvPink)
                    .clickable { onBackClick() }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(text = "Назад к компаниям", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        return
    }

    val isMember = group.memberIds.contains(currentUser.id) || group.isUserJoined || group.creatorId == currentUser.id

    if (!group.isOpen && !isMember) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = VryvPink, modifier = Modifier.size(54.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Это закрытая компания", color = VryvTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Доступ к чату предоставляется только после одобрения заявки создателем (${group.creatorName}).",
                color = VryvTextSecondary,
                fontSize = 13.5.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (group.isUserPending) VryvCardElevated else VryvPink)
                    .clickable {
                        if (!group.isUserPending) repository.joinGroup(group.id)
                    }
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (group.isUserPending) "Заявка отправлена" else "Подать заявку на вступление",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Назад",
                color = VryvTextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.clickable { onBackClick() }.padding(8.dp)
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Chat Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(VryvCard)
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("chat_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = VryvTextPrimary
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (group.isOpen) Color(0x33C7FF28) else Color(0x33FF1D8E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = if (group.isOpen) VryvLime else VryvPink,
                    modifier = Modifier.size(22.dp)
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

            IconButton(onClick = { showMembersSheet = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Участники",
                    tint = VryvTextSecondary
                )
            }
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
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isMyMsg) 16.dp else 4.dp,
                                        bottomEnd = if (isMyMsg) 4.dp else 16.dp
                                    )
                                )
                                .background(if (isMyMsg) VryvPink else VryvCard)
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
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickReplies) { reply ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(VryvCardElevated)
                        .clickable { repository.sendGroupMessage(group.id, reply) }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = reply,
                        color = VryvTextPrimary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Input Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(VryvCard)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Сообщение в компанию...", color = VryvTextMuted, fontSize = 14.sp) },
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = VryvInputBg,
                    unfocusedContainerColor = VryvInputBg,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
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
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(VryvPink)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Отправить",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }

    // Participants Bottom Sheet
    if (showMembersSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMembersSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VryvCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Состав компании · ${group.currentMembersCount} из ${group.maxMembers}",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
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
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = name, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = role, color = if (role.contains("Создатель")) VryvLime else VryvTextSecondary, fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22FF1D8E))
                            .clickable {
                                repository.leaveGroup(group.id)
                                showMembersSheet = false
                                onBackClick()
                            }
                            .padding(vertical = 12.dp)
                            .testTag("group_leave_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Выйти из компании", color = VryvPink, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(VryvCardElevated)
                            .clickable {
                                showMembersSheet = false
                                onReportClick("Группа", group.title)
                            }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Пожаловаться", color = VryvTextSecondary, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
