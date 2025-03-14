package net.kenji.kenjiscombatforms.entity.client.EntityRenderers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.ShadowPlayerModel;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.ShadowPlayerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ShadowPlayerRenderer extends MobRenderer<ShadowPlayerEntity, ShadowPlayerModel<ShadowPlayerEntity>> {

    public ShadowPlayerRenderer(EntityRendererProvider.Context context) {
        super(context, new ShadowPlayerModel<>(context.bakeLayer(ModModelLayers.SHADOW_PLAYER_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowPlayerEntity decoyEntity) {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/shadow_player.png");
    }
}
