package net.kenji.kenjiscombatforms.block;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.block.custom.ScrollFormingBlock;
import net.kenji.kenjiscombatforms.block.custom.EssenceInfusionBlock;
import net.kenji.kenjiscombatforms.block.custom.VoidRiftBlock;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<Block> ESSENCE_CHANNELING_STATION =
            registerblock("essence_infusion_station", () -> new EssenceInfusionBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(4)));

    public static final RegistryObject<Block> SCROLL_FORMING_STATION =
            registerblock("scroll_forming_table", () -> new ScrollFormingBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(4)));

    public static final RegistryObject<Block> VOID_RIFT =
            registerblock("void_rift", () -> new VoidRiftBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(4)));



        public static <T extends Block>RegistryObject<T> registerblock(String name, Supplier<T> block) {
            RegistryObject<T> toReturn = BLOCKS.register(name, block);
           registerblockitem(name, toReturn);
            return toReturn;
        }

        public static <T extends Block>RegistryObject<Item> registerblockitem(String name, RegistryObject<T> block){
            return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        };

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}