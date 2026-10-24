package com.example.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
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
import com.example.ui.components.GoogleMulticolorIcon
import com.example.ui.components.VryvButton
import com.example.ui.components.VryvButtonVariant
import com.example.ui.components.VryvChip
import com.example.ui.components.VryvFieldState
import com.example.ui.components.VryvGroupList
import com.example.ui.components.VryvIconButton
import com.example.ui.components.VryvIcons
import com.example.ui.components.VryvInputField
import com.example.ui.components.VryvOptionCard
import com.example.ui.components.VryvOtpRow
import com.example.ui.components.VryvSubScreenHeader
import com.example.ui.components.VryvTag
import com.example.ui.components.VryvTagVariant
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    repository: VryvRepository,
    onContinueWithGoogle: () -> Unit,
    onLoginWithEmail: () -> Unit,
    onGoToRegister: () -> Unit,
    onGuestEntry: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showGoogleAccountPicker by remember { mutableStateOf(false) }
    var showGoogleLinkState by remember { mutableStateOf(false) }
    var linkPassword by remember { mutableStateOf("TechnoMinsk2026") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        // Top 500.dp Hero Image with gradient fade into #0B0B0F (welcome.png)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .align(Alignment.TopCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.techno),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.0f to Color(0x590B0B0F),
                            0.45f to Color(0x400B0B0F),
                            0.82f to Color(0xF50B0B0F),
                            1.0f to VryvBg
                        )
                    )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "ВРЫВ Логотип",
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(72.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Город происходит\nсейчас",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 37.sp,
                letterSpacing = (-1.0).sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Концерты, бары, выставки и люди, с которыми по пути — в одном месте.",
                color = Color(0xFFC2C2CE),
                fontFamily = InterFontFamily,
                fontSize = 15.sp,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Primary Google CTA (White #F5F5F7 with multicolor G icon)
            VryvButton(
                text = "Продолжить с Google",
                onClick = {
                    showGoogleLinkState = false
                    showGoogleAccountPicker = true
                },
                variant = VryvButtonVariant.WHITE,
                leadingIcon = { GoogleMulticolorIcon() },
                testTag = "welcome_google_btn"
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Secondary Email CTA (#24242C with Mail icon)
            VryvButton(
                text = "Войти по почте",
                onClick = onLoginWithEmail,
                variant = VryvButtonVariant.SECONDARY,
                leadingIcon = {
                    Icon(
                        imageVector = VryvIcons.Mail,
                        contentDescription = null,
                        tint = VryvTextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                testTag = "welcome_email_btn"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Create Account · Guest Entry Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Создать аккаунт",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { onGoToRegister() }
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                        .testTag("welcome_register_btn")
                )
                Text(
                    text = "·",
                    color = Color(0xFF4A4A56),
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                Text(
                    text = "Смотреть афишу >",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { onGuestEntry() }
                        .padding(vertical = 4.dp, horizontal = 6.dp)
                        .testTag("welcome_guest_link")
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Продолжая, ты соглашаешься с Условиями сервиса и Политикой конфиденциальности",
                color = VryvTextMuted,
                fontFamily = InterFontFamily,
                fontSize = 11.5.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Google Account Picker (gpick.png) & Account Linking (glink.png)
    if (showGoogleAccountPicker) {
        ModalBottomSheet(
            onDismissRequest = { showGoogleAccountPicker = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color(0xFF17171C),
            dragHandle = null
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(38.dp)
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(Color(0xFF3A3A44))
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(14.dp))

                if (!showGoogleLinkState) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GoogleMulticolorIcon()
                        Text(
                            text = "Вход через Google",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Выбери аккаунт",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "для входа в приложение «ВРЫВ»",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    VryvGroupList {
                        // Account 1: Max Kovalev (Direct login)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showGoogleAccountPicker = false
                                    repository.login("max.vryv@gmail.com", "TechnoMinsk2026")
                                    onContinueWithGoogle()
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.av2),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Максим Ковалёв",
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "max.vryv@gmail.com",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp
                                )
                            }
                            Icon(
                                imageVector = VryvIcons.ChevRight,
                                contentDescription = null,
                                tint = Color(0xFF5A5A66),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        HorizontalDivider(color = VryvLine, thickness = 0.5.dp, modifier = Modifier.padding(start = 68.dp))

                        // Account 2: Work account -> triggers glink.png state
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showGoogleLinkState = true }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(VryvPink),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "М",
                                    color = Color.White,
                                    fontFamily = InterFontFamily,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Максим (рабочая)",
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "m.kovalev@studio.by",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp
                                )
                            }
                            Icon(
                                imageVector = VryvIcons.ChevRight,
                                contentDescription = null,
                                tint = Color(0xFF5A5A66),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        HorizontalDivider(color = VryvLine, thickness = 0.5.dp, modifier = Modifier.padding(start = 68.dp))

                        // Account 3: Alina (Visitor)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    showGoogleAccountPicker = false
                                    repository.login("alina.tech@gmail.com", "Alina2026")
                                    onContinueWithGoogle()
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.av1),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Алина (Посетитель)",
                                    color = VryvTextPrimary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "alina.tech@gmail.com",
                                    color = VryvTextSecondary,
                                    fontFamily = InterFontFamily,
                                    fontSize = 12.5.sp
                                )
                            }
                            Icon(
                                imageVector = VryvIcons.ChevRight,
                                contentDescription = null,
                                tint = Color(0xFF5A5A66),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Google передаст приложению «ВРЫВ» твоё имя, адрес почты и фото профиля.",
                        color = VryvTextMuted,
                        fontFamily = InterFontFamily,
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    // glink.png — Account Linking screen
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.av2),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .border(2.dp, VryvPink, CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .offset(x = (-12).dp)
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(3.dp, VryvBg, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            GoogleMulticolorIcon()
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Аккаунт с этой почтой уже есть",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Почта max.kovalev@gmail.com уже зарегистрирована во «ВРЫВ». Введи пароль один раз, чтобы связать вход через Google с существующим профилем.",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    VryvInputField(
                        value = linkPassword,
                        onValueChange = { linkPassword = it },
                        label = "Пароль от аккаунта ВРЫВ",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    VryvButton(
                        text = "Связать и войти",
                        onClick = {
                            showGoogleAccountPicker = false
                            repository.login("max.vryv@gmail.com", "TechnoMinsk2026")
                            onContinueWithGoogle()
                        },
                        variant = VryvButtonVariant.PRIMARY
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    VryvButton(
                        text = "Назад к выбору аккаунта",
                        onClick = { showGoogleLinkState = false },
                        variant = VryvButtonVariant.GHOST
                    )
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onSuccessLogin: () -> Unit,
    onGoToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("max.vryv@gmail.com") }
    var password by remember { mutableStateOf("TechnoMinsk2026") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .verticalScroll(rememberScrollState())
    ) {
        VryvSubScreenHeader(
            title = "",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Text(
                text = "С возвращением",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.6).sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Войди по почте или через Google",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            VryvInputField(
                value = email,
                onValueChange = {
                    email = it
                    errorMessage = null
                },
                label = "Электронная почта",
                placeholder = "max.kovalev@gmail.com",
                testTag = "login_email_input"
            )

            Spacer(modifier = Modifier.height(14.dp))

            VryvInputField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = null
                },
                label = "Пароль",
                rightLabel = {
                    Text(
                        text = "Забыли пароль?",
                        color = VryvPink,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onForgotPasswordClick() }
                    )
                },
                isPassword = true,
                state = if (errorMessage != null) VryvFieldState.ERROR else VryvFieldState.DEFAULT,
                hintText = errorMessage,
                testTag = "login_password_input"
            )

            Spacer(modifier = Modifier.height(22.dp))

            VryvButton(
                text = "Войти",
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Пожалуйста, заполните Email и пароль"
                    } else {
                        val (success, msg) = repository.login(email, password)
                        if (success) {
                            onSuccessLogin()
                        } else {
                            errorMessage = msg ?: "Неверный пароль. Осталось 4 попытки до временной блокировки"
                        }
                    }
                },
                variant = VryvButtonVariant.PRIMARY,
                testTag = "login_submit_btn"
            )

            // Divider "или"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = VryvLine, thickness = 0.5.dp)
                Text(
                    text = "или",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = VryvLine, thickness = 0.5.dp)
            }

            VryvButton(
                text = "Продолжить с Google",
                onClick = {
                    repository.login("max.vryv@gmail.com", "TechnoMinsk2026")
                    onSuccessLogin()
                },
                variant = VryvButtonVariant.WHITE,
                leadingIcon = { GoogleMulticolorIcon() }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Ещё нет аккаунта? ",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp
                )
                Text(
                    text = "Создать аккаунт",
                    color = VryvPink,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }
        }
    }
}

@Composable
fun RegisterScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onCodeSent: () -> Unit,
    onGoToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Минск") }
    var agreeTerms by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val hasMinLength = password.length >= 6
    val hasLettersAndDigits = password.any { it.isLetter() } && password.any { it.isDigit() }
    val hasUppercase = password.any { it.isUpperCase() }
    val strengthScore = listOf(hasMinLength, hasLettersAndDigits, hasUppercase, password.length >= 10).count { it }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .verticalScroll(rememberScrollState())
    ) {
        VryvSubScreenHeader(
            title = "",
            onBackClick = onBackClick,
            rightAction = {
                Text(
                    text = "Войти",
                    color = VryvPink,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable { onGoToLogin() }
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 10.dp)
        ) {
            Text(
                text = "Новый аккаунт",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.6).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Займёт меньше минуты",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            VryvInputField(
                value = name,
                onValueChange = { name = it; errorMessage = null },
                label = "Как тебя зовут",
                placeholder = "Аня Ковалёва",
                testTag = "reg_name_input"
            )

            Spacer(modifier = Modifier.height(14.dp))

            VryvInputField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = "Электронная почта",
                placeholder = "anya.kovaleva@gmail.com",
                state = if (email.contains("@") && errorMessage == null) VryvFieldState.OK else VryvFieldState.DEFAULT,
                hintText = if (email.contains("@") && errorMessage == null) "На эту почту придёт код подтверждения" else null,
                testTag = "reg_email_input"
            )

            Spacer(modifier = Modifier.height(14.dp))

            VryvInputField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = "Пароль",
                placeholder = "Минимум 8 символов",
                isPassword = true,
                testTag = "reg_password_input"
            )

            // 4-segment Password Strength Bar (.pw-bars in s2.css)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                for (i in 1..4) {
                    val barColor = when {
                        password.isEmpty() -> Color(0xFF282832)
                        strengthScore == 1 && i == 1 -> VryvPink
                        i <= strengthScore -> VryvLime
                        else -> Color(0xFF282832)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(barColor)
                    )
                }
            }

            // Password requirements checklist
            Column(
                modifier = Modifier.padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                PasswordRequirementRow("Минимум 8 символов", hasMinLength)
                PasswordRequirementRow("Буквы и цифры", hasLettersAndDigits)
                PasswordRequirementRow("Заглавная буква", hasUppercase)
            }

            Spacer(modifier = Modifier.height(14.dp))

            VryvInputField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = null },
                label = "Повторите пароль",
                placeholder = "Повторите пароль",
                isPassword = true,
                state = if (confirmPassword.isNotEmpty() && confirmPassword != password) VryvFieldState.ERROR else VryvFieldState.DEFAULT,
                testTag = "reg_confirm_password_input"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Terms checkbox row matching s2-auth.js
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { agreeTerms = !agreeTerms },
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(if (agreeTerms) VryvPink else VryvCard)
                        .border(1.dp, if (agreeTerms) VryvPink else VryvLine, RoundedCornerShape(7.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (agreeTerms) {
                        Icon(
                            imageVector = VryvIcons.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Принимаю Условия сервиса и Политику конфиденциальности, мне исполнилось 18 лет",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage!!,
                    color = VryvPink,
                    fontFamily = InterFontFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            VryvButton(
                text = "Продолжить",
                onClick = {
                    when {
                        name.isBlank() || email.isBlank() || password.isBlank() -> {
                            errorMessage = "Заполните все обязательные поля"
                        }
                        password != confirmPassword -> {
                            errorMessage = "Пароли не совпадают"
                        }
                        password.length < 6 -> {
                            errorMessage = "Пароль должен быть не менее 6 символов"
                        }
                        !agreeTerms -> {
                            errorMessage = "Необходимо согласиться с условиями"
                        }
                        else -> {
                            val (success, msg) = repository.register(name, email, password, city)
                            if (success) {
                                onCodeSent()
                            } else {
                                errorMessage = msg
                            }
                        }
                    }
                },
                variant = VryvButtonVariant.PRIMARY,
                testTag = "reg_submit_btn"
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PasswordRequirementRow(text: String, met: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(if (met) Color(0x2EC7FF28) else Color(0xFF26262E)),
            contentAlignment = Alignment.Center
        ) {
            if (met) {
                Icon(
                    imageVector = VryvIcons.Check,
                    contentDescription = null,
                    tint = VryvLime,
                    modifier = Modifier.size(11.dp)
                )
            }
        }
        Text(
            text = text,
            color = if (met) Color(0xFFD0D0DC) else VryvTextSecondary,
            fontFamily = InterFontFamily,
            fontSize = 12.5.sp
        )
    }
}

@Composable
fun EmailCodeVerificationScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onVerified: () -> Unit,
    modifier: Modifier = Modifier
) {
    var code by remember { mutableStateOf("2410") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        VryvSubScreenHeader(
            title = "",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Проверим почту",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.6).sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Отправили проверочный код на указанный Email",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            VryvOtpRow(
                code = code,
                onCodeChange = {
                    code = it
                    errorMessage = null
                },
                cellCount = 6,
                state = if (errorMessage != null) VryvFieldState.ERROR else VryvFieldState.DEFAULT,
                testTag = "code_input"
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage!!,
                    color = VryvPink,
                    fontFamily = InterFontFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Не пришло письмо?",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp
                )
                Text(
                    text = "Отправить снова · 0:42",
                    color = VryvTextMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            VryvButton(
                text = "Подтвердить",
                onClick = {
                    if (repository.confirmEmailCode(code)) {
                        onVerified()
                    } else {
                        errorMessage = "Код не подошёл. Проверь письмо или запроси новый"
                    }
                },
                variant = VryvButtonVariant.PRIMARY,
                testTag = "code_submit_btn"
            )
        }
    }
}

@Composable
fun ForgotPasswordScreen(
    repository: VryvRepository,
    onBackClick: () -> Unit,
    onSuccessReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("max.vryv@gmail.com") }
    var code by remember { mutableStateOf("2410") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var step by remember { mutableIntStateOf(1) } // 1: Email (rec1), 2: Code & New Password (rec2/rec3), 3: Success (rec4)
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        if (step < 3) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VryvIconButton(
                    icon = VryvIcons.Back,
                    onClick = { if (step > 1) step -= 1 else onBackClick() }
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (i in 1..3) {
                        val color = when {
                            i == step -> VryvPink
                            i < step -> VryvPink.copy(alpha = 0.45f)
                            else -> Color(0xFF26262F)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(color)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(40.dp))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalArrangement = if (step == 3) Arrangement.SpaceBetween else Arrangement.Top
        ) {
            when (step) {
                1 -> {
                    Text(
                        text = "Забыли пароль?",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Введи почту, на которую зарегистрирован аккаунт — пришлём 6-значный код",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    VryvInputField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Электронная почта",
                        placeholder = "max.kovalev@gmail.com"
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    VryvButton(
                        text = "Отправить код",
                        onClick = { step = 2 },
                        variant = VryvButtonVariant.PRIMARY
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(VryvCard)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x24FF1D8E)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Mail,
                                contentDescription = null,
                                tint = VryvPink,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = "Если ты входил через Google, пароль сбрасывать не нужно — просто выбери «Продолжить с Google» на экране входа.",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.5.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                2 -> {
                    Text(
                        text = "Код и новый пароль",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Код отправлен на $email",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    VryvOtpRow(
                        code = code,
                        onCodeChange = { code = it; errorMessage = null },
                        cellCount = 6
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    VryvInputField(
                        value = newPassword,
                        onValueChange = { newPassword = it; errorMessage = null },
                        label = "Новый пароль",
                        placeholder = "Минимум 6 символов",
                        isPassword = true
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    VryvInputField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; errorMessage = null },
                        label = "Повторите новый пароль",
                        placeholder = "Повторите пароль",
                        isPassword = true
                    )
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = errorMessage!!, color = VryvPink, fontFamily = InterFontFamily, fontSize = 12.5.sp)
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    VryvButton(
                        text = "Сохранить пароль",
                        onClick = {
                            if (code != "2410" && code != "481920") {
                                errorMessage = "Неверный код"
                            } else if (newPassword.length < 6) {
                                errorMessage = "Пароль должен быть от 6 символов"
                            } else {
                                repository.resetPassword(email, newPassword)
                                step = 3
                            }
                        },
                        variant = VryvButtonVariant.PRIMARY
                    )
                }
                3 -> {
                    Spacer(modifier = Modifier.height(40.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(CircleShape)
                                .background(Color(0x24C7FF28))
                                .border(1.5.dp, Color(0x73C7FF28), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = VryvIcons.Check,
                                contentDescription = null,
                                tint = VryvLime,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(22.dp))
                        Text(
                            text = "Пароль обновлён",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Все активные сессии на других устройствах завершены. Теперь можно войти с новым паролем.",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    VryvButton(
                        text = "Войти в аккаунт",
                        onClick = onSuccessReset,
                        variant = VryvButtonVariant.PRIMARY,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    repository: VryvRepository,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) } // 1: Интересы (p1), 2: Бюджет (p2), 3: Формат (p3), 4: Лента готова (p4)

    val musicInterests = listOf("Электроника", "Техно", "Концерты", "Инди", "Джаз", "Поп", "Хип-хоп", "Вечеринки", "Диджей-сеты", "Винил")
    val cultureInterests = listOf("Стендап", "Выставки", "Театр", "Кинопоказы", "Лекции", "Винные дегустации", "Гастрономия", "Квизы", "Настолки", "Бег", "Йога", "Маркеты")
    val selectedInterests = remember { mutableStateListOf("Электроника", "Техно", "Стендап", "Выставки", "Винные дегустации") }

    val budgetOptions = listOf(
        "Бесплатно" to "Вход свободный, открытые площадки",
        "До 30 BYN" to "Бары, стендап, камерные вечера",
        "До 50 BYN" to "Концерты, клубы, большинство событий",
        "Любой бюджет" to "Не ограничивать ленту по цене"
    )
    var selectedBudget by remember { mutableStateOf("До 50 BYN") }

    val formatOptions = listOf(
        "Спонтанно на сегодня" to "Решаю вечером, куда сорваться через час",
        "Планирую на выходные" to "Собираю программу на пятницу и субботу",
        "Ищу компанию" to "Хочу ходить на события с новыми людьми",
        "Хожу со своими" to "Зову друзей и делюсь планами"
    )
    val selectedFormats = remember { mutableStateListOf("Спонтанно на сегодня", "Ищу компанию") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (step < 4) {
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                // Header with progress bar and "Пропустить"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (step > 1) {
                        VryvIconButton(
                            icon = VryvIcons.Back,
                            onClick = { step -= 1 }
                        )
                    } else {
                        Spacer(modifier = Modifier.width(40.dp))
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (s in 1..3) {
                            val color = when {
                                s == step -> VryvPink
                                s < step -> VryvPink.copy(alpha = 0.45f)
                                else -> Color(0xFF26262F)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(color)
                            )
                        }
                    }

                    Text(
                        text = "Пропустить",
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            repository.finishOnboarding(
                                selectedInterests.toList(),
                                selectedBudget,
                                selectedFormats.toList()
                            )
                            onComplete()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "ШАГ $step ИЗ 3",
                    color = VryvPink,
                    fontFamily = InterFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.6.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                when (step) {
                    1 -> {
                        Text(
                            text = "Что тебе близко?",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.6).sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Выбери от 3 интересов — подстроим ленту и рекомендации",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Музыка и вечеринки",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            musicInterests.forEach { tag ->
                                val isSelected = selectedInterests.contains(tag)
                                VryvChip(
                                    text = tag,
                                    selected = isSelected,
                                    isBig = true,
                                    onClick = {
                                        if (isSelected) selectedInterests.remove(tag)
                                        else selectedInterests.add(tag)
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Культура и город",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            cultureInterests.forEach { tag ->
                                val isSelected = selectedInterests.contains(tag)
                                VryvChip(
                                    text = tag,
                                    selected = isSelected,
                                    isBig = true,
                                    onClick = {
                                        if (isSelected) selectedInterests.remove(tag)
                                        else selectedInterests.add(tag)
                                    }
                                )
                            }
                        }
                    }

                    2 -> {
                        Text(
                            text = "Комфортный бюджет",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.6).sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Покажем события в твоём диапазоне в первую очередь",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            budgetOptions.forEach { (title, sub) ->
                                VryvOptionCard(
                                    title = title,
                                    subtitle = sub,
                                    selected = selectedBudget == title,
                                    onClick = { selectedBudget = title }
                                )
                            }
                        }
                    }

                    3 -> {
                        Text(
                            text = "Как ты обычно выбираешь планы?",
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.6).sp,
                            lineHeight = 30.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Можно выбрать несколько вариантов",
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            formatOptions.forEach { (title, sub) ->
                                val isSelected = selectedFormats.contains(title)
                                VryvOptionCard(
                                    title = title,
                                    subtitle = sub,
                                    selected = isSelected,
                                    onClick = {
                                        if (isSelected) selectedFormats.remove(title)
                                        else selectedFormats.add(title)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)) {
                if (step == 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Выбрано: ${selectedInterests.size} из 3 минимум",
                            color = if (selectedInterests.size >= 3) VryvLime else VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (selectedInterests.isNotEmpty()) {
                            Text(
                                text = "Сбросить",
                                color = VryvTextSecondary,
                                fontFamily = InterFontFamily,
                                fontSize = 13.sp,
                                modifier = Modifier.clickable { selectedInterests.clear() }
                            )
                        }
                    }
                }
                VryvButton(
                    text = "Продолжить",
                    onClick = { step += 1 },
                    variant = VryvButtonVariant.PRIMARY,
                    testTag = "onboarding_next_btn"
                )
            }
        } else {
            // Step 4: p4.png ("Лента готова" with 3-card fan)
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 3-card fan matching p4.png
                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .height(210.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dj),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .offset(x = (-36).dp, y = 12.dp)
                            .rotate(-8f)
                            .width(160.dp)
                            .height(190.dp)
                            .clip(RoundedCornerShape(22.dp))
                    )
                    Image(
                        painter = painterResource(id = R.drawable.rooftop),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .offset(x = 36.dp, y = 14.dp)
                            .rotate(7f)
                            .width(160.dp)
                            .height(190.dp)
                            .clip(RoundedCornerShape(22.dp))
                    )
                    Box(
                        modifier = Modifier
                            .width(176.dp)
                            .height(200.dp)
                            .clip(RoundedCornerShape(24.dp))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.techno),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        0.3f to Color.Transparent,
                                        1.0f to Color(0xEB0B0B0F)
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(12.dp)
                        ) {
                            VryvTag("По твоим интересам", variant = VryvTagVariant.PINK)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ночь синтвейва",
                                color = VryvTextPrimary,
                                fontFamily = InterFontFamily,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Пт, 23:00 · Клуб «Модуль»",
                                color = Color(0xFFC8C8D4),
                                fontFamily = InterFontFamily,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(VryvPink),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = VryvIcons.Spark,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Лента готова",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1.0).sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Подобрали 28 событий в Минске на эти выходные под твои интересы. Настройки всегда можно поменять в профиле.",
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    lineHeight = 21.sp,
                    textAlign = TextAlign.Center
                )
            }

            VryvButton(
                text = "Открыть афишу",
                onClick = {
                    repository.finishOnboarding(
                        selectedInterests.toList(),
                        selectedBudget,
                        selectedFormats.toList()
                    )
                    onComplete()
                },
                variant = VryvButtonVariant.PRIMARY,
                testTag = "onboarding_next_btn",
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestAuthBottomSheet(
    actionDescription: String,
    repository: VryvRepository,
    onDismiss: () -> Unit,
    onOpenLogin: () -> Unit,
    onOpenRegister: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color(0xFF17171C),
        dragHandle = null
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(38.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF3A3A44))
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x29FF1D8E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = VryvIcons.Heart,
                    contentDescription = null,
                    tint = VryvPink,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Войди, чтобы сохранять события",
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.4).sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = actionDescription.ifBlank {
                    "В гостевом режиме можно смотреть афишу и карту. Для закладок, планов, свайпов и поиска компании нужен аккаунт."
                },
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            VryvButton(
                text = "Продолжить с Google",
                onClick = {
                    repository.login("max.vryv@gmail.com", "TechnoMinsk2026")
                    onDismiss()
                },
                variant = VryvButtonVariant.WHITE,
                leadingIcon = { GoogleMulticolorIcon() }
            )

            Spacer(modifier = Modifier.height(10.dp))

            VryvButton(
                text = "Войти по почте",
                onClick = {
                    onDismiss()
                    onOpenLogin()
                },
                variant = VryvButtonVariant.PRIMARY
            )

            Spacer(modifier = Modifier.height(10.dp))

            VryvButton(
                text = "Создать аккаунт",
                onClick = {
                    onDismiss()
                    onOpenRegister()
                },
                variant = VryvButtonVariant.SECONDARY
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Продолжить без входа",
                color = VryvTextSecondary,
                fontFamily = InterFontFamily,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDismiss() }
                    .padding(vertical = 6.dp)
            )
        }
    }
}
