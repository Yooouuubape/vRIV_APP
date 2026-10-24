package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.data.EventStatus
import com.example.data.VryvEvent
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTagBg
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EventCardFeatured(
    event: VryvEvent,
    onClick: () -> Unit,
    onSaveToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(280.dp)
            .height(340.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(VryvCard)
            .clickable { onClick() }
            .testTag("event_featured_${event.id}")
    ) {
        Image(
            painter = painterResource(id = event.coverRes),
            contentDescription = event.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0x800B0B0F),
                            Color(0xFA0B0B0F)
                        ),
                        startY = 140f
                    )
                )
        )

        // Bookmark button top-right
        IconButton(
            onClick = onSaveToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0x66000000))
                .testTag("bookmark_${event.id}")
        ) {
            Icon(
                imageVector = if (event.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "Сохранить",
                tint = if (event.isBookmarked) VryvPink else Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        // Price badge top-left
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xCC1A1A20))
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(
                text = event.priceString,
                color = if (event.priceByn == 0.0) VryvLime else VryvTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Bottom info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Genre tags
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                event.genres.take(2).forEach { genre ->
                    Text(
                        text = genre,
                        color = VryvPink,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0x33FF1D8E))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Text(
                text = event.title,
                color = VryvTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${event.dateDisplay} · ${event.timeDisplay}",
                color = VryvTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "${event.venueName} · ${event.address}",
                color = VryvTextMuted,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(id = event.coverRes),
                contentDescription = event.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color(0x66000000))
                    .clickable { onSaveToggle() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (event.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = if (event.isBookmarked) VryvPink else Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            if (event.status != EventStatus.PUBLISHED) {
                StatusBadge(
                    status = event.status,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = event.title,
            color = VryvTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "${event.dateDisplay} · ${event.priceString}",
            color = VryvTextSecondary,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}

@Composable
fun StatusBadge(
    status: EventStatus,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status) {
        EventStatus.PUBLISHED -> Color(0x33C7FF28) to VryvLime
        EventStatus.PLANNED -> Color(0x33FF1D8E) to VryvPink
        EventStatus.MODERATION -> Color(0x33FFC107) to Color(0xFFFFC107)
        EventStatus.DRAFT -> Color(0x339696A3) to VryvTextSecondary
        EventStatus.CANCELLED -> Color(0x33FF3D00) to Color(0xFFFF3D00)
        EventStatus.RESCHEDULED -> Color(0x3300E5FF) to Color(0xFF00E5FF)
        EventStatus.COMPLETED -> Color(0x339696A3) to VryvTextSecondary
        EventStatus.FULL -> Color(0x33E040FB) to Color(0xFFE040FB)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
