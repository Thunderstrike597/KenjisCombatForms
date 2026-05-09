package net.kenji.kenjiscombatforms.gameasset;

import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.client.model.MeshPartDefinition;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.VertexBuilder;

import java.util.List;
import java.util.Map;

public class CombatFormsMesh extends SkinnedMesh {
    public final SkinnedMeshPart main;
    public CombatFormsMesh(@Nullable Map<String, Number[]> arrayMap, @Nullable Map<MeshPartDefinition, List<VertexBuilder>> partBuilders, @Nullable SkinnedMesh parent, RenderProperties properties) {
        super(arrayMap, partBuilders, parent, properties);
        this.main = this.getOrLogException(this.parts, "noGroups");
    }
}
