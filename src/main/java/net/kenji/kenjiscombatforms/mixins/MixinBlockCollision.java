package net.kenji.kenjiscombatforms.mixins;

import com.google.j2objc.annotations.ReflectionSupport;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherDash;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
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
        if (player != null && pos.closerToCenterThan(player.position(), 5.0)) {
          if(!WitherFormAbility.getInstance().getWitherFormActive(player) && !ClientWitherData.getIsWitherActive()) {
              if (WitherDash.getInstance().getDashActive(player) || ClientWitherData.getIsDashActive()) {
                  if(WitherDash.getInstance().getIgnoreCollide(player) || ClientWitherData.isCanIgnoreCollide()){
                      cir.setReturnValue(Shapes.empty());
                  }
              }
          }else {
              if (WitherFormAbility.getInstance().getDashActive(player) || ClientWitherData.getIsWitherDashActive() || isInBlock(player)) {
                  cir.setReturnValue(Shapes.empty());
              }
          }
        }
    }
}
