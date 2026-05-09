package net.kenji.kenjiscombatforms.item;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static class Items {

        public static final TagKey<Item> COMBAT_WEAPONS =
                ItemTags.create(new ResourceLocation(KenjisCombatForms.MOD_ID, "combat_weapons"));

    }
}