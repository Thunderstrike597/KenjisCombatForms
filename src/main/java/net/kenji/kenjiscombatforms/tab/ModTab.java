package net.kenji.kenjiscombatforms.tab;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class ModTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<CreativeModeTab> KENJISMODDEDTAB = CREATIVE_MODE_TABS.register("kenjiscombattab", () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.kenjiscombattab"))
            .icon(() -> new ItemStack(ModItems.SWIFTFORM1.get()))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(ModItems.BASICFORM1.get());
                output.accept(ModItems.BASICFORM2.get());
                output.accept(ModItems.BASICFORM3.get());

                output.accept(ModItems.SWIFTFORM1.get());
                output.accept(ModItems.SWIFTFORM2.get());
                output.accept(ModItems.SWIFTFORM3.get());

                output.accept(ModItems.POWERFORM1.get());
                output.accept(ModItems.POWERFORM2.get());
                output.accept(ModItems.POWERFORM3.get());


                output.accept(ModItems.TYPE1_COMPONENT.get());
                output.accept(ModItems.TYPE2_COMPONENT.get());
                output.accept(ModItems.TYPE3_COMPONENT.get());
                output.accept(ModItems.WITHER_CORE_COMPONENT.get());
                output.accept(ModItems.VOID_CORE_COMPONENT.get());
                output.accept(ModItems.SWIFT_CORE_COMPONENT.get());
                output.accept(ModItems.POWER_CORE_COMPONENT.get());



                output.accept(ModItems.ABILITY_ESSENCE.get());

                output.accept(ModItems.SWIFTNESSESSENCE_TIER1.get());
                output.accept(ModItems.SWIFTNESSESSENCE_TIER2.get());
                output.accept(ModItems.SWIFTNESSESSENCE_TIER3.get());


                output.accept(ModItems.POWERESSENCE_TIER1.get());
                output.accept(ModItems.POWERESSENCE_TIER2.get());
                output.accept(ModItems.POWERESSENCE_TIER3.get());


                output.accept(ModItems.SWIFTFORMTEMPLATE.get());
                output.accept(ModItems.POWERFORMTEMPLATE.get());
                output.accept(ModItems.EMPTYSLOT.get());
                output.accept(ModItems.SLOTLINER.get());



                output.accept(ModItems.UNDEAD_SENSEI_SPAWN_EGG.get());
                output.accept(ModItems.EXILED_SENSEI_SPAWN_EGG.get());
                output.accept(ModItems.EXILED_DEVIL_SPAWN_EGG.get());
                output.accept(ModItems.ABILITY_TRADER_SPAWN_EGG.get());


                output.accept(ModBlocks.ESSENCE_CHANNELING_STATION.get());
                output.accept(ModBlocks.SCROLL_FORMING_STATION.get());

                output.accept(ModItems.ENDER_CHARGE.get());
                output.accept(ModItems.NETHER_CHARGE.get());
                output.accept(ModItems.OVERCHARGED_ENDER_PLASMA.get());
                output.accept(ModItems.OVERCHARGED_NETHER_PLASMA.get());
                output.accept(ModItems.VOID_FORM_SCROLL.get());
                output.accept(ModItems.WITHER_FORM_SCROLL.get());
                output.accept(ModItems.SWIFT_FORM_SCROLL.get());
                output.accept(ModItems.POWER_FORM_SCROLL.get());

                output.accept(ModItems.VOID_ABILITY1_SCROLL.get());
                output.accept(ModItems.VOID_ABILITY2_SCROLL.get());
                output.accept(ModItems.VOID_FINAL_ABILITY_SCROLL.get());
                output.accept(ModItems.WITHER_ABILITY1_SCROLL.get());
                output.accept(ModItems.WITHER_ABILITY2_SCROLL.get());
                output.accept(ModItems.WITHER_FINAL_ABILITY_SCROLL.get());

                output.accept(ModItems.SWIFT_ABILITY1_SCROLL.get());
                output.accept(ModItems.SWIFT_ABILITY2_SCROLL.get());


                output.accept(ModItems.POWER_ABILITY1_SCROLL.get());
                output.accept(ModItems.POWER_ABILITY2_SCROLL.get());



                output.accept(ModItems.BLANK_FORM_SCROLL.get());
                output.accept(ModItems.BLANK_ABILITY_SCROLL.get());
                output.accept(ModItems.MYSTERIOUS_DUST.get());
                output.accept(ModItems.SCROLL_DUST.get());

                output.accept(ModItems.VOID_ABILITY1_COMPONENT.get());
                output.accept(ModItems.VOID_ABILITY2_COMPONENT.get());
                output.accept(ModItems.VOID_ABILITY3_COMPONENT.get());
                output.accept(ModItems.WITHER_ABILITY1_COMPONENT.get());
                output.accept(ModItems.WITHER_ABILITY2_COMPONENT.get());
                output.accept(ModItems.WITHER_ABILITY3_COMPONENT.get());

                output.accept(ModItems.SWIFT_ABILITY1_COMPONENT.get());
                output.accept(ModItems.SWIFT_ABILITY2_COMPONENT.get());

                output.accept(ModItems.POWER_ABILITY1_COMPONENT.get());
                output.accept(ModItems.POWER_ABILITY2_COMPONENT.get());

            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
