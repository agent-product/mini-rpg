package com.bruce.mini_rpg.model

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class BattleLogEntry(
    val id: String,
    val timestamp: String, // ISO timestamp string
    val monster: Monster,
    val battleResult: BattleResult,
    val playerLevelBefore: Int,
    val playerLevelAfter: Int
)

@Serializable
data class BattleResult(
    val victory: Boolean = true, // Always true in this simple version
    val xpGained: Int,
    val goldGained: Int,
    val criticalHit: Boolean = false,
    val message: String
)

// Battle message templates for variety
object BattleMessages {
    private val victoryMessages = listOf(
        "You defeated the {monster}!",
        "The {monster} falls before your might!",
        "Victory! The {monster} has been slain!",
        "You emerge victorious against the {monster}!",
        "The {monster} crumbles to dust!",
        "Your blade finds its mark! The {monster} is defeated!"
    )
    
    private val criticalMessages = listOf(
        "Critical hit! You devastate the {monster}!",
        "A perfect strike! The {monster} didn't stand a chance!",
        "Your weapon glows with power as you strike the {monster}!",
        "Lightning-fast reflexes! Critical hit on the {monster}!",
        "The {monster} staggers from your devastating blow!"
    )
    
    private val lootMessages = listOf(
        "You found {gold} gold coins!",
        "The {monster} drops {gold} gold!",
        "You search the remains and find {gold} gold!",
        "Treasure! You discover {gold} gold pieces!",
        "Your victory yields {gold} gold coins!"
    )
    
    fun getRandomVictoryMessage(monsterName: String): String {
        return victoryMessages.random().replace("{monster}", monsterName)
    }
    
    fun getRandomCriticalMessage(monsterName: String): String {
        return criticalMessages.random().replace("{monster}", monsterName)
    }
    
    fun getRandomLootMessage(monsterName: String, gold: Int): String {
        return lootMessages.random()
            .replace("{monster}", monsterName)
            .replace("{gold}", gold.toString())
    }
    
    fun generateBattleMessage(monster: Monster, result: BattleResult): String {
        val baseMessage = if (result.criticalHit) {
            getRandomCriticalMessage(monster.name)
        } else {
            getRandomVictoryMessage(monster.name)
        }
        
        val xpMessage = "You gained ${result.xpGained} XP!"
        val goldMessage = if (result.goldGained > 0) {
            getRandomLootMessage(monster.name, result.goldGained)
        } else null
        
        return listOfNotNull(baseMessage, xpMessage, goldMessage).joinToString(" ")
    }
}

@Serializable
data class BattleLog(
    val entries: List<BattleLogEntry> = emptyList()
) {
    fun addEntry(entry: BattleLogEntry): BattleLog {
        val newEntries = (listOf(entry) + entries).take(10) // Keep only last 10 battles
        return copy(entries = newEntries)
    }
    
    fun getRecentEntries(count: Int = 5): List<BattleLogEntry> {
        return entries.take(count)
    }
}
