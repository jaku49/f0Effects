package me.f0rant.f0effects.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerData {
    private final UUID uuid;
    private String selectedEffect = "NONE";
    private String selectedVisual = "NONE";
    private List<String> unlockedVisuals = new ArrayList<>();
    private Map<String, Integer> levels = new HashMap<>();

    private boolean ownEffectsEnabled = true;
    private boolean otherEffectsEnabled = true;
    private boolean chatMessagesEnabled = true;
    private boolean bossBarEnabled = true;
    private float volume = 1.0f;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() { return uuid; }

    public String getSelectedEffect() { return selectedEffect; }
    public void setSelectedEffect(String selectedEffect) { this.selectedEffect = selectedEffect; }

    public String getSelectedVisual() { return selectedVisual; }
    public void setSelectedVisual(String selectedVisual) { this.selectedVisual = selectedVisual; }

    public List<String> getUnlockedVisuals() { return unlockedVisuals; }
    public void setUnlockedVisuals(List<String> unlockedVisuals) { this.unlockedVisuals = unlockedVisuals; }
    public void unlockVisual(String visual) {
        if (!unlockedVisuals.contains(visual)) unlockedVisuals.add(visual);
    }

    public int getLevel(String effect) {
        return levels.getOrDefault(effect, 0);
    }
    public void setLevel(String effect, int level) {
        levels.put(effect, level);
    }
    public Map<String, Integer> getLevels() { return levels; }

    // --- GETTERY I SETTERY USTAWIEŃ ---
    public boolean isOwnEffectsEnabled() { return ownEffectsEnabled; }
    public void setOwnEffectsEnabled(boolean ownEffectsEnabled) { this.ownEffectsEnabled = ownEffectsEnabled; }

    public boolean isOtherEffectsEnabled() { return otherEffectsEnabled; }
    public void setOtherEffectsEnabled(boolean otherEffectsEnabled) { this.otherEffectsEnabled = otherEffectsEnabled; }

    public boolean isChatMessagesEnabled() { return chatMessagesEnabled; }
    public void setChatMessagesEnabled(boolean chatMessagesEnabled) { this.chatMessagesEnabled = chatMessagesEnabled; }

    public boolean isBossBarEnabled() { return bossBarEnabled; }
    public void setBossBarEnabled(boolean bossBarEnabled) { this.bossBarEnabled = bossBarEnabled; }

    public float getVolume() { return volume; }
    public void setVolume(float volume) { this.volume = volume; }
}