package net.kenji.kenjiscombatforms.api.powers.VoidPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.SwiftPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbilityDamageGainStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.block.ModBlocks;
import net.kenji.kenjiscombatforms.block.custom.VoidRiftBlock;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.event.sound.SoundManager;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.particle_packets.RiftSummonParticlesTickPacket;
import net.kenji.kenjiscombatforms.network.voidform.ability2.SyncVoidData2Packet;
import net.kenji.kenjiscombatforms.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

public class VoidAnchorRift implements Ability {

    private final EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
    private final Map<UUID, EnderPlayerDataSets.VoidRiftPlayerData> playerDataMap = dataSets.A2playerDataMap;
    private static final VoidAnchorRift INSTANCE = new VoidAnchorRift();
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();



    @Override
    public String getName() {
        return AbilityManager.AbilityOption2.VOID_ABILITY2.name();
    }




    private EnderPlayerDataSets.VoidRiftPlayerData getPlayerData(Player player) {
      return (EnderPlayerDataSets.VoidRiftPlayerData) getAbilityData(player);
    }


    @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerForgeEvents {
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().getPlayerData(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().setAbilityCooldown(serverPlayer, getInstance().getPlayerData(serverPlayer).getMAX_COOLDOWN());
            }
        }

        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            getInstance().playerDataMap.remove(event.getEntity().getUUID());
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.getOriginal() instanceof ServerPlayer originalPlayer &&
                    event.getEntity() instanceof ServerPlayer newPlayer) {

                EnderPlayerDataSets.VoidRiftPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                EnderPlayerDataSets.VoidRiftPlayerData newData = getInstance().getPlayerData(newPlayer);

                newData.abilityCooldown = originalData.abilityCooldown;

            }
        }
    }




    public VoidAnchorRift() {
        // Private constructor to prevent instantiation
    }

    public static VoidAnchorRift getInstance() {
        return INSTANCE;
    }


    public int getAbilityCooldown(ServerPlayer player) {
        return getPlayerData(player).abilityCooldown;
    }

    public void setAbilityCooldown(ServerPlayer player, int cooldown) {
        getPlayerData(player).abilityCooldown = Math.min(Math.max(cooldown, 0), getPlayerData(player).getMAX_COOLDOWN());
    }













    private void timerRiftPlacement(ServerPlayer player){
        EnderPlayerDataSets.VoidRiftPlayerData data = getPlayerData(player);

        Block voidRiftBlock = ModBlocks.VOID_RIFT.get();
        BlockState voidRiftState = voidRiftBlock.defaultBlockState();

        if(data.canCount) {
            data.timerCount++;
            if (data.timerCount > 100) {
                if (data.riftPosition != null) {
                    playSound2(player);
                    player.level().setBlock(data.riftPosition, voidRiftState, 3);
                    data.timerCount = 0;
                    data.canCount = false;
                    if(player.level().isClientSide) {
                        stopCustomSound(player);
                    }
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void stopCustomSound(Player player){
        EnderPlayerDataSets.VoidRiftPlayerData data = playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderPlayerDataSets.VoidRiftPlayerData());
        SoundManager.stopCustomSound(player.level(), data.riftPosition);
    }
    @OnlyIn(Dist.CLIENT)
    private void playCustomSound(Player player){
        EnderPlayerDataSets.VoidRiftPlayerData data = playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderPlayerDataSets.VoidRiftPlayerData());
        SoundManager.playCustomSound(player.level(), player.blockPosition(), ModSounds.VOID_RIFT_SOUND.get(), SoundSource.BLOCKS, 0.8f, 1);
    }



    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateVoidRiftPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        EnderPlayerDataSets.VoidRiftPlayerData data = getPlayerData(player);
        int cooldown = data.abilityCooldown;
        if (cooldown > 0){
            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {
        EnderPlayerDataSets.VoidRiftPlayerData data = getPlayerData(player);
        data.tickCount = dataHandlers.getTickCount(data.tickCount);
        data.abilityCooldown = dataHandlers.increaseCooldown(data.abilityCooldown, data.tickCount);
    }



    public static class CurrentDamageGainStrategy implements AbilityDamageGainStrategy {


        @Override
        public void fillDamageCooldown(Player player) {
            EnderPlayerDataSets.VoidRiftPlayerData data = getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderPlayerDataSets.VoidRiftPlayerData());

            if(KenjisCombatFormsCommon.ABILITY2_COMBAT_MODE.get()) {
               if(!data.isAbilityActive()) {
                   if (data.abilityCooldown > 0) {
                       data.abilityCooldown = data.abilityCooldown - KenjisCombatFormsCommon.COMBAT_MODE_GAIN_AMOUNT.get();
                   }
                   if (player instanceof ServerPlayer serverPlayer) {
                       getInstance().syncDataToClient(serverPlayer);
                   }
               }
            }
        }
    }

    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.VoidRiftPlayerData data = getPlayerData(serverPlayer);
        if (data.abilityCooldown <= 0) {
            activateAbility(serverPlayer);
        }else deactivateAbilityOptional(serverPlayer);

    }

    @Override
    public void activateAbility(ServerPlayer player) {
        EnderPlayerDataSets.VoidRiftPlayerData data = getPlayerData(player);
        data.isRiftActive = true;
        data.canCount = true;
        if(player.level().isClientSide) {
            playCustomSound(player);
        }
        BlockPos riftBlockPos = player.blockPosition();
        data.riftPosition = riftBlockPos.immutable();

        NetworkHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new RiftSummonParticlesTickPacket(player.blockPosition(), 4, 64, 24)
        );
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.VoidRiftPlayerData data = getPlayerData(serverPlayer);
        if (data.isRiftActive && data.riftPosition != null) {
            ServerLevel level = serverPlayer.serverLevel();
            if (level.getBlockState(data.riftPosition).getBlock() instanceof VoidRiftBlock) {
                level.setBlock(data.riftPosition, Blocks.AIR.defaultBlockState(), 3);
                level.sendBlockUpdated(data.riftPosition, level.getBlockState(data.riftPosition), Blocks.AIR.defaultBlockState(), 3);
                serverPlayer.connection.send(new ClientboundBlockUpdatePacket(level, data.riftPosition));
            }
            data.isRiftActive = false;
            data.riftPosition = null; // Clear the stored position
        }
    }

    @Override
    public void decrementCooldown(Player player) {
        EnderPlayerDataSets.VoidRiftPlayerData data = playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderPlayerDataSets.VoidRiftPlayerData());

            if (isAbilityChosenOrEquipped(player)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if (EffectiveSide.get() == LogicalSide.SERVER) {
                        if(!KenjisCombatFormsCommon.ABILITY2_COMBAT_MODE.get()) {
                            if (!data.isRiftActive) {
                                fillPerSecondCooldown(player);
                            }
                        }
                        if (data.abilityCooldown < data.getMAX_COOLDOWN() && data.isRiftActive) {
                            drainPerSecondCooldown(player);
                            drainPerSecondCooldown(player);
                        }
                        if (data.abilityCooldown >= data.getMAX_COOLDOWN() && data.isRiftActive) {

                            deactivateAbilityOptional(serverPlayer);
                            if (!data.hasPlayedSound) {
                                data.hasPlayedSound = true;
                                playSound(player);
                            }
                        }
                    }
                }
        }
    }

    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        EnderPlayerDataSets.VoidRiftPlayerData data = getPlayerData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        if(abilityData.chosenAbility2.name().equals(getName())) {
            getInstance().decrementCooldown(player);
            syncDataToClient(player);
            timerRiftPlacement(player);
        }
    }

    @Override
    public void tickClientAbilityData(Player player) {
    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        EnderPlayerDataSets.VoidRiftPlayerData data = getPlayerData(player);
        if(isAbilityChosenOrEquipped(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncVoidData2Packet(data.abilityCooldown)
            );
        }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        return abilityData.chosenAbility2.name().equals(getName());
    }

    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDERMAN_STARE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
