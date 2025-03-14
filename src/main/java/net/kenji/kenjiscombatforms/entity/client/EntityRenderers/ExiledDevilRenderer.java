package net.kenji.kenjiscombatforms.entity.client.EntityRenderers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.ExiledDevilModel;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.kenji.kenjiscombatforms.entity.custom.SenseiEntities.ExiledDevilEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ExiledDevilRenderer extends MobRenderer<ExiledDevilEntity, ExiledDevilModel<ExiledDevilEntity>> {

    public ExiledDevilRenderer(EntityRendererProvider.Context context) {
        super(context, new ExiledDevilModel<>(context.bakeLayer(ModModelLayers.EXILED_DEVIL_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ExiledDevilEntity exiledDevilEntity) {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/exiled_devil.png");
    }
}
