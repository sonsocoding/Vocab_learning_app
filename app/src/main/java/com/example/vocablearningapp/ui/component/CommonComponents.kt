package com.example.vocablearningapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.FsrsState
import com.example.vocablearningapp.domain.model.VocabularyItem
import com.example.vocablearningapp.domain.model.VocabularySet
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.AccentDark
import com.example.vocablearningapp.ui.theme.AccentSoft
import com.example.vocablearningapp.ui.theme.Border
import com.example.vocablearningapp.ui.theme.Canvas
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.InkSoft
import com.example.vocablearningapp.ui.theme.Muted
import com.example.vocablearningapp.ui.theme.Forgot
import com.example.vocablearningapp.ui.theme.ForgotSoft
import com.example.vocablearningapp.ui.theme.Learning
import com.example.vocablearningapp.ui.theme.LearningSoft
import com.example.vocablearningapp.ui.theme.Mastered
import com.example.vocablearningapp.ui.theme.MasteredSoft
import com.example.vocablearningapp.ui.theme.Surface
import com.example.vocablearningapp.ui.theme.SurfaceMuted

object VocabDimens {
    val ScreenPadding = 20.dp
    val SectionGap = 28.dp
    val ItemGap = 12.dp
    val CardRadius = 20.dp
    val SmallRadius = 14.dp
}

@Composable
fun SectionHeader(
    title: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = Ink
        )
        if (actionLabel != null && onAction != null) {
            TextAction(text = actionLabel, onClick = onAction)
        }
    }
}

@Composable
fun VocabProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = Accent
) {
    LinearProgressIndicator(
        progress = { progress.coerceIn(0f, 1f) },
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(50)),
        color = color,
        trackColor = SurfaceMuted
    )
}

@Composable
fun VocabularySetCard(
    set: VocabularySet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(VocabDimens.CardRadius),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(if (compact) 16.dp else 18.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(if (compact) 40.dp else 46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(AccentSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkBorder,
                        contentDescription = null,
                        tint = Accent,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = set.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${set.words.size} words · ${set.level} · ${set.category}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Muted
                    )
                }
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = Muted,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(if (compact) 14.dp else 18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VocabProgressBar(
                    progress = set.progress / 100f,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${set.progress}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = Accent,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun VocabularyRow(
    item: VocabularyItem,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailingContent: (@Composable RowScope.() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        shape = RoundedCornerShape(VocabDimens.SmallRadius),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.word,
                    style = MaterialTheme.typography.titleMedium,
                    color = Ink
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "${item.pronunciation} · ${item.partOfSpeech.label}",
                    style = MaterialTheme.typography.labelMedium,
                    color = Muted
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = item.meaning,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            FsrsStatePill(state = item.fsrsState)
            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}

@Composable
fun FsrsStatePill(state: FsrsState, modifier: Modifier = Modifier) {
    val (background, foreground) = fsrsColors(state)
    Surface(
        modifier = modifier,
        color = background,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = state.label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            style = MaterialTheme.typography.labelMedium,
            color = foreground
        )
    }
}

@Composable
fun StudyModeCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(VocabDimens.CardRadius),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(AccentSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Accent)
            }
            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Muted,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(VocabDimens.CardRadius),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = Accent, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.headlineSmall, color = Ink)
            Spacer(modifier = Modifier.height(3.dp))
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Muted)
        }
    }
}

@Composable
fun EmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(AccentSoft),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.BookmarkBorder, contentDescription = null, tint = Accent)
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(text = title, style = MaterialTheme.typography.titleMedium, color = Ink)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Muted)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, color = Ink) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Ink)
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Canvas)
    )
}

@Composable
fun AvatarButton(
    initials: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(AccentSoft)
    ) {
        Text(text = initials, color = AccentDark, fontWeight = FontWeight.Bold)
    }
}

fun fsrsColors(state: FsrsState): Pair<Color, Color> = when (state) {
    FsrsState.NEW -> SurfaceMuted to Muted
    FsrsState.LEARNING -> LearningSoft to Learning
    FsrsState.REVIEW -> MasteredSoft to Mastered
    FsrsState.RELEARNING -> ForgotSoft to Forgot
}
