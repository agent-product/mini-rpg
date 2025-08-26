package com.bruce.mini_rpg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bruce.mini_rpg.data.GameDataStore
import com.bruce.mini_rpg.data.GameRepository
import com.bruce.mini_rpg.ui.screens.MainScreen
import com.bruce.mini_rpg.ui.theme.MiniRPGTheme
import com.bruce.mini_rpg.viewmodel.GameViewModel
import com.bruce.mini_rpg.viewmodel.GameViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MiniRPGTheme {
                MiniRPGApp()
            }
        }
    }
}

@Composable
fun MiniRPGApp() {
    // Create dependencies
    val gameDataStore = remember { GameDataStore(context = androidx.compose.ui.platform.LocalContext.current) }
    val gameRepository = remember { GameRepository(gameDataStore) }
    val viewModel: GameViewModel = viewModel(factory = GameViewModelFactory(gameRepository))
    
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        MainScreen(
            uiState = uiState,
            onFightClick = { viewModel.fightMonster() },
            onDismissBattleResult = { viewModel.dismissBattleResult() },
            onDismissError = { viewModel.dismissError() },
            onDismissBoredMessage = { viewModel.dismissBoredMessage() },
            modifier = Modifier.padding(innerPadding)
        )
    }
}