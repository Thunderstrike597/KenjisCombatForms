package net.kenji.kenjiscombatforms.screen;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {
    public static DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, KenjisCombatForms.MOD_ID);

    public static final RegistryObject<MenuType<EssenceInfusingMenu>> CRYSTAL_CHARGING_MENU = registerMenuType("essence_infusing_menu", EssenceInfusingMenu::new);
    public static final RegistryObject<MenuType<ScrollFormingMenu>> ABILITY_INFUSING_MENU = registerMenuType("ability_infusing_menu", ScrollFormingMenu::new);


    private static<T extends AbstractContainerMenu>RegistryObject<MenuType<T>> registerMenuType(String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }



    public static void register(IEventBus eventBus){
        MENUS.register(eventBus);
    }
}
