package net.kenji.kenjiscombatforms.entity.client.EntityRenderers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.client.EntityModels.ScrollTraderModel;
import net.kenji.kenjiscombatforms.entity.client.ModModelLayers;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ScrollTraderRenderer extends MobRenderer<ScrollTraderEntity, ScrollTraderModel<ScrollTraderEntity>> {

    public ScrollTraderRenderer(EntityRendererProvider.Context context) {
        super(context, new ScrollTraderModel<>(context.bakeLayer(ModModelLayers.ABILITY_TRADER_LAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(ScrollTraderEntity abilityTraderEntity) {
        return new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/entity/scroll_trader.png");
    }
}
