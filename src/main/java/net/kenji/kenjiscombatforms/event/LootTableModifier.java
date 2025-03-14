package net.kenji.kenjiscombatforms.event;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.datagen.ModGlobalLootModifiers;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.UndeadSenseiEntity;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.Random;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LootTableModifier {

    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        // Check if the loot table being loaded is the one you want to modify

        if (event.getName().equals(new ResourceLocation(KenjisCombatForms.MOD_ID, "entities/undead_sensei"))) {
            LootTable lootTable = event.getTable();

            // Create a single pool with both items
            LootPool.Builder essencePool = LootPool.lootPool()
                    .name("tier1_essence_pool")

                    .add(LootItem.lootTableItem(ModItems.SWIFTNESSESSENCE_TIER1.get())
                            .setWeight(1)) // Adjust weights as needed
                    .add(LootItem.lootTableItem(ModItems.POWERESSENCE_TIER1.get())
                            .setWeight(1))
                    .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, KenjisCombatFormsCommon.TIER1_ESSENCE_CHANCE.get().floatValue())).when(LootItemKilledByPlayerCondition.killedByPlayer()));


            lootTable.addPool(essencePool.build());
        }




        else  if (event.getName().equals(new ResourceLocation(KenjisCombatForms.MOD_ID, "entities/exiled_sensei"))) {
            LootTable lootTable = event.getTable();

            // Create a single pool with both items
            LootPool.Builder essencePool2 = LootPool.lootPool()
                    .name("tier2_essence_pool")

                    .add(LootItem.lootTableItem(ModItems.SWIFTNESSESSENCE_TIER2.get())
                            .setWeight(1)) // Adjust weights as needed
                    .add(LootItem.lootTableItem(ModItems.POWERESSENCE_TIER2.get())
                            .setWeight(1))
                    .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, KenjisCombatFormsCommon.TIER2_ESSENCE_CHANCE.get().floatValue())).when(LootItemKilledByPlayerCondition.killedByPlayer()));




            lootTable.addPool(essencePool2.build());
        }

        else  if (event.getName().equals(new ResourceLocation(KenjisCombatForms.MOD_ID, "entities/exiled_devil"))) {
            LootTable lootTable = event.getTable();

            // Create a single pool with both items
            LootPool.Builder essencePool2 = LootPool.lootPool()
                    .name("tier3_essence_pool")

                    .add(LootItem.lootTableItem(ModItems.SWIFTNESSESSENCE_TIER3.get())
                            .setWeight(1)) // Adjust weights as needed
                    .add(LootItem.lootTableItem(ModItems.POWERESSENCE_TIER3.get())
                            .setWeight(1))
                    .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, KenjisCombatFormsCommon.TIER3_ESSENCE_CHANCE.get().floatValue())).when(LootItemKilledByPlayerCondition.killedByPlayer()));


            lootTable.addPool(essencePool2.build());
        }

        if (event.getName().equals(new ResourceLocation("minecraft", "entities/enderman"))) {
            LootTable lootTable = event.getTable();

            // Create a single pool with both items
            LootPool.Builder essencePool = LootPool.lootPool()
                    .name("ender_charge_pool")

                    .add(LootItem.lootTableItem(ModItems.ENDER_CHARGE.get())
                            .setWeight(1)) // Adjust weights as needed
                    .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, KenjisCombatFormsCommon.CHARGE_CHANCE.get().floatValue())).when(LootItemKilledByPlayerCondition.killedByPlayer()));


            lootTable.addPool(essencePool.build());
        }
        if (event.getName().equals(new ResourceLocation("minecraft", "entities/wither_skeleton")) ||
                event.getName().equals(new ResourceLocation("minecraft", "entities/blaze")) ||
                event.getName().equals(new ResourceLocation("minecraft", "entities/zombie_piglin"))) {
            LootTable lootTable = event.getTable();

            // Create a single pool with both items
            LootPool.Builder essencePool = LootPool.lootPool()
                    .name("nether_charge_pool")

                    .add(LootItem.lootTableItem(ModItems.NETHER_CHARGE.get())
                            .setWeight(1)) // Adjust weights as needed
                    .apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, KenjisCombatFormsCommon.CHARGE_CHANCE.get().floatValue())).when(LootItemKilledByPlayerCondition.killedByPlayer()));


            lootTable.addPool(essencePool.build());
        }
    }
}

