package net.kenji.kenjiscombatforms.api.interfaces.ability;

public abstract class AbstractAbilityData {
    protected int tickCount = 0;
    protected int MAX_COOLDOWN;
    protected int abilityCooldown;
    protected boolean hasPlayedSound = false;

    public AbstractAbilityData(int maxCooldown) {
        this.MAX_COOLDOWN = maxCooldown;
        this.abilityCooldown = maxCooldown;
    }

    // Common getters and setters
    public int getTickCount() { return tickCount; }
    public void setTickCount(int tickCount) { this.tickCount = tickCount; }
    public int getAbilityCooldown() { return abilityCooldown; }
    public void setAbilityCooldown(int cooldown) { this.abilityCooldown = cooldown; }
    public boolean hasPlayedSound(){ return hasPlayedSound; }
    public int getMAX_COOLDOWN() {return MAX_COOLDOWN;}
    public void setHasPlayedSound(boolean hasPlayed) { this.hasPlayedSound = hasPlayed; }

    // Abstract methods for ability-specific logic
    public abstract void resetAbility();
    public abstract boolean isAbilityActive();
}