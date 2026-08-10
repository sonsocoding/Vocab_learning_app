package com.example.vocablearningapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.vocablearningapp.domain.model.FsrsRating
import com.example.vocablearningapp.ui.theme.Accent
import com.example.vocablearningapp.ui.theme.Ink
import com.example.vocablearningapp.ui.theme.SurfaceMuted
import com.example.vocablearningapp.ui.theme.Surface

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(52.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Accent,
            contentColor = Surface
        )
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(19.dp))
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text = text)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink)
    ) {
        if (icon != null) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(7.dp))
        }
        Text(text = text)
    }
}

@Composable
fun TextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    androidx.compose.material3.TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = Accent)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = text)
            if (icon != null) {
                Spacer(modifier = Modifier.width(3.dp))
                Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun FsrsRatingButton(
    rating: FsrsRating,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val stateForColor = when (rating) {
        FsrsRating.AGAIN -> com.example.vocablearningapp.domain.model.FsrsState.RELEARNING
        FsrsRating.HARD -> com.example.vocablearningapp.domain.model.FsrsState.LEARNING
        FsrsRating.GOOD -> com.example.vocablearningapp.domain.model.FsrsState.REVIEW
        FsrsRating.EASY -> com.example.vocablearningapp.domain.model.FsrsState.REVIEW
    }
    val (_, foreground) = fsrsColors(stateForColor)
    val (background, _) = fsrsColors(stateForColor)
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(54.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = foreground,
            contentColor = Surface,
            disabledContainerColor = background,
            disabledContentColor = foreground
        )
    ) {
        Text(text = rating.label)
    }
}
