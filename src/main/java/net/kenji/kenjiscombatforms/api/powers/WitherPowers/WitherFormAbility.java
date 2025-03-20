package net.kenji.kenjiscombatforms.api.powers.WitherPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbilityDamageGainStrategy;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherPlayerEntity;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.EnderFormItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.WitherFormItem;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.particle_packets.LargeSmokeParticlesTickPacket;
import net.kenji.kenjiscombatforms.network.slots.SwitchItemPacket;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.kenji.kenjiscombatforms.network.witherform.ability3.SyncWitherData3Packet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.PacketDistributor;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class WitherFormAbility implements Ability {



    private final WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
    private final Map<UUID, WitherPlayerDataSets.WitherFormPlayerData> playerDataMap = dataSets.A3playerDataMap;
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();
    private Entity witherEntity;


    @Override
    public String getName() {
        return AbilityManager.AbilityOption3.WITHER_FINAL.name();
    }

    private WitherPlayerDataSets.WitherDashPlayerData getOrCreateDashPlayerData(Player player) {
        return dataSets.getOrCreateDashPlayerData(player);
    }

    private WitherPlayerDataSets.SoulDriftPlayerData getOrCreateSoulDriftPlayerData(Player player) {
        return dataSets.getOrCreateSoulDriftPlayerData(player);
    }

    public WitherPlayerDataSets.WitherFormPlayerData getPlayerData(Player player) {
      return (WitherPlayerDataSets.WitherFormPlayerData) getAbilityData(player);
    }





    @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerForgeEvents {
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().getPlayerData(serverPlayer);
                getInstance().syncDataToClient(serverPlayer);
                getInstance().deactivateAbilityOptional(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            Entity existingDecoy = getInstance().getPlayerData(event.getEntity()).playerWitherMap.remove(event.getEntity().getUUID());
            if (existingDecoy != null) {
                existingDecoy.remove(Entity.RemovalReason.DISCARDED);
            }
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().setAbilityCooldown(serverPlayer, getInstance().getPlayerData(serverPlayer).getMAX_COOLDOWN());
                getInstance().syncDataToClient(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
            Entity existingDecoy = getInstance().getPlayerData(event.getEntity()).playerWitherMap.remove(event.getEntity().getUUID());
            if (existingDecoy != null) {
                existingDecoy.remove(Entity.RemovalReason.DISCARDED);
            }
            getInstance().playerDataMap.remove(event.getEntity().getUUID());
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.getOriginal() instanceof ServerPlayer originalPlayer &&
                    event.getEntity() instanceof ServerPlayer newPlayer) {


                WitherPlayerDataSets.WitherFormPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                WitherPlayerDataSets.WitherFormPlayerData newData = getInstance().getPlayerData(newPlayer);

                // Transfer the cooldown data
                newData.abilityCooldown = originalData.abilityCooldown;

                // If you have other data to transfer, do it here
                // newData.otherAttribute = originalData.otherAttribute;

                // Sync the data to the client
                getInstance().syncDataToClient(newPlayer);
            }
        }
    }


    private static final WitherFormAbility INSTANCE = new WitherFormAbility();


    public WitherFormAbility() {
        // Private constructor to prevent instantiation
    }

    public static WitherFormAbility getInstance() {
        return INSTANCE;
    }


    public int getAbilityCooldown(ServerPlayer player) {
        return getPlayerData(player).abilityCooldown;
    }

    public Map<UUID, WitherPlayerDataSets.WitherFormPlayerData> getPlayerDataMap() {
        return playerDataMap;
    }

    public void setAbilityCooldown(ServerPlayer player, int cooldown) {
        getPlayerData(player).abilityCooldown = Math.min(Math.max(cooldown, 0), getPlayerData(player).getMAX_COOLDOWN());
    }

    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateWitherFormPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(player);
        data.tickCount = dataHandlers.getTickCount(data.tickCount);
        data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
    }

    @Override
    public void drainPerSecondCooldown(Player player) {
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(player);
        data.tickCount = dataHandlers.getTickCount(data.tickCount);
        data.abilityCooldown = dataHandlers.increaseCooldown(data.abilityCooldown, data.tickCount);
    }



    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(serverPlayer);
        if (!data.isWitherActive && data.abilityCooldown == 0) {
            activateAbility(serverPlayer);
            data.hasPlayedSound = false;
        } else {
            deactivateAbilityOptional(serverPlayer);
            restoreItem(serverPlayer);
        }
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(serverPlayer);
        data.isWitherActive = true;
        playSound(serverPlayer);
        setWitherFormItem(serverPlayer);
        syncDataToClient(serverPlayer);
    }

    private static int originalSlot = CommonEventHandler.getInstance().getOriginalSlot();


    private void setWitherFormItem(Player player){
            int selectedSlot = player.getInventory().selected;
            ItemStack currentItem = player.getInventory().getItem(selectedSlot);
            player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                ItemStack storedItem = container.getStoredItem();
                if (storedItem.isEmpty()) {
                    if (!(currentItem.getItem() instanceof BaseFistClass)) {
                        container.setStoredItem(currentItem.copy());
                    }
                    player.getInventory().setItem(selectedSlot, ItemStack.EMPTY);
                    player.getInventory().setItem(selectedSlot, WitherFormItem.getInstance().getDefaultInstance());
                    originalSlot = selectedSlot; // Save the original slot
                    NetworkHandler.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                            new SwitchItemPacket(originalSlot, storedItem)
                    );                player.getInventory().setChanged();

                    player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
                }
            });
        }
        private void restoreItem(Player player){
            int selectedSlot = player.getInventory().selected;
            ItemStack currentItem = player.getInventory().getItem(selectedSlot);
            player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                ItemStack storedItem = container.getStoredItem();
                if (!storedItem.isEmpty()) {
                    player.getInventory().setItem(originalSlot, container.getStoredItem());
                    container.setStoredItem(ItemStack.EMPTY);

                }else {
                    player.getInventory().setItem(originalSlot, ItemStack.EMPTY);
                }
                originalSlot = -1;

                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> (ServerPlayer) player),
                        new SwitchItemPacket(originalSlot, storedItem)
                );
                player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);

            });
        }

    public boolean getWitherFormActive(Player player){
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(player);
        return data.isWitherActive;
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(serverPlayer);
        data.isWitherActive = false;

        serverPlayer.removeEffect(MobEffects.INVISIBILITY);
        serverPlayer.removeEffect(MobEffects.LEVITATION);



        Entity existingWither = data.playerWitherMap.remove(serverPlayer.getUUID());
        //data.witherEntity = existingWither;
        if (existingWither != null) {
            existingWither.remove(Entity.RemovalReason.DISCARDED);
        }
        syncDataToClient(serverPlayer);
    }

    public static class CurrentDamageGainStrategy implements AbilityDamageGainStrategy {


        @Override
        public void fillDamageCooldown(Player player) {
            WitherPlayerDataSets.WitherFormPlayerData data = getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherPlayerDataSets.WitherFormPlayerData());
            if(KenjisCombatFormsCommon.ABILITY3_COMBAT_MODE.get()) {
               if(!data.isAbilityActive()) {
                   if (data.abilityCooldown > 0) {
                       data.abilityCooldown = data.abilityCooldown - KenjisCombatFormsCommon.COMBAT_MODE_GAIN_AMOUNT.get();
                   }
               } else {
                   if (data.abilityCooldown > 0) {
                       data.abilityCooldown = data.abilityCooldown - KenjisCombatFormsCommon.COMBAT_MODE_GAIN_AMOUNT.get() / 2;
                   }
               }

                if(player instanceof ServerPlayer serverPlayer) {
                    getInstance().syncDataToClient(serverPlayer);
                }
            }
        }
    }

    @Override
    public void decrementCooldown(Player player) {
        WitherPlayerDataSets.WitherFormPlayerData data = playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherPlayerDataSets.WitherFormPlayerData());
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        if (isAbilityChosenOrEquipped(player)) {
            if (EffectiveSide.get() == LogicalSide.SERVER) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if (data.abilityCooldown > 0 && !data.isWitherActive) {
                        if (!KenjisCombatFormsCommon.ABILITY3_COMBAT_MODE.get()) {

                            WitherPlayerDataSets.WitherDashPlayerData sData = getOrCreateDashPlayerData(serverPlayer);
                            WitherPlayerDataSets.SoulDriftPlayerData bData = getOrCreateSoulDriftPlayerData(serverPlayer);
                            if (abilityData.chosenFinal == AbilityManager.AbilityOption3.WITHER_FINAL)
                                fillPerSecondCooldown(player);
                        }
                    }if (data.abilityCooldown < data.getMAX_COOLDOWN() && data.isWitherActive) {
                        drainPerSecondCooldown(player);
                    }
                    if (data.abilityCooldown >= data.getMAX_COOLDOWN() && data.isWitherActive) {
                        deactivateAbilityOptional(serverPlayer);
                        Entity existingShadow = data.playerWitherMap.remove(player.getUUID());
                        if (existingShadow != null) {
                            existingShadow.remove(Entity.RemovalReason.DISCARDED);
                        }
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
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        if(AbilityManager.getInstance().getPlayerAbilityData(player).chosenFinal.name().equals(getName())) {

            if (abilityData.chosenFinal.name().equals(getName())) {
                getInstance().decrementCooldown(player);
            }
            syncDataToClient(player);
            if (data.isWitherActive) {

                    player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 5, -1, false, false));

                    NetworkHandler.INSTANCE.send(
                            PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> player),
                            new LargeSmokeParticlesTickPacket(player.getX(), player.getY(), player.getZ(), player.isInvisible())
                    );
            }
        }
    }

    public void upDown(Player player, boolean sneak, boolean jump){
        Vec3 velocity = player.getDeltaMovement(); // Get current velocity

        if (jump) {
            player.setDeltaMovement(velocity.x, 0.2, velocity.z); // Move up
        }
        if (sneak) {
            player.setDeltaMovement(velocity.x, -0.2, velocity.z); // Move down
        }
    }


    @Override
    public void tickClientAbilityData(Player player) {
        WitherPlayerDataSets.WitherFormPlayerData data = getInstance().playerDataMap.computeIfAbsent(player.getUUID(), k -> new WitherPlayerDataSets.WitherFormPlayerData());
         if(AbilityManager.getInstance().getPlayerAbilityData(player).chosenFinal.name().equals(getName())) {
             if (ClientWitherData.getIsWitherActive()) {
                 //preventCombatActions(player);

                 //player.noPhysics = true;
             }
             if (!ClientWitherData.getIsWitherActive()) {

             }
         }
    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(player);
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncWitherData3Packet(data.abilityCooldown, data.isWitherActive, player.getUUID())
            );
    }


    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        return abilityData.chosenFinal.name().equals(getName());
    }


/*
    public void activateWitherSummon(ServerPlayer player) {
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(player);
        data.hasPlayedSound = false;
        data.isWitherActive = true;

        getNearestEntity(player);
        witherEntity = ModEntities.WITHER_PLAYER.get().spawn(player.serverLevel(), player.blockPosition(), MobSpawnType.TRIGGERED);
        data.witherEntity = witherEntity;
        if(data.witherEntity != null) {
            data.witherEntity.setYRot(player.getYRot());
            data.witherEntity.setXRot(player.getXRot());

            if (witherEntity != null) {
                data.playerWitherMap.put(player.getUUID(), witherEntity);
            }
        }
    }
*/


    /*public void preventCombatActions(Player player) {
        WitherPlayerDataSets.WitherFormPlayerData data = getPlayerData(player);

        player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
            if (cap instanceof PlayerPatch playerPatch) {
                playerPatch.getAnimator().playAnimation(Animations.BIPED_WALK, 0.0F);
                playerPatch.getEntityState().setState(EntityState.INACTION, true);
                playerPatch.setLastAttackSuccess(false);
                playerPatch.getOriginal().skipAttackInteraction(data.witherEntity);

            }
        });
    }*/

    private Entity getNearestEntity(Player player) {
        double searchRadius = 10.0; // Adjust this value as needed
        AABB searchBox = player.getBoundingBox().inflate(searchRadius);

        List<Entity> nearbyEntities = player.level().getEntities(player, searchBox);

        Entity closestEntity = null;
        double closestDistanceSq = Double.MAX_VALUE;

        for (Entity entity : nearbyEntities) {
            if (entity instanceof EnderEntity) continue;
            double distanceSq = player.distanceToSqr(entity);
            if (distanceSq < closestDistanceSq) {
                closestDistanceSq = distanceSq;
                closestEntity = entity;
            }
        }

        return closestEntity;
    }





    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }


    @OnlyIn(Dist.CLIENT)
    public void spawnParticles(Player player) {
        int currentVoidCooldown = ClientVoidData.getCooldown3();
        BlockParticleOption whiteFallingDust = new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.WHITE_WOOL.defaultBlockState());

        Random random = new Random();
        int particleCount = 100;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        for (int i = 0; i < particleCount; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2; // Spread in X direction
            double offsetY = (random.nextDouble() - 0.5) * 2; // Spread in Y direction
            double offsetZ = (random.nextDouble() - 0.5) * 2; // Spread in Z direction

            double velocityX = (random.nextDouble() - 0.5) * 0.5; // Random X velocity
            double velocityY = random.nextDouble() * 0.5; // Upward Y velocity
            double velocityZ = (random.nextDouble() - 0.5) * 0.5; // Random Z velocity

            player.level().addParticle(
                    ParticleTypes.PORTAL,
                    x + offsetX,
                    y + 1 + offsetY,
                    z + offsetZ,
                    velocityX,
                    velocityY,
                    velocityZ
            );
        }
    }
}
