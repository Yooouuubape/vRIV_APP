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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
fun WelcomeScreen(
    repository: VryvRepository,
    onContinueWithGoogle: () -> Unit,
    onLoginWithEmail: () -> Unit,
    onGoToRegister: () -> Unit,
    onGuestEntry: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showGoogleAccountPicker by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
    ) {
        Image(
            painter = painterResource(id = R.drawable.rooftop),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xDF0B0B0F))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "ВРЫВ Логотип",
                    modifier = Modifier
                        .height(84.dp)
                        .width(220.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Город происходит сейчас",
                    color = VryvTextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "События, спонтанный отдых и поиск компании в Минске",
                    color = VryvTextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Google Button (White)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .clickable { showGoogleAccountPicker = true }
                        .testTag("welcome_google_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Продолжить с Google",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Email Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCard)
                        .clickable { onLoginWithEmail() }
                        .testTag("welcome_email_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Войти по Email",
                        color = VryvTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Register Link
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .border(1.dp, VryvLine, RoundedCornerShape(14.dp))
                        .clickable { onGoToRegister() }
                        .testTag("welcome_register_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Зарегистрироваться",
                        color = VryvPink,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Guest link
                Text(
                    text = "Смотреть афишу без входа",
                    color = VryvTextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .clickable { onGuestEntry() }
                        .padding(vertical = 8.dp)
                        .testTag("welcome_guest_link")
                )

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }

    if (showGoogleAccountPicker) {
        ModalBottomSheet(
            onDismissRequest = { showGoogleAccountPicker = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VryvCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Вход через Google",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Выберите тестовый аккаунт для демонстрации:",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Account 1: Maxim
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VryvBg)
                        .clickable {
                            showGoogleAccountPicker = false
                            repository.login("max.vryv@gmail.com", "TechnoMinsk2026")
                            onContinueWithGoogle()
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.av2),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Максим Салуков (Владелец Loft 13)", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "max.vryv@gmail.com", color = VryvTextSecondary, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Account 2: Alina
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(VryvBg)
                        .clickable {
                            showGoogleAccountPicker = false
                            repository.login("alina.tech@gmail.com", "Alina2026")
                            onContinueWithGoogle()
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.av1),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Алина (Посетитель)", color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                        Text(text = "alina.tech@gmail.com", color = VryvTextSecondary, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
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
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = VryvTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Вход по Email",
            color = VryvTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Введите ваши учетные данные ВРЫВ",
            color = VryvTextSecondary,
            fontSize = 13.sp
        )

        // Demo test credentials quick fill
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(VryvCard)
                .padding(12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = VryvLime, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Тестовые аккаунты для входа:", color = VryvLime, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(VryvBg)
                            .clickable {
                                email = "max.vryv@gmail.com"
                                password = "TechnoMinsk2026"
                                errorMessage = null
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Максим (Loft 13)", color = VryvTextPrimary, fontSize = 12.sp)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(VryvBg)
                            .clickable {
                                email = "alina.tech@gmail.com"
                                password = "Alina2026"
                                errorMessage = null
                            }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(text = "Алина (Посетитель)", color = VryvTextPrimary, fontSize = 12.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Email", color = VryvTextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null
            },
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
                .testTag("login_email_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(text = "Пароль", color = VryvTextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = VryvTextSecondary
                    )
                }
            },
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
                .testTag("login_password_input")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Забыли пароль?",
                color = VryvTextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.clickable { onForgotPasswordClick() }
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = VryvPink,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(VryvPink)
                .clickable {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Пожалуйста, заполните Email и пароль"
                    } else {
                        val (success, msg) = repository.login(email, password)
                        if (success) {
                            onSuccessLogin()
                        } else {
                            errorMessage = msg
                        }
                    }
                }
                .testTag("login_submit_btn"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Войти",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Нет аккаунта? ", color = VryvTextSecondary, fontSize = 13.sp)
            Text(
                text = "Зарегистрироваться",
                color = VryvPink,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onGoToRegister() }
            )
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = VryvTextPrimary)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Регистрация",
            color = VryvTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Создай аккаунт для доступа ко всем возможностям ВРЫВ",
            color = VryvTextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Name
        Text(text = "Имя и фамилия", color = VryvTextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it; errorMessage = null },
            placeholder = { Text("Например, Полина Салукова", color = VryvTextMuted, fontSize = 14.sp) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = VryvInputBg,
                unfocusedContainerColor = VryvInputBg,
                focusedBorderColor = VryvPink,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = VryvTextPrimary,
                unfocusedTextColor = VryvTextPrimary
            ),
            modifier = Modifier.fillMaxWidth().testTag("reg_name_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Email
        Text(text = "Email", color = VryvTextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it; errorMessage = null },
            placeholder = { Text("example@gmail.com", color = VryvTextMuted, fontSize = 14.sp) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = VryvInputBg,
                unfocusedContainerColor = VryvInputBg,
                focusedBorderColor = VryvPink,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = VryvTextPrimary,
                unfocusedTextColor = VryvTextPrimary
            ),
            modifier = Modifier.fillMaxWidth().testTag("reg_email_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // City
        Text(text = "Город", color = VryvTextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = city,
            onValueChange = { city = it; errorMessage = null },
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

        Spacer(modifier = Modifier.height(12.dp))

        // Password
        Text(text = "Пароль", color = VryvTextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it; errorMessage = null },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = VryvInputBg,
                unfocusedContainerColor = VryvInputBg,
                focusedBorderColor = VryvPink,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = VryvTextPrimary,
                unfocusedTextColor = VryvTextPrimary
            ),
            modifier = Modifier.fillMaxWidth().testTag("reg_password_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Confirm Password
        Text(text = "Повторите пароль", color = VryvTextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it; errorMessage = null },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = VryvInputBg,
                unfocusedContainerColor = VryvInputBg,
                focusedBorderColor = VryvPink,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = VryvTextPrimary,
                unfocusedTextColor = VryvTextPrimary
            ),
            modifier = Modifier.fillMaxWidth().testTag("reg_confirm_password_input")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Agreement Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = agreeTerms,
                onCheckedChange = { agreeTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = VryvPink)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Я принимаю условия сервиса и политику конфиденциальности",
                color = VryvTextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = VryvPink,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(VryvPink)
                .clickable {
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
                }
                .testTag("reg_submit_btn"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Продолжить",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Уже есть аккаунт? ", color = VryvTextSecondary, fontSize = 13.sp)
            Text(
                text = "Войти",
                color = VryvPink,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onGoToLogin() }
            )
        }
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
            .padding(20.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = VryvTextPrimary)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Подтверждение Email",
            color = VryvTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Мы отправили 4-значный проверочный код на указанный Email",
            color = VryvTextSecondary,
            fontSize = 13.5.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Demo test code banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(VryvLime.copy(alpha = 0.15f))
                .border(1.dp, VryvLime, RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VryvLime, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "Тестовый код подтверждения:", color = VryvLime, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(text = "2410", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Код подтверждения", color = VryvTextSecondary, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = code,
            onValueChange = {
                code = it
                errorMessage = null
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = VryvInputBg,
                unfocusedContainerColor = VryvInputBg,
                focusedBorderColor = VryvPink,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = VryvTextPrimary,
                unfocusedTextColor = VryvTextPrimary
            ),
            modifier = Modifier.fillMaxWidth().testTag("code_input")
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = VryvPink,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(VryvPink)
                .clickable {
                    if (repository.confirmEmailCode(code)) {
                        onVerified()
                    } else {
                        errorMessage = "Неверный код. Введите 2410"
                    }
                }
                .testTag("code_submit_btn"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Подтвердить",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
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
    var step by remember { mutableIntStateOf(1) } // 1: Email, 2: Code & New Password, 3: Success
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .padding(20.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад", tint = VryvTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Восстановление пароля",
            color = VryvTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))

        when (step) {
            1 -> {
                Text(
                    text = "Введите Email вашего аккаунта для получения кода сброса",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvPink)
                        .clickable { step = 2 },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Отправить код (Демо: 2410)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            2 -> {
                Text(
                    text = "Введите код и придумайте новый пароль",
                    color = VryvTextSecondary,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("Код (2410)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Новый пароль") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage!!, color = VryvPink, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VryvPink)
                        .clickable {
                            if (code != "2410") {
                                errorMessage = "Неверный код"
                            } else if (newPassword.length < 6) {
                                errorMessage = "Пароль должен быть от 6 символов"
                            } else {
                                repository.resetPassword(email, newPassword)
                                step = 3
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Сохранить новый пароль", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            3 -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VryvLime, modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Пароль успешно изменён!", color = VryvTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(24.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(VryvPink)
                            .clickable { onSuccessReset() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Войти с новым паролем", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen(
    repository: VryvRepository,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) } // 1: Интересы, 2: Бюджет, 3: Формат, 4: Финал

    val allInterests = listOf("Техно", "Хаус", "Концерты", "Стендап", "Бары", "Клубы", "Выставки", "Квизы", "Искусство", "Кино", "Андеграунд", "Бесплатные мероприятия")
    val selectedInterests = remember { mutableStateListOf("Техно", "Хаус", "Стендап", "Выставки") }

    val budgetOptions = listOf("Бесплатно", "До 20 BYN", "До 50 BYN", "До 100 BYN", "Цена неважна")
    var selectedBudget by remember { mutableStateOf("До 50 BYN") }

    val formatOptions = listOf("С друзьями", "Новые знакомства", "Люблю ходить один", "Спонтанные события", "Планирую заранее", "Спокойный отдых", "Активные вечеринки")
    val selectedFormats = remember { mutableStateListOf("С друзьями", "Новые знакомства", "Спонтанные события") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(VryvBg)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (1..3).forEach { s ->
                        Box(
                            modifier = Modifier
                                .size(if (s == step) 22.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (s <= step) VryvPink else VryvCard)
                        )
                    }
                }
                if (step < 4) {
                    Text(
                        text = "Шаг $step из 3",
                        color = VryvTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            when (step) {
                1 -> {
                    Text(
                        text = "Что тебе по вайбу?",
                        color = VryvTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Выбери от 3 интересов для персональной афиши",
                        color = VryvTextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        allInterests.forEach { tag ->
                            val isSelected = selectedInterests.contains(tag)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) VryvPink else VryvCard)
                                    .clickable {
                                        if (isSelected) selectedInterests.remove(tag)
                                        else selectedInterests.add(tag)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = tag,
                                    color = if (isSelected) Color.White else VryvTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }

                2 -> {
                    Text(
                        text = "Какой бюджет?",
                        color = VryvTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Поможет отфильтровать подходящие вечеринки и билеты",
                        color = VryvTextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        budgetOptions.forEach { opt ->
                            val isSelected = selectedBudget == opt
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0x33FF1D8E) else VryvCard)
                                    .border(
                                        1.dp,
                                        if (isSelected) VryvPink else Color.Transparent,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedBudget = opt }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = opt,
                                    color = VryvTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = VryvPink)
                                }
                            }
                        }
                    }
                }

                3 -> {
                    Text(
                        text = "Формат отдыха",
                        color = VryvTextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Как ты обычно проводишь вечера и выходные?",
                        color = VryvTextSecondary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        formatOptions.forEach { fmt ->
                            val isSelected = selectedFormats.contains(fmt)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0x33C7FF28) else VryvCard)
                                    .border(
                                        1.dp,
                                        if (isSelected) VryvLime else Color.Transparent,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        if (isSelected) selectedFormats.remove(fmt)
                                        else selectedFormats.add(fmt)
                                    }
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = fmt,
                                    color = VryvTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = VryvLime)
                                }
                            }
                        }
                    }
                }

                4 -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = null,
                            modifier = Modifier
                                .height(64.dp)
                                .width(180.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Всё готово!",
                            color = VryvTextPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Мы настроили персональную ленту событий Минска специально под твои вкусы.",
                            color = VryvTextSecondary,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(VryvPink)
                .clickable {
                    if (step < 3) {
                        step += 1
                    } else if (step == 3) {
                        step = 4
                    } else {
                        repository.finishOnboarding(
                            selectedInterests.toList(),
                            selectedBudget,
                            selectedFormats.toList()
                        )
                        onComplete()
                    }
                }
                .testTag("onboarding_next_btn"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (step == 4) "Открыть афишу" else "Далее",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
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
        sheetState = rememberModalBottomSheetState(),
        containerColor = VryvCard
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Lock,
                contentDescription = null,
                tint = VryvPink,
                modifier = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Требуется авторизация",
                color = VryvTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = actionDescription,
                color = VryvTextSecondary,
                fontSize = 13.5.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Quick login as Maxim
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .clickable {
                        repository.login("max.vryv@gmail.com", "TechnoMinsk2026")
                        onDismiss()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Войти как Максим Салуков", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick login as Alina
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(VryvCardElevated)
                    .clickable {
                        repository.login("alina.tech@gmail.com", "Alina2026")
                        onDismiss()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Войти как Алина (Посетитель)", color = VryvTextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VryvInputBg)
                        .clickable {
                            onDismiss()
                            onOpenLogin()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Ввести Email", color = VryvTextPrimary, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VryvPink)
                        .clickable {
                            onDismiss()
                            onOpenRegister()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Регистрация", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
