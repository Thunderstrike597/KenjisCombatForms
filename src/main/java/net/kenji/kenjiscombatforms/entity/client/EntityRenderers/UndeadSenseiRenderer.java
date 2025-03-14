package net.kenji.kenjiscombatforms.entity.client.EntityRenderers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.UndeadSenseiModel;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.UndeadSenseiEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class UndeadSenseiRenderer extends MobRenderer<UndeadSenseiEntity, UndeadSenseiModel<UndeadSenseiEntity>> {

    public UndeadSenseiRenderer(EntityRendererProvider.Context context) {
        super(context, new UndeadSenseiModel<>(context.bakeLayer(ModModelLayers.UNDEAD_SENSEI_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(UndeadSenseiEntity undeadSenseiEntity) {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/undead_sensei.png");
    }
}
