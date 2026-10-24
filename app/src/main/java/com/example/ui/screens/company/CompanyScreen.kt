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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.CompanyGroup
import com.example.data.UserProfile
import com.example.data.VryvRepository
import com.example.ui.components.VryvAvatarStack
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.components.VryvTag
import com.example.ui.components.VryvTagVariant
import com.example.ui.theme.InterFontFamily
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompanyScreen(
    repository: VryvRepository,
    filterEventId: String?,
    onClearEventFilter: () -> Unit,
    onOpenGroupChat: (String) -> Unit,
    onOpenDirectChat: (String) -> Unit,
    onOpenMessagesList: () -> Unit,
    onCreateCompanyClick: () -> Unit,
    onReportClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val groups by repository.companyGroups.collectAsState()
    val soloUsers by repository.soloUsers.collectAsState()
    val events by repository.events.collectAsState()
    val directThreads by repository.directThreads.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedEventIndex by remember { mutableStateOf(0) }

    // Sub-screens matching s2-mvp.js: member.png, compSent.png, compPage.png, compMember.png, friendsPick.png, invSent.png
    var activeMemberProfile by remember { mutableStateOf<UserProfile?>(null) }
    var showRequestSentForUser by remember { mutableStateOf<UserProfile?>(null) }
    var activeGroupPage by remember { mutableStateOf<CompanyGroup?>(null) }
    var showInviteFriendsForGroup by remember { mutableStateOf(false) }
    var showInviteSentConfirmation by remember { mutableStateOf(false) }

    val unreadCount = directThreads.sumOf { it.unreadCount }

    val contextEvent = remember(filterEventId, events, selectedEventIndex) {
        if (filterEventId != null) {
            events.find { it.id == filterEventId } ?: events.firstOrNull()
        } else {
            events.getOrNull(selectedEventIndex % events.size.coerceAtLeast(1))
        }
    }

    val filteredGroups = remember(groups, searchQuery) {
        groups.filter { g ->
            searchQuery.isBlank() ||
                g.title.contains(searchQuery, ignoreCase = true) ||
                g.eventTitle.contains(searchQuery, ignoreCase = true) ||
                g.tags.any { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    val filteredSolo = remember(soloUsers, searchQuery) {
        soloUsers.filter { u ->
            searchQuery.isBlank() ||
                u.name.contains(searchQuery, ignoreCase = true) ||
                u.username.contains(searchQuery, ignoreCase = true) ||
                u.interests.any { it.contains(searchQuery, ignoreCase = true) }
        }
    }

    // 1. Sub-screen: Invite Sent Confirmation (invSent.png)
    if (showInviteSentConfirmation) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(Color(0x2EC7FF28)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Send,
                        contentDescription = null,
                        tint = VryvLime,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Приглашения отправлены",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Алина, Дима и Артём получат пуш. Как только ответят — появятся в списке участников.",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(18.dp))
                VryvAvatarStack(
                    avatars = listOf(R.drawable.av1, R.drawable.av4, R.drawable.av2),
                    avatarSize = 40.dp
                )
            }
            VryvButton(
                text = "Готово",
                style = VryvButtonStyle.PrimaryPink,
                onClick = {
                    showInviteSentConfirmation = false
                    showInviteFriendsForGroup = false
                }
            )
        }
        return
    }

    // 2. Sub-screen: Friends Picker (friendsPick.png)
    if (showInviteFriendsForGroup) {
        val selectedFriends = remember { mutableStateListOf("u_alina", "u_dima", "u_artem") }
        var friendQuery by remember { mutableStateOf("") }
        val friendsList = listOf(
            Triple("u_alina", "Алина, 24", "@alina.tech · любит Техно") to R.drawable.av1,
            Triple("u_dima", "Дима, 25", "@dima.vinyl · был на 3 событиях с тобой") to R.drawable.av4,
            Triple("u_katya", "Катя, 22", "@katya.ink") to R.drawable.av3,
            Triple("u_artem", "Артём, 30", "@artem_jazz") to R.drawable.av2,
            Triple("u_vika", "Вика, 23", "@vika_minsk") to R.drawable.av5
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
        ) {
            VryvSubScreenHeader(
                title = "Пригласить друзей",
                onBackClick = { showInviteFriendsForGroup = false },
                rightContent = {
                    Text(
                        text = "Выбрано: ${selectedFriends.size}",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
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
                    Icon(VryvIcons.Search, contentDescription = null, tint = VryvTextSecondary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    BasicTextField(
                        value = friendQuery,
                        onValueChange = { friendQuery = it },
                        singleLine = true,
                        textStyle = TextStyle(color = VryvTextPrimary, fontFamily = InterFontFamily, fontSize = 14.sp),
                        cursorBrush = SolidColor(VryvPink),
                        modifier = Modifier.weight(1f),
                        decorationBox = { inner ->
                            if (friendQuery.isEmpty()) {
                                Text("Поиск по друзьям", color = VryvTextSecondary, fontSize = 14.sp)
                            }
                            inner()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(14.dp))
                VryvGroupList {
                    friendsList.forEachIndexed { index, (info, avRes) ->
                        val (fId, fName, fSub) = info
                        val isChecked = selectedFriends.contains(fId)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isChecked) selectedFriends.remove(fId) else selectedFriends.add(fId)
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = avRes),
                                contentDescription = fName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(fName, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                Text(fSub, color = VryvTextSecondary, fontSize = 12.5.sp)
                            }
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .clip(RoundedCornerShape(7.dp))
                                    .background(if (isChecked) VryvPink else VryvCardElevated)
                                    .border(1.5.dp, if (isChecked) VryvPink else Color(0x47FFFFFF), RoundedCornerShape(7.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isChecked) {
                                    Icon(VryvIcons.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                        if (index < friendsList.lastIndex) {
                            HorizontalDivider(color = VryvLine, thickness = 1.dp)
                        }
                    }
                }
            }
            Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                VryvButton(
                    text = "Отправить приглашение · ${selectedFriends.size}",
                    style = VryvButtonStyle.PrimaryPink,
                    onClick = { showInviteSentConfirmation = true }
                )
            }
        }
        return
    }

    // 3. Sub-screen: Request Sent (compSent.png)
    if (showRequestSentForUser != null) {
        val targetUser = showRequestSentForUser!!
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(VryvCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Clock,
                        contentDescription = null,
                        tint = VryvLime,
                        modifier = Modifier.size(34.dp)
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Заявка отправлена",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${targetUser.name} получит уведомление. Когда ответит — откроется общий чат и планы синхронизируются.",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = targetUser.avatarRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${targetUser.name}, ${targetUser.age}",
                            color = VryvTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Заявка · Ждёт ответа",
                            color = VryvTextSecondary,
                            fontSize = 12.5.sp
                        )
                    }
                    VryvTag(text = "Ожидание", variant = VryvTagVariant.NEUTRAL)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                VryvButton(
                    text = "Понятно",
                    style = VryvButtonStyle.PrimaryPink,
                    onClick = {
                        showRequestSentForUser = null
                        activeMemberProfile = null
                    }
                )
                VryvButton(
                    text = "Отменить заявку",
                    style = VryvButtonStyle.Secondary,
                    onClick = {
                        repository.showToast("Заявка отменена")
                        showRequestSentForUser = null
                    }
                )
            }
        }
        return
    }

    // 4. Sub-screen: Solo Member Profile (member.png)
    if (activeMemberProfile != null) {
        val member = activeMemberProfile!!
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
        ) {
            VryvSubScreenHeader(
                title = "",
                onBackClick = { activeMemberProfile = null },
                rightContent = {
                    VryvIconButton(
                        icon = VryvIcons.More,
                        onClick = { onReportClick("user", member.name) }
                    )
                }
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .size(104.dp)
                        .clip(CircleShape)
                        .border(2.5.dp, VryvPink, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = member.avatarRes),
                        contentDescription = member.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${member.name}, ${member.age}",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "@${member.username.removePrefix("@")} · ${member.city}",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = member.bio,
                    color = Color(0xFFD0D0DC),
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Общие интересы",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        member.interests.forEach { tag ->
                            VryvChip(
                                text = tag,
                                selected = true,
                                onClick = {}
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("4", color = VryvTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("События вместе", color = VryvTextSecondary, fontSize = 11.5.sp)
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(16.dp))
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("${member.friendsCount}", color = VryvTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Друзей", color = VryvTextSecondary, fontSize = 11.5.sp)
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VryvButton(
                    text = "Пойти вместе",
                    style = VryvButtonStyle.PrimaryPink,
                    onClick = {
                        repository.inviteSoloUser(member.id)
                        showRequestSentForUser = member
                    }
                )
                VryvButton(
                    text = "Написать",
                    style = VryvButtonStyle.Secondary,
                    onClick = {
                        activeMemberProfile = null
                        onOpenDirectChat(member.id)
                    }
                )
            }
        }
        return
    }

    // 5. Sub-screen: Company Page (compPage.png Creator / compMember.png Member)
    if (activeGroupPage != null) {
        val group = groups.find { it.id == activeGroupPage!!.id } ?: activeGroupPage!!
        var isCreatorView by remember { mutableStateOf(true) }
        var pendingApplicantApproved by remember { mutableStateOf(false) }

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(VryvBg)
        ) {
            VryvSubScreenHeader(
                title = "Компания",
                onBackClick = { activeGroupPage = null },
                rightContent = {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(999.dp))
                                .background(VryvCardElevated)
                                .clickable { isCreatorView = !isCreatorView }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isCreatorView) "Создатель" else "Участник",
                                color = VryvPink,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        VryvIconButton(
                            icon = if (isCreatorView) VryvIcons.Edit else VryvIcons.More,
                            onClick = {
                                if (!isCreatorView) onReportClick("group", group.title)
                                else repository.showToast("Редактирование компании")
                            }
                        )
                    }
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    VryvAvatarStack(
                        avatars = group.memberAvatars,
                        avatarSize = 44.dp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = group.title,
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${if (group.isOpen) "Открытая" else "По заявке"} · ${group.currentMembers} из ${group.maxMembers} мест",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Event card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.techno),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "МЕРОПРИЯТИЕ",
                            color = VryvTextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.6.sp
                        )
                        Text(
                            text = group.eventTitle,
                            color = VryvTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Сегодня · 23:00 · ${group.eventVenue}",
                            color = VryvTextSecondary,
                            fontSize = 12.5.sp
                        )
                    }
                    Icon(VryvIcons.ChevronRight, contentDescription = null, tint = VryvTextSecondary, modifier = Modifier.size(18.dp))
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Meeting place card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(VryvCard)
                        .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(VryvCardElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(VryvIcons.Pin, contentDescription = null, tint = VryvPink, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Встреча в ${group.meetingTime}",
                            color = VryvTextPrimary,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${group.meetingPlace} · не опаздывать",
                            color = VryvTextSecondary,
                            fontSize = 12.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Участники · ${group.currentMembers}",
                    color = VryvTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                VryvGroupList {
                    val members = listOf(
                        Triple("Максим · создатель", if (isCreatorView) "Вы" else "@max.vryv", R.drawable.av2),
                        Triple("Алина, 24", "@alina.tech", R.drawable.av1),
                        Triple("Дима, 25", "@dima.vinyl", R.drawable.av4)
                    )
                    members.forEachIndexed { idx, (mName, mSub, mAv) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = mAv),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(mName, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                Text(mSub, color = VryvTextSecondary, fontSize = 12.5.sp)
                            }
                            if (!isCreatorView && idx > 0) {
                                Icon(VryvIcons.ChevronRight, contentDescription = null, tint = VryvTextSecondary, modifier = Modifier.size(18.dp))
                            }
                        }
                        if (idx < members.lastIndex) {
                            HorizontalDivider(color = VryvLine, thickness = 1.dp)
                        }
                    }
                }

                if (isCreatorView && !pendingApplicantApproved) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Заявки · 1",
                        color = VryvTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    VryvGroupList {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.av3),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Катя, 22", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                                Text("Хочет присоединиться", color = VryvTextSecondary, fontSize = 12.5.sp)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .height(34.dp)
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(VryvCardElevated)
                                        .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                                        .clickable {
                                            pendingApplicantApproved = true
                                            repository.showToast("Заявка отклонена")
                                        }
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Отклонить", color = VryvTextPrimary, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Box(
                                    modifier = Modifier
                                        .height(34.dp)
                                        .clip(RoundedCornerShape(999.dp))
                                        .background(VryvPink)
                                        .clickable {
                                            pendingApplicantApproved = true
                                            repository.showToast("Катя добавлена в компанию")
                                        }
                                        .padding(horizontal = 12.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Принять", color = Color.White, fontSize = 12.5.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isCreatorView) {
                    VryvButton(
                        text = "Чат компании",
                        style = VryvButtonStyle.PrimaryPink,
                        onClick = {
                            val gId = group.id
                            activeGroupPage = null
                            onOpenGroupChat(gId)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    VryvButton(
                        text = "Пригласить друзей",
                        style = VryvButtonStyle.Secondary,
                        onClick = { showInviteFriendsForGroup = true }
                    )
                } else {
                    VryvButton(
                        text = "Открыть чат",
                        style = VryvButtonStyle.PrimaryPink,
                        onClick = {
                            val gId = group.id
                            activeGroupPage = null
                            onOpenGroupChat(gId)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    VryvButton(
                        text = "Выйти из компании",
                        style = VryvButtonStyle.Danger,
                        onClick = {
                            repository.leaveGroup(group.id)
                            activeGroupPage = null
                        }
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
        return
    }

    // Main Company Screen matching compSearch.png 1-to-1
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header: "Компания" + Plus circle + Messages circle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Компания",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.6).sp
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(VryvCardElevated)
                        .clickable { onCreateCompanyClick() }
                        .testTag("create_company_fab"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Plus,
                        contentDescription = "Создать компанию",
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(VryvCardElevated)
                        .clickable { onOpenMessagesList() }
                        .testTag("messages_list_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Message,
                        contentDescription = "Личные сообщения",
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                    if (unreadCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 7.dp, end = 7.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(VryvPink)
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search bar matching compSearch.png
            item {
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
                        modifier = Modifier
                            .weight(1f)
                            .testTag("company_search_input"),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Найти группу или человека",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 14.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = VryvIcons.Close,
                            contentDescription = "Очистить",
                            tint = VryvTextSecondary,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { searchQuery = "" }
                        )
                    }
                }
            }

            // Event context card ("ИЩЕМ КОМПАНИЮ НА") matching compSearch.png
            if (contextEvent != null) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(VryvCard)
                            .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
                            .clickable {
                                if (filterEventId != null) {
                                    onClearEventFilter()
                                } else {
                                    selectedEventIndex += 1
                                }
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = contextEvent.coverRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "ИЩЕМ КОМПАНИЮ НА",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.6.sp
                            )
                            Text(
                                text = contextEvent.title,
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                            Text(
                                text = "${contextEvent.dateDisplay} · ${contextEvent.timeDisplay.substringBefore("–").trim()} · ${contextEvent.venueName}",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 12.5.sp,
                                maxLines = 1
                            )
                        }
                        Icon(
                            imageVector = VryvIcons.Swap,
                            contentDescription = "Сменить мероприятие",
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Section 1: Группы на это мероприятие
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Группы на это мероприятие",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text = "Создать",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onCreateCompanyClick() }
                    )
                }
            }

            items(filteredGroups, key = { it.id }) { group ->
                CompanyGroupCardRef(
                    group = group,
                    onCardClick = { activeGroupPage = group },
                    onJoinClick = { repository.joinGroup(group.id) },
                    onOpenChatClick = { onOpenGroupChat(group.id) }
                )
            }

            // Section 2: Тоже идут одни
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Тоже идут одни",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.3).sp
                    )
                    Text(
                        text = "Все",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { searchQuery = "" }
                    )
                }
            }

            // Grouped list of solo attendees matching compSearch.png
            item {
                VryvGroupList {
                    filteredSolo.forEachIndexed { idx, user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { activeMemberProfile = user }
                                .padding(horizontal = 16.dp, vertical = 13.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = user.avatarRes),
                                contentDescription = user.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${user.name}, ${user.age}",
                                        color = VryvTextPrimary,
                                        fontFamily = InterFontFamily,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "@${user.username.removePrefix("@")}",
                                        color = VryvTextSecondary,
                                        fontFamily = InterFontFamily,
                                        fontSize = 12.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = user.interests.joinToString(" · "),
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp,
                                    maxLines = 1
                                )
                            }
                            Icon(
                                imageVector = VryvIcons.ChevronRight,
                                contentDescription = null,
                                tint = VryvTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        if (idx < filteredSolo.lastIndex) {
                            HorizontalDivider(color = VryvLine, thickness = 1.dp)
                        }
                    }
                }
            }

            // Bottom privacy caption matching compSearch.png
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 100.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = VryvIcons.Lock,
                        contentDescription = null,
                        tint = VryvTextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Показываем только тех, кто открыл планы и разрешил контакт",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun CompanyGroupCardRef(
    group: CompanyGroup,
    onCardClick: () -> Unit,
    onJoinClick: () -> Unit,
    onOpenChatClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(22.dp))
            .clickable { onCardClick() }
            .padding(14.dp)
            .testTag("company_group_card_${group.id}")
    ) {
        // Top row: Avatar stack + Title/Subtitle + Lock icon if closed
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VryvAvatarStack(
                avatars = group.memberAvatars,
                avatarSize = 26.dp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.title,
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Сбор ${group.meetingTime} · ${group.meetingPlace} · ${group.currentMembers} из ${group.maxMembers} мест",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    maxLines = 1
                )
            }
            if (!group.isOpen) {
                Icon(
                    imageVector = VryvIcons.Lock,
                    contentDescription = "Закрытая группа",
                    tint = VryvTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Tags row
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            group.tags.forEach { tag ->
                VryvTag(
                    text = tag,
                    variant = VryvTagVariant.NEUTRAL
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action buttons row matching compSearch.png
        if (group.isJoined) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(VryvLime)
                        .clickable { onOpenChatClick() }
                        .testTag("open_chat_btn_${group.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Открыть чат",
                        color = Color(0xFF0B0B0F),
                        fontFamily = InterFontFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(VryvCardElevated)
                        .border(1.dp, VryvLine, CircleShape)
                        .clickable { onCardClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Users,
                        contentDescription = "О компании",
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        } else if (group.isRequested) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(VryvCardElevated)
                    .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                    .clickable { onCardClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Заявка отправлена",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else if (group.isOpen) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(999.dp))
                        .background(VryvPink)
                        .clickable { onJoinClick() }
                        .testTag("join_group_btn_${group.id}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Присоединиться",
                        color = Color.White,
                        fontFamily = InterFontFamily,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(VryvCardElevated)
                        .border(1.dp, VryvLine, CircleShape)
                        .clickable { onOpenChatClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Message,
                        contentDescription = "Чат",
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(VryvCardElevated)
                    .border(1.dp, VryvLine, RoundedCornerShape(999.dp))
                    .clickable { onJoinClick() }
                    .testTag("join_group_btn_${group.id}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Запросить вход",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
