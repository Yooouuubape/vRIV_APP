package com.example.ui.screens.company

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.VryvRepository
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvInputBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCompanySheet(
    repository: VryvRepository,
    onDismiss: () -> Unit,
    onCompanyCreated: (String) -> Unit
) {
    var title by remember { mutableStateOf("Идём на Techno Room") }
    var meetingTime by remember { mutableStateOf("22:30") }
    var meetingPlace by remember { mutableStateOf("Главный вход Re:Public") }
    var maxMembers by remember { mutableStateOf("6") }
    var isOpen by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = VryvCard
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Новая компания",
                color = VryvTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Событие: Techno Room: Ночь 3",
                color = VryvPink,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title Field
            Text(text = "Название компании", color = VryvTextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = VryvInputBg,
                    unfocusedContainerColor = VryvInputBg,
                    focusedBorderColor = VryvPink,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = VryvTextPrimary,
                    unfocusedTextColor = VryvTextPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_company_title")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Time & Limit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Время встречи", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = meetingTime,
                        onValueChange = { meetingTime = it },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvInputBg,
                            unfocusedContainerColor = VryvInputBg,
                            focusedBorderColor = VryvPink,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "Лимит мест", color = VryvTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = maxMembers,
                        onValueChange = { maxMembers = it },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = VryvInputBg,
                            unfocusedContainerColor = VryvInputBg,
                            focusedBorderColor = VryvPink,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = VryvTextPrimary,
                            unfocusedTextColor = VryvTextPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Meeting Place
            Text(text = "Место сбора", color = VryvTextSecondary, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = meetingPlace,
                onValueChange = { meetingPlace = it },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = VryvInputBg,
                    unfocusedContainerColor = VryvInputBg,
                    focusedBorderColor = VryvPink,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = VryvTextPrimary,
                    unfocusedTextColor = VryvTextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Open / Closed Switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(VryvInputBg)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isOpen) "Открытая компания" else "Закрытая компания",
                        color = VryvTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = if (isOpen) "Любой может вступить сразу" else "Вступление только по подтверждению",
                        color = VryvTextSecondary,
                        fontSize = 11.sp
                    )
                }
                Switch(
                    checked = isOpen,
                    onCheckedChange = { isOpen = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = VryvLime,
                        uncheckedThumbColor = VryvTextMuted,
                        uncheckedTrackColor = VryvCard
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(VryvPink)
                    .clickable {
                        val limit = maxMembers.toIntOrNull() ?: 6
                        val newGroup = repository.createGroup(
                            title = title.ifBlank { "Компания на Techno" },
                            eventId = "e_techno",
                            meetingTime = meetingTime,
                            meetingPlace = meetingPlace,
                            maxMembers = limit,
                            isOpen = isOpen
                        )
                        onCompanyCreated(newGroup.id)
                    }
                    .padding(vertical = 14.dp)
                    .testTag("submit_create_company"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Создать компанию",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
