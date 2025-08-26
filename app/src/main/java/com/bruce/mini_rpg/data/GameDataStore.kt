package com.bruce.mini_rpg.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.bruce.mini_rpg.model.GameState
import com.bruce.mini_rpg.model.Player
import com.bruce.mini_rpg.model.BattleLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "game_preferences")

class GameDataStore(private val context: Context) {
    
    private object PreferencesKeys {
        val PLAYER_DATA = stringPreferencesKey("player_data")
        val BATTLE_LOG_DATA = stringPreferencesKey("battle_log_data")
        val LAST_FIGHT_DATE = stringPreferencesKey("last_fight_date")
        val DAYS_WITHOUT_FIGHT = intPreferencesKey("days_without_fight")
    }
    
    private val json = Json { 
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    
    val gameStateFlow: Flow<GameState> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val playerJson = preferences[PreferencesKeys.PLAYER_DATA]
            val battleLogJson = preferences[PreferencesKeys.BATTLE_LOG_DATA]
            val lastFightDate = preferences[PreferencesKeys.LAST_FIGHT_DATE]
            val daysWithoutFight = preferences[PreferencesKeys.DAYS_WITHOUT_FIGHT] ?: 0
            
            val player = if (playerJson != null) {
                try {
                    json.decodeFromString<Player>(playerJson).copy(lastFightDate = lastFightDate)
                } catch (e: Exception) {
                    Player(lastFightDate = lastFightDate)
                }
            } else {
                Player(lastFightDate = lastFightDate)
            }
            
            val battleLog = if (battleLogJson != null) {
                try {
                    json.decodeFromString<BattleLog>(battleLogJson)
                } catch (e: Exception) {
                    BattleLog()
                }
            } else {
                BattleLog()
            }
            
            GameState(
                player = player,
                battleLog = battleLog,
                daysWithoutFight = daysWithoutFight,
                canFightToday = canFightToday(lastFightDate)
            )
        }
    
    suspend fun updateGameState(gameState: GameState) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PLAYER_DATA] = json.encodeToString(gameState.player)
            preferences[PreferencesKeys.BATTLE_LOG_DATA] = json.encodeToString(gameState.battleLog)
            gameState.player.lastFightDate?.let { date ->
                preferences[PreferencesKeys.LAST_FIGHT_DATE] = date
            }
            preferences[PreferencesKeys.DAYS_WITHOUT_FIGHT] = gameState.daysWithoutFight
        }
    }
    
    suspend fun updateLastFightDate(date: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_FIGHT_DATE] = date
        }
    }
    
    suspend fun updateDaysWithoutFight(days: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DAYS_WITHOUT_FIGHT] = days
        }
    }
    
    private fun canFightToday(lastFightDate: String?): Boolean {
        if (lastFightDate == null) return true
        
        return try {
            val today = kotlinx.datetime.Clock.System.now()
                .toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault())
                .date
                .toString()
            
            lastFightDate != today
        } catch (e: Exception) {
            true // If we can't parse the date, allow fighting
        }
    }
}
