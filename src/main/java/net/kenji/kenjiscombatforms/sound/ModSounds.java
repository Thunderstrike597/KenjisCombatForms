package net.kenji.kenjiscombatforms.sound;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<SoundEvent> ESSENCE_INFUSION_SOUND = SOUND_EVENTS.register("essence_infusion_sound",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(KenjisCombatForms.MOD_ID, "essence_infusion_sound")));
    public static final RegistryObject<SoundEvent> VOID_RIFT_SOUND = SOUND_EVENTS.register("void_rift_sound",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(KenjisCombatForms.MOD_ID, "void_rift_sound")));

    public static void register(IEventBus event){
        SOUND_EVENTS.register(event);
    }
}
