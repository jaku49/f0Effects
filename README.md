# 🧪 f0Effects

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21-green?style=for-the-badge&logo=minecraft)
![Engine](https://img.shields.io/badge/Engine-Paper%20%2F%20Purpur-blue?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

[English Documentation](#-english-version) | [Polska Dokumentacja](#-polska-wersja)

---

### 👥 Autorzy / Credits
* **f0rant** – pomysł i realizacja projektu.
* **AI** – wsparcie techniczne i optymalizacja kodu.

---

## 🇺🇸 English Version

**f0Effects** is a professional kill-effects system designed for competitive Minecraft servers. It allows players to select unique combat bonuses that activate immediately after defeating an opponent.

### 🔥 Key Features
* **Kill Effects**: Choose between Speed, Resistance, or Regeneration to gain an edge in battle.
* **3-Tier Upgrade System**: Spend server economy to increase effect duration and potency (Amplifier).
* **Interactive GUIs**: Beautifully crafted menus for effect selection and the upgrade shop.
* **Advanced MySQL**: High-performance data management using HikariCP for zero-lag database operations.
* **Custom Sounds**: Immersive audio feedback for every action (buying, selecting, level-up).

### ⚙️ Technical Requirements
* **Java 21**.
* **Paper/Purpur 1.21**.
* **MySQL/MariaDB** database.
* **Vault** (for economy integration).

### 💻 Commands
| Command | Description | Permission |
| :--- | :--- | :--- |
| `/efekty` | Opens the main effects menu | *None* |
| `/f0ef reload` | Reloads the configuration | `f0effects.admin` |
| `/f0ef set <nick> <effect> <lvl>` | Manually sets player's level | `f0effects.admin` |

---

## 🇵🇱 Polska Wersja

**f0Effects** to profesjonalny system efektów po zabójstwie, stworzony z myślą o serwerach PvP i Survival. Pozwala graczom na wybór unikalnych bonusów, które aktywują się natychmiast po wyeliminowaniu przeciwnika.

### 🔥 Główne Funkcje
* **Efekty po Zabiciu**: Wybieraj między Szybkością, Odpornością a Regeneracją.
* **3 Poziomy Ulepszeń**: Inwestuj walutę serwerową, aby wydłużyć czas trwania i zwiększyć moc (Amplifier) swoich efektów.
* **Intuicyjne GUI**: Przejrzyste menu wyboru efektów oraz dedykowany sklep z ulepszeniami.
* **Wydajność MySQL**: Zoptymalizowana obsługa bazy danych przez HikariCP (brak lagów przy zapisie).
* **Efekty Dźwiękowe**: Klimatyczne dźwięki przy zakupach, awansach i wyborze bonusów.

### ⚙️ Wymagania
* **Java 21**.
* **Paper/Purpur 1.21**.
* **Baza MySQL/MariaDB**.
* **Vault** (wymagany do działania sklepu).

### 💻 Komendy i Uprawnienia
| Komenda | Opis | Uprawnienie |
| :--- | :--- | :--- |
| `/efekty` | Otwiera menu wyboru efektów | *brak* |
| `/f0ef reload` | Przeładowuje plik config.yml | `f0effects.admin` |
| `/f0ef set <nick> <efekt> <lvl>` | Ustawia poziom efektu gracza | `f0effects.admin` |

---
Created with ❤️ by **f0rant** & **AI**.
