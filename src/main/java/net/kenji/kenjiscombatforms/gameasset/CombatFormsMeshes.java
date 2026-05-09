package net.kenji.kenjiscombatforms.gameasset;


import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import yesman.epicfight.api.client.model.Meshes;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CombatFormsMeshes implements PreparableReloadListener {
    public static final Meshes.MeshAccessor<CombatFormsMesh> SENSEI = Meshes.MeshAccessor.<CombatFormsMesh>create(KenjisCombatForms.MOD_ID, "entity/sensei", (jsonModelLoader) -> (CombatFormsMesh) jsonModelLoader.loadSkinnedMesh(CombatFormsMesh::new));

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return null;
    }
}
