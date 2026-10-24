package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class VryvRepository private constructor() {

    private var sharedPreferences: SharedPreferences? = null

    // Multi-user database of accounts
    private val _allUsers = MutableStateFlow(MockDataProvider.getDemoUsers())
    val allUsers: StateFlow<List<VryvUser>> = _allUsers.asStateFlow()

    private val _currentUser = MutableStateFlow(MockDataProvider.getInitialUser())
    val currentUser: StateFlow<VryvUser> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isGuest = MutableStateFlow(false)
    val isGuest: StateFlow<Boolean> = _isGuest.asStateFlow()

    private val _onboardingCompleted = MutableStateFlow(true)
    val onboardingCompleted: StateFlow<Boolean> = _onboardingCompleted.asStateFlow()

    private val _isBusinessMode = MutableStateFlow(false)
    val isBusinessMode: StateFlow<Boolean> = _isBusinessMode.asStateFlow()

    private val _events = MutableStateFlow(MockDataProvider.getInitialEvents())
    val events: StateFlow<List<VryvEvent>> = _events.asStateFlow()

    private val _groups = MutableStateFlow(MockDataProvider.getInitialGroups())
    val groups: StateFlow<List<CompanyGroup>> = _groups.asStateFlow()

    private val _companions = MutableStateFlow(MockDataProvider.getInitialCompanionUsers())
    val companions: StateFlow<List<CompanionUser>> = _companions.asStateFlow()

    fun toggleCompanionRequest(companionId: String) {
        if (_isGuest.value || !_isLoggedIn.value) {
            promptGuestAuth("Поиск компании доступен авторизованным пользователям")
            return
        }

        _companions.update { list ->
            list.map { c ->
                if (c.id == companionId) {
                    val newSent = !c.isRequestSent
                    showToast(if (newSent) "Запрос отправлен ${c.name}" else "Запрос отозван")
                    c.copy(isRequestSent = newSent)
                } else c
            }
        }
    }

    private val _directDialogs = MutableStateFlow(MockDataProvider.getInitialDirectDialogs(_companions.value))
    val directDialogs: StateFlow<List<DirectDialog>> = _directDialogs.asStateFlow()

    private val _notifications = MutableStateFlow(MockDataProvider.getInitialNotifications())
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    private val _businessVenue = MutableStateFlow(BusinessVenue())
    val businessVenue: StateFlow<BusinessVenue> = _businessVenue.asStateFlow()

    private val _venueApplications = MutableStateFlow<List<VenueApplication>>(emptyList())
    val venueApplications: StateFlow<List<VenueApplication>> = _venueApplications.asStateFlow()

    private val _reports = MutableStateFlow<List<ReportRecord>>(emptyList())
    val reports: StateFlow<List<ReportRecord>> = _reports.asStateFlow()

    private val _businessInquiries = MutableStateFlow(MockDataProvider.getInitialBusinessInquiries())
    val businessInquiries: StateFlow<List<BusinessInquiryMessage>> = _businessInquiries.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _guestAuthPromptAction = MutableStateFlow<String?>(null)
    val guestAuthPromptAction: StateFlow<String?> = _guestAuthPromptAction.asStateFlow()

    private val _businessMetricsPeriod = MutableStateFlow("Сегодня")
    val businessMetricsPeriod: StateFlow<String> = _businessMetricsPeriod.asStateFlow()

    private val _businessMetricsMap = MutableStateFlow(MockDataProvider.getInitialBusinessMetrics())
    val businessMetricsMap: StateFlow<Map<String, BusinessMetricSet>> = _businessMetricsMap.asStateFlow()

    fun initPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("vryv_state_prefs", Context.MODE_PRIVATE)
        loadPersistentState()
    }

    private fun loadPersistentState() {
        val prefs = sharedPreferences ?: return
        val loggedIn = prefs.getBoolean("is_logged_in", false)
        val guest = prefs.getBoolean("is_guest", false)
        val onboarded = prefs.getBoolean("onboarding_completed", true)
        val activeUserId = prefs.getString("current_user_id", "u_max")

        val user = _allUsers.value.find { it.id == activeUserId } ?: MockDataProvider.getInitialUser()
        _currentUser.value = user
        _isLoggedIn.value = loggedIn
        _isGuest.value = guest
        _onboardingCompleted.value = onboarded
    }

    private fun savePersistentState() {
        val prefs = sharedPreferences?.edit() ?: return
        prefs.putBoolean("is_logged_in", _isLoggedIn.value)
        prefs.putBoolean("is_guest", _isGuest.value)
        prefs.putBoolean("onboarding_completed", _onboardingCompleted.value)
        prefs.putString("current_user_id", _currentUser.value.id)
        prefs.apply()
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun promptGuestAuth(actionDescription: String) {
        _guestAuthPromptAction.value = actionDescription
    }

    fun cancelGuestAuthPrompt() {
        _guestAuthPromptAction.value = null
    }

    /**
     * Checks if current action is permitted. If in guest mode, prompts auth modal.
     */
    fun requireAuth(actionDesc: String, onAuthorized: () -> Unit): Boolean {
        if (_isLoggedIn.value && !_isGuest.value) {
            onAuthorized()
            return true
        } else {
            promptGuestAuth(actionDesc)
            return false
        }
    }

    // --- Authentication & Session ---

    fun login(email: String, password: String): Pair<Boolean, String> {
        val user = _allUsers.value.find { it.email.equals(email.trim(), ignoreCase = true) }
            ?: return Pair(false, "Пользователь с таким Email не найден")

        if (user.password != password.trim()) {
            return Pair(false, "Неверный пароль. Проверьте правильность ввода")
        }

        _currentUser.value = user
        _isLoggedIn.value = true
        _isGuest.value = false
        _onboardingCompleted.value = true
        _guestAuthPromptAction.value = null
        savePersistentState()
        showToast("Добро пожаловать, ${user.name}!")
        return Pair(true, "Успешный вход")
    }

    fun register(name: String, email: String, password: String, city: String): Pair<Boolean, String> {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Pair(false, "Заполните все обязательные поля")
        }
        if (_allUsers.value.any { it.email.equals(email.trim(), ignoreCase = true) }) {
            return Pair(false, "Пользователь с таким Email уже зарегистрирован")
        }

        val newUser = VryvUser(
            id = "u_${System.currentTimeMillis()}",
            name = name.trim(),
            username = email.substringBefore("@").trim(),
            email = email.trim(),
            password = password.trim(),
            avatarRes = R.drawable.av2,
            bio = "Новый пользователь ВРЫВ в $city",
            city = city,
            budget = "До 50 BYN",
            interests = listOf("Техно", "Хаус", "Стендап"),
            formatOfRest = listOf("С друзьями", "Новые знакомства"),
            friendsCount = 0,
            savedCount = 0,
            visitedCount = 0,
            venueId = null,
            isGuest = false
        )

        _allUsers.update { it + newUser }
        _currentUser.value = newUser
        return Pair(true, "Код подтверждения отправлен на $email (тестовый код: 2410)")
    }

    fun confirmEmailCode(code: String): Boolean {
        if (code.trim() == "2410") {
            _isLoggedIn.value = true
            _isGuest.value = false
            _onboardingCompleted.value = false // Start onboarding
            _guestAuthPromptAction.value = null
            savePersistentState()
            showToast("Email успешно подтверждён!")
            return true
        }
        return false
    }

    fun resetPassword(email: String, newPassword: String): Boolean {
        val user = _allUsers.value.find { it.email.equals(email.trim(), ignoreCase = true) } ?: return false
        val updated = user.copy(password = newPassword.trim())
        _allUsers.update { list -> list.map { if (it.id == user.id) updated else it } }
        showToast("Пароль успешно изменён! Войдите с новым паролем.")
        return true
    }

    fun startGuestSession() {
        _isLoggedIn.value = true
        _isGuest.value = true
        _onboardingCompleted.value = true
        _currentUser.value = VryvUser(
            id = "u_guest",
            name = "Гость",
            username = "guest",
            email = "guest@vryv.by",
            password = "",
            avatarRes = R.drawable.av2,
            bio = "Просмотр афиши в гостевом режиме",
            city = "Минск",
            venueId = null,
            isGuest = true
        )
        savePersistentState()
        showToast("Вход в гостевом режиме: доступен просмотр афиши")
    }

    fun switchAccount(userId: String) {
        val user = _allUsers.value.find { it.id == userId } ?: return
        _currentUser.value = user
        _isLoggedIn.value = true
        _isGuest.value = false
        _onboardingCompleted.value = true
        _isBusinessMode.value = false
        savePersistentState()
        showToast("Переключен аккаунт: ${user.name}")
    }

    fun finishOnboarding(interests: List<String>, budget: String, format: List<String>) {
        _currentUser.update {
            it.copy(interests = interests, budget = budget, formatOfRest = format)
        }
        _onboardingCompleted.value = true
        savePersistentState()
        showToast("Всё готово. Минск ждёт тебя!")
    }

    fun logout() {
        _isLoggedIn.value = false
        _isGuest.value = false
        _isBusinessMode.value = false
        savePersistentState()
        showToast("Выход из аккаунта")
    }

    // --- Role Switching & Business Venue Registration ---

    fun setBusinessMode(enabled: Boolean): Boolean {
        if (enabled) {
            val user = _currentUser.value
            if (user.isGuest) {
                promptGuestAuth("Для входа в бизнес-режим необходимо авторизоваться")
                return false
            }
            _isBusinessMode.value = true
            showToast("Переключено в «ВРЫВ для бизнеса»")
            return true
        } else {
            _isBusinessMode.value = false
            showToast("Возврат в личный профиль")
            return true
        }
    }

    fun submitVenueApplication(application: VenueApplication) {
        _venueApplications.update { list ->
            val filtered = list.filter { it.id != application.id }
            listOf(application.copy(status = VenueApplicationStatus.ON_MODERATION)) + filtered
        }
        showToast("Заявка на регистрацию «${application.venueName}» отправлена на модерацию!")
    }

    /**
     * Demo moderation simulator for reviewer/testing (no regular user self-approval).
     */
    fun simulateModeratorApplicationDecision(appId: String, approved: Boolean) {
        val app = _venueApplications.value.find { it.id == appId } ?: return
        val newStatus = if (approved) VenueApplicationStatus.APPROVED else VenueApplicationStatus.REJECTED
        _venueApplications.update { list ->
            list.map { if (it.id == appId) it.copy(status = newStatus) else it }
        }

        if (approved) {
            val newVenue = BusinessVenue(
                id = "v_${app.id}",
                ownerId = app.userId,
                name = app.venueName,
                category = app.category,
                address = app.address,
                description = "Новое проверенное заведение в Минске.",
                isVerified = true,
                phone = app.phone,
                telegram = app.telegram,
                schedule = app.schedule.ifBlank { "Пн–Вс 18:00–02:00" }
            )
            _businessVenue.value = newVenue
            _currentUser.update { it.copy(venueId = newVenue.id) }
            _allUsers.update { list ->
                list.map { if (it.id == app.userId) it.copy(venueId = newVenue.id) else it }
            }
            showToast("Демо: Заявка «${app.venueName}» одобрена модератором! Кабинет разблокирован.")
        } else {
            showToast("Демо: Заявка «${app.venueName}» отклонена модератором.")
        }
    }

    fun updateVenueInfo(name: String, address: String, schedule: String, phone: String, telegram: String) {
        _businessVenue.update {
            it.copy(name = name, address = address, schedule = schedule, phone = phone, telegram = telegram)
        }
        showToast("Данные заведения обновлены")
    }

    // --- Events Management & Isolation ---

    fun getVenueEvents(venueId: String): List<VryvEvent> {
        return _events.value.filter { it.venueId == venueId }
    }

    fun toggleSaveEvent(eventId: String) {
        if (_isGuest.value || !_isLoggedIn.value) {
            promptGuestAuth("Сохранение событий доступно авторизованным пользователям")
            return
        }

        var isNowSaved = false
        _events.update { list ->
            list.map { ev ->
                if (ev.id == eventId) {
                    isNowSaved = !ev.isBookmarked
                    ev.copy(isBookmarked = isNowSaved)
                } else ev
            }
        }
        val currentSaved = _events.value.count { it.isBookmarked }
        _currentUser.update { it.copy(savedCount = currentSaved) }
        showToast(if (isNowSaved) "Событие сохранено в закладки" else "Удалено из закладок")
    }

    fun toggleGoingEvent(eventId: String) {
        if (_isGuest.value || !_isLoggedIn.value) {
            promptGuestAuth("Добавление в планы доступно авторизованным пользователям")
            return
        }

        var isNowGoing = false
        _events.update { list ->
            list.map { ev ->
                if (ev.id == eventId) {
                    isNowGoing = !ev.isGoing
                    val newCount = if (isNowGoing) ev.goingCount + 1 else (ev.goingCount - 1).coerceAtLeast(0)
                    ev.copy(isGoing = isNowGoing, goingCount = newCount)
                } else ev
            }
        }
        showToast(if (isNowGoing) "Ты идёшь! Событие добавлено в планы" else "Решение изменено: не идёшь")
    }

    fun createBusinessEvent(
        title: String,
        description: String,
        category: String,
        genres: List<String>,
        dateDisplay: String,
        timeDisplay: String,
        priceByn: Double,
        ageLimit: String,
        coverRes: Int,
        isDraft: Boolean
    ): VryvEvent {
        val venue = _businessVenue.value
        val newEvent = VryvEvent(
            id = "e_${System.currentTimeMillis()}",
            venueId = venue.id,
            title = title,
            description = description,
            coverRes = coverRes,
            category = category,
            genres = genres,
            dateDisplay = dateDisplay,
            timeDisplay = timeDisplay,
            dateCategory = "TODAY",
            venueName = venue.name,
            address = venue.address,
            priceByn = priceByn,
            ageLimit = ageLimit,
            status = if (isDraft) EventStatus.DRAFT else EventStatus.MODERATION,
            isBookmarked = false,
            isGoing = false,
            goingCount = 0,
            canFindCompany = true,
            mapX = 0.53f,
            mapY = 0.50f,
            viewsCount = 0,
            ticketClicksCount = 0
        )
        _events.update { listOf(newEvent) + it }
        showToast(if (isDraft) "Черновик сохранён" else "Мероприятие отправлено на модерацию!")
        return newEvent
    }

    fun updateBusinessEvent(updatedEvent: VryvEvent) {
        _events.update { list ->
            list.map { if (it.id == updatedEvent.id) updatedEvent else it }
        }
        showToast("Событие «${updatedEvent.title}» обновлено")
    }

    fun duplicateBusinessEvent(eventId: String) {
        val event = _events.value.find { it.id == eventId } ?: return
        val copy = event.copy(
            id = "e_${System.currentTimeMillis()}",
            title = "${event.title} (Копия)",
            status = EventStatus.DRAFT,
            isGoing = false,
            isBookmarked = false,
            goingCount = 0,
            viewsCount = 0,
            ticketClicksCount = 0
        )
        _events.update { listOf(copy) + it }
        showToast("Создан дубликат в черновиках")
    }

    fun simulateEventApproval(eventId: String) {
        _events.update { list ->
            list.map { if (it.id == eventId) it.copy(status = EventStatus.PUBLISHED) else it }
        }
        val event = _events.value.find { it.id == eventId }
        showToast("Демо: Событие «${event?.title}» одобрено и опубликовано в афише!")
    }

    fun cancelBusinessEvent(eventId: String, reason: String) {
        val event = _events.value.find { it.id == eventId } ?: return
        _events.update { list ->
            list.map { ev ->
                if (ev.id == eventId) {
                    ev.copy(status = EventStatus.CANCELLED, cancellationReason = reason)
                } else ev
            }
        }

        // Notify interested users
        val notif = NotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            type = "event_change",
            title = "Событие отменено: ${event.title}",
            subtitle = "Причина: $reason. Планы обновлены.",
            time = "Только что",
            isRead = false,
            relatedEventId = eventId
        )
        _notifications.update { listOf(notif) + it }
        showToast("Мероприятие отменено. Гости оповещены в центре уведомлений.")
    }

    fun rescheduleBusinessEvent(eventId: String, newDate: String, newTime: String) {
        val event = _events.value.find { it.id == eventId } ?: return
        _events.update { list ->
            list.map { ev ->
                if (ev.id == eventId) {
                    ev.copy(
                        status = EventStatus.RESCHEDULED,
                        oldDateDisplay = ev.dateDisplay,
                        dateDisplay = newDate,
                        timeDisplay = newTime
                    )
                } else ev
            }
        }

        val notif = NotificationItem(
            id = "notif_${System.currentTimeMillis()}",
            type = "event_change",
            title = "Перенос события: ${event.title}",
            subtitle = "Новая дата: $newDate, $newTime. Подтвердите участие.",
            time = "Только что",
            isRead = false,
            relatedEventId = eventId
        )
        _notifications.update { listOf(notif) + it }
        showToast("Дата изменена. Уведомление отправлено гостям.")
    }

    // --- Groups & Social ---

    fun joinGroup(groupId: String): Boolean {
        if (_isGuest.value || !_isLoggedIn.value) {
            promptGuestAuth("Вступление в компанию доступно авторизованным пользователям")
            return false
        }

        val group = _groups.value.find { it.id == groupId } ?: return false
        val user = _currentUser.value

        // Prevent joining if already member or full
        if (group.memberIds.contains(user.id)) {
            showToast("Ты уже состоишь в этой компании")
            return false
        }
        if (group.currentMembersCount >= group.maxMembers) {
            showToast("В компании больше нет свободных мест")
            return false
        }

        if (group.isOpen) {
            _groups.update { list ->
                list.map { g ->
                    if (g.id == groupId) {
                        g.copy(
                            isUserJoined = true,
                            currentMembersCount = (g.currentMembersCount + 1).coerceAtMost(g.maxMembers),
                            memberIds = g.memberIds + user.id,
                            memberAvatars = g.memberAvatars + user.avatarRes
                        )
                    } else g
                }
            }
            showToast("Ты вступил в компанию «${group.title}»!")
            return true
        } else {
            // Closed group -> submit application
            val app = GroupApplication(
                id = "gapp_${System.currentTimeMillis()}",
                groupId = groupId,
                userId = user.id,
                userName = user.name,
                userAvatarRes = user.avatarRes,
                status = "PENDING",
                timestamp = "Только что"
            )
            _groups.update { list ->
                list.map { g ->
                    if (g.id == groupId) {
                        g.copy(
                            isUserPending = true,
                            pendingApplications = g.pendingApplications + app
                        )
                    } else g
                }
            }

            // Send notification to group creator
            val notif = NotificationItem(
                id = "notif_${System.currentTimeMillis()}",
                type = "group",
                title = "Заявка в группу «${group.title}»",
                subtitle = "${user.name} хочет присоединиться к твоей компании",
                time = "Только что",
                isRead = false,
                relatedGroupId = groupId,
                relatedUserId = user.id
            )
            _notifications.update { listOf(notif) + it }

            showToast("Заявка на вступление отправлена создателю (${group.creatorName})")
            return true
        }
    }

    fun leaveGroup(groupId: String) {
        val user = _currentUser.value
        _groups.update { list ->
            list.map { g ->
                if (g.id == groupId) {
                    g.copy(
                        isUserJoined = false,
                        isUserPending = false,
                        currentMembersCount = (g.currentMembersCount - 1).coerceAtLeast(1),
                        memberIds = g.memberIds.filter { it != user.id },
                        memberAvatars = g.memberAvatars.filter { it != user.avatarRes }
                    )
                } else g
            }
        }
        showToast("Ты вышел из компании")
    }

    fun acceptGroupApplication(groupId: String, applicantId: String) {
        val applicant = _allUsers.value.find { it.id == applicantId } ?: return
        _groups.update { list ->
            list.map { g ->
                if (g.id == groupId) {
                    val updatedApps = g.pendingApplications.map {
                        if (it.userId == applicantId) it.copy(status = "ACCEPTED") else it
                    }
                    g.copy(
                        currentMembersCount = (g.currentMembersCount + 1).coerceAtMost(g.maxMembers),
                        memberIds = g.memberIds + applicant.id,
                        memberAvatars = g.memberAvatars + applicant.avatarRes,
                        pendingApplications = updatedApps
                    )
                } else g
            }
        }
        showToast("Заявка пользователя ${applicant.name} принята!")
    }

    fun rejectGroupApplication(groupId: String, applicantId: String) {
        _groups.update { list ->
            list.map { g ->
                if (g.id == groupId) {
                    g.copy(
                        pendingApplications = g.pendingApplications.filter { it.userId != applicantId }
                    )
                } else g
            }
        }
        showToast("Заявка отклонена")
    }

    fun createGroup(
        title: String,
        eventId: String,
        meetingTime: String,
        meetingPlace: String,
        maxMembers: Int,
        isOpen: Boolean
    ): CompanyGroup {
        val user = _currentUser.value
        val event = _events.value.find { it.id == eventId }
        val newGroup = CompanyGroup(
            id = "g_${System.currentTimeMillis()}",
            eventId = eventId,
            eventTitle = event?.title ?: "Мероприятие",
            title = title,
            meetingTime = meetingTime,
            meetingPlace = meetingPlace,
            maxMembers = maxMembers,
            currentMembersCount = 1,
            isOpen = isOpen,
            creatorId = user.id,
            creatorName = user.name,
            isUserJoined = true,
            isUserCreator = true,
            memberIds = listOf(user.id),
            memberAvatars = listOf(user.avatarRes),
            chatMessages = listOf(
                ChatMessage(
                    id = "msg_${System.currentTimeMillis()}",
                    senderId = user.id,
                    senderName = user.name,
                    text = "Всем привет! Создал компанию для похода на ${event?.title ?: "событие"}. Встречаемся: $meetingPlace в $meetingTime.",
                    time = "Только что",
                    isMe = true
                )
            )
        )
        _groups.update { listOf(newGroup) + it }
        showToast("Компания «$title» успешно создана!")
        return newGroup
    }

    fun sendGroupMessage(groupId: String, text: String) {
        if (text.isBlank()) return
        val user = _currentUser.value
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderId = user.id,
            senderName = user.name,
            text = text.trim(),
            time = "Только что",
            isMe = true
        )
        _groups.update { list ->
            list.map { g ->
                if (g.id == groupId) {
                    g.copy(chatMessages = g.chatMessages + newMsg)
                } else g
            }
        }
    }

    // --- Direct Messages ---

    fun sendDirectMessage(dialogId: String, text: String) {
        if (text.isBlank()) return
        val user = _currentUser.value
        val newMsg = ChatMessage(
            id = "dm_${System.currentTimeMillis()}",
            senderId = user.id,
            senderName = user.name,
            text = text.trim(),
            time = "Только что",
            isMe = true
        )
        _directDialogs.update { list ->
            list.map { d ->
                if (d.id == dialogId) {
                    d.copy(
                        lastMessage = text.trim(),
                        time = "Только что",
                        unreadCount = 0,
                        messages = d.messages + newMsg
                    )
                } else d
            }
        }
    }

    // --- Friend Invitations ---

    fun inviteFriendsToEvent(eventId: String, selectedUserIds: List<String>) {
        if (_isGuest.value || !_isLoggedIn.value) {
            promptGuestAuth("Приглашение друзей доступно авторизованным пользователям")
            return
        }

        val event = _events.value.find { it.id == eventId } ?: return
        val sender = _currentUser.value

        selectedUserIds.forEach { friendId ->
            val inviteNotif = NotificationItem(
                id = "inv_notif_${System.currentTimeMillis()}_$friendId",
                type = "invite",
                title = "Приглашение от ${sender.name}",
                subtitle = "${sender.name} зовёт тебя на «${event.title}»",
                time = "Только что",
                isRead = false,
                relatedEventId = eventId,
                relatedUserId = sender.id,
                inviteId = "inv_${System.currentTimeMillis()}_$friendId"
            )
            _notifications.update { listOf(inviteNotif) + it }
        }

        showToast("Приглашение на «${event.title}» отправлено (${selectedUserIds.size})")
    }

    fun respondToInvite(notificationId: String, accept: Boolean) {
        val notif = _notifications.value.find { it.id == notificationId }
        val eventId = notif?.relatedEventId

        if (eventId != null && accept) {
            _events.update { list ->
                list.map { ev ->
                    if (ev.id == eventId) {
                        ev.copy(isGoing = true, goingCount = ev.goingCount + 1)
                    } else ev
                }
            }
        }

        _notifications.update { list ->
            list.filter { it.id != notificationId }
        }

        showToast(if (accept) "Приглашение принято! Событие добавлено в планы" else "Приглашение отклонено")
    }

    // --- Reports & Moderation ---

    fun submitReport(
        targetType: String,
        targetId: String,
        targetTitle: String,
        reason: String,
        comment: String
    ): ReportRecord {
        val report = ReportRecord(
            id = "rep_${System.currentTimeMillis()}",
            reporterId = _currentUser.value.id,
            targetType = targetType,
            targetId = targetId,
            targetTitle = targetTitle,
            reason = reason,
            comment = comment,
            date = "Сегодня",
            status = "На рассмотрении"
        )
        _reports.update { listOf(report) + it }
        showToast("Жалоба на «$targetTitle» зарегистрирована и отправлена модераторам")
        return report
    }

    fun blockUser(userId: String) {
        _currentUser.update {
            it.copy(blockedUserIds = it.blockedUserIds + userId)
        }
        _companions.update { list ->
            list.map { u -> if (u.id == userId) u.copy(isBlocked = true) else u }
        }
        _directDialogs.update { list ->
            list.filter { it.otherUser.id != userId }
        }
        showToast("Пользователь заблокирован. Диалог скрыт.")
    }

    // --- Business Inquiries ---

    fun replyBusinessInquiry(inquiryId: String, text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = "binq_msg_${System.currentTimeMillis()}",
            senderId = _businessVenue.value.id,
            senderName = _businessVenue.value.name,
            text = text.trim(),
            time = "Только что",
            isMe = true
        )
        _businessInquiries.update { list ->
            list.map { inq ->
                if (inq.id == inquiryId) {
                    inq.copy(
                        lastMessage = text.trim(),
                        time = "Только что",
                        unreadCount = 0,
                        messages = inq.messages + newMsg
                    )
                } else inq
            }
        }
        showToast("Ответ гостю отправлен")
    }

    // --- Profile & Preferences ---

    fun updateUserProfile(name: String, bio: String, privacy: String) {
        _currentUser.update {
            it.copy(name = name, bio = bio, privacyPlansVisibility = privacy)
        }
        _allUsers.update { list ->
            list.map { if (it.id == _currentUser.value.id) _currentUser.value else it }
        }
        showToast("Профиль обновлен")
    }

    fun updateUserPreferences(interests: List<String>, budget: String, formats: List<String>) {
        _currentUser.update {
            it.copy(interests = interests, budget = budget, formatOfRest = formats)
        }
        showToast("Настройки рекомендаций сохранены")
    }

    fun setMetricsPeriod(period: String) {
        _businessMetricsPeriod.value = period
    }

    fun markAllNotificationsRead() {
        _notifications.update { list ->
            list.map { it.copy(isRead = true) }
        }
        showToast("Все уведомления прочитаны")
    }

    fun resetToDefaults() {
        _allUsers.value = MockDataProvider.getDemoUsers()
        _currentUser.value = MockDataProvider.getInitialUser()
        _events.value = MockDataProvider.getInitialEvents()
        _groups.value = MockDataProvider.getInitialGroups()
        _companions.value = MockDataProvider.getInitialCompanionUsers()
        _directDialogs.value = MockDataProvider.getInitialDirectDialogs(_companions.value)
        _notifications.value = MockDataProvider.getInitialNotifications()
        _businessVenue.value = BusinessVenue()
        _venueApplications.value = emptyList()
        _reports.value = emptyList()
        _businessInquiries.value = MockDataProvider.getInitialBusinessInquiries()
        _isLoggedIn.value = false
        _isGuest.value = false
        _onboardingCompleted.value = true
        _isBusinessMode.value = false
        _guestAuthPromptAction.value = null
        savePersistentState()
        showToast("Демо-данные сброшены в исходное состояние")
    }

    companion object {
        val instance by lazy { VryvRepository() }
    }
}
