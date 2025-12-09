package com.example.guitar_share.presentation.screens.forum

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.guitar_share.R
import com.example.guitar_share.presentation.components.bottom_navbar.BottomNavBar
import com.example.guitar_share.presentation.components.topbar.SharedTopBar
import com.example.guitar_share.presentation.screens.login.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ForumSocialScreen(
    navController: NavController,
    viewModel: ForumViewModel = viewModel(),
    authViewModel: AuthViewModel = koinViewModel() // Inject AuthViewModel
) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchSuggestions by viewModel.searchSuggestions.collectAsState()

    // Ensure user data is loaded
    LaunchedEffect(Unit) {
        authViewModel.fetchCurrentUser()
    }
    val currentUser = authViewModel.currentUserData

    val filteredPosts = if (searchQuery.isBlank()) {
        posts
    } else {
        posts.filter { post ->
            post.title.contains(searchQuery, ignoreCase = true) ||
                    post.body.contains(searchQuery, ignoreCase = true) ||
                    post.authorName.contains(searchQuery, ignoreCase = true) ||
                    post.tag.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            SharedTopBar(title = "Foro")
        },
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
                ForumHeader(
                    navController = navController,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { viewModel.onSearchQueryChanged(it) },
                    searchSuggestions = searchSuggestions,
                    onSuggestionClick = { suggestion ->
                        viewModel.onSearchQueryChanged(suggestion)
                    },
                    currentUserProfileUrl = currentUser?.profilePictureUrl
                )
            }

            item {
                when {
                    isLoading && posts.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    error != null && posts.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.changotristeconlibro),
                                    contentDescription = "Error",
                                    modifier = Modifier.size(120.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = error ?: "Error desconocido",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    filteredPosts.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.changotristeconlibro),
                                    contentDescription = "Sin resultados",
                                    modifier = Modifier.size(120.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = if (searchQuery.isBlank())
                                        "Aún no hay publicaciones en el foro"
                                    else
                                        "No se encontraron resultados",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }
            }

            items(filteredPosts, key = { it.id }) { post ->
                ForumPostCard(
                    post = post,
                    onCommentClick = { postId ->
                        navController.navigate("comments/$postId")
                    },
                    onVote = { postId, voteType ->
                         if (currentUser != null) {
                             viewModel.votePost(postId, currentUser.id, voteType)
                         }
                    }
                )
            }
        }
    }
}