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
import androidx.compose.material3.HorizontalDivider
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
    badgeLabel: String = "По твоим интересам"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(262.dp)
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
                        0.0f to Color(0x260B0B0F),
                        0.38f to Color(0x590B0B0F),
                        0.78f to Color(0xEB0B0B0F),
                        1.0f to VryvBg
                    )
                )
        )

        // Top tags + Save button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                VryvTag(
                    text = event.genres.firstOrNull() ?: "Электроника",
                    style = VryvTagStyle.PINK
                )
                VryvTag(
                    text = badgeLabel,
                    style = VryvTagStyle.DARK
                )
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (event.isBookmarked) VryvPink else Color(0x7014141A))
                    .border(1.dp, Color(0x24FFFFFF), CircleShape)
                    .clickable { onSaveToggle() }
                    .testTag("bookmark_hero_${event.id}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = VryvIcons.Save,
                    contentDescription = "Сохранить",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = event.title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                lineHeight = 25.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${event.dateDisplay}, ${event.timeDisplay} · ${event.venueName}, ${event.address}",
                color = Color(0xFFD4D4DE),
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(
                color = Color(0x14FFFFFF),
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp, bottom = 10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VryvAvatarStack(
                        avatars = listOf(R.drawable.av1, R.drawable.av2, R.drawable.av3),
                        size = 24.dp
                    )
                    Text(
                        text = "${event.goingCount} идут · 4 друга",
                        color = Color(0xFFC8C8D4),
                        fontFamily = InterFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = event.priceString,
                    color = if (event.priceByn == 0.0) VryvLime else VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.4).sp
                )
            }
        }
    }
}

@Composable
fun EventCardFeatured(
    event: VryvEvent,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(228.dp)
            .height(268.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(VryvCard)
            .border(1.dp, VryvLine, RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .testTag("event_featured_${event.id}")
    ) {
        Image(
            painter = painterResource(id = event.coverRes),
            contentDescription = event.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay matching prototype
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0x0D0B0B0F),
                        0.45f to Color(0x400B0B0F),
                        0.85f to Color(0xF00B0B0F),
                        1.0f to VryvBg
                    )
                )
        )

        // Top row: Tag + Bookmark button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            VryvTag(
                text = if (event.priceByn == 0.0) "Бесплатно" else (event.genres.firstOrNull() ?: "Событие"),
                style = if (event.priceByn == 0.0) VryvTagStyle.LIME else VryvTagStyle.DARK
            )

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(if (event.isBookmarked) VryvPink else Color(0x7014141A))
                    .border(1.dp, Color(0x24FFFFFF), CircleShape)
                    .clickable { onSaveToggle() }
                    .testTag("bookmark_${event.id}"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = VryvIcons.Save,
                    contentDescription = "Сохранить",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // Bottom info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(
                text = event.title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.3).sp,
                lineHeight = 20.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${event.dateDisplay}, ${event.timeDisplay} · ${event.venueName}",
                color = Color(0xFFC8C8D2),
                fontFamily = InterFontFamily,
                fontSize = 12.5.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = event.priceString,
                    color = if (event.priceByn == 0.0) VryvLime else VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${event.goingCount} идут",
                    color = VryvTextMuted,
                    fontFamily = InterFontFamily,
                    fontSize = 11.5.sp
                )
            }
        }
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
                    imageVector = VryvIcons.Save,
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
