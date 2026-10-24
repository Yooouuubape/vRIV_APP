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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvInputBg
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
    val dialogs by repository.directDialogs.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredDialogs = remember(dialogs, searchQuery) {
        if (searchQuery.isBlank()) dialogs
        else dialogs.filter {
            it.otherUser.name.contains(searchQuery, ignoreCase = true) ||
            it.lastMessage.contains(searchQuery, ignoreCase = true)
        }
    }

    val unreadDialogs = filteredDialogs.filter { it.unreadCount > 0 }
    val earlierDialogs = filteredDialogs.filter { it.unreadCount == 0 }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = VryvTextPrimary
                )
            }
            Text(
                text = "Сообщения",
                color = VryvTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Search Bar
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Поиск по диалогам...", color = VryvTextMuted, fontSize = 14.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = VryvTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                },
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = VryvCard,
                    unfocusedContainerColor = VryvCard,
                    focusedBorderColor = VryvPink,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = VryvTextPrimary,
                    unfocusedTextColor = VryvTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            if (unreadDialogs.isNotEmpty()) {
                item {
                    Text(
                        text = "Непрочитанные · ${unreadDialogs.size}",
                        color = VryvTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(unreadDialogs) { dialog ->
                    DialogRowItem(
                        dialog = dialog,
                        onClick = { onOpenDirectChat(dialog.otherUser.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            if (earlierDialogs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Ранее",
                        color = VryvTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(earlierDialogs) { dialog ->
                    DialogRowItem(
                        dialog = dialog,
                        onClick = { onOpenDirectChat(dialog.otherUser.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun DialogRowItem(
    dialog: com.example.data.DirectDialog,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VryvCard)
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = dialog.otherUser.avatarRes),
            contentDescription = dialog.otherUser.name,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dialog.otherUser.name,
                    color = VryvTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = dialog.time,
                    color = VryvTextMuted,
                    fontSize = 11.sp
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = dialog.lastMessage,
                color = if (dialog.unreadCount > 0) VryvTextPrimary else VryvTextSecondary,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (dialog.unreadCount > 0) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(VryvPink)
            )
        }
    }
}
