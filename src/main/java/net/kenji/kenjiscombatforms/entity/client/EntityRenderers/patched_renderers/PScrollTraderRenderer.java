package net.kenji.kenjiscombatforms.entity.client.EntityRenderers.patched_renderers;

import net.kenji.kenjiscombatforms.entity.client.EntityModels.ScrollTraderModel;
import net.kenji.kenjiscombatforms.entity.client.EntityRenderers.ScrollTraderRenderer;
import net.kenji.kenjiscombatforms.entity.custom.traders.ScrollTraderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.mesh.VillagerMesh;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;

public class PScrollTraderRenderer<E extends ScrollTraderEntity, T extends MobPatch<E>>
        extends PatchedLivingEntityRenderer<E, T, ScrollTraderModel<E>, ScrollTraderRenderer<E>, VillagerMesh> {

    public PScrollTraderRenderer(EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
    }

    @Override
    public AssetAccessor<VillagerMesh> getDefaultMesh() {
        return Meshes.VILLAGER_ZOMBIE;
    }
}