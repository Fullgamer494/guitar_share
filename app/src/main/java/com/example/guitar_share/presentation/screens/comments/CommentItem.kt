package com.example.guitar_share.presentation.screens.comments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.guitar_share.R

@Composable
fun CommentItem(
    comment: CommentData,
    onReplyClick: (CommentData) -> Unit
) {
    val isReply = comment.parentId != null
    val paddingStart = if (isReply) 48.dp else 16.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingStart, end = 16.dp, top = 8.dp, bottom = 8.dp)
    ) {
        // Author Profile Picture
        AsyncImage(
            model = comment.authorProfileUrl ?: R.drawable.changotristeconlibro,
            contentDescription = null,
            placeholder = painterResource(R.drawable.changotristeconlibro),
            error = painterResource(R.drawable.changotristeconlibro),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            // Box for name and content
            Column(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = comment.authorName,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = comment.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (comment.imageUrl != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = comment.imageUrl,
                        contentDescription = "Imagen del comentario",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }

            // Action buttons (Reply)
            Row(
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            ) {
                // "una jerarquización de solo dos niveles... generando una mención"
                // Always show reply button now.
                Text(
                    text = "Responder",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable { onReplyClick(comment) }
                        .padding(4.dp)
                )
            }
        }
    }
}
