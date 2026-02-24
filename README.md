<h1 align="center">⚔ f0Effect</h1>
<p align="center">
  Advanced Kill Effect System for Competitive Minecraft PvP Servers
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Minecraft-1.19+-blue.svg">
  <img src="https://img.shields.io/badge/Platform-Spigot%20%7C%20Paper-blue">
  <img src="https://img.shields.io/badge/Storage-MySQL-blue">
  <img src="https://img.shields.io/badge/License-MIT-blue">
  <img src="https://img.shields.io/badge/Status-Active%20Development-blue">
</p>

---

## ✨ Overview

**f0Effect** is a lightweight but powerful PvP arena plugin that grants temporary combat effects after killing another player.

Each effect can be upgraded:
- Higher level → stronger amplifier  
- Higher level → shorter duration  
- Higher level → higher cost  

Designed for competitive servers where balance, speed and clean UI matter.

---

# 🎮 Main Effect Selection GUI

<p align="center">
  <img src="images/gui-selection.png" alt="Effect Selection GUI" width="800"/>
</p>

### What players can do:
- Select one active effect
- View their current upgrade level
- Access the upgrade shop
- Read system explanation

Minimal layout. Fast interaction. Clear feedback.

---

# ⬆ Upgrade Shop GUI

<p align="center">
  <img src="images/gui-upgrades.png" alt="Upgrade Shop GUI" width="800"/>
</p>

### Upgrade Logic

| Level | Duration | Power | Cost |
|-------|----------|--------|------|
| I     | 10s      | Base   | Low  |
| II    | 7s       | Medium | High |
| III   | 5s       | High   | Very High |

This ensures:
- No long-term snowballing
- Short but impactful bursts
- High skill ceiling

---

# 📊 BossBar Display

<p align="center">
  <img src="images/bossbar.png" alt="BossBar Preview" width="800"/>
</p>

When an effect is active:

- Blue BossBar appears
- Displays effect name
- Displays level
- Automatically disappears

Fully configurable color and style.

---

# 🔥 Default Effects

| Effect       | Description |
|-------------|------------|
| ⚡ SPEED        | Movement boost after kill |
| 🛡 RESISTANCE   | Damage reduction |
| ❤ REGENERATION | Health recovery |
| ⚔ STRENGTH     | Increased attack power |

All durations are measured in ticks.  
20 ticks = 1 second.

---

# ⚙ Configuration

Fully customizable:

- Messages (HEX colors supported)
- GUI layout and materials
- Sounds
- MySQL database
- Effect costs and scaling
- BossBar style and color

### Official References

Materials:  
https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html  

Sounds:  
https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html  

---

# 🛠 Installation

1. Drop the plugin into `/plugins`
2. Restart the server
3. Edit `config.yml`
4. Done

Supports modern Spigot and Paper builds.

---
Clean modular design for easier maintenance and future expansion.

---

# 🚀 Roadmap

### Planned Features

- [ ] PlaceholderAPI support  
- [ ] Vault economy integration improvements  
- [ ] Per-arena effect configuration  
- [ ] Cooldown system  
- [ ] Statistics tracking (kills with effect active)  
- [ ] Effect particle customization  
- [ ] Redis support for network servers  

---

# 📈 Why f0Effect?

- Built for PvP balance
- Short, impactful mechanics
- No unnecessary complexity
- Clean GUI
- Clear feedback system
- Competitive-focused design

---

# 📜 License

MIT License  
You are free to modify and redistribute.

---

# 🤝 Support

If you encounter issues:

- Open a GitHub issue
- Provide server version
- Provide plugin version
- Attach console logs

---

<p align="center">
  Made for competitive Minecraft servers.
</p>
