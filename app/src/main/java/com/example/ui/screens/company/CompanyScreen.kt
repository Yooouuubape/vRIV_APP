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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import com.example.data.VryvRepository
import com.example.ui.components.VryvAvatarStack
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvStatusPill
import com.example.ui.components.VryvStatusStyle
import com.example.ui.components.VryvTag
import com.example.ui.components.VryvTagStyle
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalLayoutApi::class)
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

    var selectedEventFilterChip by remember(filterEventId) {
        mutableStateOf(targetEvent?.title ?: "Все события")
    }

    val eventFilterChips = listOf("Все события", "Techno Room", "Indie Jam", "Стендап")

    val visibleGroups = remember(allGroups, filterEventId, selectedEventFilterChip) {
        when {
            filterEventId != null -> allGroups.filter { it.eventId == filterEventId }
            selectedEventFilterChip == "Все события" -> allGroups
            else -> allGroups.filter {
                it.eventTitle.contains(selectedEventFilterChip, ignoreCase = true) ||
                it.title.contains(selectedEventFilterChip, ignoreCase = true)
            }.ifEmpty { allGroups }
        }
    }

    // Modal state for group applications management / group details (compPage / compMember)
    var managingApplicationsGroup by remember { mutableStateOf<CompanyGroup?>(null) }
    // Modal state for companion profile sheet (member.png)
    var selectedCompanionProfile by remember { mutableStateOf<CompanionUser?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header matching screen_05.png
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    if (filterEventId != null) {
                        VryvIconButton(
                            icon = VryvIcons.Back,
                            onClick = onClearEventFilter
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Column {
                        Text(
                            text = if (targetEvent != null) "Компании на событие" else "Компания",
                            color = VryvTextPrimary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.6).sp
                        )
                        Text(
                            text = if (targetEvent != null) targetEvent.title else "Люди и группы на события Минска",
                            color = VryvTextSecondary,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Messages icon button with unread badge
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(VryvCardElevated)
                            .border(1.dp, VryvLine, CircleShape)
                            .clickable { onOpenMessagesList() }
                            .testTag("messages_list_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        BadgedBox(
                            badge = {
                                if (totalUnread > 0) {
                                    Badge(
                                        containerColor = VryvPink,
                                        contentColor = Color.White
                                    ) {
                                        Text("$totalUnread", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = VryvIcons.Msg,
                                contentDescription = "Сообщения",
                                tint = VryvTextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // "+ Создать" pink pill button matching screen_05.png
                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(VryvPink)
                            .clickable { onCreateCompanyClick() }
                            .padding(horizontal = 14.dp)
                            .testTag("create_company_fab"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = VryvIcons.Plus,
                                contentDescription = "Создать компанию",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Создать",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Event context banner or filter chips row (screen_05.png)
            if (targetEvent != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvPink.copy(alpha = 0.14f))
                        .border(1.dp, VryvPink.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
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
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .clickable { onClearEventFilter() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Close,
                                contentDescription = "Сбросить фильтр",
                                tint = VryvTextSecondary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            } else {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(eventFilterChips) { chip ->
                        VryvChip(
                            text = chip,
                            selected = selectedEventFilterChip == chip,
                            onClick = { selectedEventFilterChip = chip }
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                // Section 1: Хотят пойти вместе (screen_05.png & compSearch.png)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Хотят пойти вместе",
                            color = VryvTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${activeCompanions.size} чел.",
                            color = VryvTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                items(activeCompanions) { companion ->
                    CompanionCardItem(
                        companion = companion,
                        onCardClick = { selectedCompanionProfile = companion },
                        onRequestToggle = { repository.toggleCompanionRequest(companion.id) },
                        onMessageClick = { onOpenDirectChat(companion.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Section 2: Группы мероприятия (screen_05.png)
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (targetEvent != null) "Группы на «${targetEvent.title}» (${visibleGroups.size})" else "Все компании (${visibleGroups.size})",
                            color = VryvTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (visibleGroups.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(VryvCard)
                                .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
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
                                VryvButton(
                                    text = "Создать первую компанию",
                                    style = VryvButtonStyle.PrimaryPink,
                                    onClick = onCreateCompanyClick
                                )
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
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

    // Companion Profile Modal Bottom Sheet (member.png)
    if (selectedCompanionProfile != null) {
        val comp = selectedCompanionProfile!!
        VryvFloatingBottomSheet(
            onDismiss = { selectedCompanionProfile = null }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    Image(
                        painter = painterResource(id = comp.avatarRes),
                        contentDescription = comp.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                    if (comp.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .background(VryvLime)
                                .border(2.5.dp, VryvCard, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = comp.name,
                    color = VryvTextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "@${comp.username} · Минск",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = comp.bio,
                    color = VryvTextPrimary,
                    fontSize = 13.5.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    comp.sharedTags.forEach { tag ->
                        VryvTag(text = tag, style = VryvTagStyle.Lime)
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    VryvButton(
                        text = "Пожаловаться",
                        style = VryvButtonStyle.SecondaryDark,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedCompanionProfile = null
                            onReportClick("Пользователь", comp.name)
                        }
                    )
                    VryvButton(
                        text = "Написать",
                        style = VryvButtonStyle.PrimaryPink,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            selectedCompanionProfile = null
                            onOpenDirectChat(comp.id)
                        }
                    )
                }
            }
        }
    }

    // Applications Management Modal Bottom Sheet (compMember.png / compPage.png)
    if (managingApplicationsGroup != null) {
        val group = managingApplicationsGroup!!
        val pendingApps = group.pendingApplications.filter { it.status == "PENDING" }

        VryvFloatingBottomSheet(
            onDismiss = { managingApplicationsGroup = null }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Заявки в компанию «${group.title}»",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
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
                                .background(VryvCardElevated)
                                .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = app.userAvatarRes),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = app.userName, color = VryvTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    Text(text = app.timestamp, color = VryvTextSecondary, fontSize = 11.5.sp)
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                    Text(text = "Принять", color = Color(0xFF0B0B0F), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(VryvBg)
                                        .clickable {
                                            repository.rejectGroupApplication(group.id, app.userId)
                                            managingApplicationsGroup = repository.groups.value.find { it.id == group.id }
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(text = "Отклонить", color = VryvPink, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
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
            .border(
                width = 1.dp,
                color = if (isMember) VryvLime.copy(alpha = 0.4f) else VryvLine,
                shape = RoundedCornerShape(20.dp)
            )
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
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "${group.meetingPlace} · в ${group.meetingTime}",
                        color = VryvTextSecondary,
                        fontSize = 12.5.sp
                    )
                }

                VryvStatusPill(
                    text = if (group.isOpen) "Открытая" else "По заявке",
                    style = if (group.isOpen) VryvStatusStyle.Lime else VryvStatusStyle.Pink
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    VryvAvatarStack(
                        avatarResIds = group.memberAvatars.take(4),
                        avatarSize = 28.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${group.currentMembersCount} из ${group.maxMembers} мест",
                        color = if (isFull) VryvPink else VryvLime,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

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
                                color = Color(0xFF0B0B0F),
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    isMember -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(VryvLime.copy(alpha = 0.14f))
                                .border(1.dp, VryvLime, RoundedCornerShape(12.dp))
                                .clickable { onChatClick() }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("group_chat_btn_${group.id}")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = VryvIcons.Msg,
                                    contentDescription = null,
                                    tint = VryvLime,
                                    modifier = Modifier.size(14.dp)
                                )
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
                                .border(1.dp, VryvLine, RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = VryvIcons.Clock,
                                    contentDescription = null,
                                    tint = VryvTextMuted,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Ожидает решения",
                                    color = VryvTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
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
                                .border(
                                    width = 1.dp,
                                    color = if (group.isOpen) VryvPink else VryvLine,
                                    shape = RoundedCornerShape(12.dp)
                                )
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
    onCardClick: () -> Unit,
    onRequestToggle: () -> Unit,
    onMessageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
            .clickable { onCardClick() }
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
                Spacer(modifier = Modifier.height(5.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    companion.sharedTags.take(3).forEach { tag ->
                        VryvTag(text = tag, style = VryvTagStyle.Lime)
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            VryvIconButton(
                icon = VryvIcons.Msg,
                onClick = onMessageClick
            )
        }
    }
}
