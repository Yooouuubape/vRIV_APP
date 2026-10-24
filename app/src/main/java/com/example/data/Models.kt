package com.example.data

import androidx.annotation.DrawableRes

enum class EventStatus(val label: String) {
    DRAFT("Черновик"),
    MODERATION("На модерации"),
    PUBLISHED("Опубликовано"),
    PLANNED("Запланировано"),
    CANCELLED("Отменено"),
    RESCHEDULED("Перенесено"),
    COMPLETED("Завершено"),
    FULL("Нет мест")
}

enum class VenueApplicationStatus(val label: String) {
    DRAFT("Черновик"),
    ON_MODERATION("На проверке"),
    NEEDS_INFO("Нужны уточнения"),
    APPROVED("Подтверждено"),
    REJECTED("Отклонено")
}

data class VryvUser(
    val id: String = "u_max",
    val name: String = "Максим Салуков",
    val username: String = "max.vryv",
    val email: String = "max.vryv@gmail.com",
    val password: String = "TechnoMinsk2026",
    @DrawableRes val avatarRes: Int,
    val bio: String = "Техно по пятницам, стендап по средам. Ищу компанию на Warehouse.",
    val city: String = "Минск",
    val budget: String = "До 50 BYN",
    val interests: List<String> = listOf("Техно", "Хаус", "Стендап", "Бары", "Концерты"),
    val formatOfRest: List<String> = listOf("С друзьями", "Новые знакомства", "Спонтанные события"),
    val friendsCount: Int = 86,
    val savedCount: Int = 17,
    val visitedCount: Int = 48,
    val privacyPlansVisibility: String = "Друзья", // Только я / Друзья / Все
    val venueId: String? = "v_loft13",
    val isGuest: Boolean = false,
    val blockedUserIds: List<String> = emptyList()
) {
    val isOwner: Boolean
        get() = venueId != null

    val preferredBudget: String
        get() = budget

    val preferredFormats: List<String>
        get() = formatOfRest
}

data class VryvEvent(
    val id: String,
    val venueId: String = "v_loft13",
    val title: String,
    val description: String,
    @DrawableRes val coverRes: Int,
    val category: String,
    val genres: List<String>,
    val dateDisplay: String,
    val timeDisplay: String,
    val dateCategory: String = "TODAY", // TODAY, TOMORROW, WEEKEND, LATER
    val venueName: String,
    val address: String,
    val priceByn: Double, // 0.0 means "Бесплатно"
    val ageLimit: String = "18+",
    val status: EventStatus = EventStatus.PUBLISHED,
    val oldDateDisplay: String? = null,
    val isBookmarked: Boolean = false,
    val isGoing: Boolean = false,
    val goingCount: Int = 142,
    val canFindCompany: Boolean = true,
    val mapX: Float = 0.5f, // relative to Minsk map (0.0 to 1.0)
    val mapY: Float = 0.5f,
    val cancellationReason: String? = null,
    val viewsCount: Int = 340,
    val ticketClicksCount: Int = 42,
    val matchPercent: Int = 96,
    val shortDatePill: String = "",
    val distanceText: String = "1,2 км",
    val fullDateTitle: String = "Суббота, 24 октября",
    val doorsInfo: String = "20:00 — 23:30 · двери в 19:00",
    val ticketsLeftText: String = "Осталось 38 билетов",
    val friendsGoingCount: Int = 6,
    val groupsLookingCount: Int = 3
) {
    val priceString: String
        get() = if (priceByn == 0.0) "Бесплатно" else "от ${priceByn.toInt()} BYN"

    val pillTimeText: String
        get() = shortDatePill.ifBlank { "${dateDisplay.substringBefore(",")} · ${timeDisplay.substringBefore(" ")}" }
}

data class GroupApplication(
    val id: String,
    val groupId: String,
    val userId: String,
    val userName: String,
    @DrawableRes val userAvatarRes: Int,
    val status: String = "PENDING", // PENDING, ACCEPTED, REJECTED
    val timestamp: String = "Только что"
)

data class CompanyGroup(
    val id: String,
    val eventId: String,
    val eventTitle: String,
    val title: String,
    val meetingTime: String,
    val meetingPlace: String,
    val maxMembers: Int,
    val currentMembersCount: Int,
    val isOpen: Boolean,
    val creatorId: String = "u_max",
    val creatorName: String = "Максим",
    val isUserJoined: Boolean = false,
    val isUserPending: Boolean = false,
    val isUserCreator: Boolean = false,
    val memberAvatars: List<Int> = emptyList(),
    val memberIds: List<String> = emptyList(),
    val pendingApplications: List<GroupApplication> = emptyList(),
    val chatMessages: List<ChatMessage> = emptyList()
)

data class CompanionUser(
    val id: String,
    val name: String,
    val username: String,
    @DrawableRes val avatarRes: Int,
    val bio: String,
    val sharedTags: List<String>,
    val isRequestSent: Boolean = false,
    val isBlocked: Boolean = false,
    val isFriend: Boolean = false,
    val isOnline: Boolean = true
)

data class ChatMessage(
    val id: String,
    val senderId: String,
    val senderName: String,
    val text: String,
    val time: String,
    val isMe: Boolean
)

data class DirectDialog(
    val id: String,
    val otherUser: CompanionUser,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val messages: List<ChatMessage> = emptyList()
)

data class FriendInvite(
    val id: String,
    val senderId: String,
    val senderName: String,
    @DrawableRes val senderAvatarRes: Int,
    val receiverId: String,
    val eventId: String,
    val eventTitle: String,
    val status: String = "SENT", // SENT, ACCEPTED, DECLINED
    val time: String = "Только что"
)

data class NotificationItem(
    val id: String,
    val type: String, // "invite", "message", "friend", "group", "event_change"
    val title: String,
    val subtitle: String,
    val time: String,
    val isRead: Boolean = false,
    val relatedEventId: String? = null,
    val relatedGroupId: String? = null,
    val relatedUserId: String? = null,
    val inviteId: String? = null
)

data class ReportRecord(
    val id: String,
    val reporterId: String,
    val targetType: String, // "Пользователь", "Группа", "Событие", "Заведение"
    val targetId: String,
    val targetTitle: String,
    val reason: String,
    val comment: String,
    val date: String,
    val status: String = "На рассмотрении"
)

data class BusinessVenue(
    val id: String = "v_loft13",
    val ownerId: String = "u_max",
    val name: String = "Loft 13",
    val category: String = "Бар · Немига",
    val address: String = "Минск, ул. Зыбицкая, 6",
    val description: String = "Атмосферный лофт-бар в историческом центре. Коктейли, диджеи и вечеринки каждые выходные.",
    val isVerified: Boolean = true,
    val phone: String = "+375 (29) 613-13-13",
    val telegram: String = "@loft13minsk",
    val schedule: String = "Пн–Чт 18:00–02:00, Пт–Сб 18:00–06:00, Вс 18:00–03:00"
)

data class VenueApplication(
    val id: String,
    val userId: String,
    val venueName: String,
    val category: String,
    val city: String = "Минск",
    val address: String,
    val mapX: Float = 0.52f,
    val mapY: Float = 0.51f,
    val genres: List<String> = emptyList(),
    val features: List<String> = emptyList(),
    val schedule: String = "",
    val contactName: String = "",
    val email: String = "",
    val phone: String = "",
    val telegram: String = "",
    val website: String = "",
    val verificationMethod: String = "Выписка из ЕГР / Договор аренды",
    val status: VenueApplicationStatus = VenueApplicationStatus.ON_MODERATION,
    val rejectionReason: String? = null,
    val submissionDate: String = "Сегодня"
)

data class BusinessMetricSet(
    val period: String, // "Сегодня", "7 дней", "30 дней"
    val views: Int,
    val saves: Int,
    val going: Int,
    val ticketClicks: Int,
    val conversionRate: String,
    val newGuests: Int
)

data class BusinessInquiryMessage(
    val id: String,
    val venueId: String,
    val userId: String,
    val userName: String,
    @DrawableRes val userAvatarRes: Int,
    val subject: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val messages: List<ChatMessage> = emptyList()
)
