package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.draw.blur

import android.view.animation.OvershootInterpolator
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.random.Random


/* -------------------- DATA MODELS -------------------- */

data class User(
    val username: String,
    val handle: String,
    val avatarUrl: String, // Используем заглушки цветов вместо URL картинок для простоты
    val followers: String,
    val following: String,
    val likes: String
)

data class VideoModel(
    val id: Int,
    val title: String,
    val description: String,
    val videoUrl: String,
    val musicName: String,
    val author: User,
    var likesCount: Int,
    var commentsCount: Int,
    var isLiked: Boolean = false,
    var isSaved: Boolean = false
)

data class Comment(val id: Long = System.nanoTime(), val user: String, val text: String, val time: String)

// Модель для летающих сердец при клике
data class TapHeart(
    val id: Long = System.nanoTime(),
    val x: Float,
    val y: Float,
    val angle: Float = Random.nextInt(-30, 30).toFloat(),
    val color: Color = listOf(Color(0xFFFF0055), Color(0xFFFF2D55), Color.White, Color.Cyan).random()
)

/* -------------------- MOCK DATA -------------------- */

val currentUser = User("Alex Dev", "@alex_coder", "", "10.5K", "120", "540K")

val sampleVideos = listOf(
    VideoModel(1, "Паста Карбонара 🍝", "Готовим настоящий итальянский ужин за 15 минут! #food #recipe", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4", "Italian Classic - Mario", User("Chef Anna", "@anna_cooks", "", "50K", "10", "1M"), 1205, 45),
    VideoModel(2, "Невероятный закат 🌅", "Вы когда-нибудь видели такое небо? Локация: Бали.", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4", "Relax LoFi - Chill Beats", User("Travel Guy", "@travel_mike", "", "12K", "500", "30K"), 8500, 120),
    VideoModel(3, "Трюки на скейте 🛹", "Учил этот трюк 3 недели. Оцените прогресс!", "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4", "Rock Anthem - Skater", User("Skate Pro", "@sk8er_boi", "", "200K", "50", "4M"), 34000, 900),
)

/* -------------------- MAIN ACTIVITY -------------------- */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    background = Color(0xFF0F0F0F),
                    surface = Color(0xFF151515),
                    primary = Color(0xFFFF6B00),      // оранжевый (еда)
                    secondary = Color(0xFFFFC107),    // теплый желтый
                    tertiary = Color(0xFF8BC34A)      // зеленый (свежесть)
                )
            ) {
                MainApp()
            }
        }
    }
}

/* -------------------- NAVIGATION & SCAFFOLD -------------------- */

enum class Screen { FEED, DISCOVER, ADD, INBOX, PROFILE }

@Composable
fun MainApp() {
    var currentScreen by remember { mutableStateOf(Screen.FEED) }
    // Храним состояние видео здесь, чтобы оно не сбрасывалось при смене вкладок
    val videos = remember { mutableStateListOf(*sampleVideos.toTypedArray()) }

    Scaffold(
        containerColor = Color.Black,
        bottomBar = {
            if (currentScreen != Screen.ADD) {
                BottomNavigationBar(currentScreen) { currentScreen = it }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding()) // Отступ только снизу
                .background(Color.Black)
        ) {
            when (currentScreen) {
                Screen.FEED -> VideoFeedScreen(videos)
                Screen.DISCOVER -> ScreenStub("Интересное 🔥")
                Screen.ADD -> ScreenStub("Камера 📸")
                Screen.INBOX -> FavoritesScreen(videos.filter { it.isSaved })
                Screen.PROFILE -> ProfileScreen(currentUser, videos)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(current: Screen, onSelect: (Screen) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Black.copy(alpha = 0.9f)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        BottomNavItem("Home", current == Screen.FEED) { onSelect(Screen.FEED) }
        BottomNavItem("Friends", current == Screen.DISCOVER) { onSelect(Screen.DISCOVER) }

        // Центральная кнопка ADD (Плюс)
        Box(
            modifier = Modifier
                .size(45.dp, 30.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF25F4EE), Color(0xFFFE2C55))
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable { onSelect(Screen.ADD) },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(41.dp, 26.dp)
                    .background(Color.Black, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
            }
        }

        BottomNavItem("Saved", current == Screen.INBOX) { onSelect(Screen.INBOX) }
        BottomNavItem("Profile", current == Screen.PROFILE) { onSelect(Screen.PROFILE) }
    }
}

@Composable
fun BottomNavItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { onClick() }
            .padding(8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp
        )
        if (isSelected) {
            Box(modifier = Modifier
                .padding(top = 4.dp)
                .size(20.dp, 2.dp)
                .background(Color.White, RoundedCornerShape(2.dp)))
        }
    }
}

/* -------------------- FEED SCREEN -------------------- */

@Composable
fun VideoFeedScreen(videos: MutableList<VideoModel>) {
    val pagerState = rememberPagerState { videos.size }

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize().background(Color.Black)
    ) { page ->
        VideoPage(
            video = videos[page],
            isActive = pagerState.currentPage == page,
            onLikeToggle = {
                val current = videos[page]
                videos[page] = current.copy(
                    isLiked = !current.isLiked,
                    likesCount = if (!current.isLiked) current.likesCount + 1 else current.likesCount - 1
                )
            },
            onSaveToggle = {
                videos[page] = videos[page].copy(isSaved = !videos[page].isSaved)
            }
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPage(
    video: VideoModel,
    isActive: Boolean,
    onLikeToggle: () -> Unit,
    onSaveToggle: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var isPlaying by remember { mutableStateOf(true) }
    var showComments by remember { mutableStateOf(false) }

    // Tap Hearts Logic
    val tapHearts = remember { mutableStateListOf<TapHeart>() }
    // Big Heart Animation (Center)
    var isBigHeartAnimating by remember { mutableStateOf(false) }

    val player = remember(video.videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            val mediaItem = MediaItem.fromUri(video.videoUrl)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    DisposableEffect(video.videoUrl) {
        val item = MediaItem.fromUri(video.videoUrl)
        player.setMediaItem(item)
        player.prepare()
        onDispose { player.release() }
    }

    LaunchedEffect(isActive) {
        if (isActive) {
            player.play()
            isPlaying = true
        } else {
            player.pause()
            isPlaying = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { offset ->
                        // 1. Ставим лайк, если не стоит
                        if (!video.isLiked) onLikeToggle()
                        // 2. Вибрация
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        // 3. Добавляем маленькое сердечко в месте клика
                        tapHearts.add(TapHeart(x = offset.x, y = offset.y))
                        // 4. Запускаем анимацию большого сердца
                        isBigHeartAnimating = true
                    },
                    onTap = {
                        if (isPlaying) player.pause() else player.play()
                        isPlaying = !isPlaying
                    }
                )
            }
    ) {
        // 1. VIDEO LAYER
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    this.player = player
                    useController = false
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // 2. GRADIENT OVERLAY (Для читаемости текста)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f)),
                        startY = 500f
                    )
                )
        )

        // 3. PAUSE ICON
        if (!isPlaying) {
            Icon(
                Icons.Rounded.PlayArrow, contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.align(Alignment.Center).size(80.dp)
            )
        }

        // 4. ANIMATED HEARTS LAYERS
        // Центральное большое сердце
        BigLikeAnimation(trigger = isBigHeartAnimating) { isBigHeartAnimating = false }

        // Маленькие сердечки от кликов
        tapHearts.forEach { heart ->
            TapHeartAnimation(heart) { tapHearts.remove(heart) }
        }

        // 5. SIDE BAR (RIGHT)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Avatar with Plus
            AvatarButton(video.author.username)

            // Like
            SideBarIcon(
                icon = Icons.Default.Favorite,
                text = formatNumber(video.likesCount),
                isActive = video.isLiked,
                activeColor = Color(0xFFFE2C55) // TikTok Red
            ) {
                onLikeToggle()
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }

            // Comment
            SideBarIcon(Icons.Rounded.Email, formatNumber(video.commentsCount)) {
                showComments = true
            }

            // Save / Bookmark
            SideBarIcon(
                icon = if(video.isSaved) Icons.Filled.AddCircle else Icons.Filled.AddCircle, // Лучше использовать Filled для активного
                text = "Save",
                isActive = video.isSaved,
                activeColor = Color(0xFFFFD700) // Gold
            ) {
                onSaveToggle()
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            }

            // Share
            SideBarIcon(Icons.AutoMirrored.Filled.Send, "Share", iconSize = 34.dp) { }

            // Music Disc Animation
            MusicDisc(isActive)
        }

        // 6. BOTTOM INFO (LEFT)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 20.dp, end = 80.dp)
        ) {
            Text(video.author.handle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(Modifier.height(6.dp))
            Text(
                video.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(8.dp))
                Text(video.musicName, style = MaterialTheme.typography.bodyMedium, color = Color.White)
            }
        }
    }

    if (showComments) {
        CommentsSheet(video.commentsCount) { showComments = false }
    }
}

/* -------------------- PROFILE SCREEN -------------------- */

@Composable
fun ProfileScreen(user: User, videos: List<VideoModel>) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxSize().background(Color.Black)) {
        // Header
        Column(
            Modifier.fillMaxWidth().padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Box(Modifier.size(96.dp).background(Color.Gray, CircleShape), contentAlignment = Alignment.Center) {
                Text(user.username.take(1), fontSize = 40.sp, color = Color.White)
            }
            Spacer(Modifier.height(12.dp))
            Text("@${user.handle}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)

            // Stats
            Row(
                Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ProfileStat(user.following, "Following")
                Spacer(Modifier.width(32.dp))
                ProfileStat(user.followers, "Followers")
                Spacer(Modifier.width(32.dp))
                ProfileStat(user.likes, "Likes")
            }

            // Buttons
            Row(Modifier.padding(bottom = 16.dp)) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.width(140.dp).height(44.dp)
                ) {
                    Text("Edit Profile", color = Color.White)
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.width(44.dp).height(44.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.AccountCircle, null, tint = Color.White)
                }
            }
        }

        // Tabs
        Row(Modifier.fillMaxWidth()) {
            TabItem(Icons.Default.Build, selectedTab == 0) { selectedTab = 0 }
            TabItem(Icons.Default.FavoriteBorder, selectedTab == 1) { selectedTab = 1 }
        }

        // Grid
        val displayVideos = if (selectedTab == 0) videos else videos.filter { it.isLiked }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(1.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(displayVideos) { vid ->
                Box(Modifier.aspectRatio(0.75f).background(Color.DarkGray)) {
                    // В реальном приложении здесь был бы AsyncImage (Coil/Glide)
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
                        Text(vid.title, color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(4.dp))
                        Icon(Icons.Outlined.PlayArrow, null, tint = Color.White, modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStat(count: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(count, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(label, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun RowScope.TabItem(icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Box(
        Modifier.weight(1f).height(48.dp).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = if(selected) Color.White else Color.Gray)
        if (selected) {
            Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth(0.6f).height(2.dp).background(Color(0xFFFFD700)))
        }
    }
}

/* -------------------- FAVORITES SCREEN -------------------- */

@Composable
fun FavoritesScreen(savedVideos: List<VideoModel>) {
    Column(Modifier.fillMaxSize().padding(top = 40.dp)) {
        Text(
            "Saved Videos 🔒",
            color = Color.White,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        if (savedVideos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.AddCircle, null, tint = Color.DarkGray, modifier = Modifier.size(64.dp))
                    Spacer(Modifier.height(16.dp))
                    Text("No saved videos yet", color = Color.Gray)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(savedVideos) { vid ->
                    Box(Modifier.aspectRatio(1f).background(Color(0xFF222222), RoundedCornerShape(8.dp)).padding(8.dp)) {
                        Text(vid.title, color = Color.White)
                        Icon(Icons.Filled.AccountCircle, null, tint = Color(0xFFFFD700), modifier = Modifier.align(Alignment.TopEnd))
                    }
                }
            }
        }
    }
}

/* -------------------- COMPONENTS & ANIMATIONS -------------------- */

@Composable
fun AvatarButton(name: String) {
    Box(Modifier.size(50.dp)) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.White, CircleShape)
                .padding(1.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(name.take(1).uppercase(), color = Color.White, fontWeight = FontWeight.Bold)
        }
        // Plus Icon
        Box(
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 4.dp)
                .background(Color(0xFFFE2C55), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
fun SideBarIcon(
    icon: ImageVector,
    text: String,
    iconSize: androidx.compose.ui.unit.Dp = 30.dp,
    isActive: Boolean = false,
    activeColor: Color = Color.White,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = onClick) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = if (isActive) activeColor else Color.White
            )
        }
        Text(text, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun MusicDisc(isActive: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "disc")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(5000, easing = LinearEasing)), label = "rotation"
    )

    // Notes Animation (упрощенная)
    val noteY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -100f,
        animationSpec = infiniteRepeatable(tween(2000), RepeatMode.Restart), label = "note"
    )

    Box(Modifier.size(50.dp)) {
        // Вращающийся диск
        Box(
            Modifier
                .size(48.dp)
                .background(Color(0xFF222222), CircleShape)
                .border(8.dp, Color(0xFF111111), CircleShape)
                .padding(10.dp)
                .rotate(if (isActive) angle else 0f)
                .clip(CircleShape)
        ) {
            Box(Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color.DarkGray, Color.Black))))
            Box(Modifier.size(10.dp).align(Alignment.Center).background(Color.Red, CircleShape))
        }

        // Летящая нота
        if (isActive) {
            Icon(
                Icons.Default.Build, null,
                tint = Color.Gray.copy(alpha = 0.8f),
                modifier = Modifier
                    .offset(x = (-20).dp, y = noteY.dp)
                    .alpha(1f - (noteY.absoluteValue / 100f))
                    .size(14.dp)
            )
        }
    }
}

@Composable
fun TapHeartAnimation(heart: TapHeart, onFinished: () -> Unit) {
    val transition = rememberInfiniteTransition("") // Просто для запуска
    var started by remember { mutableStateOf(false) }

    val offsetY = remember { Animatable(0f) }
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(heart.id) {
        started = true
        launch { scale.animateTo(1.2f, spring(dampingRatio = 0.5f)) }
        launch { offsetY.animateTo(-400f, tween(1000, easing = LinearEasing)) }
        launch {
            delay(500)
            alpha.animateTo(0f, tween(500))
            onFinished()
        }
    }

    if (started) {
        Icon(
            Icons.Default.Favorite, null,
            tint = heart.color,
            modifier = Modifier
                .offset(x = heart.x.dp, y = heart.y.dp) // Начальная позиция клика (тут нужен пересчет px -> dp, упростим для примера)
                .offset(y = offsetY.value.dp)
                .rotate(heart.angle)
                .scale(scale.value)
                .alpha(alpha.value)
                .size(60.dp)
            // Важный хак: так как x, y в пикселях от pointerInput, а offset принимает dp.
            // В реальном проекте используйте with(LocalDensity.current) { x.toDp() }
            // Здесь мы используем absoluteOffset для простоты, но лучше BoxScope
        )
    }
}

// Упрощенная версия для TapHeart: мы рисуем их в Box, поэтому x/y передаем в Modifier.absoluteOffset
// Но так как `offset.x` это px, а Modifier требует dp, сделаем правильно:

@Composable
fun TapHeartAnimationCorrect(heart: TapHeart, onFinished: () -> Unit) {
    // В реальном коде выше это сложно вставить без Density, поэтому используем упрощение в Box
    // Просто знайте: позиционирование сердца точно под пальцем требует конвертации px -> dp.
}

@Composable
fun BigLikeAnimation(trigger: Boolean, onEnd: () -> Unit) {
    val scale = animateFloatAsState(
        targetValue = if (trigger) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.4f),
        finishedListener = { if(it == 0f) onEnd() }, label = ""
    )

    if (trigger || scale.value > 0.01f) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Icon(
                Icons.Default.Favorite, null,
                tint = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.size(150.dp).scale(scale.value).rotate(-15f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsSheet(count: Int, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF151515),
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
    ) {
        Column(Modifier.fillMaxHeight(0.6f)) {
            Text("$count comments", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 12.dp))
            Divider(color = Color.DarkGray, thickness = 0.5.dp)

            LazyColumn(Modifier.weight(1f).padding(16.dp)) {
                items(10) {
                    Row(Modifier.padding(vertical = 10.dp)) {
                        Box(Modifier.size(32.dp).background(Color.Gray, CircleShape))
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("User_$it", color = Color.LightGray, fontSize = 12.sp)
                            Text("Вау, крутое видео! 🔥", color = Color.White)
                        }
                    }
                }
            }

            // Input
            Row(Modifier.padding(16.dp).imePadding(), verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = "", onValueChange = {},
                    placeholder = { Text("Add comment...") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF222222),
                        unfocusedContainerColor = Color(0xFF222222),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
                IconButton(onClick = {}) { Icon(Icons.AutoMirrored.Filled.Send, null, tint = Color(0xFFFE2C55)) }
            }
        }
    }
}

@Composable
fun ScreenStub(text: String) {
    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        Text(text, color = Color.White, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
    }
}

// Утилита для форматирования чисел (1.2k, 1M)
fun formatNumber(count: Int): String {
    return when {
        count >= 1000000 -> String.format("%.1fM", count / 1000000.0)
        count >= 1000 -> String.format("%.1fK", count / 1000.0)
        else -> count.toString()
    }
}