package net.kenji.kenjiscombatforms.entity.client.EntityRenderers.patched_renderers;

import net.kenji.kenjiscombatforms.entity.client.EntityModels.SenseiModel;
import net.kenji.kenjiscombatforms.entity.client.EntityRenderers.ScrollTraderRenderer;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.kenji.kenjiscombatforms.gameasset.CombatFormsMesh;
import net.kenji.kenjiscombatforms.gameasset.CombatFormsMeshes;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.client.renderer.patched.entity.PHumanoidRenderer;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

public class PSenseiRenderer<E extends ScrollTraderEntity, T extends HumanoidMobPatch<E>>
        extends PatchedLivingEntityRenderer<E, T, SenseiModel<E>, ScrollTraderRenderer<E>, CombatFormsMesh> {

    public PSenseiRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
        this.addPatchedLayer(ItemInHandLayer.class, new PatchedItemInHandLayer());
    }

    @Override
    public AssetAccessor<CombatFormsMesh> getDefaultMesh() {
        return CombatFormsMeshes.SENSEI;
    }
}