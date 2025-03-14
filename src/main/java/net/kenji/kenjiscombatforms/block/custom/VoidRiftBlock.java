package net.kenji.kenjiscombatforms.block.custom;

import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.particle_packets.RiftParticlesTickPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VoidRiftBlock extends Block {
    public static final VoxelShape SHAPE = Block.box(0, 0,0, 6,6, 6);

    public static final int PULL_RADIUS = 10;

    public VoidRiftBlock(Properties pProperties) {
        super(pProperties);
    }




    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return true;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return level.setBlock(pos, fluid.createLegacyBlock(), level.isClientSide ? 11 : 3);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (level.isClientSide) {
            // Add client-side particle effects here
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 20); // Schedule the first tick
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        pullNearbyEntities(level, pos);
        level.scheduleTick(pos, this, 2); // Schedule the next tick
        sendParticlePacket(level, pos);
    }

    private void pullNearbyEntities(ServerLevel level, BlockPos pos) {
        AABB pullArea = new AABB(pos).inflate(PULL_RADIUS);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, pullArea,
                entity -> !(entity instanceof Player && ((Player)entity).getAbilities().flying));

        for (Entity entity : entities) {
            applyPull(entity, pos);
        }
    }

    private void sendParticlePacket(ServerLevel level, BlockPos pos) {
        // Get all players in the server
        for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
            // Check if the player is in the same dimension as the rift
            if (player.level() == level) {
                // Check if the rift is within the player's view distance
                int viewDistance = player.getServer().getPlayerList().getViewDistance() * 16;
                if (player.blockPosition().closerThan(pos, viewDistance)) {
                    NetworkHandler.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new RiftParticlesTickPacket(pos, PULL_RADIUS, 4, 1)
                    );
                }
            }
        }
    }

    private void applyPull(Entity entity, BlockPos riftPos) {
        Vec3 pullVector = Vec3.atCenterOf(riftPos).subtract(entity.position()).normalize();
        double strength = 0.1; // Adjust pull strength as needed
        entity.setDeltaMovement(entity.getDeltaMovement().add(pullVector.scale(strength)));
    }
}
