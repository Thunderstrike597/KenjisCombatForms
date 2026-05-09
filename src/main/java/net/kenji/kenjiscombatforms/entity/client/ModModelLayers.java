package net.kenji.kenjiscombatforms.entity.client;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {

    public static final ModelLayerLocation SENSEI_LAYER = new ModelLayerLocation(
            new ResourceLocation(KenjisCombatForms.MOD_ID, "sensei_layer"), "main");

    public static final ModelLayerLocation SHADOW_PLAYER_LAYER = new ModelLayerLocation(
            new ResourceLocation(KenjisCombatForms.MOD_ID, "shadow_player_layer"), "main");


    public static final ModelLayerLocation ENDER_PLAYER_LAYER = new ModelLayerLocation(
            new ResourceLocation(KenjisCombatForms.MOD_ID, "ender_player_layer"), "main");

    public static final ModelLayerLocation WITHER_PLAYER_LAYER = new ModelLayerLocation(
            new ResourceLocation(KenjisCombatForms.MOD_ID, "wither_player_layer"), "main");

    public static final ModelLayerLocation WITHER_MINION_LAYER = new ModelLayerLocation(
            new ResourceLocation(KenjisCombatForms.MOD_ID, "wither_minion_layer"), "main");


    public static final ModelLayerLocation CUSTOM_PLAYER_LAYER = new ModelLayerLocation(
            new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/purple_player_texture.png"), "main");
}
