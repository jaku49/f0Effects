<h1 align="center">⚔ f0Effects</h1>

<p align="center">
  <b>Advanced Kill Effect & Visuals System for Competitive Minecraft</b>
  <br>
  <i>Version 1.4.0 - Built for Performance and Style</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.19+-blue.svg">
  <img src="https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper-blue">
  <img src="https://img.shields.io/badge/Storage-MySQL%20%7C%20SQLite-blue">
  <img src="https://img.shields.io/badge/Colors-MiniMessage%20%26%20HEX-blue">
</p>

---

## 🎬 Preview

<p align="center">
  <a href="https://youtu.be/5trLWXZLhJA">
    <img src="https://github.com/jaku49/f0Effects/blob/main/images/8612b441-f561-4864-be6e-f0340d504cc1.png?raw=true" width="900" alt="f0Effects - YouTube Presentation">
  </a>
</p>

<p align="center">
  <b>Watch the full plugin presentation on YouTube</b>
</p>

---

## ✨ Overview

**f0Effects** is a high-performance reward system for PvP networks. It transforms every kill into a strategic advantage and a visual spectacle. With full **MiniMessage** and **HEX** support, it offers deep customization and a polished player experience.

### 🚀 Key Features
* **Modern Formatting:** Powered by **MiniMessage**. Use `<gradient>`, `<rainbow>`, and `<hover>` in messages and GUIs.
* **Player Empowerment:** A dedicated **Settings GUI** lets players customize volume, performance, and visibility.
* **Progressive Economy:** Integrated upgrade system where power scales with player investment.
* **Zero Lag Infrastructure:** Asynchronous data handling with **MySQL** or **SQLite** backends.
* **Clean UI/UX:** Responsive BossBars and intuitive GUI navigation.
* **Visual Cosmetics:** Extra kill visuals make every fight feel rewarding.

---

## ⚙ Advanced Settings GUI

We believe in player comfort. The **Settings Menu** allows users to toggle features based on hardware or preference:

* 🔊 **Dynamic Volume Slider:** 5-step volume control from 0% to 100% for kill sounds.
* ✨ **Performance Toggles:** Disable other players' particles to boost FPS.
* 📊 **Interface Control:** Toggle BossBars and Kill Messages on or off.
* 🥛 **Quick Reset:** Integrated "Milk Bucket" feature to instantly clear active potion effects.

---

## 🔗 Dependencies

The plugin requires the following dependencies to function properly:

* **Vault** + any economy plugin, for example **EssentialsX Economy** — handles currency and upgrades
* **PlaceholderAPI** *(optional)* — enables placeholder integration
* **Permissions plugin** (e.g. **LuckPerms**) — manages player permissions

> [!IMPORTANT]
> Make sure Vault is properly hooked into your economy plugin, otherwise the upgrade system will not work.
>
> To use the latest formatting features, ensure your server supports HEX colors (1.16+).

---

## ⌨ Commands & Permissions

Access the system through these aliases: `killeffects`, `ke`, `keffect`, `efectsfromkill`, `efekty`, `effects`, `f0ef`.

### 🕹 Player Commands
| Command | Description | Permission |
|:---|:---|:---|
| `/f0effects` | Opens the main GUI (Effects, Upgrades, Visuals, Settings). | `f0effects.use` |

### 💼 Admin Management Commands
| Command | Description | Permission |
|:---|:---|:---|
| `/f0effects reload` | Reloads config, messages, and database. | `f0effects.admin` |
| `/f0effects set <p> <effect> <lvl>` | Manually set a player's upgrade level for an effect. | `f0effects.admin` |
| `/f0effects clear <player>` | Clears the currently selected effect for a player. | `f0effects.admin` |
| `/f0effects about` | Information about the plugin, version, and configuration. | `f0effects.admin` |

> [!NOTE]
> The `<effect>` argument corresponds to the internal names: `SPEED`, `RESISTANCE`, `REGENERATION`, `STRENGTH`.

---

## 🧩 PlaceholderAPI Support

Customizable outputs through `messages.yml`. Use these in TAB, scoreboards, or other integrations.

### 📊 Player Statistics
| Placeholder | Description | Example |
|:---|:---|:---|
| `%f0effects_selected%` | Name of the active kill effect. | `Speed ⚡` |
| `%f0effects_selected_format%` | Formatted name + level. | `Speed (Lvl 2)` |
| `%f0effects_selected_duration%` | Active effect duration. | `15s` |
| `%f0effects_selected_visual%` | Name of the selected cosmetic. | `Blood Blast` |

### 💲 Economy & Levels
| Placeholder | Description |
|:---|:---|
| `%f0effects_level_<KEY>%` | Player's level for a specific effect. |
| `%f0effects_next_cost_<KEY>%` | Cost for the next upgrade, or `MAX`. |
| `%f0effects_is_max_<KEY>%` | Returns custom ON/OFF status if maxed. |

### ⚙ Settings & Preferences
| Placeholder | Description |
|:---|:---|
| `%f0effects_setting_volume%` | Current volume level, for example `50%`. |
| `%f0effects_setting_bossbar%` | Status of the BossBar toggle. |
| `%f0effects_setting_chat%` | Status of the kill messages toggle. |

---

## 📷 Interface & Interaction

The interface is designed for speed. No clutter, just clean navigation that gets players back into the fight.

### Effect Selection GUI
Players can toggle their active combat benefit with a single click in a clean, modern menu.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/140mm.png" width="800" alt="Effect Selection GUI"/>
</p>


### ⬆ Upgrade Shop
The upgrade menu clearly displays progression, showing exactly what is unlocked, what can be purchased, and the costs involved.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/140um.png" width="800" alt="Upgrade Shop"/>
</p>

### 👀 Visual Selection GUI
Beyond combat buffs, players can select visual animations in a dedicated GUI.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/140ve.png" width="800" alt="Visual Selection GUI"/>
</p>

### ⚙ Settings
Players can change their settings in a dedicated GUI.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/140sm.png" width="800" alt="Visual Selection GUI"/>
</p>
---

## 🛡 Combat Effects & Meta

| Effect | Level 3 Power | Duration | Cost |
|:---|:---|:---|:---|
| ⚡ **SPEED** | Speed III | 5s | 14,500$ |
| 🛡 **RESISTANCE** | Resistance III | 5s | 18,000$ |
| ❤ **REGENERATION** | Regen III | 5s | 21,500$ |
| ⚔ **STRENGTH** | Strength III | 5s | 25,000$ |

---

## 👁 Visual Cosmetics

Make every kill a spectacle. These visual effects turn combat into something memorable.

**Featured Visuals:**
* ⚡ **Lightning Bolt:** Strikes the ground with a thunderous roar.
* 🎆 **Firework Burst:** A celebratory explosion in the sky.
* 🔊 **Sonic Boom:** Uses the Warden's devastating shockwave effect.
* 💀 **Soul Reap:** Souls of the fallen rise from the ground.
* 🐉 **Dragon's Breath:** The ultimate cosmetic featuring the Ender Dragon's growl.

---

## ⚙ Technical Highlights

### 🐉 Dynamic BossBar
When an effect is triggered, an elegant BossBar appears at the top of the screen and acts as a countdown timer.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/javaw_dbVw7VrrTr.png" width="900" alt="BossBar Display"/>
</p>

---

## 🚀 Roadmap

- [x] Full MySQL / SQLite support
- [x] MiniMessage & HEX support
- [x] Advanced Settings GUI
- [x] Multi-language support (English, Polish)
- [x] Visual effects system
- [x] Admin `set`, `about` and `clear` command system
- [x] Particle customization
- [x] Settings menu
- [x] Transfer remaining messages to config
- [ ] Kill streak system
- [ ] WorldGuard region blacklist
- [ ] Effect cooldown system
- [ ] Custom kill messages (ActionBars)
- [ ] Per-arena configuration
- [x] Add support for German and Polish

---

<p align="center">
  <b>Built for competitive Minecraft networks.</b><br>
  <i>Empower your players. Reward the grind. Master the battlefield.</i>
</p>
