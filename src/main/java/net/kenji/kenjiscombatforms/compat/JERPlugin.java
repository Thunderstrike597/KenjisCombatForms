package net.kenji.kenjiscombatforms.compat;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import jeresources.api.IJERAPI;
import jeresources.api.IJERPlugin;
import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.LootDrop;
import jeresources.compatibility.api.JERAPI;
import jeresources.entry.AbstractVillagerEntry;
import jeresources.registry.VillagerRegistry;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;

import java.util.ArrayList;
import java.util.List;

@jeresources.api.JERPlugin
public class JERPlugin{


    public static void receive(IJERAPI api) {
        Level level = api.getLevel();
        if (level == null) return;

        ScrollTraderEntity entity = ModEntities.SCROLL_TRADER.get().create(level);
        if (entity == null) return;
        List<LootDrop> offersList = new ArrayList<>();
        for(MerchantOffer offer : entity.getOffers()){
            LootDrop drop = new LootDrop(offer.getResult());
            offersList.add(drop);
        }

        offersList.add(new LootDrop(ModItems.COMBAT_DAGGER.get().getDefaultInstance()));
        LootDrop[] drops = offersList.toArray(new LootDrop[0]);
        api.getMobRegistry().register(
                entity,
                LightLevel.any,
                drops
        );
    }

    public static void init() {
        Int2ObjectMap<VillagerTrades.ItemListing[]> listingsMap = new Int2ObjectOpenHashMap<>();

        listingsMap.put(1, new VillagerTrades.ItemListing[]{
                (trader, random) -> trader instanceof ScrollTraderEntity scrollTraderEntity ? scrollTraderEntity.getOffers().get(0) : null,
                (trader, random) -> trader instanceof ScrollTraderEntity scrollTraderEntity ? scrollTraderEntity.getOffers().get(1) : null,
                (trader, random) -> trader instanceof ScrollTraderEntity scrollTraderEntity ? scrollTraderEntity.getOffers().get(2) : null,
                (trader, random) -> trader instanceof ScrollTraderEntity scrollTraderEntity ? scrollTraderEntity.getOffers().get(3) : null,
                (trader, random) -> trader instanceof ScrollTraderEntity scrollTraderEntity ? scrollTraderEntity.getOffers().get(4) : null,
                // Skip the random combat dagger trade — it's chance-based so just always include it in JER display
                (trader, random) -> trader instanceof ScrollTraderEntity scrollTraderEntity ? new MerchantOffer(new ItemStack(Items.EMERALD, 38), new ItemStack(ModItems.MYSTERIOUS_SCRAP_METAL.get()), new ItemStack(ModItems.COMBAT_DAGGER.get()), 1, 0, 1.0f) : null,
        });
        VillagerRegistry.getInstance().addVillagerEntry(new AbstractVillagerEntry<AbstractVillager>(listingsMap) {
            @Override
            public String getName() {
                return Component.translatable("entity.kenjiscombatforms.scroll_trader").getString();
            }

            @Override
            public String getDisplayName() {
                return Component.translatable("entity.kenjiscombatforms.scroll_trader").getString();
            }

            @Override
            public AbstractVillager getVillagerEntity() {
                return null; // JER only uses this for rendering; handle null in render if needed
            }

            @Override
            public List<ItemStack> getPois() {
                return List.of();
            }

            @Override
            public boolean hasPois() {
                return false;
            }

            @Override
            public boolean hasLevels() {
                return false;
            }
        });
    }
}