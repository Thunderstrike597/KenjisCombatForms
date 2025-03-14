package net.kenji.kenjiscombatforms.entity.client.EntityRenderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.WitherMinionModel;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherMinionEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WitherMinionRenderer extends MobRenderer<WitherMinionEntity, WitherMinionModel<WitherMinionEntity>> {

    public WitherMinionRenderer(EntityRendererProvider.Context context) {
        super(context, new WitherMinionModel<>(context.bakeLayer(ModModelLayers.WITHER_MINION_LAYER)), 0.5f);
    }

    @Override
    public void render(WitherMinionEntity entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();

        // Apply opacity
        float opacity = entityIn.getOpacity();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, opacity);

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);

        // Reset color
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        matrixStackIn.popPose();
    }

    @Override
    protected RenderType getRenderType(WitherMinionEntity entity, boolean showBody, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }


    @Override
    public ResourceLocation getTextureLocation(WitherMinionEntity witherMinionEntity) {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/wither_minion.png");
    }
}


