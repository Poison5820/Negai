package com.example.ui.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.request.ImageRequest
import coil.request.videoFrameMicros
import com.example.data.PlaybackProgressEntity
import com.example.data.PlaybackResumeHelper
import com.example.model.VideoResolutionFormatter
import com.example.ui.components.LastPlayedFileButton
import com.example.ui.components.ResolutionBadgeChip
import com.example.ui.components.formatTimeMs
import com.example.ui.i18n.tr

@Composable
internal fun RecentTabContent(
    recentItems: List<PlaybackProgressEntity>,
    compactMode: Boolean = false,
    onResumeRecent: (PlaybackProgressEntity) -> Unit,
    onOpenSingleFile: () -> Unit = {},
    onClearHistory: () -> Unit = {},
    lastPlayedFile: PlaybackProgressEntity? = null,
    resumePlaybackEnabled: Boolean = true,
    onOpenLastPlayed: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val videoThumbnailLoader = remember(context) {
        ImageLoader.Builder(context)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .crossfade(true)
            .build()
    }

    if (recentItems.isEmpty() && lastPlayedFile == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(44.dp)
                    )
                    Text(
                        text = tr("История просмотра пуста", "Watch history is empty"),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = tr(
                            "Откройте любую папку или отдельный видеофайл — прогресс воспроизведения сохраняется автоматически.",
                            "Open any folder or video file — playback progress is saved automatically."
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FilledTonalButton(onClick = onOpenSingleFile) {
                        Icon(Icons.Default.VideoFile, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tr("Открыть видеофайл", "Open video file"))
                    }
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 1200.dp)
            .testTag("recent_lazy_list"),
        contentPadding = PaddingValues(
            horizontal = if (compactMode) 12.dp else 16.dp,
            vertical = if (compactMode) 6.dp else 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(if (compactMode) 8.dp else 10.dp)
    ) {
        item(key = "recent_header_row") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tr("Недавно просмотренные (${recentItems.size})", "Recently watched (${recentItems.size})"),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (recentItems.isNotEmpty()) {
                    TextButton(
                        onClick = onClearHistory,
                        modifier = Modifier.testTag("recent_clear_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(tr("Очистить", "Clear"))
                    }
                }
            }
        }

        if (lastPlayedFile != null) {
            item(key = "recent_last_played_hero") {
                LastPlayedFileButton(
                    item = lastPlayedFile,
                    resumeEnabled = resumePlaybackEnabled,
                    compactMode = compactMode,
                    onClick = onOpenLastPlayed
                )
            }
        }

        items(
            items = recentItems,
            key = { it.videoUri }
        ) { item ->
            val progress = if (item.durationMs > 0) {
                (item.positionMs.toFloat() / item.durationMs.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
            val resumePosMs = PlaybackResumeHelper.computeResumePositionMs(
                positionMs = item.positionMs,
                durationMs = item.durationMs,
                resumeEnabled = resumePlaybackEnabled
            )
            val canExtractThumbnail = remember(item.videoUri) {
                val lower = item.videoUri.lowercase()
                lower.startsWith("content://") || lower.startsWith("file://") || lower.startsWith("/")
            }
            val frameMicros = remember(item.positionMs, item.durationMs) {
                val targetMs = when {
                    item.positionMs > 1_000L -> item.positionMs
                    item.durationMs > 10_000L -> (item.durationMs / 6L).coerceAtMost(30_000L)
                    else -> 3_000L
                }
                targetMs * 1_000L
            }
            Card(
                shape = RoundedCornerShape(if (compactMode) 14.dp else 18.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(if (compactMode) 14.dp else 18.dp))
                    .clickable { onResumeRecent(item) }
                    .testTag("recent_item_card")
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = if (compactMode) 10.dp else 14.dp,
                        vertical = if (compactMode) 8.dp else 11.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(if (compactMode) 72.dp else 82.dp)
                                .height(if (compactMode) 44.dp else 50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                .testTag("recent_item_thumbnail_box"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = tr("Продолжить", "Resume"),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.72f),
                                modifier = Modifier.size(20.dp)
                            )
                            if (canExtractThumbnail) {
                                val imageRequest = remember(item.videoUri, frameMicros) {
                                    ImageRequest.Builder(context)
                                        .data(
                                            if (item.videoUri.startsWith("/")) {
                                                java.io.File(item.videoUri)
                                            } else {
                                                Uri.parse(item.videoUri)
                                            }
                                        )
                                        .videoFrameMicros(frameMicros)
                                        .size(240, 140)
                                        .crossfade(true)
                                        .build()
                                }
                                AsyncImage(
                                    model = imageRequest,
                                    imageLoader = videoThumbnailLoader,
                                    contentDescription = item.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(Color(0x99060A12)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            if (item.durationMs > 0L) {
                                Surface(
                                    color = Color(0xBF050810),
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(2.dp)
                                ) {
                                    Text(
                                        text = formatTimeMs(item.positionMs),
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 3.5.dp, vertical = 0.5.dp)
                                    )
                                }
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            val resBadge = VideoResolutionFormatter.formatBadge(
                                width = item.videoWidth,
                                height = item.videoHeight,
                                fileName = item.title
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                if (resBadge != null) {
                                    ResolutionBadgeChip(
                                        badge = resBadge,
                                        compact = true,
                                        modifier = Modifier.testTag("recent_item_resolution_badge")
                                    )
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.folderName} • ${formatTimeMs(item.positionMs)} / ${formatTimeMs(item.durationMs)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                if (resumePosMs > 0L) {
                                    Text(
                                        text = tr(
                                            "с ${formatTimeMs(resumePosMs)}",
                                            "from ${formatTimeMs(resumePosMs)}"
                                        ),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.SemiBold
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(if (compactMode) 6.dp else 10.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                }
            }
        }
    }
}
