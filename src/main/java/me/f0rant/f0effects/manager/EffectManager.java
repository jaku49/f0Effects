package me.f0rant.f0effects.manager;

import me.f0rant.f0effects.f0Effects;
import me.f0rant.f0effects.model.PlayerData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EffectManager {
    private final f0Effects plugin;
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    public EffectManager(f0Effects plugin) {
        this.plugin = plugin;
    }

    public PlayerData getPlayerData(UUID uuid) {
        return playerDataMap.computeIfAbsent(uuid, PlayerData::new);
    }

    public void unloadPlayer(UUID uuid) {
        playerDataMap.remove(uuid);
    }
}