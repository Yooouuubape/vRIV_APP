package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendsSheet(
    eventId: String,
    repository: VryvRepository,
    onDismiss: () -> Unit
) {
    val companions by repository.companions.collectAsState()
    val events by repository.events.collectAsState()
    val event = events.find { it.id == eventId }
    val friends = companions.filter { it.isFriend }
    val selectedFriendIds = remember {
        mutableStateListOf<String>().apply {
            friends.take(3).forEach { add(it.id) }
        }
    }
    var searchQuery by remember { mutableStateOf("") }
    var customMessage by remember { mutableStateOf("Ребята, пойдём в пятницу? Возьму столик") }
    var isSent by remember { mutableStateOf(false) }

    val filteredFriends = friends.filter {
        searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) || it.username.contains(searchQuery, ignoreCase = true)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color(0xFF16161C),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            // Handle
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0x38FFFFFF))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (!isSent) {
                Text(
                    text = "Позвать друзей",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (event != null) "${event.title} · ${event.dateDisplay} · ${event.timeDisplay}" else "Ночь синтвейва · Пт, 24 окт · 23:00",
                    color = VryvTextMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                VryvInputField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = "Поиск по друзьям",
                    leadingIcon = VryvIcons.Search
                )

                Spacer(modifier = Modifier.height(12.dp))

                VryvGroupList {
                    filteredFriends.forEachIndexed { index, friend ->
                        val isChecked = selectedFriendIds.contains(friend.id)
                        val statusText = when (index % 4) {
                            0 -> "В онлайне · любит синтвейв"
                            1 -> "Был 15 мин назад"
                            2 -> "Собирался на прошлой неделе"
                            else -> "Не в сети"
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isChecked) selectedFriendIds.remove(friend.id) else selectedFriendIds.add(friend.id)
                                }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = friend.avatarRes),
                                contentDescription = friend.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = friend.name,
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = statusText,
                                    color = VryvTextMuted,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(if (isChecked) VryvPink else Color.Transparent)
                                    .then(
                                        if (!isChecked) Modifier.border(1.5.dp, Color(0x38FFFFFF), CircleShape)
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChecked) {
                                    Icon(
                                        imageVector = VryvIcons.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                        if (index < filteredFriends.lastIndex) {
                            HorizontalDivider(color = VryvLine, thickness = 1.dp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Необязательное сообщение",
                    color = Color(0xFFB8B8C6),
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                VryvInputField(
                    value = customMessage,
                    onValueChange = { customMessage = it },
                    placeholder = "Напиши сообщение..."
                )

                Spacer(modifier = Modifier.height(14.dp))

                VryvButton(
                    text = "Отправить приглашение (${selectedFriendIds.size})",
                    onClick = {
                        if (selectedFriendIds.isNotEmpty()) {
                            repository.inviteFriendsToEvent(eventId, selectedFriendIds.toList())
                            isSent = true
                        }
                    },
                    style = VryvButtonStyle.PRIMARY,
                    enabled = selectedFriendIds.isNotEmpty()
                )
                Spacer(modifier = Modifier.height(10.dp))
            } else {
                // invSent state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0x1EC7FF28))
                            .border(1.dp, Color(0x59C7FF28), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Check,
                            contentDescription = null,
                            tint = VryvLime,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Приглашения отправлены",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Аня, Влад и Даша получат уведомление и смогут ответить в один тап",
                        color = VryvTextMuted,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    VryvButton(
                        text = "Готово",
                        onClick = onDismiss,
                        style = VryvButtonStyle.PRIMARY
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    targetType: String,
    targetTitle: String,
    repository: VryvRepository,
    onDismiss: () -> Unit
) {
    val reasons = listOf(
        "Неверная информация о событии" to "Дата, адрес или цена не совпадают",
        "Спам или реклама" to "Посторонние ссылки, навязчивые продажи",
        "Неприемлемый контент" to "Оскорбления, запрещённые материалы",
        "Мошенничество" to "Поддельное место или сбор денег"
    )
    var selectedReason by remember { mutableStateOf(reasons[0].first) }
    var comment by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color(0xFF16161C),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0x38FFFFFF))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (!isSubmitted) {
                Text(
                    text = "Пожаловаться",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Мы проверим обращение в течение 24 часов и не расскажем автору, кто отправил жалобу",
                    color = VryvTextMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    reasons.forEach { (title, sub) ->
                        VryvOptionCard(
                            title = title,
                            subtitle = sub,
                            selected = selectedReason == title,
                            onClick = { selectedReason = title }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Комментарий (необязательно)",
                    color = Color(0xFFB8B8C6),
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                VryvInputField(
                    value = comment,
                    onValueChange = { comment = it },
                    placeholder = "Например: в кассе клуба сказали, что концерт отменён"
                )

                Spacer(modifier = Modifier.height(14.dp))

                VryvButton(
                    text = "Отправить жалобу",
                    onClick = {
                        repository.submitReport(
                            targetType,
                            "id_${System.currentTimeMillis()}",
                            targetTitle,
                            selectedReason,
                            comment
                        )
                        isSubmitted = true
                    },
                    style = VryvButtonStyle.PRIMARY
                )
                Spacer(modifier = Modifier.height(10.dp))
            } else {
                // reportOk.png
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0x1EC7FF28))
                            .border(1.dp, Color(0x59C7FF28), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = VryvIcons.Check,
                            contentDescription = null,
                            tint = VryvLime,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Спасибо, мы проверим",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Обращение #VR-4829 принято. Если нарушение подтвердится, мы скроем публикацию",
                        color = VryvTextMuted,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    VryvButton(
                        text = "Понятно",
                        onClick = onDismiss,
                        style = VryvButtonStyle.PRIMARY
                    )
                }
            }
        }
    }
}
