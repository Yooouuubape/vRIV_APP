package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvCard
import com.example.ui.theme.VryvCardElevated
import com.example.ui.theme.VryvDisabledBg
import com.example.ui.theme.VryvDisabledText
import com.example.ui.theme.VryvGlassBg
import com.example.ui.theme.VryvIconTileBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvLine
import com.example.ui.theme.VryvOverlay
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvPinkLight
import com.example.ui.theme.VryvSheetBg
import com.example.ui.theme.VryvTextMuted
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary

/**
 * Exact 1:1 SVG Vector Icons translated directly from prototype/icons.js and prototype/s2-lib.js
 * (24x24 viewport, 1.8dp strokeWidth, Round cap & join).
 */
object VryvIcons {

    private fun strokeIcon(name: String, block: androidx.compose.ui.graphics.vector.ImageVector.Builder.() -> Unit): ImageVector {
        return ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply(block).build()
    }

    val Home: ImageVector by lazy {
        strokeIcon("Home") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3.5f, 10.2f)
                lineTo(12f, 3.5f)
                lineTo(20.5f, 10.2f)
                verticalLineTo(19f)
                arcTo(1.5f, 1.5f, 0f, false, true, 19f, 20.5f)
                horizontalLineToRelative(-4.2f)
                verticalLineToRelative(-5.8f)
                horizontalLineTo(9.2f)
                verticalLineToRelative(5.8f)
                horizontalLineTo(5f)
                arcTo(1.5f, 1.5f, 0f, false, true, 3.5f, 19f)
                close()
            }
        }
    }

    val Cards: ImageVector by lazy {
        strokeIcon("Cards") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // M8.5 3.5h8a3 3 0 0 1 3 3v10
                moveTo(8.5f, 3.5f)
                horizontalLineToRelative(8f)
                arcTo(3f, 3f, 0f, false, true, 19.5f, 6.5f)
                verticalLineToRelative(10f)
                // rect x="4.5" y="6.5" width="11.5" height="14" rx="2.6"
                moveTo(7.1f, 6.5f)
                horizontalLineTo(13.4f)
                arcTo(2.6f, 2.6f, 0f, false, true, 16f, 9.1f)
                verticalLineTo(17.9f)
                arcTo(2.6f, 2.6f, 0f, false, true, 13.4f, 20.5f)
                horizontalLineTo(7.1f)
                arcTo(2.6f, 2.6f, 0f, false, true, 4.5f, 17.9f)
                verticalLineTo(9.1f)
                arcTo(2.6f, 2.6f, 0f, false, true, 7.1f, 6.5f)
                close()
            }
        }
    }

    val Map: ImageVector by lazy {
        strokeIcon("Map") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // M9 4.2 3.5 6.4v13.4L9 17.6l6 2.2 5.5-2.2V4.2L15 6.4z
                moveTo(9f, 4.2f)
                lineTo(3.5f, 6.4f)
                verticalLineToRelative(13.4f)
                lineTo(9f, 17.6f)
                lineToRelative(6f, 2.2f)
                lineToRelative(5.5f, -2.2f)
                verticalLineTo(4.2f)
                lineTo(15f, 6.4f)
                close()
                // M9 4.2v13.4M15 6.4v13.4
                moveTo(9f, 4.2f)
                verticalLineToRelative(13.4f)
                moveTo(15f, 6.4f)
                verticalLineToRelative(13.4f)
            }
        }
    }

    val Spark: ImageVector by lazy {
        strokeIcon("Spark") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 2.5f)
                lineTo(14.4f, 9.6f)
                lineTo(21.5f, 12f)
                lineToRelative(-7.1f, 2.4f)
                lineTo(12f, 21.5f)
                lineToRelative(-2.4f, -7.1f)
                lineTo(2.5f, 12f)
                lineToRelative(7.1f, -2.4f)
                close()
            }
        }
    }

    val Pin: ImageVector by lazy {
        strokeIcon("Pin") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 21.5f)
                curveTo(12f, 21.5f, 19f, 15.3f, 19f, 10f)
                arcTo(7f, 7f, 0f, false, false, 5f, 10f)
                curveTo(5f, 15.3f, 12f, 21.5f, 12f, 21.5f)
                close()
                moveTo(14.6f, 10f)
                arcTo(2.6f, 2.6f, 0f, true, true, 9.4f, 10f)
                arcTo(2.6f, 2.6f, 0f, true, true, 14.6f, 10f)
                close()
            }
        }
    }

    val Users: ImageVector by lazy {
        strokeIcon("Users") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // circle(9, 9, 3.2)
                moveTo(12.2f, 9f)
                arcTo(3.2f, 3.2f, 0f, true, true, 5.8f, 9f)
                arcTo(3.2f, 3.2f, 0f, true, true, 12.2f, 9f)
                close()
                // M3 19.5c0-3 2.7-5.2 6-5.2s6 2.2 6 5.2
                moveTo(3f, 19.5f)
                curveTo(3f, 16.5f, 5.7f, 14.3f, 9f, 14.3f)
                reflectiveCurveTo(15f, 16.5f, 15f, 19.5f)
                // circle(16.5, 9.5, 2.5)
                moveTo(19f, 9.5f)
                arcTo(2.5f, 2.5f, 0f, true, true, 14f, 9.5f)
                arcTo(2.5f, 2.5f, 0f, true, true, 19f, 9.5f)
                close()
                // M16 14.5c2.6.2 4.8 2 4.8 4.6
                moveTo(16f, 14.5f)
                curveTo(18.6f, 14.7f, 20.8f, 16.5f, 20.8f, 19.1f)
            }
        }
    }

    val User: ImageVector by lazy {
        strokeIcon("User") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // circle(12, 8.5, 3.8)
                moveTo(15.8f, 8.5f)
                arcTo(3.8f, 3.8f, 0f, true, true, 8.2f, 8.5f)
                arcTo(3.8f, 3.8f, 0f, true, true, 15.8f, 8.5f)
                close()
                // M4.5 20c0-3.8 3.4-6.5 7.5-6.5s7.5 2.7 7.5 6.5
                moveTo(4.5f, 20f)
                curveTo(4.5f, 16.2f, 7.9f, 13.5f, 12f, 13.5f)
                reflectiveCurveTo(19.5f, 16.2f, 19.5f, 20f)
            }
        }
    }

    val Grid: ImageVector by lazy {
        strokeIcon("Grid") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // 4 rounded rects: (3,3,7,7), (14,3,7,7), (3,14,7,7), (14,14,7,7)
                moveTo(5f, 3f); horizontalLineTo(8f); arcTo(2f, 2f, 0f, false, true, 10f, 5f); verticalLineTo(8f); arcTo(2f, 2f, 0f, false, true, 8f, 10f); horizontalLineTo(5f); arcTo(2f, 2f, 0f, false, true, 3f, 8f); verticalLineTo(5f); arcTo(2f, 2f, 0f, false, true, 5f, 3f); close()
                moveTo(16f, 3f); horizontalLineTo(19f); arcTo(2f, 2f, 0f, false, true, 21f, 5f); verticalLineTo(8f); arcTo(2f, 2f, 0f, false, true, 19f, 10f); horizontalLineTo(16f); arcTo(2f, 2f, 0f, false, true, 14f, 8f); verticalLineTo(5f); arcTo(2f, 2f, 0f, false, true, 16f, 3f); close()
                moveTo(5f, 14f); horizontalLineTo(8f); arcTo(2f, 2f, 0f, false, true, 10f, 16f); verticalLineTo(19f); arcTo(2f, 2f, 0f, false, true, 8f, 21f); horizontalLineTo(5f); arcTo(2f, 2f, 0f, false, true, 3f, 19f); verticalLineTo(16f); arcTo(2f, 2f, 0f, false, true, 5f, 14f); close()
                moveTo(16f, 14f); horizontalLineTo(19f); arcTo(2f, 2f, 0f, false, true, 21f, 16f); verticalLineTo(19f); arcTo(2f, 2f, 0f, false, true, 19f, 21f); horizontalLineTo(16f); arcTo(2f, 2f, 0f, false, true, 14f, 19f); verticalLineTo(16f); arcTo(2f, 2f, 0f, false, true, 16f, 14f); close()
            }
        }
    }

    val Cal: ImageVector by lazy {
        strokeIcon("Cal") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 5f)
                horizontalLineTo(18f)
                arcTo(2.5f, 2.5f, 0f, false, true, 20.5f, 7.5f)
                verticalLineTo(18f)
                arcTo(2.5f, 2.5f, 0f, false, true, 18f, 20.5f)
                horizontalLineTo(6f)
                arcTo(2.5f, 2.5f, 0f, false, true, 3.5f, 18f)
                verticalLineTo(7.5f)
                arcTo(2.5f, 2.5f, 0f, false, true, 6f, 5f)
                close()
                moveTo(8f, 3.5f)
                verticalLineToRelative(3f)
                moveTo(16f, 3.5f)
                verticalLineToRelative(3f)
                moveTo(3.5f, 9.5f)
                horizontalLineToRelative(17f)
            }
        }
    }

    val Chart: ImageVector by lazy {
        strokeIcon("Chart") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 19f); verticalLineTo(11f)
                moveTo(10f, 19f); verticalLineTo(5f)
                moveTo(16f, 19f); verticalLineToRelative(-8f)
                moveTo(21f, 19f); horizontalLineTo(3f)
            }
        }
    }

    val Chat: ImageVector by lazy {
        strokeIcon("Chat") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20f, 14.5f)
                arcTo(2.5f, 2.5f, 0f, false, true, 17.5f, 17f)
                horizontalLineTo(8f)
                lineToRelative(-4f, 3.5f)
                verticalLineTo(6.5f)
                arcTo(2.5f, 2.5f, 0f, false, true, 6.5f, 4f)
                horizontalLineToRelative(11f)
                arcTo(2.5f, 2.5f, 0f, false, true, 20f, 6.5f)
                close()
            }
        }
    }

    val Gear: ImageVector by lazy {
        strokeIcon("Gear") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 12f)
                arcTo(3f, 3f, 0f, true, true, 9f, 12f)
                arcTo(3f, 3f, 0f, true, true, 15f, 12f)
                close()
                moveTo(12f, 2.5f); verticalLineTo(5f)
                moveTo(12f, 19f); verticalLineTo(21.5f)
                moveTo(4.9f, 4.9f); lineTo(6.7f, 6.7f)
                moveTo(17.3f, 17.3f); lineTo(19.1f, 19.1f)
                moveTo(2.5f, 12f); horizontalLineTo(5f)
                moveTo(19f, 12f); horizontalLineTo(21.5f)
                moveTo(4.9f, 19.1f); lineTo(6.7f, 17.3f)
                moveTo(17.3f, 6.7f); lineTo(19.1f, 4.9f)
            }
        }
    }

    val Search: ImageVector by lazy {
        strokeIcon("Search") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(17.8f, 11f)
                arcTo(6.8f, 6.8f, 0f, true, true, 4.2f, 11f)
                arcTo(6.8f, 6.8f, 0f, true, true, 17.8f, 11f)
                close()
                moveTo(20f, 20f)
                lineToRelative(-4.2f, -4.2f)
            }
        }
    }

    val Bell: ImageVector by lazy {
        strokeIcon("Bell") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 9.5f)
                arcTo(6f, 6f, 0f, false, true, 18f, 9.5f)
                curveTo(18f, 14.5f, 20f, 16f, 20f, 16f)
                horizontalLineTo(4f)
                curveTo(4f, 16f, 6f, 14.5f, 6f, 9.5f)
                close()
                moveTo(10f, 19f)
                arcTo(2f, 2f, 0f, false, false, 14f, 19f)
            }
        }
    }

    val Sliders: ImageVector by lazy {
        strokeIcon("Sliders") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 7f); horizontalLineTo(13f)
                moveTo(18f, 7f); horizontalLineTo(20f)
                moveTo(4f, 17f); horizontalLineTo(7f)
                moveTo(11.3f, 17f); horizontalLineTo(20f)
                // circle cx=15.5, cy=7, r=2.3
                moveTo(17.8f, 7f)
                arcTo(2.3f, 2.3f, 0f, true, true, 13.2f, 7f)
                arcTo(2.3f, 2.3f, 0f, true, true, 17.8f, 7f)
                close()
                // circle cx=9, cy=17, r=2.3
                moveTo(11.3f, 17f)
                arcTo(2.3f, 2.3f, 0f, true, true, 6.7f, 17f)
                arcTo(2.3f, 2.3f, 0f, true, true, 11.3f, 17f)
                close()
            }
        }
    }

    val Heart: ImageVector by lazy {
        strokeIcon("Heart") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 20.5f)
                curveTo(12f, 20.5f, 4.5f, 15.8f, 4.5f, 10.3f)
                arcTo(4.3f, 4.3f, 0f, false, true, 12f, 7.3f)
                arcTo(4.3f, 4.3f, 0f, false, true, 19.5f, 10.3f)
                curveTo(19.5f, 15.8f, 12f, 20.5f, 12f, 20.5f)
                close()
            }
        }
    }

    val HeartFill: ImageVector by lazy {
        strokeIcon("HeartFill") {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 20.5f)
                curveTo(12f, 20.5f, 4.5f, 15.8f, 4.5f, 10.3f)
                arcTo(4.3f, 4.3f, 0f, false, true, 12f, 7.3f)
                arcTo(4.3f, 4.3f, 0f, false, true, 19.5f, 10.3f)
                curveTo(19.5f, 15.8f, 12f, 20.5f, 12f, 20.5f)
                close()
            }
        }
    }

    val Bookmark: ImageVector by lazy {
        strokeIcon("Bookmark") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6.5f, 4f)
                horizontalLineToRelative(11f)
                arcTo(1.5f, 1.5f, 0f, false, true, 19f, 5.5f)
                verticalLineToRelative(15f)
                lineToRelative(-7f, -4f)
                lineToRelative(-7f, 4f)
                verticalLineToRelative(-15f)
                arcTo(1.5f, 1.5f, 0f, false, true, 6.5f, 4f)
                close()
            }
        }
    }

    val BookmarkFill: ImageVector by lazy {
        strokeIcon("BookmarkFill") {
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6.5f, 4f)
                horizontalLineToRelative(11f)
                arcTo(1.5f, 1.5f, 0f, false, true, 19f, 5.5f)
                verticalLineToRelative(15f)
                lineToRelative(-7f, -4f)
                lineToRelative(-7f, 4f)
                verticalLineToRelative(-15f)
                arcTo(1.5f, 1.5f, 0f, false, true, 6.5f, 4f)
                close()
            }
        }
    }

    val Close: ImageVector by lazy {
        strokeIcon("Close") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 6f); lineTo(18f, 18f)
                moveTo(18f, 6f); lineTo(6f, 18f)
            }
        }
    }

    val Check: ImageVector by lazy {
        strokeIcon("Check") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2.0f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 12.5f)
                lineToRelative(4.5f, 4.5f)
                lineTo(19f, 7.5f)
            }
        }
    }

    val Back: ImageVector by lazy {
        strokeIcon("Back") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 5f)
                lineToRelative(-7f, 7f)
                lineToRelative(7f, 7f)
            }
        }
    }

    val ChevRight: ImageVector by lazy {
        strokeIcon("ChevRight") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9f, 6f)
                lineToRelative(6f, 6f)
                lineToRelative(-6f, 6f)
            }
        }
    }

    val ChevDown: ImageVector by lazy {
        strokeIcon("ChevDown") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 9f)
                lineToRelative(6f, 6f)
                lineToRelative(6f, -6f)
            }
        }
    }

    val Share: ImageVector by lazy {
        strokeIcon("Share") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 3f); verticalLineToRelative(12f)
                moveTo(7.5f, 7.5f); lineTo(12f, 3f); lineToRelative(4.5f, 4.5f)
                moveTo(5f, 13f); verticalLineToRelative(5.5f)
                arcTo(1.5f, 1.5f, 0f, false, false, 6.5f, 20f)
                horizontalLineToRelative(11f)
                arcTo(1.5f, 1.5f, 0f, false, false, 19f, 18.5f)
                verticalLineTo(13f)
            }
        }
    }

    val Undo: ImageVector by lazy {
        strokeIcon("Undo") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 9f)
                horizontalLineToRelative(10f)
                arcTo(5f, 5f, 0f, false, true, 14f, 19f)
                horizontalLineTo(9f)
                moveTo(7.5f, 5.5f)
                lineTo(4f, 9f)
                lineToRelative(3.5f, 3.5f)
            }
        }
    }

    val Plus: ImageVector by lazy {
        strokeIcon("Plus") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 5f); verticalLineToRelative(14f)
                moveTo(5f, 12f); horizontalLineToRelative(14f)
            }
        }
    }

    val Route: ImageVector by lazy {
        strokeIcon("Route") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(8.5f, 18f)
                arcTo(2.5f, 2.5f, 0f, true, true, 3.5f, 18f)
                arcTo(2.5f, 2.5f, 0f, true, true, 8.5f, 18f)
                close()
                moveTo(20.5f, 6f)
                arcTo(2.5f, 2.5f, 0f, true, true, 15.5f, 6f)
                arcTo(2.5f, 2.5f, 0f, true, true, 20.5f, 6f)
                close()
                moveTo(8.5f, 18f)
                horizontalLineToRelative(4f)
                arcTo(3.5f, 3.5f, 0f, false, false, 12.5f, 11f)
                horizontalLineToRelative(-1f)
                arcTo(3.5f, 3.5f, 0f, false, true, 11.5f, 4f)
                horizontalLineToRelative(4f)
            }
        }
    }

    val Ticket: ImageVector by lazy {
        strokeIcon("Ticket") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 8f)
                arcTo(2f, 2f, 0f, false, true, 6f, 6f)
                horizontalLineToRelative(12f)
                arcTo(2f, 2f, 0f, false, true, 20f, 8f)
                verticalLineToRelative(2f)
                arcTo(2f, 2f, 0f, false, false, 20f, 14f)
                verticalLineToRelative(2f)
                arcTo(2f, 2f, 0f, false, true, 18f, 18f)
                horizontalLineTo(6f)
                arcTo(2f, 2f, 0f, false, true, 4f, 16f)
                verticalLineToRelative(-2f)
                arcTo(2f, 2f, 0f, false, false, 4f, 10f)
                close()
                moveTo(13f, 6f)
                verticalLineToRelative(12f)
            }
        }
    }

    val Eye: ImageVector by lazy {
        strokeIcon("Eye") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2.5f, 12f)
                curveTo(4.3f, 7.6f, 7.8f, 5f, 12f, 5f)
                reflectiveCurveToRelative(7.7f, 2.6f, 9.5f, 7f)
                curveToRelative(-1.8f, 4.4f, -5.3f, 7f, -9.5f, 7f)
                reflectiveCurveTo(4.3f, 16.4f, 2.5f, 12f)
                close()
                moveTo(15f, 12f)
                arcTo(3f, 3f, 0f, true, true, 9f, 12f)
                arcTo(3f, 3f, 0f, true, true, 15f, 12f)
                close()
            }
        }
    }

    val EyeOff: ImageVector by lazy {
        strokeIcon("EyeOff") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 3f); lineTo(21f, 21f)
                moveTo(10.6f, 10.6f); arcTo(2f, 2f, 0f, false, false, 13.4f, 13.4f)
                moveTo(9.9f, 5.1f)
                arcTo(9.8f, 9.8f, 0f, false, true, 12f, 5f)
                curveToRelative(4.2f, 0f, 7.7f, 2.6f, 9.5f, 7f)
                arcTo(10.5f, 10.5f, 0f, false, true, 18.2f, 16.5f)
                moveTo(6.2f, 6.2f)
                arcTo(10.5f, 10.5f, 0f, false, false, 2.5f, 12f)
                curveToRelative(1.8f, 4.4f, 5.3f, 7f, 9.5f, 7f)
                curveToRelative(1.8f, 0f, 3.5f, -0.5f, 5f, -1.4f)
            }
        }
    }

    val Mail: ImageVector by lazy {
        strokeIcon("Mail") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5.5f, 5f)
                horizontalLineToRelative(13f)
                arcTo(2.5f, 2.5f, 0f, false, true, 21f, 7.5f)
                verticalLineToRelative(9f)
                arcTo(2.5f, 2.5f, 0f, false, true, 18.5f, 19f)
                horizontalLineToRelative(-13f)
                arcTo(2.5f, 2.5f, 0f, false, true, 3f, 16.5f)
                verticalLineToRelative(-9f)
                arcTo(2.5f, 2.5f, 0f, false, true, 5.5f, 5f)
                close()
                moveTo(4f, 7f)
                lineToRelative(8f, 5.5f)
                lineTo(20f, 7f)
            }
        }
    }

    val Lock: ImageVector by lazy {
        strokeIcon("Lock") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7.5f, 11f)
                horizontalLineToRelative(9f)
                arcTo(2.5f, 2.5f, 0f, false, true, 19f, 13.5f)
                verticalLineToRelative(5f)
                arcTo(2.5f, 2.5f, 0f, false, true, 16.5f, 21f)
                horizontalLineToRelative(-9f)
                arcTo(2.5f, 2.5f, 0f, false, true, 5f, 18.5f)
                verticalLineToRelative(-5f)
                arcTo(2.5f, 2.5f, 0f, false, true, 7.5f, 11f)
                close()
                moveTo(8f, 11f)
                verticalLineTo(7.5f)
                arcTo(4f, 4f, 0f, false, true, 16f, 7.5f)
                verticalLineTo(11f)
            }
        }
    }

    val Swap: ImageVector by lazy {
        strokeIcon("Swap") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7f, 4f); lineTo(3f, 8f); lineToRelative(4f, 4f)
                moveTo(3f, 8f); horizontalLineToRelative(14f)
                moveTo(17f, 20f); lineToRelative(4f, -4f); lineToRelative(-4f, -4f)
                moveTo(21f, 16f); horizontalLineTo(7f)
            }
        }
    }

    val Store: ImageVector by lazy {
        strokeIcon("Store") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 9f)
                lineToRelative(1.5f, -5f)
                horizontalLineToRelative(15f)
                lineTo(21f, 9f)
                moveTo(3f, 9f)
                curveToRelative(0f, 1.7f, 1.3f, 3f, 3f, 3f)
                reflectiveCurveToRelative(3f, -1.3f, 3f, -3f)
                curveToRelative(0f, 1.7f, 1.3f, 3f, 3f, 3f)
                reflectiveCurveToRelative(3f, -1.3f, 3f, -3f)
                curveToRelative(0f, 1.7f, 1.3f, 3f, 3f, 3f)
                reflectiveCurveToRelative(3f, -1.3f, 3f, -3f)
                moveTo(5f, 12f)
                verticalLineToRelative(8f)
                horizontalLineToRelative(14f)
                verticalLineToRelative(-8f)
                moveTo(10f, 20f)
                verticalLineToRelative(-5f)
                horizontalLineToRelative(4f)
                verticalLineToRelative(5f)
            }
        }
    }

    val Send: ImageVector by lazy {
        strokeIcon("Send") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 3f)
                lineTo(10f, 14f)
                moveTo(21f, 3f)
                lineToRelative(-7f, 18f)
                lineToRelative(-4f, -7f)
                lineToRelative(-7f, -4f)
                close()
            }
        }
    }

    val More: ImageVector by lazy {
        strokeIcon("More") {
            path(fill = SolidColor(Color.White)) {
                moveTo(6.6f, 12f); arcTo(1.6f, 1.6f, 0f, true, true, 3.4f, 12f); arcTo(1.6f, 1.6f, 0f, true, true, 6.6f, 12f); close()
                moveTo(13.6f, 12f); arcTo(1.6f, 1.6f, 0f, true, true, 10.4f, 12f); arcTo(1.6f, 1.6f, 0f, true, true, 13.6f, 12f); close()
                moveTo(20.6f, 12f); arcTo(1.6f, 1.6f, 0f, true, true, 17.4f, 12f); arcTo(1.6f, 1.6f, 0f, true, true, 20.6f, 12f); close()
            }
        }
    }

    val Edit: ImageVector by lazy {
        strokeIcon("Edit") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15.5f, 4.5f)
                lineToRelative(4f, 4f)
                lineTo(8f, 20f)
                horizontalLineTo(4f)
                verticalLineToRelative(-4f)
                close()
            }
        }
    }

    val Copy: ImageVector by lazy {
        strokeIcon("Copy") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(11f, 9f)
                horizontalLineToRelative(7f)
                arcTo(2f, 2f, 0f, false, true, 20f, 11f)
                verticalLineToRelative(7f)
                arcTo(2f, 2f, 0f, false, true, 18f, 20f)
                horizontalLineToRelative(-7f)
                arcTo(2f, 2f, 0f, false, true, 9f, 18f)
                verticalLineToRelative(-7f)
                arcTo(2f, 2f, 0f, false, true, 11f, 9f)
                close()
                moveTo(5f, 15f)
                verticalLineTo(6f)
                arcTo(2f, 2f, 0f, false, true, 7f, 4f)
                horizontalLineToRelative(9f)
            }
        }
    }

    val Flag: ImageVector by lazy {
        strokeIcon("Flag") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 21f); verticalLineTo(4f)
                moveTo(5f, 4f); horizontalLineToRelative(12f); lineToRelative(-2.5f, 4.5f); lineTo(17f, 13f); horizontalLineTo(5f)
            }
        }
    }

    val Ban: ImageVector by lazy {
        strokeIcon("Ban") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20.5f, 12f)
                arcTo(8.5f, 8.5f, 0f, true, true, 3.5f, 12f)
                arcTo(8.5f, 8.5f, 0f, true, true, 20.5f, 12f)
                close()
                moveTo(6f, 6f); lineTo(18f, 18f)
            }
        }
    }

    val WifiOff: ImageVector by lazy {
        strokeIcon("WifiOff") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2f, 2f); lineTo(22f, 22f)
                moveTo(8.5f, 16.5f); arcTo(5f, 5f, 0f, false, true, 15.5f, 16.5f)
                moveTo(5f, 12.8f); arcTo(10f, 10f, 0f, false, true, 10.2f, 10.6f)
                moveTo(14.5f, 10.8f); arcTo(10f, 10f, 0f, false, true, 19f, 12.8f)
                moveTo(2f, 9f); arcTo(15f, 15f, 0f, false, true, 6.8f, 6.2f)
                moveTo(11f, 5.1f); arcTo(15f, 15f, 0f, false, true, 22f, 9f)
                moveTo(12f, 20f); horizontalLineToRelative(0.01f)
            }
        }
    }

    val Clock: ImageVector by lazy {
        strokeIcon("Clock") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20.5f, 12f)
                arcTo(8.5f, 8.5f, 0f, true, true, 3.5f, 12f)
                arcTo(8.5f, 8.5f, 0f, true, true, 20.5f, 12f)
                close()
                moveTo(12f, 7f)
                verticalLineTo(12f)
                lineTo(15.5f, 14f)
            }
        }
    }

    val Camera: ImageVector by lazy {
        strokeIcon("Camera") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 8f)
                arcTo(2f, 2f, 0f, false, true, 6f, 6f)
                horizontalLineToRelative(2.2f)
                lineToRelative(1.4f, -2f)
                horizontalLineToRelative(4.8f)
                lineToRelative(1.4f, 2f)
                horizontalLineTo(18f)
                arcTo(2f, 2f, 0f, false, true, 20f, 8f)
                verticalLineToRelative(9f)
                arcTo(2f, 2f, 0f, false, true, 18f, 19f)
                horizontalLineTo(6f)
                arcTo(2f, 2f, 0f, false, true, 4f, 17f)
                close()
                moveTo(15.2f, 12.5f)
                arcTo(3.2f, 3.2f, 0f, true, true, 8.8f, 12.5f)
                arcTo(3.2f, 3.2f, 0f, true, true, 15.2f, 12.5f)
                close()
            }
        }
    }

    val Globe: ImageVector by lazy {
        strokeIcon("Globe") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20.5f, 12f)
                arcTo(8.5f, 8.5f, 0f, true, true, 3.5f, 12f)
                arcTo(8.5f, 8.5f, 0f, true, true, 20.5f, 12f)
                close()
                moveTo(3.6f, 12f); horizontalLineTo(20.4f)
                moveTo(12f, 3.5f); curveTo(14.5f, 6f, 15.8f, 9f, 15.8f, 12f); curveTo(15.8f, 15f, 14.5f, 18f, 12f, 20.5f)
                moveTo(12f, 3.5f); curveTo(9.5f, 6f, 8.2f, 9f, 8.2f, 12f); curveTo(8.2f, 15f, 9.5f, 18f, 12f, 20.5f)
            }
        }
    }

    val Shield: ImageVector by lazy {
        strokeIcon("Shield") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 3f)
                lineTo(19f, 6f)
                verticalLineToRelative(5.5f)
                curveTo(19f, 16.2f, 16f, 19.6f, 12f, 21f)
                curveTo(8f, 19.6f, 5f, 16.2f, 5f, 11.5f)
                verticalLineTo(6f)
                close()
            }
        }
    }

    val Alert: ImageVector by lazy {
        strokeIcon("Alert") {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 4f)
                lineTo(21f, 19.5f)
                horizontalLineTo(3f)
                close()
                moveTo(12f, 10f); verticalLineTo(14f)
                moveTo(12f, 17f); horizontalLineToRelative(0.01f)
            }
        }
    }

    val ChevronDown: ImageVector get() = ChevDown
    val ChevronRight: ImageVector get() = ChevRight
    val Save: ImageVector get() = Bookmark
    val Calendar: ImageVector get() = Cal
    val Msg: ImageVector get() = Chat
    val Flame: ImageVector get() = Spark
}

/**
 * Multicolor 4-path Google G Logo from s2-lib.js (`gLogo`).
 */
@Composable
fun GoogleMulticolorIcon(modifier: Modifier = Modifier.size(20.dp)) {
    val gVector = remember {
        ImageVector.Builder(
            name = "GoogleLogo",
            defaultWidth = 20.dp,
            defaultHeight = 20.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(fill = SolidColor(Color(0xFF4285F4))) {
                moveTo(21.6f, 12.23f)
                curveToRelative(0f, -0.7f, -0.06f, -1.37f, -0.18f, -2.02f)
                horizontalLineTo(12f)
                verticalLineToRelative(3.83f)
                horizontalLineToRelative(5.38f)
                arcToRelative(4.6f, 4.6f, 0f, false, true, -2f, 3.02f)
                verticalLineToRelative(2.5f)
                horizontalLineToRelative(3.24f)
                curveToRelative(1.9f, -1.75f, 2.98f, -4.33f, 2.98f, -7.33f)
                close()
            }
            path(fill = SolidColor(Color(0xFF34A853))) {
                moveTo(12f, 22f)
                curveToRelative(2.7f, 0f, 4.96f, -0.9f, 6.62f, -2.44f)
                lineToRelative(-3.24f, -2.5f)
                curveToRelative(-0.9f, 0.6f, -2.04f, 0.96f, -3.38f, 0.96f)
                curveToRelative(-2.6f, 0f, -4.8f, -1.76f, -5.59f, -4.12f)
                horizontalLineTo(3.07f)
                verticalLineToRelative(2.58f)
                arcTo(10f, 10f, 0f, false, false, 12f, 22f)
                close()
            }
            path(fill = SolidColor(Color(0xFFFBBC05))) {
                moveTo(6.41f, 13.9f)
                arcToRelative(6f, 6f, 0f, false, true, 0f, -3.8f)
                verticalLineTo(7.52f)
                horizontalLineTo(3.07f)
                arcToRelative(10f, 10f, 0f, false, false, 0f, 8.96f)
                lineToRelative(3.34f, -2.58f)
                close()
            }
            path(fill = SolidColor(Color(0xFFEA4335))) {
                moveTo(12f, 5.98f)
                curveToRelative(1.47f, 0f, 2.78f, 0.5f, 3.82f, 1.5f)
                lineToRelative(2.86f, -2.86f)
                arcTo(9.96f, 9.96f, 0f, false, false, 12f, 2f)
                arcTo(10f, 10f, 0f, false, false, 3.07f, 7.52f)
                lineToRelative(3.34f, 2.58f)
                curveTo(7.2f, 7.74f, 9.4f, 5.98f, 12f, 5.98f)
                close()
            }
        }.build()
    }
    Image(
        painter = rememberVectorPainter(gVector),
        contentDescription = "Google",
        modifier = modifier
    )
}

@Composable
fun GoogleMulticolorIcon(size: Dp, modifier: Modifier = Modifier) {
    GoogleMulticolorIcon(modifier = modifier.size(size))
}

enum class VryvButtonVariant {
    PRIMARY,    // #FF1D8E, white text
    SECONDARY,  // #24242C, white text
    WHITE,      // #F5F5F7, #0B0B0F text (Google button)
    LIME,       // #C7FF28, #0B0B0F text
    GHOST,      // transparent, #9696A3 text
    DANGER,     // #FF4D67, white text
    DISABLED,   // #2A2A33, #6C6C78 text
    PrimaryPink,
    SecondaryDark,
    PrimaryLime,
    Ghost,
    Danger
}

typealias VryvButtonStyle = VryvButtonVariant

/**
 * Exact 1:1 Pill CTA Button (.btn.pri, .btn.sec, .btn.white, .btn.dis, .btn.sm)
 * Height 54.dp, Radius 27.dp, 17.sp SemiBold (or 36.dp / 18.dp / 14.sp when isSmall = true).
 */
@Composable
fun VryvButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: VryvButtonVariant = VryvButtonVariant.PRIMARY,
    style: VryvButtonVariant = variant,
    isSmall: Boolean = false,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    testTag: String? = null
) {
    val effectiveVariant = if (!enabled) VryvButtonVariant.DISABLED else style
    val bgColor = when (effectiveVariant) {
        VryvButtonVariant.PRIMARY, VryvButtonVariant.PrimaryPink -> VryvPink
        VryvButtonVariant.SECONDARY, VryvButtonVariant.SecondaryDark -> VryvCardElevated
        VryvButtonVariant.WHITE -> VryvTextPrimary
        VryvButtonVariant.LIME, VryvButtonVariant.PrimaryLime -> VryvLime
        VryvButtonVariant.GHOST, VryvButtonVariant.Ghost -> Color.Transparent
        VryvButtonVariant.DANGER, VryvButtonVariant.Danger -> Color(0xFFFF4D67)
        VryvButtonVariant.DISABLED -> VryvDisabledBg
    }
    val textColor = when (effectiveVariant) {
        VryvButtonVariant.PRIMARY, VryvButtonVariant.PrimaryPink -> Color.White
        VryvButtonVariant.SECONDARY, VryvButtonVariant.SecondaryDark -> VryvTextPrimary
        VryvButtonVariant.WHITE -> VryvBg
        VryvButtonVariant.LIME, VryvButtonVariant.PrimaryLime -> VryvBg
        VryvButtonVariant.GHOST, VryvButtonVariant.Ghost -> VryvTextSecondary
        VryvButtonVariant.DANGER, VryvButtonVariant.Danger -> Color.White
        VryvButtonVariant.DISABLED -> VryvDisabledText
    }
    val height = if (isSmall) 36.dp else 54.dp
    val radius = if (isSmall) 18.dp else 27.dp
    val fontSize = if (isSmall) 14.sp else 17.sp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(radius))
            .background(bgColor)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .clickable(enabled = enabled && !isLoading) { onClick() }
            .padding(horizontal = if (isSmall) 14.dp else 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = textColor,
                    strokeWidth = 2.2.dp,
                    modifier = Modifier.size(18.dp)
                )
            } else if (leadingIcon != null) {
                leadingIcon()
            }
            Text(
                text = text,
                color = textColor,
                fontFamily = InterFontFamily,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.25).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Exact 1:1 Circular 40.dp Icon Button (.ibtn and .ibtn.glass)
 */
@Composable
fun VryvIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    isGlass: Boolean = false,
    tint: Color = VryvTextPrimary,
    hasUnreadDot: Boolean = false,
    dotColor: Color = VryvPink,
    testTag: String? = null
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(if (isGlass) VryvGlassBg else VryvCard)
            .then(
                if (isGlass) Modifier.border(0.5.dp, Color(0x1FFFFFFF), CircleShape)
                else Modifier.border(1.dp, VryvLine, CircleShape)
            )
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        if (hasUnreadDot) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(dotColor)
                    .border(1.5.dp, VryvCard, CircleShape)
            )
        }
    }
}

enum class VryvFieldState {
    DEFAULT,
    FOCUS,
    ERROR,
    OK
}

/**
 * Exact 1:1 Input Field (.field + .flabel + .inp + .fhint) from s2.css:
 * - Label above: 13.sp SemiBold #9696A3
 * - Field box: 54.dp height (or 96.dp for multi-line), r=16.dp, #1A1A20 bg
 * - Focus border: 1.5.dp rgba(245,245,247,.55)
 * - Error border: 1.5.dp #FF1D8E + rgba(255,29,142,.06) bg
 * - Ok border: 1.5.dp rgba(199,255,40,.55)
 */
@Composable
fun VryvInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    rightLabel: (@Composable () -> Unit)? = null,
    placeholder: String = "",
    hintText: String? = null,
    state: VryvFieldState = VryvFieldState.DEFAULT,
    isPassword: Boolean = false,
    isMultiline: Boolean = false,
    singleLine: Boolean = !isMultiline,
    minLines: Int = 1,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    testTag: String? = null
) {
    val multilineEffective = isMultiline || !singleLine || minLines > 1
    var isFocused by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val effectiveState = when {
        state != VryvFieldState.DEFAULT -> state
        isFocused -> VryvFieldState.FOCUS
        else -> VryvFieldState.DEFAULT
    }

    val borderColor = when (effectiveState) {
        VryvFieldState.ERROR -> VryvPink
        VryvFieldState.OK -> Color(0x8CC7FF28)
        VryvFieldState.FOCUS -> Color(0x8CF5F5F7)
        VryvFieldState.DEFAULT -> Color.Transparent
    }
    val bgColor = when (effectiveState) {
        VryvFieldState.ERROR -> Color(0x10FF1D8E)
        else -> VryvCard
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (label != null || rightLabel != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (label != null) {
                    Text(
                        text = label,
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.1).sp
                    )
                }
                if (rightLabel != null) {
                    rightLabel()
                }
            }
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = !multilineEffective,
            textStyle = TextStyle(
                fontFamily = InterFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = VryvTextPrimary,
                lineHeight = 21.sp
            ),
            cursorBrush = SolidColor(VryvPink),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
                .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (multilineEffective) Modifier.heightIn(min = 96.dp)
                            else Modifier.height(54.dp)
                        )
                        .clip(RoundedCornerShape(16.dp))
                        .background(bgColor)
                        .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
                        .padding(
                            horizontal = 16.dp,
                            vertical = if (multilineEffective) 14.dp else 0.dp
                        ),
                    verticalAlignment = if (multilineEffective) Alignment.Top else Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (leadingIcon != null) {
                        Icon(
                            imageVector = leadingIcon,
                            contentDescription = null,
                            tint = VryvTextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                color = VryvTextMuted,
                                fontFamily = InterFontFamily,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                        innerTextField()
                    }
                    if (isPassword) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = if (passwordVisible) VryvIcons.EyeOff else VryvIcons.Eye,
                            contentDescription = "Показать пароль",
                            tint = VryvTextSecondary,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { passwordVisible = !passwordVisible }
                        )
                    } else if (trailingContent != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        trailingContent()
                    } else if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Icon(
                            imageVector = trailingIcon,
                            contentDescription = null,
                            tint = VryvTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        )

        if (hintText != null) {
            val hintColor = when (state) {
                VryvFieldState.ERROR -> VryvPink
                VryvFieldState.OK -> VryvLime
                else -> VryvTextSecondary
            }
            Text(
                text = hintText,
                color = hintColor,
                fontFamily = InterFontFamily,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

/**
 * Exact 1:1 6-Cell OTP Code Row (.code > span) from s2.css:
 * 6 cells, 50x62.dp, r=16.dp, #1A1A20 bg, 26.sp Bold.
 */
@Composable
fun VryvOtpRow(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    cellCount: Int = 6,
    state: VryvFieldState = VryvFieldState.DEFAULT,
    testTag: String? = null
) {
    BasicTextField(
        value = code,
        onValueChange = { newVal ->
            val filtered = newVal.filter { it.isDigit() }.take(cellCount)
            onCodeChange(filtered)
        },
        modifier = modifier
            .fillMaxWidth()
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 0 until cellCount) {
                    val char = code.getOrNull(i)?.toString() ?: ""
                    val isCurrentFocus = i == code.length
                    val borderColor = when {
                        state == VryvFieldState.ERROR -> VryvPink
                        state == VryvFieldState.OK -> VryvLime
                        isCurrentFocus -> VryvTextPrimary
                        else -> Color.Transparent
                    }
                    val bgColor = when {
                        state == VryvFieldState.ERROR -> Color(0x10FF1D8E)
                        else -> VryvCard
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(62.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            color = VryvTextPrimary,
                            fontFamily = InterFontFamily,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    )
}

/**
 * Exact 1:1 Chip (.chip and .chip.big) from common.css & s2.css:
 * Standard: 34.dp height, r=17.dp, 13.5.sp SemiBold
 * Big (onboarding): 44.dp height, r=22.dp, 15.sp SemiBold
 */
@Composable
fun VryvChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isBig: Boolean = false,
    activeColor: Color = VryvPink,
    activeTextColor: Color = Color.White,
    leadingDotColor: Color? = null,
    trailingChevron: Boolean = false,
    testTag: String? = null
) {
    val height = if (isBig) 44.dp else 34.dp
    val radius = if (isBig) 22.dp else 17.dp
    val hPad = if (isBig) 18.dp else 14.dp
    val fontSize = if (isBig) 15.sp else 13.5.sp

    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(radius))
            .background(if (selected) activeColor else VryvCard)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .clickable { onClick() }
            .padding(horizontal = hPad),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (leadingDotColor != null) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(leadingDotColor)
                )
            }
            Text(
                text = text,
                color = if (selected) activeTextColor else VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = fontSize,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.15).sp
            )
            if (trailingChevron) {
                Icon(
                    imageVector = VryvIcons.ChevDown,
                    contentDescription = null,
                    tint = if (selected) activeTextColor else VryvTextSecondary,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

enum class VryvTagVariant {
    PINK,
    LIME,
    DARK,
    OUTLINE,
    Pink,
    Lime,
    Dark,
    Outline
}

typealias VryvTagStyle = VryvTagVariant

/**
 * Exact 1:1 Tag (.tag) from common.css:
 * 24.dp height, r=8.dp, 12.sp SemiBold.
 */
@Composable
fun VryvTag(
    text: String,
    modifier: Modifier = Modifier,
    variant: VryvTagVariant = VryvTagVariant.DARK,
    style: VryvTagVariant = variant
) {
    val bgColor = when (style) {
        VryvTagVariant.PINK, VryvTagVariant.Pink -> VryvPink
        VryvTagVariant.LIME, VryvTagVariant.Lime -> VryvLime
        VryvTagVariant.DARK, VryvTagVariant.Dark -> Color(0xA816161C)
        VryvTagVariant.OUTLINE, VryvTagVariant.Outline -> Color(0x14FFFFFF)
    }
    val textColor = when (style) {
        VryvTagVariant.PINK, VryvTagVariant.Pink -> Color.White
        VryvTagVariant.LIME, VryvTagVariant.Lime -> Color(0xFF0B0B0F)
        VryvTagVariant.DARK, VryvTagVariant.Dark -> VryvTextPrimary
        VryvTagVariant.OUTLINE, VryvTagVariant.Outline -> VryvTextPrimary
    }
    Box(
        modifier = modifier
            .height(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontFamily = InterFontFamily,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = (-0.1).sp
        )
    }
}

/**
 * Exact 1:1 Segmented Control (.seg) from common.css:
 * 38.dp height, r=19.dp, #1A1A20 bg, 3.dp padding, active pill #2C2C35.
 */
@Composable
fun VryvSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit = {},
    onSelect: (Int) -> Unit = onOptionSelected,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(38.dp)
            .clip(RoundedCornerShape(19.dp))
            .background(VryvCard)
            .padding(3.dp)
    ) {
        options.forEachIndexed { index, label ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(32.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) Color(0xFF2C2C35) else Color.Transparent)
                    .clickable {
                        onOptionSelected(index)
                        onSelect(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = label,
                    color = if (isSelected) VryvTextPrimary else VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Exact 1:1 Option Card (.opt) from s2.css:
 * min-height 64.dp, r=20.dp, #1A1A20 bg, 1.5.dp #FF1D8E active border & rgba(255,29,142,.1) bg,
 * 22.dp radio/checkbox indicator on right.
 */
@Composable
fun VryvOptionCard(
    title: String,
    subtitle: String? = null,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 64.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) Color(0x1AFF1D8E) else VryvCard)
            .border(
                width = 1.5.dp,
                color = if (selected) VryvPink else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                color = VryvTextPrimary,
                fontFamily = InterFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = (-0.2).sp
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 13.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(if (selected) VryvPink else Color.Transparent)
                .border(
                    width = 1.8.dp,
                    color = if (selected) VryvPink else Color(0xFF464652),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Icon(
                    imageVector = VryvIcons.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(13.dp)
                )
            }
        }
    }
}

/**
 * Exact 1:1 iOS-style Switch (.tg) from s2.css:
 * 51x31.dp, r=16.dp, #2E2E38 inactive, #C7FF28 active, 27.dp knob.
 */
@Composable
fun VryvToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(51.dp)
            .height(31.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(if (checked) VryvLime else Color(0xFF2E2E38))
            .clickable { onCheckedChange(!checked) }
            .padding(2.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(27.dp)
                .clip(CircleShape)
                .background(if (checked) Color(0xFF0B0B0F) else Color.White)
        )
    }
}

/**
 * Exact 1:1 Grouped List Container (.grp) and Row (.li) from s2.css:
 * Container: r=20.dp, #1A1A20 bg.
 * Row: min-height 54.dp, 34x34.dp r=10.dp #26262E icon box, divider inset 62.dp from left.
 */
@Composable
fun VryvGroupList(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VryvCard),
        content = content
    )
}

@Composable
fun VryvGroupRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconTint: Color = VryvTextPrimary,
    titleColor: Color = VryvTextPrimary,
    rightValue: String? = null,
    showChevron: Boolean = true,
    toggleState: Boolean? = null,
    onToggleChange: ((Boolean) -> Unit)? = null,
    showDivider: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 54.dp)
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(VryvIconTileBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = titleColor,
                    fontFamily = InterFontFamily,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        color = VryvTextSecondary,
                        fontFamily = InterFontFamily,
                        fontSize = 12.5.sp,
                        lineHeight = 16.sp
                    )
                }
            }

            if (rightValue != null) {
                Text(
                    text = rightValue,
                    color = VryvTextSecondary,
                    fontFamily = InterFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(end = if (showChevron) 6.dp else 0.dp)
                )
            }

            if (toggleState != null && onToggleChange != null) {
                VryvToggle(checked = toggleState, onCheckedChange = onToggleChange)
            } else if (showChevron && onClick != null) {
                Icon(
                    imageVector = VryvIcons.ChevRight,
                    contentDescription = null,
                    tint = Color(0xFF5A5A66),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        if (showDivider) {
            HorizontalDivider(
                color = VryvLine,
                thickness = 0.5.dp,
                modifier = Modifier.padding(start = if (icon != null) 62.dp else 16.dp)
            )
        }
    }
}

enum class VryvBadgeStyle {
    OK,    // .st-ok: rgba(199,255,40,.14), #C7FF28
    WAIT,  // .st-wait: rgba(255,196,46,.14), #FFD35A
    PINK,  // .st-pink: rgba(255,29,142,.15), #FF79BE
    MUTE,  // .st-mute: rgba(255,255,255,.07), #9696A3
    OFF,   // .st-off: rgba(255,90,90,.14), #FF8C8C
    Lime,
    Pink,
    Warning,
    Neutral,
    Danger,
    INFO,
    WARN,
    MUTED,
    ERR
}

typealias VryvStatusStyle = VryvBadgeStyle

/**
 * Exact 1:1 Status Pill Badge (.st-pill) from s2.css:
 * 24.dp height, r=8.dp, 9.dp horizontal padding, 11.5.sp Bold, 6.dp dot.
 */
@Composable
fun VryvStatusPill(
    text: String,
    style: VryvBadgeStyle,
    modifier: Modifier = Modifier,
    showDot: Boolean = true
) {
    val (bgColor, fgColor) = when (style) {
        VryvBadgeStyle.OK, VryvBadgeStyle.Lime -> Color(0x24C7FF28) to VryvLime
        VryvBadgeStyle.WAIT, VryvBadgeStyle.Warning, VryvBadgeStyle.WARN -> Color(0x24FFC42E) to Color(0xFFFFD35A)
        VryvBadgeStyle.PINK, VryvBadgeStyle.Pink, VryvBadgeStyle.INFO -> Color(0x26FF1D8E) to VryvPinkLight
        VryvBadgeStyle.MUTE, VryvBadgeStyle.Neutral, VryvBadgeStyle.MUTED -> Color(0x12FFFFFF) to VryvTextSecondary
        VryvBadgeStyle.OFF, VryvBadgeStyle.Danger, VryvBadgeStyle.ERR -> Color(0x24FF5A5A) to Color(0xFFFF8C8C)
    }

    Box(
        modifier = modifier
            .height(24.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 9.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (showDot) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(fgColor)
                )
            }
            Text(
                text = text,
                color = fgColor,
                fontFamily = InterFontFamily,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.1).sp
            )
        }
    }
}

/**
 * Exact 1:1 Top Toast Notification (.toast) from s2.css:
 * Positioned under status bar: #1E1E26 bg, 1.dp border rgba(255,255,255,.12), r=16.dp,
 * 28.dp icon box, text 13.5.sp SemiBold, optional right action button.
 */
@Composable
fun VryvTopToast(
    message: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E1E26))
            .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(if (isError) Color(0x29FF1D8E) else Color(0x29C7FF28)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isError) VryvIcons.WifiOff else VryvIcons.Check,
                contentDescription = null,
                tint = if (isError) VryvPink else VryvLime,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = message,
            color = VryvTextPrimary,
            fontFamily = InterFontFamily,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
        if (actionText != null && onActionClick != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = actionText,
                color = VryvPink,
                fontFamily = InterFontFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Composable
fun VryvTopToast(
    title: String,
    subtitle: String? = null,
    isSuccess: Boolean = true,
    modifier: Modifier = Modifier
) {
    VryvTopToast(
        message = title,
        modifier = modifier,
        isError = !isSuccess
    )
}

/**
 * Exact 1:1 Floating Bottom Sheet (.bsheet) from s2.css:
 * #17171C background, r=38.dp, 8.dp margins, 38x5.dp #3A3A44 grab handle.
 */
@Composable
fun VryvFloatingBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VryvOverlay)
            .clickable { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(38.dp))
                .background(VryvSheetBg)
                .border(0.5.dp, Color(0x1AFFFFFF), RoundedCornerShape(38.dp))
                .clickable(enabled = false) {}
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 28.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 14.dp)
                    .width(38.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0xFF3A3A44))
            )
            content()
        }
    }
}

/**
 * Overlapping Avatar Stack (.avatars) from common.css:
 * 24.dp circles with -7.dp overlap and 2.dp border.
 */
@Composable
fun VryvAvatarStack(
    avatarResList: List<Int>,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    borderColor: Color = VryvCard,
    extraCountText: String? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        avatarResList.forEachIndexed { index, res ->
            Image(
                painter = painterResource(id = res),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .offset(x = (-7 * index).dp)
                    .size(size)
                    .clip(CircleShape)
                    .border(2.dp, borderColor, CircleShape)
            )
        }
        if (extraCountText != null) {
            Box(
                modifier = Modifier
                    .offset(x = (-7 * avatarResList.size).dp)
                    .height(size)
                    .clip(RoundedCornerShape(size / 2))
                    .background(VryvPink)
                    .border(2.dp, borderColor, RoundedCornerShape(size / 2))
                    .padding(horizontal = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = extraCountText,
                    color = Color.White,
                    fontFamily = InterFontFamily,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun VryvAvatarStack(
    avatars: List<Int>,
    size: Dp = 24.dp,
    borderColor: Color = VryvCard,
    extraCountText: String? = null,
    modifier: Modifier = Modifier
) {
    VryvAvatarStack(
        avatarResList = avatars,
        modifier = modifier,
        size = size,
        borderColor = borderColor,
        extraCountText = extraCountText
    )
}

@Composable
fun VryvAvatarStack(
    avatarResIds: List<Int>,
    avatarSize: Dp = 24.dp,
    borderColor: Color = VryvCard,
    extraCountText: String? = null,
    modifier: Modifier = Modifier
) {
    VryvAvatarStack(
        avatarResList = avatarResIds,
        modifier = modifier,
        size = avatarSize,
        borderColor = borderColor,
        extraCountText = extraCountText
    )
}
