package com.example.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.InterFontFamily
import com.example.ui.theme.VryvBg
import com.example.ui.theme.VryvLime
import com.example.ui.theme.VryvPink
import com.example.ui.theme.VryvTextPrimary
import com.example.ui.theme.VryvTextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/*
 * Motion-заставка «ВРЫВ».
 *
 * Идея вдохновлена логотипом: граффити-надпись, розовый мазок кисти и корона.
 * Таймлайн (~2.8 с):
 *  0.00 – 0.45  розовый мазок кисти пролетает по диагонали экрана (угол как у подчёркивания в лого)
 *  0.30 – 0.80  надпись «ВРЫВ» «врывается»: масштаб 2.2 → 1, диагональный wipe-reveal за кистью
 *  0.80         удар: тряска кадра, вспышка, разлёт брызг краски (розовые / лаймовые / белые)
 *  0.85 – 1.45  корона падает сверху с вращением и пружинит на место
 *  1.30 – 1.90  пульсирующее розовое свечение + слоган и лаймовая полоска загрузки
 *  2.35 – 2.80  выход: заставка приближается и растворяется, открывая приложение
 * Тап по экрану — пропустить.
 */

// Доли изображения logo.png (1171×1078), где находится корона.
private const val CROWN_LEFT = 0.29f
private const val CROWN_RIGHT = 0.675f
private const val CROWN_BOTTOM = 0.375f
private const val LOGO_ASPECT = 1171f / 1078f

private val SlamEasing = CubicBezierEasing(0.12f, 0.9f, 0.2f, 1f)

private data class PaintDrop(
    val angle: Float,
    val distance: Float,
    val radius: Float,
    val color: Color,
    val delay: Float
)

@Composable
fun VryvSplashScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentOnFinished by rememberUpdatedState(onFinished)

    val sweep = remember { Animatable(0f) }      // мазок кисти по экрану
    val reveal = remember { Animatable(0f) }     // wipe-reveal надписи
    val slam = remember { Animatable(0f) }       // масштаб/прозрачность надписи
    val impact = remember { Animatable(0f) }     // тряска + брызги + вспышка
    val crownDrop = remember { Animatable(0f) }  // падение короны
    val glow = remember { Animatable(0f) }       // свечение за логотипом
    val tagline = remember { Animatable(0f) }    // слоган
    val progress = remember { Animatable(0f) }   // полоска загрузки
    val exit = remember { Animatable(0f) }       // уход заставки

    val drops = remember {
        val rnd = Random(13)
        val palette = listOf(VryvPink, VryvPink, VryvPink, VryvLime, Color.White)
        List(34) {
            PaintDrop(
                angle = rnd.nextFloat() * 2f * PI.toFloat(),
                distance = 0.45f + rnd.nextFloat() * 0.75f,
                radius = 2f + rnd.nextFloat() * 6f,
                color = palette[rnd.nextInt(palette.size)],
                delay = rnd.nextFloat() * 0.25f
            )
        }
    }

    LaunchedEffect(Unit) {
        launch { sweep.animateTo(1f, tween(450, easing = FastOutSlowInEasing)) }
        delay(300)
        launch { reveal.animateTo(1f, tween(420, easing = LinearEasing)) }
        slam.animateTo(1f, tween(500, easing = SlamEasing))
        launch { impact.animateTo(1f, tween(650, easing = LinearEasing)) }
        delay(50)
        launch {
            crownDrop.animateTo(
                1f,
                spring(dampingRatio = 0.42f, stiffness = Spring.StiffnessMediumLow)
            )
        }
        delay(450)
        launch { glow.animateTo(1f, tween(600, easing = FastOutSlowInEasing)) }
        launch { tagline.animateTo(1f, tween(500, easing = FastOutSlowInEasing)) }
        progress.animateTo(1f, tween(1000, easing = FastOutSlowInEasing))
        exit.animateTo(1f, tween(450, easing = FastOutSlowInEasing))
        currentOnFinished()
    }

    val density = LocalDensity.current
    val shakePx = with(density) { 10.dp.toPx() }

    // Тряска кадра после удара
    val ip = impact.value
    val shakeX = if (ip in 0.001f..0.6f) {
        val p = ip / 0.6f
        sin(p * PI.toFloat() * 9f) * (1f - p) * shakePx
    } else 0f
    val shakeY = if (ip in 0.001f..0.6f) {
        val p = ip / 0.6f
        cos(p * PI.toFloat() * 7f) * (1f - p) * shakePx * 0.5f
    } else 0f

    Box(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = 1f - exit.value
                val s = 1f + 0.14f * exit.value
                scaleX = s
                scaleY = s
            }
            .background(VryvBg)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { currentOnFinished() }
            .testTag("vryv_splash"),
        contentAlignment = Alignment.Center
    ) {
        // 1. Фон: розовое свечение, мазок кисти, брызги, вспышка
        Canvas(modifier = Modifier.fillMaxSize()) {
            val c = Offset(size.width / 2f, size.height * 0.44f)

            // Пульсирующее свечение за логотипом
            val g = glow.value
            if (g > 0f) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(VryvPink.copy(alpha = 0.32f * g), Color.Transparent),
                        center = c,
                        radius = size.width * (0.55f + 0.1f * g)
                    ),
                    radius = size.width * (0.55f + 0.1f * g),
                    center = c
                )
            }

            // Диагональный мазок кисти через весь экран
            val sw = sweep.value
            if (sw > 0f && sw < 1f || (sw >= 1f && slam.value < 1f)) {
                rotate(degrees = -14f, pivot = c) {
                    val y = c.y + size.width * 0.28f
                    val startX = -size.width * 0.3f
                    val endX = size.width * 1.3f
                    val head = startX + (endX - startX) * sw
                    val tail = startX + (endX - startX) * (sw - 0.45f).coerceAtLeast(0f) / 0.55f
                    val fade = (1f - slam.value).coerceIn(0f, 1f)
                    // основная полоса
                    drawLine(
                        color = VryvPink.copy(alpha = 0.95f * fade),
                        start = Offset(tail, y),
                        end = Offset(head, y),
                        strokeWidth = size.width * 0.075f,
                        cap = StrokeCap.Round
                    )
                    // «рваные» волокна кисти
                    for (i in -2..2) {
                        drawLine(
                            color = VryvPink.copy(alpha = 0.45f * fade),
                            start = Offset(tail + size.width * 0.06f * (i + 2), y + i * size.width * 0.02f),
                            end = Offset(head - size.width * 0.02f * (2 - kotlin.math.abs(i)), y + i * size.width * 0.02f),
                            strokeWidth = size.width * 0.008f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }

            // Вспышка в момент удара
            if (ip > 0f && ip < 0.35f) {
                val f = 1f - ip / 0.35f
                drawRect(color = Color.White.copy(alpha = 0.10f * f))
                drawCircle(
                    color = VryvPink.copy(alpha = 0.55f * f),
                    radius = size.width * (0.2f + 0.6f * (ip / 0.35f)),
                    center = c,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = size.width * 0.012f * f + 1f)
                )
            }

            // Брызги краски
            if (ip > 0f && ip < 1f) {
                val maxDist = size.width * 0.62f
                drops.forEach { d ->
                    val local = ((ip - d.delay) / (1f - d.delay)).coerceIn(0f, 1f)
                    if (local > 0f && local < 1f) {
                        val eased = 1f - (1f - local) * (1f - local) * (1f - local)
                        val dist = maxDist * d.distance * eased
                        val pos = Offset(c.x + cos(d.angle) * dist, c.y + sin(d.angle) * dist + local * local * size.width * 0.08f)
                        drawCircle(
                            color = d.color.copy(alpha = (1f - local)),
                            radius = (d.radius * (1f - 0.5f * local)).dp.toPx(),
                            center = pos
                        )
                    }
                }
            }
        }

        // 2. Логотип + слоган
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset { androidx.compose.ui.unit.IntOffset(shakeX.toInt(), shakeY.toInt()) },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = 56.dp)
                    .fillMaxWidth()
                    .aspectRatio(LOGO_ASPECT)
            ) {
                // Надпись «ВРЫВ» с нижним мазком (всё, кроме короны)
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "ВРЫВ",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val s = 2.2f - 1.2f * slam.value
                            scaleX = s
                            scaleY = s
                            alpha = slam.value.coerceIn(0f, 1f)
                            rotationZ = -6f * (1f - slam.value)
                            transformOrigin = TransformOrigin(0.5f, 0.7f)
                        }
                        .drawWithContent {
                            val w = size.width
                            val h = size.height
                            val skew = w * 0.35f
                            val x = -skew + (w + skew * 2f) * reveal.value
                            val wipe = Path().apply {
                                moveTo(-skew, 0f)
                                lineTo(x + skew, 0f)
                                lineTo(x, h)
                                lineTo(-skew, h)
                                close()
                            }
                            clipPath(wipe) {
                                clipRect(
                                    left = w * CROWN_LEFT,
                                    top = 0f,
                                    right = w * CROWN_RIGHT,
                                    bottom = h * CROWN_BOTTOM,
                                    clipOp = ClipOp.Difference
                                ) {
                                    this@drawWithContent.drawContent()
                                }
                            }
                        }
                )

                // Корона — падает отдельно и пружинит
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            val p = crownDrop.value
                            translationY = -size.height * 0.9f * (1f - p)
                            rotationZ = -28f * (1f - p)
                            alpha = (p * 3f).coerceIn(0f, 1f)
                            val s = 0.7f + 0.3f * p
                            scaleX = s
                            scaleY = s
                            transformOrigin = TransformOrigin(
                                (CROWN_LEFT + CROWN_RIGHT) / 2f,
                                CROWN_BOTTOM * 0.6f
                            )
                        }
                        .drawWithContent {
                            clipRect(
                                left = size.width * CROWN_LEFT,
                                top = 0f,
                                right = size.width * CROWN_RIGHT,
                                bottom = size.height * CROWN_BOTTOM
                            ) {
                                this@drawWithContent.drawContent()
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Слоган
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.graphicsLayer {
                    alpha = tagline.value
                    translationY = (1f - tagline.value) * 24.dp.toPx()
                }
            ) {
                Text(
                    text = "Куда сегодня в Минске?",
                    color = VryvTextPrimary,
                    fontFamily = InterFontFamily,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Афиша", "Свайпы", "Компания").forEachIndexed { i, label ->
                        if (i > 0) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(VryvPink)
                            )
                        }
                        Text(
                            text = label,
                            color = VryvTextSecondary,
                            fontFamily = InterFontFamily,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // 3. Лаймовая полоска загрузки внизу
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .width(120.dp)
                .height(4.dp)
                .graphicsLayer { alpha = tagline.value }
                .clip(CircleShape)
                .background(Color(0x1FFFFFFF))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = progress.value
                        transformOrigin = TransformOrigin(0f, 0.5f)
                    }
                    .background(VryvLime)
            )
        }
    }
}
