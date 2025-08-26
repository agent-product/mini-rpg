package com.bruce.mini_rpg.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bruce.mini_rpg.model.BattleLogEntry
import com.bruce.mini_rpg.model.MonsterDatabase
import com.bruce.mini_rpg.ui.components.*
import com.bruce.mini_rpg.ui.theme.*
import com.bruce.mini_rpg.viewmodel.GameUiState
import kotlinx.datetime.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    uiState: GameUiState,
    onFightClick: () -> Unit,
    onDismissBattleResult: () -> Unit,
    onDismissError: () -> Unit,
    onDismissBoredMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title
        Text(
            text = "Mini RPG",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Player Stats Card
        PlayerStatsCard(
            player = uiState.gameState.player,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Fight Section
        FightSection(
            canFight = uiState.gameState.canFightToday,
            isFighting = uiState.isFighting,
            onFightClick = onFightClick,
            modifier = Modifier.fillMaxWidth()
        )
        
        // Battle Log
        BattleLogSection(
            battleEntries = uiState.gameState.battleLog.getRecentEntries(),
            modifier = Modifier.fillMaxWidth().weight(1f)
        )
    }
    
    // Battle Result Dialog
    if (uiState.showBattleResult && uiState.lastBattleResult != null) {
        BattleResultDialog(
            battleResult = uiState.lastBattleResult,
            onDismiss = onDismissBattleResult
        )
    }
    
    // Error Dialog
    uiState.errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = onDismissError,
            title = { Text("Error") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = onDismissError) {
                    Text("OK")
                }
            }
        )
    }
    
    // Bored Message Dialog
    uiState.boredMessage?.let { message ->
        AlertDialog(
            onDismissRequest = onDismissBoredMessage,
            title = { Text("Your Hero") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = onDismissBoredMessage) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun PlayerStatsCard(
    player: com.bruce.mini_rpg.model.Player,
    modifier: Modifier = Modifier
) {
    PixelCard(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(
            text = "Hero Stats",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Level: ${player.level}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Gold: ${player.gold} 🪙",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = PixelGold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        StatBar(
            label = "Health",
            current = player.hp,
            max = player.maxHp,
            barColor = PixelRed
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        StatBar(
            label = "Experience",
            current = player.getXpProgress(),
            max = 99,
            barColor = PixelBlue
        )
        
        Text(
            text = "Total XP: ${player.xp} | Next Level: ${player.getXpForNextLevel()} XP",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun FightSection(
    canFight: Boolean,
    isFighting: Boolean,
    onFightClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    PixelCard(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            text = "Daily Battle",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        if (canFight) {
            Text(
                text = "A monster awaits! Ready to fight?",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Show a random monster preview
            val previewMonster = remember { MonsterDatabase.getRandomMonster() }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MonsterSprite(
                    monsterSprite = previewMonster.sprite,
                    rarity = previewMonster.rarity,
                    modifier = Modifier.size(80.dp)
                )
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = previewMonster.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    RarityBadge(rarity = previewMonster.rarity)
                    Text(
                        text = "HP: ${previewMonster.hp}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        } else {
            Text(
                text = "You've already fought today!\nCome back tomorrow for another battle.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        PixelButton(
            text = if (isFighting) "Fighting..." else "Fight!",
            onClick = onFightClick,
            enabled = canFight && !isFighting,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (canFight) PixelRed else PixelGray,
                contentColor = PixelWhite
            )
        )
    }
}

@Composable
fun BattleLogSection(
    battleEntries: List<BattleLogEntry>,
    modifier: Modifier = Modifier
) {
    PixelCard(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Text(
            text = "Battle Log",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (battleEntries.isEmpty()) {
            Text(
                text = "No battles yet. Fight your first monster!",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(battleEntries) { entry ->
                    BattleLogItem(entry = entry)
                }
            }
        }
    }
}

@Composable
fun BattleLogItem(
    entry: BattleLogEntry,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MonsterSprite(
                monsterSprite = entry.monster.sprite,
                rarity = entry.monster.rarity,
                modifier = Modifier.size(48.dp)
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.monster.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "+${entry.battleResult.xpGained} XP, +${entry.battleResult.goldGained} Gold",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                if (entry.playerLevelAfter > entry.playerLevelBefore) {
                    Text(
                        text = "LEVEL UP! ${entry.playerLevelBefore} → ${entry.playerLevelAfter}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PixelGold
                    )
                }
            }
            
            Text(
                text = formatTimestamp(entry.timestamp),
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BattleResultDialog(
    battleResult: com.bruce.mini_rpg.model.BattleResult,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (battleResult.criticalHit) "Critical Victory!" else "Victory!",
                color = if (battleResult.criticalHit) PixelGold else PixelGreen
            )
        },
        text = {
            Column {
                Text(battleResult.message)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Rewards:",
                    fontWeight = FontWeight.Bold
                )
                Text("• ${battleResult.xpGained} Experience Points")
                Text("• ${battleResult.goldGained} Gold Coins")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Awesome!")
            }
        }
    )
}

private fun formatTimestamp(timestamp: String): String {
    return try {
        val instant = Instant.parse(timestamp)
        val now = kotlinx.datetime.Clock.System.now()
        val diff = now - instant
        
        when {
            diff.inWholeMinutes < 1 -> "Just now"
            diff.inWholeMinutes < 60 -> "${diff.inWholeMinutes}m ago"
            diff.inWholeHours < 24 -> "${diff.inWholeHours}h ago"
            else -> "${diff.inWholeDays}d ago"
        }
    } catch (e: Exception) {
        "Recently"
    }
}
