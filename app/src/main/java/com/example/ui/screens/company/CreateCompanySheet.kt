package com.example.ui.screens.company

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvFloatingBottomSheet
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvToggle
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@Composable
fun CreateCompanySheet(
    repository: VryvRepository,
    onDismiss: () -> Unit,
    onCompanyCreated: (String) -> Unit
) {
    var title by remember { mutableStateOf("Идём на Techno Room") }
    var meetingTime by remember { mutableStateOf("Сегодня · 22:30") }
    var meetingPlace by remember { mutableStateOf("У входа Re:Public") }
    var maxMembers by remember { mutableStateOf("4") }
    var isOpen by remember { mutableStateOf(true) }
    var showInSearch by remember { mutableStateOf(true) }

    VryvFloatingBottomSheet(
        onDismiss = onDismiss
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Новая компания",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Title Field (preserving testTag("input_company_title"))
            Text(
                text = "Название",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = VryvCard,
                    unfocusedContainerColor = VryvCard,
                    focusedBorderColor = VryvPink,
                    unfocusedBorderColor = VryvLine,
                    focusedTextColor = VryvTextPrimary,
                    unfocusedTextColor = VryvTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_company_title")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Event card matching compNew.png
            Text(
                text = "Мероприятие",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(VryvCard)
                    .border(1.dp, VryvLine, RoundedCornerShape(18.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.techno),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Techno Room: Ночь 3",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Сегодня · 23:00 · Re:Public",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 12.5.sp
                    )
                }
                Icon(
                    imageVector = VryvIcons.ChevronRight,
                    contentDescription = null,
                    tint = VryvTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            VryvInputField(
                value = meetingTime,
                onValueChange = { meetingTime = it },
                label = "Время встречи",
                trailingIcon = VryvIcons.Clock
            )

            Spacer(modifier = Modifier.height(12.dp))

            VryvInputField(
                value = meetingPlace,
                onValueChange = { meetingPlace = it },
                label = "Место сбора",
                trailingIcon = VryvIcons.Pin
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Member count chips (2, 4, 6, 8) matching compNew.png
            Text(
                text = "Количество участников",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("2", "4", "6", "8").forEach { count ->
                    VryvChip(
                        text = count,
                        selected = maxMembers == count,
                        isBig = true,
                        onClick = { maxMembers = count }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grouped switches matching compNew.png
            VryvGroupList {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isOpen = !isOpen }
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Открытая компания",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Любой может вступить без заявки",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp
                        )
                    }
                    VryvToggle(checked = isOpen, onCheckedChange = { isOpen = it })
                }
                HorizontalDivider(color = VryvLine, thickness = 1.dp)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showInSearch = !showInSearch }
                        .padding(horizontal = 16.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Показывать в поиске",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Видна другим на странице события",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp
                        )
                    }
                    VryvToggle(checked = showInSearch, onCheckedChange = { showInSearch = it })
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Ты станешь создателем и сможешь приглашать друзей, менять время и место сбора.",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Submit Button (preserving testTag("submit_create_company"))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(VryvPink)
                    .clickable {
                        val limit = maxMembers.toIntOrNull() ?: 4
                        val newGroup = repository.createGroup(
                            title = title.ifBlank { "Идём на Techno Room" },
                            eventId = "e_techno",
                            meetingTime = meetingTime.removePrefix("Сегодня · ").trim(),
                            meetingPlace = meetingPlace,
                            maxMembers = limit,
                            isOpen = isOpen
                        )
                        onCompanyCreated(newGroup.id)
                    }
                    .testTag("submit_create_company"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Создать компанию",
                    color = Color.White,
                    fontFamily = InterFontFamily,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
