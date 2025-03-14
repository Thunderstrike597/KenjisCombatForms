package net.kenji.kenjiscombatforms.particles;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.EventBus;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<SimpleParticleType> VOID_RIFT_PARTICLE = PARTICLE_TYPES.register("rift", () -> new SimpleParticleType(true));


    public static void register(IEventBus event){
        PARTICLE_TYPES.register(event);
    }

}
