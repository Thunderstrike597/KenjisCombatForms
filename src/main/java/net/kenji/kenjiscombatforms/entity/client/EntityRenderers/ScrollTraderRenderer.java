package net.kenji.kenjiscombatforms.entity.client.EntityRenderers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.SenseiModel;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class ScrollTraderRenderer<T extends Mob>
        extends MobRenderer<T, SenseiModel<T>> {

    public ScrollTraderRenderer(EntityRendererProvider.Context context) {
        super(context, new SenseiModel<>(context.bakeLayer(ModModelLayers.SENSEI_LAYER)), 0.5f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));

    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/scroll_trader.png");
    }
}
