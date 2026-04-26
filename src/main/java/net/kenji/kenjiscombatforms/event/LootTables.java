package net.kenji.kenjiscombatforms.event;

import net.kenji.woh.WeaponsOfHarmony;
import net.kenji.woh.registry.WohItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import yesman.epicfight.data.loot.function.SetSkillFunction;
import yesman.epicfight.world.item.EpicFightItems;

@Mod.EventBusSubscriber(modid = WeaponsOfHarmony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LootTables {
    @SubscribeEvent
    public static void modifyVanillaLootPools(LootTableLoadEvent event) {

        addSkillBook(event, BuiltInLootTables.DESERT_PYRAMID, new String[]{"kenjiscombatforms:kaishu_guard"}, 0.035F);
        addSkillBook(event, BuiltInLootTables.STRONGHOLD_LIBRARY, new String[]{"kenjiscombatforms:kaishu_guard"}, 0.05F);
        addSkillBook(event, BuiltInLootTables.BASTION_TREASURE, new String[]{"kenjiscombatforms:kaishu_guard"}, 0.04F);
        addSkillBook(event, BuiltInLootTables.SIMPLE_DUNGEON, new String[]{"kenjiscombatforms:kaishu_guard"}, 0.045F);
        addSkillBook(event, BuiltInLootTables.ANCIENT_CITY, new String[]{"kenjiscombatforms:kaishu_guard"}, 0.03F);
        addSkillBook(event, BuiltInLootTables.PILLAGER_OUTPOST, new String[]{"kenjiscombatforms:kaishu_guard"}, 0.028F);
    }

    // ===== HELPERS =====
    private static void addSkillBook(LootTableLoadEvent event,
                                     net.minecraft.resources.ResourceLocation table,
                                     String[] identifiers,
                                     float chance) {

        if (!event.getName().equals(table)) return;
        for (var identifier : identifiers) {
            event.getTable().addPool(
                    LootPool.lootPool()
                            .when(LootItemRandomChanceCondition.randomChance(chance))
                            .add(LootItem.lootTableItem((ItemLike) EpicFightItems.SKILLBOOK.get())
                                    .apply(SetSkillFunction.builder(
                                            1.0F, identifier)))
                            .build()
            );
        }
    }
}
