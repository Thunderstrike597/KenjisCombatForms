package net.kenji.kenjiscombatforms;

import com.mojang.logging.LogUtils;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.*;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.*;
import net.kenji.kenjiscombatforms.api.powers.power_powers.StrengthBoost;
import net.kenji.kenjiscombatforms.api.powers.swift_powers.SpeedBoost;
import net.kenji.kenjiscombatforms.api.powers.swift_powers.SwiftEffectInflict;
import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.kenji.kenjiscombatforms.block.entity.ModBlockEntities;
import net.kenji.kenjiscombatforms.commands.*;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsClient;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.enchantments.ModEnchantments;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledDevilEntity;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledSenseiEntity;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.UndeadSenseiEntity;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFistItem;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.particles.ModParticles;
import net.kenji.kenjiscombatforms.recipe.ModRecipes;
import net.kenji.kenjiscombatforms.screen.ScrollFormingScreen;
import net.kenji.kenjiscombatforms.screen.EssenceInfusingScreen;
import net.kenji.kenjiscombatforms.screen.ModMenuTypes;
import net.kenji.kenjiscombatforms.sound.ModSounds;
import net.kenji.kenjiscombatforms.tab.ModTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;

import static net.kenji.kenjiscombatforms.entity.ModEntities.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KenjisCombatForms.MOD_ID)
public class KenjisCombatForms
{
    public static final String MOD_ID = "kenjiscombatforms";
    private static final Logger LOGGER = LogUtils.getLogger();



    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(LevelUpCurrentForm.register());
        event.getDispatcher().register(ResetLearnedAbilities.register());
        event.getDispatcher().register(ResetLearnedForms.register());
        event.getDispatcher().register(ResetCurrentFormLevel.register());
        event.getDispatcher().register(RefreshAllAbilityCooldowns.register());
        event.getDispatcher().register(RefillAllAbilityCooldowns.register());

    }

    public KenjisCombatForms() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        NetworkHandler.register();

        ModItems.ITEMS.register(modEventBus);

        ModTab.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModBlockEntities.register(modEventBus);

        ModMenuTypes.register(modEventBus);

        ModRecipes.register(modEventBus);

        ModEntities.register(modEventBus);

        ModSounds.register(modEventBus);

        ModEnchantments.register(modEventBus);

        ModParticles.register(modEventBus);


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, KenjisCombatFormsCommon.SPEC, "kenjiscombatforms-Common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, KenjisCombatFormsClient.SPEC, "kenjiscombatforms-Client.toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);



        modEventBus.addListener(this::buildContents);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
    }


    public class ClientEvents {


        @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
        public static class ClientModBusEvents {

        }
    }


    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ModTab.KENJISMODDEDTAB.getKey()) {



        }
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
     event.enqueueWork(() -> {
    SpawnPlacements.register(UNDEAD_SENSEI.get(),
            SpawnPlacements.Type.ON_GROUND,
            Heightmap.Types.WORLD_SURFACE,
            UndeadSenseiEntity::checkSpawnRules);
         SpawnPlacements.register(EXILED_SENSEI.get(),
                 SpawnPlacements.Type.ON_GROUND,
                 Heightmap.Types.WORLD_SURFACE,
                 ExiledSenseiEntity::checkSpawnRules);
         SpawnPlacements.register(EXILED_DEVIL.get(),
                 SpawnPlacements.Type.ON_GROUND,
                 Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                 ExiledDevilEntity::checkSpawnRules);
         SpawnPlacements.register(ABILITY_TRADER.get(),
                 SpawnPlacements.Type.ON_GROUND,
                 Heightmap.Types.WORLD_SURFACE,
                 ScrollTraderEntity::checkSpawnRules);

         init();

        });
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

    }


    public static void init() {
        FormManager formManager = FormManager.getInstance();
        formManager.registerForm(BasicForm.getInstance());
        formManager.registerForm(VoidForm.getInstance());
        formManager.registerForm(WitherForm.getInstance());
        formManager.registerForm(SwiftForm.getInstance());
        formManager.registerForm(PowerForm.getInstance());

        AbilityManager abilityManager = AbilityManager.getInstance();
        abilityManager.registerAbility(TeleportPlayer.getInstance());
        abilityManager.registerAbility(TeleportPlayerBackstab.getInstance());
        abilityManager.registerAbility(VoidAnchorRift.getInstance());
        abilityManager.registerAbility(EnderFormAbility.getInstance());
        abilityManager.registerAbility(EnderLevitation.getInstance());
        abilityManager.registerAbility(VoidGrab.getInstance());
        abilityManager.registerAbility(EnderWarpAbility.getInstance());

        abilityManager.registerAbility(WitherDash.getInstance());
        abilityManager.registerAbility(SoulDrift.getInstance());
        abilityManager.registerAbility(WitherFormAbility.getInstance());
        abilityManager.registerAbility(WitherMinions.getInstance());
        abilityManager.registerAbility(WitherImplode.getInstance());
        abilityManager.registerAbility(WitherFormDashAbility.getInstance());


        abilityManager.registerAbility(SpeedBoost.getInstance());
        abilityManager.registerAbility(StrengthBoost.getInstance());
        abilityManager.registerAbility(SwiftEffectInflict.getInstance());
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenuTypes.CRYSTAL_CHARGING_MENU.get(), EssenceInfusingScreen::new);
            MenuScreens.register(ModMenuTypes.ABILITY_INFUSING_MENU.get(), ScrollFormingScreen::new);


            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
