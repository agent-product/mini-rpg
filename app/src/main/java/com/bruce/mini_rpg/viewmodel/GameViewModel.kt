package com.bruce.mini_rpg.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bruce.mini_rpg.data.GameRepository
import com.bruce.mini_rpg.model.GameState
import com.bruce.mini_rpg.model.BattleResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {
    
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            // Initialize game and check daily status
            repository.checkDailyStatus()
            
            // Observe game state changes
            repository.gameState.collect { gameState ->
                _uiState.value = _uiState.value.copy(
                    gameState = gameState,
                    isLoading = false,
                    boredMessage = repository.getBoredMessage(gameState.daysWithoutFight)
                )
            }
        }
    }
    
    fun fightMonster() {
        if (_uiState.value.isFighting || !_uiState.value.gameState.canFightToday) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isFighting = true, lastBattleResult = null)
            
            try {
                val battleResult = repository.fightMonster()
                
                if (battleResult != null) {
                    _uiState.value = _uiState.value.copy(
                        isFighting = false,
                        lastBattleResult = battleResult,
                        showBattleResult = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isFighting = false,
                        errorMessage = "You've already fought today! Come back tomorrow."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isFighting = false,
                    errorMessage = "Battle failed: ${e.message}"
                )
            }
        }
    }
    
    fun dismissBattleResult() {
        _uiState.value = _uiState.value.copy(
            showBattleResult = false,
            lastBattleResult = null
        )
    }
    
    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun dismissBoredMessage() {
        _uiState.value = _uiState.value.copy(boredMessage = null)
    }
}

data class GameUiState(
    val gameState: GameState = GameState(),
    val isLoading: Boolean = true,
    val isFighting: Boolean = false,
    val lastBattleResult: BattleResult? = null,
    val showBattleResult: Boolean = false,
    val errorMessage: String? = null,
    val boredMessage: String? = null
)

class GameViewModelFactory(private val repository: GameRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
