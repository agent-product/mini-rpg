# Mini RPG - Android Pixel Game

A lightweight, pixel-style idle RPG where the player taps once a day to "fight a monster" and level up. Built with Kotlin and Jetpack Compose.

## Features

### 🎮 Core Gameplay
- **Daily Tap Mechanic**: Fight one monster per day (resets at midnight)
- **Player Progression**: Level up by gaining XP, increase HP and stats
- **Monster Variety**: 6 different monsters with varying rarity and rewards
- **Battle System**: Random outcomes with critical hits and bonus rewards

### 🎨 Visual Design
- **Pixel Art Style**: Retro-inspired UI with custom color palette
- **Monster Sprites**: Colorful emoji-based monster representations
- **Rarity System**: Common, Uncommon, Rare, and Epic monsters with color coding
- **Progress Bars**: Visual HP and XP tracking

### 💾 Persistence
- **Local Storage**: Uses Android DataStore for game save data
- **Daily Tracking**: Remembers last fight date across app restarts
- **Battle History**: Keeps log of recent battles with timestamps

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Persistence**: Android DataStore Preferences
- **Concurrency**: Kotlin Coroutines
- **Serialization**: Kotlinx Serialization
- **Date/Time**: Kotlinx DateTime
- **Build System**: Gradle (KTS)
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36

## Project Structure

```
app/src/main/java/com/bruce/mini_rpg/
├── MainActivity.kt                 # Main entry point
├── model/                         # Data models
│   ├── Player.kt                 # Player stats and progression
│   ├── Monster.kt                # Monster definitions and database
│   ├── BattleLog.kt              # Battle history and messages
│   └── GameState.kt              # Overall game state
├── data/                         # Data layer
│   ├── GameDataStore.kt          # DataStore implementation
│   └── GameRepository.kt         # Business logic and data operations
├── viewmodel/                    # ViewModels
│   └── GameViewModel.kt          # UI state management
└── ui/                          # UI components
    ├── components/
    │   └── PixelComponents.kt    # Reusable UI components
    ├── screens/
    │   └── MainScreen.kt         # Main game screen
    └── theme/                    # App theming
        ├── Color.kt              # Color palette
        ├── Theme.kt              # Material3 theme
        └── Type.kt               # Typography
```

## Setup Instructions

### Prerequisites
- Android Studio (latest version)
- JDK 11 or higher
- Android SDK with API level 24+

### Installation
1. **Clone or download** this project
2. **Open** the project in Android Studio
3. **Wait** for Gradle sync to complete
4. **Build and run** the app on device or emulator

### First Run
- The app will create a new hero automatically
- Tap "Fight!" to battle your first monster
- Come back daily to continue your adventure!

## Game Mechanics

### Player Progression
- **Level**: Calculated as `floor(XP / 100) + 1`
- **HP**: Increases by 10 per level
- **XP**: Gained from battles, varies by monster rarity
- **Gold**: Currency for future shop features

### Monster System
- **6 Monster Types**: Slime, Goblin, Skeleton, Orc, Dragon, Demon
- **Rarity Levels**: Common (50%), Uncommon (30%), Rare (15%), Epic (5%)
- **Scaling Rewards**: Higher rarity = better XP and gold
- **Critical Hits**: 15% chance for 1.5x rewards

### Daily Mechanics
- **One Fight Per Day**: Based on local date
- **Automatic Reset**: At midnight local time
- **Idle Messages**: Hero gets bored after 3+ days without fighting

## Development Notes

### Key Components
- **GameRepository**: Handles all business logic and data persistence
- **GameViewModel**: Manages UI state and user interactions
- **MainScreen**: Primary UI with player stats, fight button, and battle log
- **PixelComponents**: Reusable UI components with pixel art styling

### Data Flow
1. User taps "Fight!" button
2. ViewModel calls repository to execute battle
3. Repository generates random battle outcome
4. Player stats updated and saved to DataStore
5. UI updates with battle results and new stats

### Customization
- **Add New Monsters**: Edit `MonsterDatabase` in `Monster.kt`
- **Adjust Balance**: Modify reward calculations in `GameRepository.kt`
- **Change Colors**: Update pixel color palette in `Color.kt`
- **Add Features**: Extend models and UI components as needed

## Future Enhancements

- 🏪 **Shop System**: Spend gold on equipment and upgrades
- 🎒 **Inventory**: Collect and manage items from battles
- 🏆 **Achievements**: Unlock rewards for various milestones
- 📊 **Statistics**: Track lifetime stats and battle analytics
- 🎵 **Sound Effects**: Add pixel-style audio feedback
- 🖼️ **Real Sprites**: Replace emoji with actual pixel art

## Screenshots

The app features a retro pixel art aesthetic with:
- Dark theme with green/gold accents
- Chunky borders and rounded corners
- Color-coded monster rarities
- Animated progress bars
- Battle result dialogs with celebration messages

## License

This project is created for educational purposes. Feel free to use and modify as needed.

---

**Have fun adventuring! 🗡️⚔️🛡️**
