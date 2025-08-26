package com.bruce.mini_rpg.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bruce.mini_rpg.ui.theme.*
import com.bruce.mini_rpg.model.MonsterRarity

@Composable
fun PixelButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors()
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .border(
                BorderStroke(2.dp, if (enabled) PixelBlack else PixelGray),
                RoundedCornerShape(4.dp)
            ),
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(4.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PixelCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.outline,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .border(
                BorderStroke(2.dp, borderColor),
                RoundedCornerShape(8.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun StatBar(
    label: String,
    current: Int,
    max: Int,
    modifier: Modifier = Modifier,
    barColor: Color = PixelGreen,
    backgroundColor: Color = PixelDarkGray
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$current/$max",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(backgroundColor)
                .border(1.dp, PixelBlack, RoundedCornerShape(4.dp))
        ) {
            val progress = if (max > 0) current.toFloat() / max.toFloat() else 0f
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .clip(RoundedCornerShape(4.dp))
                    .background(barColor)
            )
        }
    }
}

@Composable
fun MonsterSprite(
    monsterSprite: String,
    rarity: MonsterRarity,
    modifier: Modifier = Modifier
) {
    val rarityColor = when (rarity) {
        MonsterRarity.COMMON -> CommonColor
        MonsterRarity.UNCOMMON -> UncommonColor
        MonsterRarity.RARE -> RareColor
        MonsterRarity.EPIC -> EpicColor
    }
    
    Box(
        modifier = modifier
            .size(96.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(PixelDarkGray)
            .border(3.dp, rarityColor, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        // For now, we'll use a colored box as placeholder for monster sprites
        // In a real app, you would load actual pixel art images here
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(getMonsterColor(monsterSprite)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = getMonsterEmoji(monsterSprite),
                fontSize = 32.sp
            )
        }
    }
}

@Composable
fun RarityBadge(
    rarity: MonsterRarity,
    modifier: Modifier = Modifier
) {
    val rarityColor = when (rarity) {
        MonsterRarity.COMMON -> CommonColor
        MonsterRarity.UNCOMMON -> UncommonColor
        MonsterRarity.RARE -> RareColor
        MonsterRarity.EPIC -> EpicColor
    }
    
    Surface(
        modifier = modifier,
        color = rarityColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = rarity.displayName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

// Helper functions for monster appearance
private fun getMonsterColor(sprite: String): Color {
    return when (sprite) {
        "slime" -> PixelLightGreen
        "goblin" -> PixelBrown
        "skeleton" -> PixelLightGray
        "orc" -> PixelDarkGreen
        "dragon" -> PixelRed
        "demon" -> PixelPurple
        else -> PixelGray
    }
}

private fun getMonsterEmoji(sprite: String): String {
    return when (sprite) {
        "slime" -> "🟢"
        "goblin" -> "👹"
        "skeleton" -> "💀"
        "orc" -> "🧌"
        "dragon" -> "🐉"
        "demon" -> "😈"
        else -> "❓"
    }
}
