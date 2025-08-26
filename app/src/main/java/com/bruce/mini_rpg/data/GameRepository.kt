package com.bruce.mini_rpg.data

import com.bruce.mini_rpg.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

class GameRepository(private val gameDataStore: GameDataStore) {
    
    val gameState: Flow<GameState> = gameDataStore.gameStateFlow
    
    suspend fun initializeGame(): GameState {
        return gameState.first()
    }
    
    suspend fun fightMonster(): BattleResult? {
        val currentState = gameState.first()
        
        // Check if player can fight today
        if (!currentState.canFightToday) {
            return null
        }
        
        // Get a random monster
        val monster = MonsterDatabase.getRandomMonster()
        
        // Calculate battle result
        val battleResult = calculateBattleResult(monster, currentState.player)
        
        // Update player stats
        val updatedPlayer = updatePlayerAfterBattle(currentState.player, battleResult)
        
        // Update game state
        val newGameState = currentState.withBattleResult(battleResult, monster, updatedPlayer)
        
        // Update last fight date
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .toString()
        
        val finalGameState = newGameState.copy(
            player = newGameState.player.copy(lastFightDate = today)
        )
        
        gameDataStore.updateGameState(finalGameState)
        
        return battleResult
    }
    
    private fun calculateBattleResult(monster: Monster, player: Player): BattleResult {
        // Base rewards
        var xpGained = monster.baseXpReward
        var goldGained = monster.baseGoldReward
        
        // Apply rarity multiplier
        xpGained = (xpGained * monster.rarity.multiplier).toInt()
        goldGained = (goldGained * monster.rarity.multiplier).toInt()
        
        // Random variance (±20%)
        val xpVariance = Random.nextInt(-20, 21) / 100f
        val goldVariance = Random.nextInt(-20, 21) / 100f
        
        xpGained = maxOf(1, (xpGained * (1 + xpVariance)).toInt())
        goldGained = maxOf(0, (goldGained * (1 + goldVariance)).toInt())
        
        // Critical hit chance (15%)
        val criticalHit = Random.nextFloat() < 0.15f
        if (criticalHit) {
            xpGained = (xpGained * 1.5f).toInt()
            goldGained = (goldGained * 1.5f).toInt()
        }
        
        // Player level bonus (5% per level above 1)
        val levelBonus = 1 + ((player.level - 1) * 0.05f)
        xpGained = (xpGained * levelBonus).toInt()
        goldGained = (goldGained * levelBonus).toInt()
        
        val message = BattleMessages.generateBattleMessage(
            monster, 
            BattleResult(
                victory = true,
                xpGained = xpGained,
                goldGained = goldGained,
                criticalHit = criticalHit,
                message = ""
            )
        )
        
        return BattleResult(
            victory = true,
            xpGained = xpGained,
            goldGained = goldGained,
            criticalHit = criticalHit,
            message = message
        )
    }
    
    private fun updatePlayerAfterBattle(player: Player, battleResult: BattleResult): Player {
        val newXp = player.xp + battleResult.xpGained
        val newGold = player.gold + battleResult.goldGained
        
        // Create updated player and check for level up
        return player.copy(
            xp = newXp,
            gold = newGold
        ).levelUpIfReady()
    }
    
    suspend fun checkDailyStatus() {
        val currentState = gameState.first()
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        
        val lastFightDate = currentState.player.lastFightDate
        
        if (lastFightDate != null) {
            try {
                val lastFight = kotlinx.datetime.LocalDate.parse(lastFightDate)
                val daysDiff = today.toEpochDays() - lastFight.toEpochDays()
                
                if (daysDiff > 0) {
                    // Update days without fight
                    val newDaysWithoutFight = if (daysDiff > 1) daysDiff.toInt() - 1 else 0
                    
                    val updatedState = currentState.copy(
                        canFightToday = true,
                        daysWithoutFight = newDaysWithoutFight
                    )
                    
                    gameDataStore.updateGameState(updatedState)
                }
            } catch (e: Exception) {
                // If we can't parse the date, reset to allow fighting
                val updatedState = currentState.copy(
                    canFightToday = true,
                    daysWithoutFight = 0
                )
                gameDataStore.updateGameState(updatedState)
            }
        }
    }
    
    fun getBoredMessage(daysWithoutFight: Int): String? {
        return when {
            daysWithoutFight >= 7 -> "Your hero has been idle for a week! They're getting very restless..."
            daysWithoutFight >= 3 -> "Your hero is bored and wants to fight!"
            else -> null
        }
    }
}
