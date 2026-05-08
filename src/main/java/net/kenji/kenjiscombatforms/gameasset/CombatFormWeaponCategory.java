package net.kenji.kenjiscombatforms.gameasset;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.client.particle.HitParticle;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.function.Supplier;

public enum CombatFormWeaponCategory implements WeaponCategory {
    COMBAT_DAGGER(() -> EpicFightSounds.BLADE_HIT, () -> EpicFightParticles.HIT_BLADE);

    public final int id;
    public final Supplier<RegistryObject<SoundEvent>> hitSound;
    public final Supplier<RegistryObject<HitParticleType>> hitParticle;

    CombatFormWeaponCategory(Supplier<RegistryObject<SoundEvent>> hitSound, Supplier<RegistryObject<HitParticleType>> hitParticle) {
        this.id = WeaponCategory.ENUM_MANAGER.assign(this);
        this.hitSound = hitSound;
        this.hitParticle = hitParticle;
    }



    public int universalOrdinal() {
        return this.id;
    }
}
