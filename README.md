<h1 align="center">⚔ f0Effects</h1>

<p align="center">
  <b>Advanced Kill Effect & Visuals System for Competitive Minecraft</b>
  <br>
  <i>Version 1.3.0 - Built for Performance and Style</i>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.19+-blue.svg">
  <img src="https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper-blue">
  <img src="https://img.shields.io/badge/Storage-MySQL%20%7C%20SQLite-blue">
  <img src="https://img.shields.io/badge/Colors-HEX%20Support-blue">
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

**f0Effects** is a premium-grade reward system for PvP servers. It bridges the gap between gameplay and aesthetics by granting players temporary combat "Potions" and stunning visual animations upon every kill.

### Key Features:
* **Dynamic Balancing:** Higher levels grant massive power but for a shorter, high-adrenaline window.
* **Modern Aesthetics:** Full **HEX Color (&#RRGGBB)** support for a truly custom look.
* **Zero Lag:** Optimized data handling with **MySQL** or **SQLite** backend.
* **Total Immersion:** Integrated **BossBar** timers and custom soundscapes.

---

---

## 🔗 Dependencies

The plugin requires the following dependencies to function properly:

* **Vault** + any economy plugin (e.g. EssentialsX Economy) — handles currency and upgrade system  
* **PlaceholderAPI** *(optional)* — enables placeholder integration  
* **Permissions plugin** (e.g. LuckPerms) — manages player permissions  

> [!IMPORTANT]  
> Make sure Vault is properly hooked into your economy plugin, otherwise the upgrade system will not work.

## ⌨ Commands & Permissions

Access the system through a wide variety of aliases: `killeffects`, `ke`, `keffect`, `efectsfromkill`, `efekty`, `effects`, `f0ef`.

### Player Commands
| Command | Description | Permission |
|:---|:---|:---|
| `/f0effects` | Opens the main GUI (Effects, Upgrades, Visuals). | `f0effects.use` |

### Admin Management Commands
| Command | Description | Permission |
|:---|:---|:---|
| `/f0effects reload` | Instantly reloads config, messages, and database. | `f0effects.admin` |
| `/f0effects set <p> <effect> <lvl>` | Manually set a player's upgrade level for an effect. | `f0effects.admin` |
| `/f0effects clear <player>` | Clears the currently selected effect for a player. | `f0effects.admin` |

> [!NOTE]
> The `<effect>` argument for the admin command corresponds to the internal names: `SPEED`, `RESISTANCE`, `REGENERATION`, `STRENGTH`.

---

## 📷 Interface & Interaction

The interface is designed for speed. No clutter, just clean navigation to get players back into the fight.

### Effect Selection GUI
Players can toggle their active combat benefit with a single click in a clean, modern menu.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/javaw_6wogNPlXTz.png" width="800" alt="Effect Selection GUI"/>
</p>
<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/javaw_xb4LLcnzcT.png" width="800" alt="Effect Selection GUI"/>
</p>

### Upgrade Shop
The upgrade menu clearly displays progression, showing exactly what is unlocked, what can be purchased, and the costs involved.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/javaw_C9DqOnKdBo.png" width="800" alt="Upgrade Shop"/>
</p>

---

## 🛡 Combat Effects (The Meta)

| Effect | Power (Lvl 3) | Duration (Lvl 3) | Total Cost |
|:---|:---|:---|:---|
| ⚡ **SPEED** | Speed III | 5 Seconds | 14,500$ |
| 🛡 **RESISTANCE** | Resistance III | 5 Seconds | 18,000$ |
| ❤ **REGENERATION** | Regen III | 5 Seconds | 21,500$ |
| ⚔ **STRENGTH** | Strength III | 5 Seconds | 25,000$ |

---

## 👁 Visual Cosmetics

Make every kill a spectacle. Beyond combat buffs, players can select visual animations in a dedicated GUI.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/javaw_0JtG4W0P57.png" width="800" alt="Visual Selection GUI"/>
</p>

**Featured Visuals:**
* ⚡ **Lightning Bolt:** Strikes the ground with a thunderous roar.
* 🎆 **Firework Burst:** A celebratory explosion in the sky.
* 🔊 **Sonic Boom:** Uses the Warden's devastating shockwave effect.
* 💀 **Soul Reap:** Souls of the fallen rise from the ground.
* 🐉 **Dragon's Breath:** The ultimate cosmetic featuring the Ender Dragon's growl.

---

## ⚙ Technical Highlights

### Dynamic BossBar
When an effect is triggered, an elegant BossBar appears at the top of the screen to act as a countdown timer.

<p align="center">
  <img src="https://raw.githubusercontent.com/jaku49/f0Effects/main/images/javaw_dbVw7VrrTr.png" width="900" alt="BossBar Display"/>
</p>

### PlaceholderAPI Support
* `%f0effects_selected%` — Active effect name.
* `%f0effects_selected_format%` — Formatted name + level.
* `%f0effects_level_<EFFECT>%` — Current level of a specific perk.

---

## 🚀 Roadmap

- [x] SQLite support
- [x] Full MySQL Cross-Server Support
- [x] Admin `set` and `clear` command system
- [X] Visual effects system
- [ ] Effect cooldown system
- [ ] Per-arena configuration
- [X] Particle customization
- [X] Transferring the remaining messages to the config
- [ ] Add support for Polish (the author's native language) and German.


---

<p align="center">
  <b>Built for competitive Minecraft networks.</b><br>
  <i>Empower your players. Reward the grind. Master the battlefield.</i>
</p>
