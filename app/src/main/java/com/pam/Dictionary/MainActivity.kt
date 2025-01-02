package com.pam.Dictionary

import WordInfoItem
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pam.Dictionary.feature_dictionary.data.local.entity.WordInfoEntity
import com.pam.Dictionary.feature_dictionary.presentation.WordInfoViewModel
import com.pam.Dictionary.ui.theme.DictionaryAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DictionaryAppTheme {
                val navController = rememberNavController()
                MainApp(navController)
            }
        }
    }
}

@Composable
fun MainApp(navController: NavHostController) {
    val scaffoldState = rememberScaffoldState() // Declare here

    Scaffold(
        scaffoldState = scaffoldState, // Pass to Scaffold
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "search",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("favorites") { FavoritesScreen() }
            composable("search") { SearchScreen(scaffoldState = scaffoldState) } // Pass scaffoldState
            composable("history") { HistoryScreen() }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation {
        val items = listOf(
            "favorites" to Icons.Default.Favorite,
            "search" to Icons.Default.Search,
            "history" to Icons.Default.Info
        )

        items.forEach { (route, icon) ->
            BottomNavigationItem(
                icon = { Icon(icon, contentDescription = route) },
                label = { Text(route.capitalize()) },
                selected = navController.currentBackStackEntry?.destination?.route == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun SearchScreen(viewModel: WordInfoViewModel = hiltViewModel(), scaffoldState: ScaffoldState) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is WordInfoViewModel.UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }


    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            TextField(
                value = viewModel.searchQuery.value,
                onValueChange = viewModel::onSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Search ...") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.wordInfoItems.size) { i ->
                    val wordInfo = state.wordInfoItems[i]
                    if (i > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    WordInfoItem(wordInfo = wordInfo, onClick = {
                        viewModel.toggleFavorite(wordInfo.word, wordInfo.meanings)
                        Toast.makeText(context, "Word add to favorites", Toast.LENGTH_SHORT).show()
                    })
                    if (i < state.wordInfoItems.size - 1) {
                        Divider()
                    }
                }
            }
        }
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun FavoritesScreen(viewModel: WordInfoViewModel = hiltViewModel()) {
    val favoriteItems =  viewModel.favorite.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadFavorite()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Column {
            if (favoriteItems.value.isNotEmpty()) {
                LazyColumn {
                    items(favoriteItems.value.size) { index ->
                        val item = favoriteItems.value[index]
                        FavoriteItemView(
                            favoriteItem = item,
                            onDelete = { viewModel.deleteFavoriteItem(item.id!!)
                                Toast.makeText(context, "Word add to favorites", Toast.LENGTH_SHORT).show()
                            },
                            onTap = {
                                val intent = Intent(context,WordInfoActivity::class.java)
                                intent.putExtra("word" , item?.toWordInfo())
                                context.startActivity(intent)
                            }

                        )
                    }
                }
            } else {
                Text(
                    text = "No favorite words available",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun FavoriteItemView(favoriteItem: WordInfoEntity, onDelete: () -> Unit , onTap: () -> Unit) {

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Align items to the start
        ) {
            TextButton (onClick = onTap ){
                Text(text = favoriteItem.word,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black)

            }


            Spacer(modifier = Modifier.weight(1f)) // Flexible space between Text and Button
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }

        }

    }
}



@Composable
fun HistoryScreen(viewModel: WordInfoViewModel = hiltViewModel()) {
    val historyItems = viewModel.history.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Column {
            if (historyItems.value.isNotEmpty()) {
                LazyColumn {
                    items(historyItems.value.size) { index ->
                        val item = historyItems.value[index]
                        HistoryItemView(
                            historyItem = item,
                            onDelete = { viewModel.deleteHistoryItem(item.word) },
                            onTap = {
                                val intent = Intent(context,WordInfoActivity::class.java)
                                intent.putExtra("word" , item?.toWordInfo())
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = "No history available",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}


@Composable
fun HistoryItemView(historyItem: WordInfoEntity, onDelete: () -> Unit , onTap: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Align items to the start
        ) {
            TextButton (onClick = onTap ){
                Text(text = historyItem.word,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black)

            }

            Spacer(modifier = Modifier.weight(1f)) // Flexible space between Text and Button
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete ${historyItem.word}")
            }

        }

    }
}

