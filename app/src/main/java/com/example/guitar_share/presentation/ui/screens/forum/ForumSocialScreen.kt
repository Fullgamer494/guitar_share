package com.example.guitar_share.presentation.ui.screens.forum

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.guitar_share.R
import com.example.guitar_share.presentation.ui.components.bottom_navbar.BottomNavBar

@Composable
fun ForumSocialScreen(navController: NavController) {
    val posts = remember {
        listOf(
            ForumPostData(
                id = 1,
                title = "Fullgamer",
                body = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s.",
                authorName = "Fullgamer",
                timeAgo = "Hace 2h",
                tag = "Técnica",
                likesCount = 200,
                commentsCount = 4,
                profilePictureUrl = "https://randomuser.me/api/portraits/men/1.jpg"
            ),
            ForumPostData(
                id = 2,
                title = "GuitaristPro",
                body = "When an unknown printer took a galley of type and scrambled it to make a type specimen book.",
                authorName = "GuitaristPro",
                timeAgo = "Hace 5h",
                tag = "Gear",
                likesCount = 42,
                commentsCount = 12,
                imageUrl = "https://images.unsplash.com/photo-1516924962500-2b4b3b99ea02?q=80&w=1000&auto=format&fit=crop"
            )
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ForumHeader(navController)
            }

            items(posts) { post ->
                ForumPostCard(post = post)
            }
        }
    }
}

@Composable
fun ForumHeader(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Foro",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )

        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Buscar...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.changotristeconlibro),
                contentDescription = "Mi Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
            )

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .weight(1f)
                    .height(45.dp),
                onClick = { navController.navigate("create_post") }
            ) {
                Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Publicar algo...", color = Color.Gray)
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Composable
fun ForumPostCard(post: ForumPostData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = post.profilePictureUrl ?: R.drawable.changotristeconlibro,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.changotristeconlibro),
                    error = painterResource(R.drawable.changotristeconlibro),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )

                Column {
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = post.tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Lorem Ipsum is simply",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.body,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                lineHeight = 20.sp
            )

            if (post.imageUrl != null) {
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = post.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.height(36.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        IconButton(onClick = { }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = "Up", modifier = Modifier.size(18.dp))
                        }
                        Text(
                            text = post.likesCount.toString(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        IconButton(onClick = { }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = "Down", modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.height(36.dp),
                    onClick = { }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        Icon(Icons.Default.Comment, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Comentarios",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}