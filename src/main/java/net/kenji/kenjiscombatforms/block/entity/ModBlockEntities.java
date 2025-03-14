package net.kenji.kenjiscombatforms.block.entity;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCKENTITIES =

            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<BlockEntityType<EssenceInfusionBlockEntity>> ESSENCE_CHANNELING_STATION_BE =
            BLOCKENTITIES.register("essence_infusion_station", () -> BlockEntityType.Builder.of(EssenceInfusionBlockEntity::new,
                    ModBlocks.ESSENCE_CHANNELING_STATION.get()).build(null));

    public static final RegistryObject<BlockEntityType<ScrollFormingBlockEntity>> SCROLL_FORMING_TABLE_BE =
            BLOCKENTITIES.register("scroll_forming_table", () -> BlockEntityType.Builder.of(ScrollFormingBlockEntity::new,
                    ModBlocks.SCROLL_FORMING_STATION.get()).build(null));



    public static void register(IEventBus eventBus){
        BLOCKENTITIES.register(eventBus);
    }
}
