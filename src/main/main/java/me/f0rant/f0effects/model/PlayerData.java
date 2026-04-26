package me.f0rant.f0effects.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private String nickname;
    
    private String selectedEffect;
    private final Map<String, Integer> effectLevels = new HashMap<>();
    
    private String selectedVisual;
    private final Set<String> unlockedVisuals = new HashSet<>();

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

    public String getSelectedVisual() { return selectedVisual; }
    public void setSelectedVisual(String visual) { this.selectedVisual = visual; }

    public Set<String> getUnlockedVisuals() { return unlockedVisuals; }
    public void unlockVisual(String visual) { unlockedVisuals.add(visual); }

    public UUID getUuid() { return uuid; }
}