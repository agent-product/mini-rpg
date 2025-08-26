package com.bruce.mini_rpg.model

import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val level: Int = 1,
    val xp: Int = 0,
    val hp: Int = 100,
    val maxHp: Int = 100,
    val gold: Int = 0,
    val lastFightDate: String? = null // ISO date string
) {
    /**
     * Calculate the current level based on XP
     * Level = floor(xp / 100) + 1
     */
    fun getCurrentLevel(): Int = (xp / 100) + 1
    
    /**
     * Calculate XP needed for next level
     */
    fun getXpForNextLevel(): Int = (getCurrentLevel() * 100) - xp
    
    /**
     * Calculate XP progress for current level (0-99)
     */
    fun getXpProgress(): Int = xp % 100
    
    /**
     * Level up the player if they have enough XP
     */
    fun levelUpIfReady(): Player {
        val newLevel = getCurrentLevel()
        if (newLevel > level) {
            // Increase max HP by 10 per level and restore full HP
            val newMaxHp = maxHp + (10 * (newLevel - level))
            return copy(
                level = newLevel,
                hp = newMaxHp,
                maxHp = newMaxHp
            )
        }
        return this
    }
}
