package com.bruce.mini_rpg.model

import kotlinx.serialization.Serializable

@Serializable
data class Monster(
    val id: String,
    val name: String,
    val hp: Int,
    val maxHp: Int,
    val sprite: String, // Resource name or drawable identifier
    val baseXpReward: Int,
    val baseGoldReward: Int,
    val rarity: MonsterRarity = MonsterRarity.COMMON
)

@Serializable
enum class MonsterRarity(val displayName: String, val multiplier: Float) {
    COMMON("Common", 1.0f),
    UNCOMMON("Uncommon", 1.5f),
    RARE("Rare", 2.0f),
    EPIC("Epic", 3.0f)
}

// Predefined monsters for the game
object MonsterDatabase {
    val monsters = listOf(
        Monster(
            id = "slime",
            name = "Green Slime",
            hp = 25,
            maxHp = 25,
            sprite = "slime",
            baseXpReward = 10,
            baseGoldReward = 2,
            rarity = MonsterRarity.COMMON
        ),
        Monster(
            id = "goblin",
            name = "Goblin Warrior",
            hp = 40,
            maxHp = 40,
            sprite = "goblin",
            baseXpReward = 15,
            baseGoldReward = 5,
            rarity = MonsterRarity.COMMON
        ),
        Monster(
            id = "skeleton",
            name = "Skeleton Archer",
            hp = 60,
            maxHp = 60,
            sprite = "skeleton",
            baseXpReward = 20,
            baseGoldReward = 8,
            rarity = MonsterRarity.UNCOMMON
        ),
        Monster(
            id = "orc",
            name = "Orc Berserker",
            hp = 80,
            maxHp = 80,
            sprite = "orc",
            baseXpReward = 30,
            baseGoldReward = 12,
            rarity = MonsterRarity.UNCOMMON
        ),
        Monster(
            id = "dragon",
            name = "Fire Dragon",
            hp = 150,
            maxHp = 150,
            sprite = "dragon",
            baseXpReward = 50,
            baseGoldReward = 25,
            rarity = MonsterRarity.RARE
        ),
        Monster(
            id = "demon",
            name = "Shadow Demon",
            hp = 200,
            maxHp = 200,
            sprite = "demon",
            baseXpReward = 75,
            baseGoldReward = 40,
            rarity = MonsterRarity.EPIC
        )
    )
    
    fun getRandomMonster(): Monster {
        // Weighted random selection based on rarity
        val weights = monsters.map { 
            when (it.rarity) {
                MonsterRarity.COMMON -> 50
                MonsterRarity.UNCOMMON -> 30
                MonsterRarity.RARE -> 15
                MonsterRarity.EPIC -> 5
            }
        }
        
        val totalWeight = weights.sum()
        val randomValue = (0 until totalWeight).random()
        
        var currentWeight = 0
        for (i in monsters.indices) {
            currentWeight += weights[i]
            if (randomValue < currentWeight) {
                return monsters[i]
            }
        }
        
        return monsters.first() // Fallback
    }
}
