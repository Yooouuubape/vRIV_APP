package com.example.ui.screens.company

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.CompanyGroup
import com.example.data.CompanionUser
import com.example.data.GroupApplication
import com.example.data.VryvRepository
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CompanyScreen(
    repository: VryvRepository,
    filterEventId: String? = null,
    onClearEventFilter: () -> Unit = {},
    onOpenGroupChat: (String) -> Unit,
    onOpenDirectChat: (String) -> Unit,
    onOpenMessagesList: () -> Unit,
    onCreateCompanyClick: () -> Unit,
    onReportClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentUser by repository.currentUser.collectAsState()
    val allGroups by repository.groups.collectAsState()
    val companions by repository.companions.collectAsState()
    val events by repository.events.collectAsState()
    val dialogs by repository.directDialogs.collectAsState()
    val totalUnread = dialogs.sumOf { it.unreadCount }

    val activeCompanions = companions.filter { !it.isBlocked && !currentUser.blockedUserIds.contains(it.id) }

    val targetEvent = remember(events, filterEventId) {
        if (filterEventId != null) events.find { it.id == filterEventId } else null
    }

    // Filter groups by specific event if navigated from event details
    val visibleGroups = remember(allGroups, filterEventId) {
        if (filterEventId != null) {
            allGroups.filter { it.eventId == filterEventId }
        } else {
            allGroups
        }
    }

    // Modal state for group applications management
    var managingApplicationsGroup by remember { mutableStateOf<CompanyGroup?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (filterEventId != null) {
                        IconButton(
                            onClick = onClearEventFilter,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад к событию", tint = VryvTextPrimary)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Column {
                        Text(
                            text = if (targetEvent != null) "Компании на событие" else "Компания",
                            color = VryvTextPrimary,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (targetEvent != null) targetEvent.title else "Ищи друзей и группы на события Минска",
                            color = VryvTextSecondary,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onOpenMessagesList,
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(VryvCard)
                        .testTag("messages_list_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (totalUnread > 0) {
                                Badge(
                                    containerColor = VryvPink,
                                    contentColor = Color.White
                                ) {
                                    Text("$totalUnread", fontSize = 10.sp)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "Сообщения",
                            tint = VryvTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Event context banner
            if (targetEvent != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0x33FF1D8E))
                        .border(1.dp, VryvPink.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(VryvLime)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = targetEvent.title,
                                color = VryvTextPrimary,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(
                            onClick = onClearEventFilter,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Сбросить фильтр",
                                tint = VryvTextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Section 1: Люди ищут компанию
                item {
                    Text(
                        text = "Хотят пойти вместе",
                        color = VryvTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                items(activeCompanions) { companion ->
                    CompanionCardItem(
                        companion = companion,
                        onRequestToggle = { repository.toggleCompanionRequest(companion.id) },
                        onMessageClick = { onOpenDirectChat(companion.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Section 2: Группы мероприятия
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (targetEvent != null) "Группы на «${targetEvent.title}» (${visibleGroups.size})" else "Все компании (${visibleGroups.size})",
                        color = VryvTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                if (visibleGroups.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(VryvCard)
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "На это мероприятие пока нет созданных групп",
                                    color = VryvTextSecondary,
                                    fontSize = 13.5.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(VryvPink)
                                        .clickable { onCreateCompanyClick() }
                                        .padding(horizontal = 16.dp, vertical = 10.dp)
                                ) {
                                    Text(text = "Создать первую компанию", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    items(visibleGroups, key = { it.id }) { group ->
                        val isUserCreator = group.creatorId == currentUser.id || group.isUserCreator
                        val isMember = group.memberIds.contains(currentUser.id) || group.isUserJoined
                        val isFull = group.currentMembersCount >= group.maxMembers
                        val pendingCount = group.pendingApplications.count { it.status == "PENDING" }

                        GroupCardItem(
                            group = group,
                            isUserCreator = isUserCreator,
                            isMember = isMember,
                            isFull = isFull,
                            pendingApplicationsCount = pendingCount,
                            onJoinClick = { repository.joinGroup(group.id) },
                            onChatClick = { onOpenGroupChat(group.id) },
                            onManageApplicationsClick = { managingApplicationsGroup = group }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(90.dp))
                }
            }
        }

        // Floating Action Button to create company
        FloatingActionButton(
            onClick = onCreateCompanyClick,
            containerColor = VryvPink,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 80.dp)
                .testTag("create_company_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать компанию")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Создать",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Applications Management Modal Bottom Sheet (for group creator)
    if (managingApplicationsGroup != null) {
        val group = managingApplicationsGroup!!
        val pendingApps = group.pendingApplications.filter { it.status == "PENDING" }

        ModalBottomSheet(
            onDismissRequest = { managingApplicationsGroup = null },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VryvCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Заявки в компанию «${group.title}»",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Свободно мест: ${group.maxMembers - group.currentMembersCount} из ${group.maxMembers}",
                    color = VryvLime,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (pendingApps.isEmpty()) {
                    Text(
                        text = "Новых заявок пока нет",
                        color = VryvTextSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    pendingApps.forEach { app ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(VryvBg)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = app.userAvatarRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp).clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = app.userName, color = VryvTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(text = app.timestamp, color = VryvTextSecondary, fontSize = 11.5.sp)
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                // Accept Button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(VryvLime)
                                        .clickable {
                                            repository.acceptGroupApplication(group.id, app.userId)
                                            managingApplicationsGroup = repository.groups.value.find { it.id == group.id }
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(text = "Принять", color = Color.Black, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                // Reject Button
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(VryvCardElevated)
                                        .clickable {
                                            repository.rejectGroupApplication(group.id, app.userId)
                                            managingApplicationsGroup = repository.groups.value.find { it.id == group.id }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = "Отклонить", color = VryvPink, fontSize = 12.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun GroupCardItem(
    group: CompanyGroup,
    isUserCreator: Boolean,
    isMember: Boolean,
    isFull: Boolean,
    pendingApplicationsCount: Int,
    onJoinClick: () -> Unit,
    onChatClick: () -> Unit,
    onManageApplicationsClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VryvCard)
            .border(1.dp, if (isMember) VryvLime.copy(alpha = 0.4f) else VryvLine, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.title,
                        color = VryvTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${group.meetingPlace} · в ${group.meetingTime}",
                        color = VryvTextSecondary,
                        fontSize = 12.5.sp
                    )
                }

                // Type Badge (Open / Closed)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (group.isOpen) Color(0x33C7FF28) else Color(0x33FF1D8E))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (group.isOpen) Icons.Default.Public else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (group.isOpen) VryvLime else VryvPink,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (group.isOpen) "Открытая" else "По заявке",
                            color = if (group.isOpen) VryvLime else VryvPink,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Member Avatars & Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Row(modifier = Modifier.padding(end = 8.dp)) {
                        group.memberAvatars.take(4).forEachIndexed { index, avatarRes ->
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .border(1.5.dp, VryvCard, CircleShape)
                            ) {
                                Image(
                                    painter = painterResource(id = avatarRes),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    Text(
                        text = "${group.currentMembersCount} из ${group.maxMembers} мест",
                        color = if (isFull) VryvPink else VryvLime,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Action Button based on membership & role
                when {
                    isUserCreator && pendingApplicationsCount > 0 -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvLime)
                                .clickable { onManageApplicationsClick() }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Заявки ($pendingApplicationsCount)",
                                color = Color.Black,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    isMember -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0x22C7FF28))
                                .border(1.dp, VryvLime, RoundedCornerShape(12.dp))
                                .clickable { onChatClick() }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("group_chat_btn_${group.id}")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Chat, contentDescription = null, tint = VryvLime, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "В чат",
                                    color = VryvLime,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    group.isUserPending -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvCardElevated)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.HourglassTop, contentDescription = null, tint = VryvTextMuted, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Ожидает решения",
                                    color = VryvTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    isFull -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvCardElevated)
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Мест нет",
                                color = VryvTextMuted,
                                fontSize = 12.sp
                            )
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (group.isOpen) VryvPink else VryvCardElevated)
                                .clickable { onJoinClick() }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("group_join_btn_${group.id}")
                        ) {
                            Text(
                                text = if (group.isOpen) "Вступить" else "Подать заявку",
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CompanionCardItem(
    companion: CompanionUser,
    onRequestToggle: () -> Unit,
    onMessageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(VryvCard)
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Image(
                    painter = painterResource(id = companion.avatarRes),
                    contentDescription = companion.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                )
                if (companion.isOnline) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(VryvLime)
                            .border(2.dp, VryvCard, CircleShape)
                            .align(Alignment.BottomEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = companion.name,
                    color = VryvTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = companion.bio,
                    color = VryvTextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    companion.sharedTags.take(3).forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(VryvCardElevated)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(text = tag, color = VryvLime, fontSize = 10.5.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = onMessageClick,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(VryvCardElevated)
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = "Написать",
                    tint = VryvTextPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
