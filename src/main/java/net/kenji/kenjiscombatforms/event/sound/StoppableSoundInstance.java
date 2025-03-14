package net.kenji.kenjiscombatforms.event.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class StoppableSoundInstance extends AbstractTickableSoundInstance {
    private boolean shouldStop = false;

    public StoppableSoundInstance(SoundEvent sound, SoundSource source, float volume, float pitch, BlockPos pos) {
        super(sound, source, SoundInstance.createUnseededRandom());
        this.x = pos.getX() + 0.5;
        this.y = pos.getY() + 0.5;
        this.z = pos.getZ() + 0.5;
        this.volume = volume;
        this.pitch = pitch;
        this.looping = true;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (shouldStop) {
            this.stop();
        }
    }

    public void stopSound() {
        this.shouldStop = true;
    }
}
