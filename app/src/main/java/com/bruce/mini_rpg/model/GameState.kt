package com.bruce.mini_rpg.model

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val player: Player = Player(),
    val battleLog: BattleLog = BattleLog(),
    val currentMonster: Monster? = null,
    val canFightToday: Boolean = true,
    val daysWithoutFight: Int = 0
) {
    fun withUpdatedPlayer(newPlayer: Player): GameState {
        return copy(player = newPlayer)
    }
    
    fun withBattleResult(
        battleResult: BattleResult,
        monster: Monster,
        newPlayer: Player
    ): GameState {
        val battleEntry = BattleLogEntry(
            id = java.util.UUID.randomUUID().toString(),
            timestamp = kotlinx.datetime.Clock.System.now().toString(),
            monster = monster,
            battleResult = battleResult,
            playerLevelBefore = player.level,
            playerLevelAfter = newPlayer.level
        )
        
        return copy(
            player = newPlayer,
            battleLog = battleLog.addEntry(battleEntry),
            currentMonster = null,
            canFightToday = false,
            daysWithoutFight = 0
        )
    }
}
