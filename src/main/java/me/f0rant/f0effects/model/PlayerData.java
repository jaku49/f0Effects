package me.f0rant.f0effects.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private String nickname;
    private String selectedEffect;
    private final Map<String, Integer> effectLevels = new HashMap<>();

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getSelectedEffect() { return selectedEffect; }
    public void setSelectedEffect(String effect) { this.selectedEffect = effect; }

    public int getLevel(String effect) { return effectLevels.getOrDefault(effect, 0); }
    public void setLevel(String effect, int level) { effectLevels.put(effect, level); }

    public Map<String, Integer> getEffectLevels() { return effectLevels; }

    public UUID getUuid() { return uuid; }
}