package net.kenji.kenjiscombatforms;

import com.mojang.logging.LogUtils;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.api.powers.EmptyAbility;
import net.kenji.kenjiscombatforms.api.powers.EmptyFinalAbility;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.*;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.*;
import net.kenji.kenjiscombatforms.api.powers.power_powers.PowerEffectInflict;
import net.kenji.kenjiscombatforms.api.powers.power_powers.StrengthBoost;
import net.kenji.kenjiscombatforms.api.powers.swift_powers.SpeedBoost;
import net.kenji.kenjiscombatforms.api.powers.swift_powers.SwiftEffectInflict;
import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.kenji.kenjiscombatforms.block.entity.ModBlockEntities;
import net.kenji.kenjiscombatforms.commands.*;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsClient;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.client.EntityRenderers.patched_renderers.ScrollTraderPatchRenderer;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledDevilEntity;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledSenseiEntity;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.UndeadSenseiEntity;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.CombatFistCapabilityPresets;
import net.kenji.kenjiscombatforms.gameasset.ModSkills;
import net.kenji.kenjiscombatforms.item.ModItems;
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
import net.minecraft.resources.ResourceLocation;
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
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;
import yesman.epicfight.api.client.forgeevent.PatchedRenderersEvent;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;

import static net.kenji.kenjiscombatforms.entity.ModEntities.*;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(KenjisCombatForms.MOD_ID)
public class KenjisCombatForms
{
    public static final String MOD_ID = "kenjiscombatforms";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static boolean isLoaded(String modid) {
        return FMLLoader.getLoadingModList().getModFileById(modid) != null;
    }

    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(LevelUpCurrentForm.register());
        event.getDispatcher().register(ResetLearnedAbilities.register());
        event.getDispatcher().register(ResetLearnedForms.register());
        event.getDispatcher().register(ResetCurrentFormLevel.register());
        event.getDispatcher().register(RefreshAllAbilityCooldowns.register());
        event.getDispatcher().register(RefillAllAbilityCooldowns.register());
        event.getDispatcher().register(DebugCrashCommand.register());

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


        ModParticles.register(modEventBus);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(KenjisCombatForms::registerPatchedEntityRenderers);        }

        modEventBus.addListener(KenjisCombatForms::RegisterWeaponType);

        modEventBus.addListener(ModSkills::buildSkillEvent);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, EpicFightCombatFormsCommon.SPEC, "kenjiscombatforms-Common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EpicFightCombatFormsClient.SPEC, "kenjiscombatforms-Client.toml");

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);



        modEventBus.addListener(this::buildContents);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
    }
    public static void RegisterWeaponType(WeaponCapabilityPresetRegistryEvent event) {
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "basic_form_1"), CombatFistCapabilityPresets.BASIC_FIST_TIER1);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "basic_form_2"), CombatFistCapabilityPresets.BASIC_FIST_TIER2);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "basic_form_3"), CombatFistCapabilityPresets.BASIC_FIST_TIER3);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "power_form_1"), CombatFistCapabilityPresets.POWER_FORM_1);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "power_form_2"), CombatFistCapabilityPresets.POWER_FORM_2);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "power_form_3"), CombatFistCapabilityPresets.POWER_FORM_3);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "swift_form_1"), CombatFistCapabilityPresets.SWIFT_FORM_1);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "swift_form_2"), CombatFistCapabilityPresets.SWIFT_FORM_2);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "swift_form_3"), CombatFistCapabilityPresets.SWIFT_FORM_3);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "void_form_1"), CombatFistCapabilityPresets.VOID_FORM_1);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "void_form_2"), CombatFistCapabilityPresets.VOID_FORM_2);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "void_form_3"), CombatFistCapabilityPresets.VOID_FORM_3);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "wither_form_1"), CombatFistCapabilityPresets.WITHER_FORM_1);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "wither_form_2"), CombatFistCapabilityPresets.WITHER_FORM_2);
        event.getTypeEntry().put(new ResourceLocation(MOD_ID, "wither_form_3"), CombatFistCapabilityPresets.WITHER_FORM_3);









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
         SpawnPlacements.register(SCROLL_TRADER.get(),
                 SpawnPlacements.Type.ON_GROUND,
                 Heightmap.Types.WORLD_SURFACE,
                 ScrollTraderEntity::checkSpawnRules);

         init();

        });
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }
    @SubscribeEvent
    public static void registerPatchedEntityRenderers(PatchedRenderersEvent.Add event) {
        event.addPatchedEntityRenderer(SCROLL_TRADER.get(), entityType -> new ScrollTraderPatchRenderer(
                        event.getContext(),
                        entityType
                )
        );
    }

    public static void init() {
        FormManager formManager = FormManager.getInstance();
        formManager.registerForm(NoneForm.getInstance());
        formManager.registerForm(BasicForm.getInstance());
        formManager.registerForm(VoidForm.getInstance());
        formManager.registerForm(WitherForm.getInstance());
        formManager.registerForm(SwiftForm.getInstance());
        formManager.registerForm(PowerForm.getInstance());

        AbilityManager abilityManager = AbilityManager.getInstance();
        abilityManager.registerAbility(EmptyAbility.getInstance());
        abilityManager.registerAbility(TeleportPlayer.getInstance());
        abilityManager.registerAbility(TeleportPlayerBackstab.getInstance());
        abilityManager.registerAbility(VoidAnchorRift.getInstance());
        abilityManager.registerAbility(EnderFormAbility.getInstance());

        abilityManager.registerFinalAbility(EmptyFinalAbility.getInstance());
        abilityManager.registerFinalAbility(EnderLevitation.getInstance());
        abilityManager.registerFinalAbility(VoidGrab.getInstance());
        abilityManager.registerFinalAbility(EnderWarpAbility.getInstance());

        abilityManager.registerAbility(WitherDash.getInstance());
        abilityManager.registerAbility(SoulDrift.getInstance());
        abilityManager.registerAbility(WitherFormAbility.getInstance());
        abilityManager.registerFinalAbility(WitherMinions.getInstance());
        abilityManager.registerFinalAbility(WitherImplode.getInstance());
        abilityManager.registerFinalAbility(WitherFormDashAbility.getInstance());


        abilityManager.registerAbility(SpeedBoost.getInstance());
        abilityManager.registerAbility(StrengthBoost.getInstance());
        abilityManager.registerAbility(SwiftEffectInflict.getInstance());
        abilityManager.registerAbility(PowerEffectInflict.getInstance());
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
