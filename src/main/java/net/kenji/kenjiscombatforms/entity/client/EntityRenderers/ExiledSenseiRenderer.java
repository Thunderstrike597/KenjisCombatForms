package net.kenji.kenjiscombatforms.entity.client.EntityRenderers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.ExiledSenseiModel;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledSenseiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ExiledSenseiRenderer extends MobRenderer<ExiledSenseiEntity, ExiledSenseiModel<ExiledSenseiEntity>> {

    public ExiledSenseiRenderer(EntityRendererProvider.Context context) {
        super(context, new ExiledSenseiModel<>(context.bakeLayer(ModModelLayers.EXILED_SENSEI_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ExiledSenseiEntity exiledSenseiEntity) {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/exiled_sensei.png");
    }
}
