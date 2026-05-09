package net.kenji.kenjiscombatforms.entity.client.EntityRenderers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.SenseiModel;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.UndeadSenseiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class UndeadSenseiRenderer extends MobRenderer<UndeadSenseiEntity, SenseiModel<UndeadSenseiEntity>> {

    public UndeadSenseiRenderer(EntityRendererProvider.Context context) {
        super(context, new SenseiModel<>(context.bakeLayer(ModModelLayers.SENSEI_LAYER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(UndeadSenseiEntity undeadSenseiEntity) {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/undead_sensei.png");
    }
}
