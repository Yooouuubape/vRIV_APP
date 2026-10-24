package com.example.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.EventStatus
import com.example.data.VryvEvent
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary

@Composable
fun EventCardHero(
    event: VryvEvent,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier,
    badgeLabel: String = "Совпадение ${event.matchPercent}%"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(28.dp))
            .clickable { onClick() }
            .testTag("event_hero_${event.id}")
    ) {
        Image(
            painter = painterResource(id = event.coverRes),
            contentDescription = event.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0x1A0B0B0F),
                        0.42f to Color(0x400B0B0F),
                        0.76f to Color(0xE00B0B0F),
                        1.0f to Color(0xF80B0B0F)
                    )
                )
        )

        // Top row: Date/Time dark pill on left + Heart glass button on right (Screenshot 1)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(0xA60B0B0F))
                    .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(15.dp))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = event.pillTimeText,
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0x8814141A))
                    .border(1.dp, Color(0x24FFFFFF), CircleShape)
                    .clickable { onSaveToggle() }
                    .testTag("bookmark_hero_${event.id}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (event.isBookmarked) VryvIcons.HeartFill else VryvIcons.Heart,
                    contentDescription = "В избранное",
                    tint = if (event.isBookmarked) VryvPink else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom overlay matching Screenshot 1 1-to-1
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Lime match badge: "Совпадение 96%"
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(VryvLime)
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = badgeLabel,
                    color = VryvBg,
                    fontFamily = InterFontFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.2).sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = event.title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.7).sp,
                lineHeight = 29.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${event.venueName} · ${event.address}",
                    color = VryvTextMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 13.5.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    VryvAvatarStack(
                        avatars = listOf(R.drawable.av1, R.drawable.av2, R.drawable.av3),
                        size = 26.dp
                    )
                    Text(
                        text = "+${(event.goingCount - 3).coerceAtLeast(128)}",
                        color = VryvTextPrimary,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Portrait card used in the "Для тебя" horizontal carousel (Screenshots 1 & 2):
 * 158dp × 200dp image with top-left dark date pill, plus Title and Price/Distance below image.
 */
@Composable
fun EventCardFeatured(
    event: VryvEvent,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(158.dp)
            .clickable { onClick() }
            .testTag("event_featured_${event.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(VryvCard)
                .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(id = event.coverRes),
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Top-left dark translucent date/time pill ("Пт · 20:00", "Сб · 19:30", "До 25 окт")
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .height(26.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color(0xA60B0B0F))
                    .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(13.dp))
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = event.pillTimeText,
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = event.title,
            color = VryvTextPrimary,
            fontFamily = InterFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(3.dp))

        val subText = if (event.priceByn == 0.0) {
            "Бесплатно"
        } else {
            "${event.priceString} · ${event.distanceText}"
        }

        Text(
            text = subText,
            color = VryvTextMuted,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Horizontal compact card used in "Прямо сейчас" section (Screenshot 2):
 * 84dp square rounded photo on left + title + "До 23:00 · 600 м" + dark tags ("Еда", "0 BYN").
 */
@Composable
fun EventCardLiveRow(
    event: VryvEvent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = event.coverRes),
            contentDescription = event.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${event.pillTimeText} · ${event.distanceText}",
                color = VryvTextMuted,
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val primaryTag = event.genres.firstOrNull() ?: "Еда"
                val priceTag = if (event.priceByn == 0.0) "0 BYN" else event.priceString

                listOf(primaryTag, priceTag).forEach { tagText ->
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF26262E))
                            .padding(horizontal = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tagText,
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

/**
 * Wide landscape card used in "На выходных" horizontal carousel (Screenshot 2):
 * 238dp × 140dp image with top-left dark date pill, plus Title and Venue · Price below image.
 */
@Composable
fun EventCardWide(
    event: VryvEvent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(238.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(VryvCard)
                .border(1.dp, VryvLine, RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(id = event.coverRes),
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .height(26.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color(0xA60B0B0F))
                    .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(13.dp))
                    .padding(horizontal = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = event.pillTimeText,
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = event.title,
            color = VryvTextPrimary,
            fontFamily = InterFontFamily,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-0.3).sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "${event.venueName} · ${event.priceString}",
            color = VryvTextMuted,
            fontFamily = InterFontFamily,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun EventCardGrid(
    event: VryvEvent,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag("event_grid_${event.id}")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(152.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(VryvCard)
        ) {
            Image(
                painter = painterResource(id = event.coverRes),
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.4f to Color.Transparent,
                            1.0f to Color(0xCC0B0B0F)
                        )
                    )
            )

            VryvTag(
                text = if (event.priceByn == 0.0) "Бесплатно" else (event.genres.firstOrNull() ?: "Событие"),
                style = if (event.priceByn == 0.0) VryvTagStyle.LIME else VryvTagStyle.DARK,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (event.isBookmarked) VryvPink else Color(0x7014141A))
                    .clickable { onSaveToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = VryvIcons.Heart,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
            }

            if (event.status != EventStatus.PUBLISHED) {
                StatusBadge(
                    status = event.status,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = event.title,
            color = VryvTextPrimary,
            fontFamily = InterFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "${event.dateDisplay}, ${event.timeDisplay} · ${event.priceString}",
            color = VryvTextMuted,
            fontFamily = InterFontFamily,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun StatusBadge(
    status: EventStatus,
    modifier: Modifier = Modifier
) {
    val style = when (status) {
        EventStatus.PUBLISHED -> VryvStatusStyle.OK
        EventStatus.PLANNED -> VryvStatusStyle.INFO
        EventStatus.MODERATION -> VryvStatusStyle.WARN
        EventStatus.DRAFT -> VryvStatusStyle.MUTED
        EventStatus.CANCELLED -> VryvStatusStyle.ERR
        EventStatus.RESCHEDULED -> VryvStatusStyle.WARN
        EventStatus.COMPLETED -> VryvStatusStyle.MUTED
        EventStatus.FULL -> VryvStatusStyle.PINK
    }

    VryvStatusPill(
        text = status.label,
        style = style,
        modifier = modifier
    )
}
