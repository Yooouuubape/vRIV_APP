package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.VryvRepository
import com.example.ui.components.BusinessTab
import com.example.ui.components.InviteFriendsSheet
import com.example.ui.components.ReportBottomSheet
import com.example.ui.components.UserTab
import com.example.ui.components.VryvBusinessTabBar
import com.example.ui.components.VryvUserTabBar
import com.example.ui.screens.auth.EmailCodeVerificationScreen
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.GuestAuthBottomSheet
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.OnboardingScreen
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.auth.WelcomeScreen
import com.example.ui.screens.business.BusinessAnalyticsScreen
import com.example.ui.screens.business.BusinessCabinetScreen
import com.example.ui.screens.business.BusinessEventsScreen
import com.example.ui.screens.business.BusinessHubScreen
import com.example.ui.screens.business.BusinessMessagesScreen
import com.example.ui.screens.business.BusinessSettingsScreen
import com.example.ui.screens.business.CreateEventScreen
import com.example.ui.screens.business.VenueRegistrationWizard
import com.example.ui.screens.company.CompanyScreen
import com.example.ui.screens.company.CreateCompanySheet
import com.example.ui.screens.company.DirectChatScreen
import com.example.ui.screens.company.GroupChatScreen
import com.example.ui.screens.company.MessagesListScreen
import com.example.ui.screens.feed.EventDetailScreen
import com.example.ui.screens.feed.FeedScreen
import com.example.ui.screens.feed.SearchScreen
import com.example.ui.screens.map.MapScreen
import com.example.ui.screens.profile.EditProfileScreen
import com.example.ui.screens.profile.NotificationsCenterScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.profile.SettingsScreen
import com.example.ui.screens.swipes.SwipesScreen
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VryvApp(
    repository: VryvRepository = VryvRepository.instance,
    modifier: Modifier = Modifier
) {
    val isLoggedIn by repository.isLoggedIn.collectAsState()
    val onboardingCompleted by repository.onboardingCompleted.collectAsState()
    val isBusinessMode by repository.isBusinessMode.collectAsState()
    val toastMessage by repository.toastMessage.collectAsState()
    val user by repository.currentUser.collectAsState()
    val venue by repository.businessVenue.collectAsState()

    var selectedUserTab by remember { mutableStateOf(UserTab.FEED) }
    var selectedBusinessTab by remember { mutableStateOf(BusinessTab.OVERVIEW) }

    // Screen navigation stack
    val backStack = remember { mutableStateListOf<String>() }

    // Auth screen navigation
    var authScreen by remember { mutableStateOf("welcome") } // "welcome", "login", "register", "code_verification", "forgot_password"

    // Company screen filter
    var companyFilterEventId by remember { mutableStateOf<String?>(null) }

    // Business registration wizard state
    var showVenueWizard by remember { mutableStateOf(false) }

    // Guest prompt state
    val guestAuthPrompt by repository.guestAuthPromptAction.collectAsState()

    // Dialog & sheet states
    var showRoleSwitcherSheet by remember { mutableStateOf(false) }
    var showCreateCompanySheet by remember { mutableStateOf(false) }
    var inviteEventId by remember { mutableStateOf<String?>(null) }
    var reportTarget by remember { mutableStateOf<Pair<String, String>?>(null) } // type to title

    // Auto-clear toast
    LaunchedEffect(toastMessage) {
        if (toastMessage != null) {
            delay(2800)
            repository.clearToast()
        }
    }

    val currentScreen = backStack.lastOrNull()

    // Handle Android system back button
    BackHandler(enabled = backStack.isNotEmpty()) {
        backStack.removeAt(backStack.lastIndex)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            if (isLoggedIn && onboardingCompleted && currentScreen == null && (!isBusinessMode || user.venueId != null)) {
                if (isBusinessMode) {
                    VryvBusinessTabBar(
                        selectedTab = selectedBusinessTab,
                        onTabSelected = { selectedBusinessTab = it }
                    )
                } else {
                    VryvUserTabBar(
                        selectedTab = selectedUserTab,
                        onTabSelected = { selectedUserTab = it }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(VryvBg)
                .padding(innerPadding)
        ) {
            if (!isLoggedIn) {
                when (authScreen) {
                    "welcome" -> WelcomeScreen(
                        repository = repository,
                        onContinueWithGoogle = { /* user state updated in google account picker */ },
                        onLoginWithEmail = { authScreen = "login" },
                        onGoToRegister = { authScreen = "register" },
                        onGuestEntry = { repository.startGuestSession() }
                    )
                    "login" -> LoginScreen(
                        repository = repository,
                        onBackClick = { authScreen = "welcome" },
                        onSuccessLogin = { /* handled */ },
                        onGoToRegister = { authScreen = "register" },
                        onForgotPasswordClick = { authScreen = "forgot_password" }
                    )
                    "register" -> RegisterScreen(
                        repository = repository,
                        onBackClick = { authScreen = "welcome" },
                        onCodeSent = { authScreen = "code_verification" },
                        onGoToLogin = { authScreen = "login" }
                    )
                    "code_verification" -> EmailCodeVerificationScreen(
                        repository = repository,
                        onBackClick = { authScreen = "register" },
                        onVerified = { /* user is registered and logged in */ }
                    )
                    "forgot_password" -> ForgotPasswordScreen(
                        repository = repository,
                        onBackClick = { authScreen = "login" },
                        onSuccessReset = { authScreen = "login" }
                    )
                }
            } else if (!onboardingCompleted) {
                OnboardingScreen(
                    repository = repository,
                    onComplete = { /* completed */ }
                )
            } else {
                // Main Authenticated View
                if (currentScreen != null) {
                    when {
                        currentScreen.startsWith("event_") -> {
                            val eventId = currentScreen.removePrefix("event_")
                            EventDetailScreen(
                                eventId = eventId,
                                repository = repository,
                                onBackClick = { backStack.removeAt(backStack.lastIndex) },
                                onFindCompanyClick = { evId ->
                                    companyFilterEventId = evId
                                    backStack.removeAt(backStack.lastIndex)
                                    selectedUserTab = UserTab.COMPANY
                                },
                                onOpenRouteClick = {
                                    backStack.removeAt(backStack.lastIndex)
                                    selectedUserTab = UserTab.MAP
                                },
                                onInviteFriendsClick = { evId -> inviteEventId = evId },
                                onReportClick = { type, title -> reportTarget = type to title }
                            )
                        }

                        currentScreen == "search" -> {
                            SearchScreen(
                                repository = repository,
                                onEventClick = { evId -> backStack.add("event_$evId") },
                                onBackClick = { backStack.removeAt(backStack.lastIndex) }
                            )
                        }

                        currentScreen == "notifications" -> {
                            NotificationsCenterScreen(
                                repository = repository,
                                onBackClick = { backStack.removeAt(backStack.lastIndex) },
                                onEventClick = { evId -> backStack.add("event_$evId") }
                            )
                        }

                        currentScreen == "messages_list" -> {
                            MessagesListScreen(
                                repository = repository,
                                onOpenDirectChat = { userId -> backStack.add("direct_chat_$userId") },
                                onBackClick = { backStack.removeAt(backStack.lastIndex) }
                            )
                        }

                        currentScreen.startsWith("group_chat_") -> {
                            val groupId = currentScreen.removePrefix("group_chat_")
                            GroupChatScreen(
                                groupId = groupId,
                                repository = repository,
                                onBackClick = { backStack.removeAt(backStack.lastIndex) },
                                onReportClick = { type, title -> reportTarget = type to title }
                            )
                        }

                        currentScreen.startsWith("direct_chat_") -> {
                            val userId = currentScreen.removePrefix("direct_chat_")
                            DirectChatScreen(
                                userId = userId,
                                repository = repository,
                                onBackClick = { backStack.removeAt(backStack.lastIndex) },
                                onReportClick = { type, title -> reportTarget = type to title }
                            )
                        }

                        currentScreen == "edit_profile" -> {
                            EditProfileScreen(
                                repository = repository,
                                onBackClick = { backStack.removeAt(backStack.lastIndex) }
                            )
                        }

                        currentScreen == "settings" -> {
                            SettingsScreen(
                                repository = repository,
                                onBackClick = { backStack.removeAt(backStack.lastIndex) },
                                onLogoutClick = {
                                    repository.logout()
                                    backStack.clear()
                                    authScreen = "welcome"
                                }
                            )
                        }

                        currentScreen == "biz_create_event" -> {
                            CreateEventScreen(
                                repository = repository,
                                onBackClick = { backStack.removeAt(backStack.lastIndex) },
                                onEventCreated = {
                                    backStack.removeAt(backStack.lastIndex)
                                    selectedBusinessTab = BusinessTab.EVENTS
                                }
                            )
                        }
                    }
                } else {
                    // Bottom Navigation Tabs
                    if (isBusinessMode) {
                        if (user.venueId == null) {
                            if (showVenueWizard) {
                                VenueRegistrationWizard(
                                    repository = repository,
                                    onBackClick = { showVenueWizard = false },
                                    onCompleted = { showVenueWizard = false }
                                )
                            } else {
                                BusinessHubScreen(
                                    repository = repository,
                                    onBackClick = { repository.setBusinessMode(false) },
                                    onOpenRegistrationWizard = { showVenueWizard = true },
                                    onOpenCabinet = { repository.setBusinessMode(true) }
                                )
                            }
                        } else {
                            when (selectedBusinessTab) {
                                BusinessTab.OVERVIEW -> BusinessCabinetScreen(
                                    repository = repository,
                                    onCreateEventClick = { backStack.add("biz_create_event") },
                                    onSwitchToPersonalClick = { repository.setBusinessMode(false) },
                                    onEventClick = { evId -> backStack.add("event_$evId") }
                                )
                                BusinessTab.EVENTS -> BusinessEventsScreen(
                                    repository = repository,
                                    onCreateEventClick = { backStack.add("biz_create_event") },
                                    onEventClick = { evId -> backStack.add("event_$evId") }
                                )
                                BusinessTab.AUDIENCE -> BusinessAnalyticsScreen(repository = repository)
                                BusinessTab.MESSAGES -> BusinessMessagesScreen(
                                    repository = repository
                                )
                                BusinessTab.SETTINGS -> BusinessSettingsScreen(
                                    repository = repository,
                                    onSwitchToPersonalClick = { repository.setBusinessMode(false) }
                                )
                            }
                        }
                    } else {
                        when (selectedUserTab) {
                            UserTab.FEED -> FeedScreen(
                                repository = repository,
                                onEventClick = { evId -> backStack.add("event_$evId") },
                                onOpenSearch = { backStack.add("search") },
                                onOpenNotifications = { backStack.add("notifications") },
                                onOpenMap = { selectedUserTab = UserTab.MAP }
                            )
                            UserTab.SWIPES -> SwipesScreen(
                                repository = repository,
                                onEventClick = { evId -> backStack.add("event_$evId") }
                            )
                            UserTab.MAP -> MapScreen(
                                repository = repository,
                                onEventClick = { evId -> backStack.add("event_$evId") }
                            )
                            UserTab.COMPANY -> CompanyScreen(
                                repository = repository,
                                filterEventId = companyFilterEventId,
                                onClearEventFilter = { companyFilterEventId = null },
                                onOpenGroupChat = { gId -> backStack.add("group_chat_$gId") },
                                onOpenDirectChat = { uId -> backStack.add("direct_chat_$uId") },
                                onOpenMessagesList = { backStack.add("messages_list") },
                                onCreateCompanyClick = { showCreateCompanySheet = true },
                                onReportClick = { type, title -> reportTarget = type to title }
                            )
                            UserTab.PROFILE -> ProfileScreen(
                                repository = repository,
                                onEditProfileClick = { backStack.add("edit_profile") },
                                onSettingsClick = { backStack.add("settings") },
                                onEventClick = { evId -> backStack.add("event_$evId") },
                                onSwitchRoleClick = { showRoleSwitcherSheet = true },
                                onOpenDirectChat = { uId -> backStack.add("direct_chat_$uId") }
                            )
                        }
                    }
                }
            }

            // Top Toast Banner
            AnimatedVisibility(
                visible = toastMessage != null,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .statusBarsPadding()
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(VryvCardElevated)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = toastMessage ?: "",
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }

    // Role Switcher Modal Sheet (sw.png)
    if (showRoleSwitcherSheet) {
        ModalBottomSheet(
            onDismissRequest = { showRoleSwitcherSheet = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = VryvCard
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp)
            ) {
                Text(
                    text = "Переключение профиля",
                    color = VryvTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(14.dp))

                // Option 1: Personal profile
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (!isBusinessMode) Color(0x22FF1D8E) else VryvCardElevated)
                        .clickable {
                            repository.setBusinessMode(false)
                            showRoleSwitcherSheet = false
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = user.avatarRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = user.name, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Личный профиль · @${user.username}", color = VryvTextSecondary, fontSize = 12.sp)
                    }
                    if (!isBusinessMode) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = VryvPink)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Option 2: Business profile (Loft 13)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isBusinessMode) Color(0x22C7FF28) else VryvCardElevated)
                        .clickable {
                            repository.setBusinessMode(true)
                            showRoleSwitcherSheet = false
                        }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(VryvLime.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Store, contentDescription = null, tint = VryvLime, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = venue.name, color = VryvTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Владелец заведения · ${venue.category}", color = VryvTextSecondary, fontSize = 12.sp)
                    }
                    if (isBusinessMode) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = VryvLime)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Create Company Sheet
    if (showCreateCompanySheet) {
        CreateCompanySheet(
            repository = repository,
            onDismiss = { showCreateCompanySheet = false },
            onCompanyCreated = { groupId ->
                showCreateCompanySheet = false
                backStack.add("group_chat_$groupId")
            }
        )
    }

    // Friend Invitation Sheet
    if (inviteEventId != null) {
        InviteFriendsSheet(
            eventId = inviteEventId!!,
            repository = repository,
            onDismiss = { inviteEventId = null }
        )
    }

    // Report Bottom Sheet
    if (reportTarget != null) {
        val (type, title) = reportTarget!!
        ReportBottomSheet(
            targetType = type,
            targetTitle = title,
            repository = repository,
            onDismiss = { reportTarget = null }
        )
    }

    // Guest Auth Prompt
    if (guestAuthPrompt != null) {
        GuestAuthBottomSheet(
            actionDescription = guestAuthPrompt!!,
            repository = repository,
            onDismiss = { repository.cancelGuestAuthPrompt() },
            onOpenLogin = {
                repository.cancelGuestAuthPrompt()
                repository.logout()
                authScreen = "login"
            },
            onOpenRegister = {
                repository.cancelGuestAuthPrompt()
                repository.logout()
                authScreen = "register"
            }
        )
    }
}
