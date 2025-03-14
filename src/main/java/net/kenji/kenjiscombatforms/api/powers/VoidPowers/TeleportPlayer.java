package net.kenji.kenjiscombatforms.api.powers.VoidPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.network.*;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.voidform.ability1.SyncVoidDataPacket;
import net.kenji.kenjiscombatforms.screen.guiEffects.BlinkEffect;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

public class TeleportPlayer implements Ability {

    private final EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
    private final Map<UUID, EnderPlayerDataSets.TeleportPlayerData> playerDataMap = dataSets.A1playerDataMap;
    private static final TeleportPlayer INSTANCE = new TeleportPlayer();
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();


    @Override
    public String getName() {
        return AbilityManager.AbilityOption1.VOID_ABILITY1.name();
    }


    public EnderPlayerDataSets.TeleportPlayerData getPlayerData(Player player) {
       return (EnderPlayerDataSets.TeleportPlayerData) getAbilityData(player);
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
                getInstance().setAbilityCooldown(serverPlayer);
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

    public TeleportPlayer() {
        // Private constructor to prevent instantiation
    }
    public static TeleportPlayer getInstance() {
        return INSTANCE;
    }
    public void setAbilityCooldown(Player player) {
        getPlayerData(player).abilityCooldown = getPlayerData(player).abilityCooldown + getPlayerData(player).getMAX_COOLDOWN() / KenjisCombatFormsCommon.ABILITY1_COOLDOWN_DIVISION.get();
    }



    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateTeleportPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(player);
        int cooldown = data.abilityCooldown;
        if (cooldown > 0){
            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {

    }



    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(serverPlayer);

        activateAbility(serverPlayer);
        setAbilityCooldown(serverPlayer);
        playSound(serverPlayer);
    }

    @Override
    public void activateAbility(ServerPlayer player) {
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(player);
        player.teleportTo(getLookingBlock(player, data.maxDist).getX(), getLookingBlock(player, data.maxDist).getY() + 1, getLookingBlock(player, data.maxDist).getZ());
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }

    @Override
    public void decrementCooldown(Player player) {
        EnderPlayerDataSets.TeleportPlayerData data = playerDataMap.computeIfAbsent(player.getUUID(), k -> new EnderPlayerDataSets.TeleportPlayerData());
       if(isAbilityChosenOrEquipped(player)) {
            fillPerSecondCooldown(player);
        }
    }


    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(player);
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        if(abilityData.chosenAbility1.name().equals(getName())) {
            getInstance().decrementCooldown(player);
            syncDataToClient(player);
        }
    }

    @Override
    public void tickClientAbilityData(Player player) {

    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        EnderPlayerDataSets.TeleportPlayerData data = getPlayerData(player);
        if(isAbilityChosenOrEquipped(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncVoidDataPacket(data.abilityCooldown)
            );
        }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        return abilityData.chosenAbility1.name().equals(getName());
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
        float currentVoidCooldown = ClientVoidData.getCooldown();

        if (currentVoidCooldown <= data.getMAX_COOLDOWN() / 2 && getLookingBlock(player, data.maxDist) != null) {
            BlinkEffect.triggerFade(player);
        }
    }



    public BlockPos getLookingBlock(Player player, int maxDistance){
        maxDistance = getPlayerData(player).maxDist;

        Vec3 eyePos = player.getEyePosition(1.0f);
        Vec3 lookVec = player.getViewVector(1.0f);
        Vec3 endVec = eyePos.add(lookVec.x * maxDistance, lookVec.y * maxDistance, lookVec.z * maxDistance);

        ClipContext clipContext = new ClipContext(eyePos, endVec, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player);
        BlockHitResult hitResult = player.level().clip(clipContext);

        if(hitResult.getType() == HitResult.Type.BLOCK){
            return hitResult.getBlockPos();
        }
        return null;
    }
}
