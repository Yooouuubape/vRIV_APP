package com.example.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonStyle
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvSegmentedControl
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun EditProfileScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user by repository.currentUser.collectAsState()
    var name by remember { mutableStateOf(user.name) }
    var username by remember { mutableStateOf(user.username.removePrefix("@")) }
    var city by remember { mutableStateOf(user.city) }
    var bio by remember { mutableStateOf(user.bio) }
    var privacy by remember { mutableStateOf(user.privacyPlansVisibility) }

    val privacyOptions = listOf("Все", "Друзья", "Только я")
    val selectedPrivacyIndex = privacyOptions.indexOf(privacy).coerceAtLeast(0)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Header matching edit.png
        VryvSubScreenHeader(
            title = "Редактировать профиль",
            onBackClick = onBackClick,
            rightContent = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            repository.updateUserProfile(name, bio, privacy)
                            onBackClick()
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("save_profile_button")
                ) {
                    Text(
                        text = "Готово",
                        color = VryvPink,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 96.dp Avatar with pink camera badge (edit.png)
            Box(
                modifier = Modifier.size(96.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = painterResource(id = user.avatarRes),
                    contentDescription = user.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(VryvPink)
                        .border(3.dp, VryvBg, CircleShape)
                        .clickable { repository.showToast("Выбор фото профиля") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Camera,
                        contentDescription = "Сменить фото",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            VryvInputField(
                value = name,
                onValueChange = { name = it },
                label = "Имя",
                placeholder = "Ваше имя"
            )

            Spacer(modifier = Modifier.height(14.dp))

            VryvInputField(
                value = "@$username",
                onValueChange = { username = it.removePrefix("@") },
                label = "Никнейм",
                placeholder = "@username"
            )

            Spacer(modifier = Modifier.height(14.dp))

            VryvInputField(
                value = city,
                onValueChange = { city = it },
                label = "Город",
                placeholder = "Минск",
                trailingIcon = VryvIcons.ChevronDown
            )

            Spacer(modifier = Modifier.height(14.dp))

            VryvInputField(
                value = bio,
                onValueChange = { bio = it },
                label = "О себе",
                placeholder = "Расскажите о музыкальных вкусах и любимых местах...",
                singleLine = false,
                minLines = 3
            )

            Spacer(modifier = Modifier.height(18.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "КТО ВИДИТ МОИ ПЛАНЫ",
                    color = VryvTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                VryvSegmentedControl(
                    options = privacyOptions,
                    selectedIndex = selectedPrivacyIndex,
                    onSelect = { idx -> privacy = privacyOptions[idx] }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            VryvButton(
                text = "Сохранить",
                style = VryvButtonStyle.PrimaryPink,
                onClick = {
                    repository.updateUserProfile(name, bio, privacy)
                    onBackClick()
                }
            )

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}
