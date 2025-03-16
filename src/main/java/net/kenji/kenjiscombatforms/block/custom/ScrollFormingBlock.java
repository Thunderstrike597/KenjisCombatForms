package net.kenji.kenjiscombatforms.block.custom;

import net.kenji.kenjiscombatforms.block.entity.ScrollFormingBlockEntity;
import net.kenji.kenjiscombatforms.block.entity.ModBlockEntities;
import net.kenji.kenjiscombatforms.event.sound.SoundManager;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.sound.SoundTriggerPacket;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4.SyncVoidData4Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ScrollFormingBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0,0, 16,11, 16);

    public ScrollFormingBlock(Properties pProperties) {
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
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newBlockState, boolean b) {
        if (blockState.getBlock() != newBlockState.getBlock()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof ScrollFormingBlockEntity) {
                ((ScrollFormingBlockEntity)blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, b);
    }



    @Override
    public @NotNull InteractionResult use(@NotNull BlockState blockState, Level plevel, @NotNull BlockPos blockPos, Player player_, @NotNull InteractionHand interactionHand, @NotNull BlockHitResult hitResult) {
        if (!plevel.isClientSide()) {
            BlockEntity entity = plevel.getBlockEntity(blockPos);
            if(entity instanceof ScrollFormingBlockEntity) {
                NetworkHooks.openScreen(((ServerPlayer)player_), (ScrollFormingBlockEntity)entity, blockPos);
                SoundManager.playCustomSound(player_.level(), blockPos, SoundEvents.UI_LOOM_SELECT_PATTERN, SoundSource.BLOCKS, 1.0f, 1.0f);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.sidedSuccess(plevel.isClientSide());
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ScrollFormingBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        if(level.isClientSide()) {
            return null;
        }
        return createTickerHelper(blockEntityType, ModBlockEntities.SCROLL_FORMING_TABLE_BE.get(),
                ((level1, blockPos, blockState1, blockEntity) -> blockEntity.tick(level1, blockPos, blockState1)));
    }
}
