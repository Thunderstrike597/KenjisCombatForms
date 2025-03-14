package net.kenji.kenjiscombatforms.api.powers.VoidPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.voidform.ability1.SyncVoidDataPacket;
import net.kenji.kenjiscombatforms.screen.guiEffects.BlinkEffect;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

public class TeleportPlayerBackstab implements Ability {

    private final EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
    private final Map<UUID, EnderPlayerDataSets.TeleportPlayerData> playerDataMap = dataSets.A1playerDataMap;
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();
    private static final TeleportPlayerBackstab INSTANCE = new TeleportPlayerBackstab();


    private EnderPlayerDataSets.TeleportPlayerData getPlayerData(Player player) {
       return (EnderPlayerDataSets.TeleportPlayerData) getAbilityData(player);
    }

    @Override
    public String getName() {
        return AbilityManager.AbilityOption1.VOID_ABILITY1.name();
    }


    @Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerForgeEvents {
        @SubscribeEvent
        public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                getInstance().getPlayerData(serverPlayer);
                getInstance().syncDataToClient(serverPlayer);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            //    getInstance().setAbilityCooldown(serverPlayer, getInstance().getPlayerData(serverPlayer).getMAX_COOLDOWN());
                getInstance().syncDataToClient(serverPlayer);
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

                EnderPlayerDataSets.TeleportPlayerData originalData = getInstance().getPlayerData(originalPlayer);
                EnderPlayerDataSets.TeleportPlayerData newData = getInstance().getPlayerData(newPlayer);

                // Transfer the cooldown data
                newData.abilityCooldown = originalData.abilityCooldown;

                // If you have other data to transfer, do it here
                // newData.otherAttribute = originalData.otherAttribute;

                // Sync the data to the client
                getInstance().syncDataToClient(newPlayer);
            }
        }
    }




    public TeleportPlayerBackstab() {
        // Private constructor to prevent instantiation
    }

    public static TeleportPlayerBackstab getInstance() {
        return INSTANCE;
    }


    public float getAbilityCooldown(ServerPlayer player) {
        return getPlayerData(player).abilityCooldown;
    }

    public void setAbilityCooldown(Player player) {
        getPlayerData(player).abilityCooldown = getPlayerData(player).abilityCooldown + getPlayerData(player).getMAX_COOLDOWN() / KenjisCombatFormsCommon.ABILITY1_COOLDOWN_DIVISION.get();
    }

    ServerPlayer serverPlayer;

    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateTeleportPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {

    }

    @Override
    public void drainPerSecondCooldown(Player player) {

    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(serverPlayer);
        long currentTime = System.currentTimeMillis();
        if (getLookingEntity(serverPlayer, data.maxDist) != null) {
            if (data.abilityCooldown <= data.getMAX_COOLDOWN() / KenjisCombatFormsCommon.ABILITY1_COOLDOWN_DIVISION.get()) {
                activateAbility(serverPlayer);
                playSound(serverPlayer);
                setAbilityCooldown(serverPlayer);
                syncDataToClient(serverPlayer);
            }
        }
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(serverPlayer);

        if(!serverPlayer.isCrouching() || !serverPlayer.isShiftKeyDown()) {
            UUID tpEntityUUID = getLookingEntity(serverPlayer, data.maxDist).getUUID();
            Vec3 targetPos = getLookingEntity(serverPlayer, data.maxDist).getPosition(1);
            Vec3 eyePos = getLookingEntity(serverPlayer, data.maxDist).getEyePosition(1.0f);
            Vec3 targetlookVec = getLookingEntity(serverPlayer, data.maxDist).getViewVector(1.0f);
            double distanceBehind = 5.0;
            double yOffset = 0.5;
            float targetPitch = getLookingEntity(serverPlayer, data.maxDist).getYRot();
            float targetYaw = getLookingEntity(serverPlayer, data.maxDist).getXRot();

            Vec3 behindPos = targetPos.subtract(targetlookVec.scale(distanceBehind));

            serverPlayer.teleportTo(behindPos.x, behindPos.y + yOffset, behindPos.z);
            serverPlayer.lookAt(EntityAnchorArgument.Anchor.FEET, serverPlayer.serverLevel().getEntity(tpEntityUUID).getPosition(1.0f));
            serverPlayer.setXRot(targetYaw);
        }
        else {
            Entity entity = getLookingEntity(serverPlayer, data.maxDist);
            if (entity != null) {
                // Get the player's look vector
                Vec3 lookVec = serverPlayer.getLookAngle();

                // Get the player's position
                Vec3 playerPos = serverPlayer.position();

                // Calculate the target position (2 blocks in front of the player)
                Vec3 targetPos = playerPos.add(lookVec.scale(3));

                // Teleport the entity
                entity.teleportTo(targetPos.x, targetPos.y + 1.5, targetPos.z);

                // Optionally, make the entity face the player after teleporting
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, serverPlayer.position());
                data.isTpHoldActive = true;
                data.holdEntity = entity;
            }
        }
    }




    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }

    @Override
    public void decrementCooldown(Player player) {
    }


    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        tickHoldEntity(player);
    }

    @Override
    public void tickClientAbilityData(Player player) {
    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(player);
        NetworkHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new SyncVoidDataPacket(data.abilityCooldown)
        );
    }

    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }


    @OnlyIn(Dist.CLIENT)
    public void blink(Player player){
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(player);
       if(getLookingEntity(player, data.maxDist) != null) {
           int currentVoidCooldown = ClientVoidData.getCooldown2();
           if (currentVoidCooldown <= data.getMAX_COOLDOWN() / 2) {
               BlinkEffect.triggerFade(player);
           }
       }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        return abilityData.chosenAbility1.name().equals(getName());
    }

    public Entity getLookingEntity(Player player, int maxDistance){
        maxDistance = getPlayerData(player).maxDist;

        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getViewVector(1.0f);
        Vec3 endVec = eyePos.add(lookVec.x * maxDistance, lookVec.y * maxDistance, lookVec.z * maxDistance);

        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                player.level(),
                player,
                eyePos,
                endVec,
                player.getBoundingBox().inflate(maxDistance),
                entity -> !entity.isSpectator(), 0.0f);

        if(hitResult != null){
            return hitResult.getEntity();
        }
        return null;
    }

    private void tickHoldEntity(Player player){
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(player);
        int holdTime = 55;

        if(isAbilityChosenOrEquipped(player)) {
            if (player.isCrouching() || player.isShiftKeyDown()) {
                Entity entity = getLookingEntity(player, data.maxDist);
                if (entity instanceof LivingEntity livingEntity) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 5, 1, false, false));
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 5, -1, false, false));

                }
            }
            if (data.isTpHoldActive) {
                data.holdTickCounter++;
                if (data.holdTickCounter < holdTime) {
                    data.holdEntity.setDeltaMovement(Vec3.ZERO);
                    if (data.holdEntity instanceof LivingEntity livingEntity) {
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING, holdTime, 1, false, false));

                    }
                }
                if (data.holdTickCounter > holdTime) {
                    if (data.holdEntity instanceof LivingEntity livingEntity) {
                        data.isTpHoldActive = false;
                        data.holdTickCounter = 0;
                        data.holdEntity = null;
                        livingEntity.removeEffect(MobEffects.GLOWING);
                    }
                }
            }
        }
    }
}
