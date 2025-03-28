package net.kenji.kenjiscombatforms.mixins;

import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherDash;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Mixin(BlockBehaviour.class)
public abstract class MixinBlockCollision {
    private static final Map<UUID, Integer> blocksPassed = new HashMap<>();
    private static final int MAX_BLOCKS = 5;

    private boolean isInBlock(Player player){
        BlockPos playerPos = player.blockPosition();
        return !player.level().getBlockState(playerPos.above()).isAir();
    }

    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private void modifyCollision(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        Player player = context instanceof EntityCollisionContext entityContext ? entityContext.getEntity() instanceof Player p ? p : null : null;
        if (player != null) {
            Ability ability3 = AbilityManager.getInstance().getCurrentAbilities(player).get(2);
            Ability ability2 = AbilityManager.getInstance().getCurrentAbilities(player).get(1);

            AbstractAbilityData ability3Data = AbilityManager.getInstance().getCurrentAbilityData(player).get(2);
            AbstractAbilityData ability2Data = AbilityManager.getInstance().getCurrentAbilityData(player).get(1);
            String chosenFinal = ability3.getName();


            if (pos.closerToCenterThan(player.position(), 5.0)) {
                if (!WitherFormAbility.getInstance().getAbilityActive(player)) {
                    if (Objects.equals(ability2.getName(), WitherDash.getInstance().getName())) {
                        if (WitherDash.getInstance().getDashActive(player) || ability2Data.isAbilityActive()) {
                            if (WitherDash.getInstance().getIgnoreCollide(player) || ability2Data.getAbilityAltActive()) {
                                cir.setReturnValue(Shapes.empty());
                            }
                        }
                    }
                } else if (Objects.equals(chosenFinal, WitherFormAbility.getInstance().getName())) {
                    if (WitherFormAbility.getInstance().getDashActive(player) || ability3Data.getAbilityAltActive() || isInBlock(player)) {
                        cir.setReturnValue(Shapes.empty());
                    }
                }
            }
        }
    }
}
