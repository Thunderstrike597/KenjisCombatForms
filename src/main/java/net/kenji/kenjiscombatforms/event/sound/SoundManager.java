package net.kenji.kenjiscombatforms.event.sound;

import net.kenji.kenjiscombatforms.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SoundManager {

    private static final SoundManager INSTANCE = new SoundManager();

    public static SoundManager getInstance(){
        return INSTANCE;
    }

    private SimpleSoundInstance currentSoundInstance;
    private SimpleSoundInstance currentSoundInstance2;
    private SimpleSoundInstance currentSoundInstance3;
    private SimpleSoundInstance currentSoundInstance4;


    private SimpleSoundInstance currentChooseFormSoundInstance;
    private SimpleSoundInstance currentChooseVoidFormSoundInstance;

    private boolean hasSoundPlayed = false;
    boolean cycleCheck = false;
    private static final Map<BlockPos, StoppableSoundInstance> activeSounds = new HashMap<>();


    public static void playCustomSound(Level level, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        if (level.isClientSide) {
            StoppableSoundInstance soundInstance = new StoppableSoundInstance(sound, source, volume, pitch, pos);
            Minecraft.getInstance().getSoundManager().play(soundInstance);
            activeSounds.put(pos, soundInstance);
        } else {
            level.playSound(null, pos, sound, source, volume, pitch);
        }
    }

    public static void stopCustomSound(Level level, BlockPos pos) {
        if (level.isClientSide) {
            StoppableSoundInstance sound = activeSounds.remove(pos);
            if (sound != null) {
                sound.stopSound();
            }
        }
    }


    // Method to play the first sound

    @OnlyIn(Dist.CLIENT)
    public static void playSound() {
           playSoundClient();
    }


    // Method to play the second sound
    @OnlyIn(Dist.CLIENT)
    public static void stopSound() {
       SoundManager.stopSoundClient();
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSound2() {
        SoundManager.playSoundClient2();
    }

    @OnlyIn(Dist.CLIENT)
    public static void playDashSound(Player player) {
        if (player != null) {
         SoundManager.playWitherDashSoundClient();
            SoundManager.playWitherDashSoundClient2();
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static void playBasicFormChooseSound(Player player) {
        if (player != null) {
            SoundManager.playFormChooseSoundClient();
        }
    }
    @OnlyIn(Dist.CLIENT)
    public static void playVoidFormChooseSound(Player player) {
        if (player != null) {
           SoundManager.playFormChooseSoundClient();
            SoundManager.playVoidFormChooseSoundClient();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSoundClient() {
        Minecraft.getInstance().execute(() -> {
            getInstance().currentSoundInstance = SimpleSoundInstance.forUI(
                    ModSounds.ESSENCE_INFUSION_SOUND.get(),
                    0.25f,
                    0.8f
            );
            Minecraft.getInstance().getSoundManager().play(getInstance().currentSoundInstance);
        });
    }


    @OnlyIn(Dist.CLIENT)
    public static void playSoundClient2() {
        Minecraft.getInstance().execute(() -> {
            getInstance().currentSoundInstance2 = SimpleSoundInstance.forUI(
                    SoundEvents.ENCHANTMENT_TABLE_USE,
                    1f,
                    1f
            );
            Minecraft.getInstance().getSoundManager().play(getInstance().currentSoundInstance2);
        });
    }



    @OnlyIn(Dist.CLIENT)
    public static void playWitherDashSoundClient() {
        Minecraft.getInstance().execute(() -> {
            getInstance().currentSoundInstance3 = SimpleSoundInstance.forUI(
                    SoundEvents.VEX_CHARGE,
                    1f,
                    1f
            );
            Minecraft.getInstance().getSoundManager().play(getInstance().currentSoundInstance3);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void playWitherDashSoundClient2() {
        Minecraft.getInstance().execute(() -> {
            getInstance().currentSoundInstance4 = SimpleSoundInstance.forUI(
                    SoundEvents.ILLUSIONER_CAST_SPELL,
                    1f,
                    1f
            );
            Minecraft.getInstance().getSoundManager().play(getInstance().currentSoundInstance4);
        });
    }
    @OnlyIn(Dist.CLIENT)
    public static void playFormChooseSoundClient() {
        Minecraft.getInstance().execute(() -> {
            getInstance().currentChooseFormSoundInstance = SimpleSoundInstance.forUI(
                    SoundEvents.ENCHANTMENT_TABLE_USE,
                    1f,
                    1f
            );
            Minecraft.getInstance().getSoundManager().play(getInstance().currentChooseFormSoundInstance);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void playVoidFormChooseSoundClient() {
        Minecraft.getInstance().execute(() -> {
            getInstance().currentChooseVoidFormSoundInstance = SimpleSoundInstance.forUI(
                    SoundEvents.ENCHANTMENT_TABLE_USE,
                    1f,
                    1f
            );
            Minecraft.getInstance().getSoundManager().play(getInstance().currentChooseVoidFormSoundInstance);
        });
    }


    // Method to stop the first sound

    @OnlyIn(Dist.CLIENT)
    public static void stopSoundClient() {
        if (getInstance().currentSoundInstance != null) {
            Minecraft.getInstance().getSoundManager().stop(getInstance().currentSoundInstance);
            getInstance().currentSoundInstance = null;
        }
    }

    // Method to stop the second sound
    @OnlyIn(Dist.CLIENT)
   public void essenceInfusionSoundEvent(Player player) {
        if (player != null) {
            player.level();
            if (!hasSoundPlayed) {
                playSound();
                hasSoundPlayed = true;
                cycleCheck = true;
            }
        }
   }

    // Method to stop the second sound
    @OnlyIn(Dist.CLIENT)
    public void essenceInfusionSoundEventCancel(Player player) {
        if (player != null) {
            if (cycleCheck) {
                hasSoundPlayed = false;
                stopSound();
                playSound2();
                cycleCheck = false;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void abilityInfusionSoundEvent(Player player) {
        if (player != null) {
            player.level();
            if (!hasSoundPlayed) {
                playSound();
                hasSoundPlayed = true;
                cycleCheck = true;
            }
        }
    }

    // Method to stop the second sound
    @OnlyIn(Dist.CLIENT)
    public void abilityInfusionSoundEventCancel(Player player) {
        if (player != null) {
            if (cycleCheck) {
                hasSoundPlayed = false;
                stopSound();
                playSound2();
                cycleCheck = false;
            }
        }
    }
}
