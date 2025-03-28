package net.kenji.kenjiscombatforms.event.forge_events;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.*;
import net.kenji.kenjiscombatforms.entity.client.EntityRenderers.*;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.kenji.kenjiscombatforms.keybinds.ModKeybinds;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.EXILED_DEVIL_LAYER, ExiledDevilModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.EXILED_SENSEI_LAYER, ExiledSenseiModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.UNDEAD_SENSEI_LAYER, UndeadSenseiModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SHADOW_PLAYER_LAYER, ShadowPlayerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ENDER_PLAYER_LAYER, EnderEntityModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WITHER_PLAYER_LAYER, WitherPlayerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WITHER_MINION_LAYER, WitherMinionModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ABILITY_TRADER_LAYER, ScrollTraderModel::createBodyLayer);
    }

    @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.EXILED_DEVIL.get(), ExiledDevilRenderer::new);
        EntityRenderers.register(ModEntities.EXILED_SENSEI.get(), ExiledSenseiRenderer::new);
        EntityRenderers.register(ModEntities.UNDEAD_SENSEI.get(), UndeadSenseiRenderer::new);
        EntityRenderers.register(ModEntities.SHADOW_PLAYER.get(), ShadowPlayerRenderer::new);
        EntityRenderers.register(ModEntities.ENDER_PLAYER.get(), EnderEntityRenderer::new);
        EntityRenderers.register(ModEntities.WITHER_PLAYER.get(), WitherPlayerRenderer::new);
        EntityRenderers.register(ModEntities.WITHER_MINION.get(), WitherMinionRenderer::new);
        EntityRenderers.register(ModEntities.SCROLL_TRADER.get(), ScrollTraderRenderer::new);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event){
        event.register(ModKeybinds.ABILITY1_KEY);
        event.register(ModKeybinds.ABILITY2_KEY);
        event.register(ModKeybinds.ABILITY3_KEY);
        event.register(ModKeybinds.FORM_MENU_OPEN_KEY);
        event.register(ModKeybinds.QUICK_FORM_CHANGE_KEY);
        event.register(ModKeybinds.TOGGLE_HAND_COMBAT_KEY);
        event.register(ModKeybinds.ACTIVATE_CURRENT_ABILITY_KEY);
        event.register(ModKeybinds.SWITCH_CURRENT_ABILITY_KEY);
    }

}
