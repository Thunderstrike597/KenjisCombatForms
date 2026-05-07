package net.kenji.kenjiscombatforms.gameasset;

import net.kenji.woh.gameasset.WohWeaponCategories;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.function.Supplier;

public enum CombatFormWeaponCategories implements WeaponCategory {
    COMBAT_DAGGER(() -> EpicFightSounds.BLADE_HIT);

    public final int id;
    public final Supplier<RegistryObject<SoundEvent>> hitSound;
    CombatFormWeaponCategories(Supplier<RegistryObject<SoundEvent>> hitSound) {
        this.id = WeaponCategory.ENUM_MANAGER.assign(this);
        this.hitSound = hitSound;
    }



    public int universalOrdinal() {
        return this.id;
    }
}
