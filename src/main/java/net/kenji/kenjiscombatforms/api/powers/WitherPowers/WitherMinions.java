package net.kenji.kenjiscombatforms.api.powers.WitherPowers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.forms.VoidForm;
import net.kenji.kenjiscombatforms.entity.ModEntities;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.WitherMinionEntity;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.globalformpackets.SyncAbility4Packet;
import net.kenji.kenjiscombatforms.network.particle_packets.MinionSummonParticlesPacket;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability4.SummonWitherMinionsPacket;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability4.SyncWitherData4Packet;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
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

public class WitherMinions implements FinalAbility {
    WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();


    private final Map<UUID, WitherPlayerDataSets.WitherMinionPlayerData> playerDataMap = WitherPlayerDataSets.getInstance().A4playerDataMap;
    public String teamName = "active_minions";
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();
    private static final WitherMinions INSTANCE = new WitherMinions();




    @Override
    public String getName() {
        return "WITHER_MINIONS";
    }

    @Override
    public String getFinalAbilityName() {
        return "WITHER_FINAL";
    }


    private WitherPlayerDataSets.WitherMinionPlayerData getPlayerData(Player player) {
       return (WitherPlayerDataSets.WitherMinionPlayerData) getAbilityData(player);
    }

    private WitherPlayerDataSets.WitherDashPlayerData getOrCreateDashPlayerData(Player player) {
        return dataSets.getOrCreateDashPlayerData(player);
    }

    private WitherPlayerDataSets.WitherFormPlayerData getOrCreateWitherFormPlayerData(Player player) {
        return dataSets.getOrCreateWitherFormPlayerData(player);
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
                getInstance().setAbilityCooldown(serverPlayer, getInstance().getPlayerData(serverPlayer).getMAX_COOLDOWN());
                getInstance().syncDataToClient(serverPlayer);
            }
        }
    }

public void witherMinionRemove(ServerPlayer player){
    WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(player);
    Entity existingMinion = data.playerMinionMap.remove(player.getUUID());
    Entity existingMinion2 = data.playerMinion2Map.remove(player.getUUID());
    Entity existingMinion3 = data.playerMinion3Map.remove(player.getUUID());
    Entity existingMinion4 = data.playerMinion4Map.remove(player.getUUID());
    if (existingMinion != null) {
        existingMinion.remove(Entity.RemovalReason.DISCARDED);
    }
    if (existingMinion2 != null) {
        existingMinion2.remove(Entity.RemovalReason.DISCARDED);
    }
    if (existingMinion3 != null) {
        existingMinion3.remove(Entity.RemovalReason.DISCARDED);
    }
    if (existingMinion4 != null) {
        existingMinion4.remove(Entity.RemovalReason.DISCARDED);
    }
    ClientWitherData.setAreMinionsActive(false);
}


    public WitherMinions() {
        // Private constructor to prevent instantiation
    }

    public static WitherMinions getInstance() {
        return INSTANCE;
    }


    public int getAbilityCooldown(ServerPlayer player) {
        return getOrCreateDashPlayerData(player).abilityCooldown;
    }

    public void setAbilityCooldown(ServerPlayer player, int cooldown) {
        getOrCreateDashPlayerData(player).abilityCooldown = Math.min(Math.max(cooldown, 0), getPlayerData(player).getMAX_COOLDOWN());
    }




    @OnlyIn(Dist.CLIENT)
    public void clientChangeActive(Player player){
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(player);
        int currentMinionCooldown = data.getClientAbilityCooldown();

        if (currentMinionCooldown == 0) {
            data.areMinionsActive = true;
        }
    }






    private void timerMinionSummon(ServerPlayer player){
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(player);
        WitherPlayerDataSets.WitherFormPlayerData bData =  getOrCreateWitherFormPlayerData(player);

        if(data.canCount) {
            data.timerCount++;
            if(data.timerCount > 100) {
                playSound2(player);
                if(data.MINION_COUNT == 1) {
                    data.minionEntity = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(0, 0, -1), MobSpawnType.TRIGGERED);
                    if(data.minionEntity != null) {
                        data.playerMinionMap.put(player.getUUID(), data.minionEntity);
                    }
                }
                else if(data.MINION_COUNT == 2) {
                    data.minionEntity = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(1, 0, 0), MobSpawnType.TRIGGERED);
                    data.minionEntity2 = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(-1, 0, 0), MobSpawnType.TRIGGERED);
                    if(data.minionEntity != null && data.minionEntity2 != null) {
                        data.playerMinionMap.put(player.getUUID(), data.minionEntity);
                        data.playerMinion2Map.put(player.getUUID(), data.minionEntity2);
                    }
                }
                else if(data.MINION_COUNT == 3) {
                    data.minionEntity = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(1, 0, 0), MobSpawnType.TRIGGERED);
                    data.minionEntity2 = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(-1, 0, 0), MobSpawnType.TRIGGERED);
                    data.minionEntity3 = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(0, 0, -1), MobSpawnType.TRIGGERED);
                    if(data.minionEntity != null && data.minionEntity2 != null && data.minionEntity3 != null) {
                        data.playerMinionMap.put(player.getUUID(), data.minionEntity);
                        data.playerMinion2Map.put(player.getUUID(), data.minionEntity2);
                        data.playerMinion3Map.put(player.getUUID(), data.minionEntity3);
                    }
                }
                else if(data.MINION_COUNT == 4) {
                    data.minionEntity = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(1, 0, 0), MobSpawnType.TRIGGERED);
                    data.minionEntity2 = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(-1, 0, 0), MobSpawnType.TRIGGERED);
                    data.minionEntity3 = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(0, 0, -1), MobSpawnType.TRIGGERED);
                    data.minionEntity4 = ModEntities.WITHER_MINION.get().spawn(player.serverLevel(), player.blockPosition().offset(0, 0, 1), MobSpawnType.TRIGGERED);
                    if(data.minionEntity != null && data.minionEntity2 != null && data.minionEntity3 != null && data.minionEntity4 != null) {
                        data.playerMinionMap.put(player.getUUID(), data.minionEntity);
                        data.playerMinion2Map.put(player.getUUID(), data.minionEntity2);
                        data.playerMinion3Map.put(player.getUUID(), data.minionEntity3);
                        data.playerMinion4Map.put(player.getUUID(), data.minionEntity4);
                    }
                }

                Scoreboard scoreboard = player.level().getScoreboard();
                bData.isDelayPause = false;


                PlayerTeam team = scoreboard.getPlayerTeam(teamName);
                if(team == null){
                    team = scoreboard.addPlayerTeam(teamName);
                    team.setColor(ChatFormatting.DARK_GRAY);
                    team.setAllowFriendlyFire(false);
                }
                if(data.minionEntity instanceof WitherMinionEntity minionEntity) {
                    minionEntity.setOwnerUUID(data.playerUUID);
                    scoreboard.addPlayerToTeam(minionEntity.getStringUUID(), team);
                }
                if(data.minionEntity2 instanceof WitherMinionEntity minionEntity2) {
                    minionEntity2.setOwnerUUID(data.playerUUID);
                    scoreboard.addPlayerToTeam(minionEntity2.getStringUUID(), team);
                }
                if(data.minionEntity3 instanceof WitherMinionEntity minionEntity3) {
                    minionEntity3.setOwnerUUID(data.playerUUID);
                    scoreboard.addPlayerToTeam(minionEntity3.getStringUUID(), team);
                }
                if(data.minionEntity4 instanceof WitherMinionEntity minionEntity4) {
                    minionEntity4.setOwnerUUID(data.playerUUID);
                    scoreboard.addPlayerToTeam(minionEntity4.getStringUUID(), team);
                }
                data.timerCount = 0;
                data.canCount = false;
            }
        }
    }





    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateMinionPlayerData(player);
    }

    @Override
    public boolean getFinalAbilityActive(Player player) {
        return AbilityManager.getInstance().getAbility(getFinalAbilityName()).getAbilityData(player).isAbilityActive();
    }

    @Override
    public boolean getAbilityActive(Player player) {
        return getAbilityData(player).isAbilityActive();
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(player);
        int cooldown = data.abilityCooldown;
        if (cooldown > 0){
            data.tickCount = dataHandlers.getTickCount(data.tickCount);
            data.abilityCooldown = dataHandlers.decreaseCooldown(data.abilityCooldown, data.tickCount);
        }
    }

    @Override
    public void drainPerSecondCooldown(Player player) {
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(player);
        data.tickCount = dataHandlers.getTickCount(data.tickCount);
        data.abilityCooldown = dataHandlers.increaseCooldown(data.abilityCooldown, data.tickCount);
    }

    @Override
    public void sendPacketToServer(Player player) {
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(player);
        String finalAbilityName = getFinalAbilityName();
        Ability finalAbility = AbilityManager.getInstance().getAbility(finalAbilityName);

        if (finalAbility.getAbilityActive(player)) {
            NetworkHandler.INSTANCE.sendToServer(new SummonWitherMinionsPacket());
            WitherMinions.getInstance().clientChangeActive(player);
        }
    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(serverPlayer);
        WitherPlayerDataSets.WitherDashPlayerData aData = dataSets.getOrCreateDashPlayerData(serverPlayer);
        WitherPlayerDataSets.WitherFormPlayerData bData = getInstance().getOrCreateWitherFormPlayerData(serverPlayer);
        long currentTime = System.currentTimeMillis();
        if (data.abilityCooldown == 0) {
            activateAbility(serverPlayer);
            bData.isDelayPause = true;
            data.playerUUID = serverPlayer.getUUID();
        }else deactivateAbilityOptional(serverPlayer);
    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(serverPlayer);
        data.areMinionsActive = true;
        ClientWitherData.setAreMinionsActive(true);
        data.canCount = true;
        WitherPlayerDataSets.WitherDashPlayerData aData = getOrCreateDashPlayerData(serverPlayer);
        playSound(serverPlayer);

        NetworkHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> serverPlayer),
                new MinionSummonParticlesPacket(serverPlayer.blockPosition(), 4, 64, 24)
        );
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(serverPlayer);
        Entity existingMinion = data.playerMinionMap.remove(serverPlayer.getUUID());
        Entity existingMinion2 = data.playerMinion2Map.remove(serverPlayer.getUUID());
        Entity existingMinion3 = data.playerMinion3Map.remove(serverPlayer.getUUID());
        Entity existingMinion4 = data.playerMinion4Map.remove(serverPlayer.getUUID());

        if (existingMinion != null) {
            existingMinion.remove(Entity.RemovalReason.DISCARDED);
        }
        if (existingMinion2 != null) {
            existingMinion2.remove(Entity.RemovalReason.DISCARDED);
        }
        if (existingMinion3 != null) {
            existingMinion3.remove(Entity.RemovalReason.DISCARDED);
        }
        if (existingMinion4 != null) {
            existingMinion4.remove(Entity.RemovalReason.DISCARDED);
        }
        data.areMinionsActive = false;

        getInstance().witherMinionRemove(serverPlayer);
        getInstance().playerDataMap.remove(serverPlayer.getUUID());
        ClientWitherData.setAreMinionsActive(false);
    }

    @Override
    public void decrementCooldown(Player player) {
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(player);
        WitherPlayerDataSets.WitherFormPlayerData bData = getInstance().getOrCreateWitherFormPlayerData(player);
        if(getFinalAbilityActive(player)) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (EffectiveSide.get() == LogicalSide.SERVER) {
                    if (!data.areMinionsActive && !getAbilityActive(player) && getFinalAbilityActive(player)) {
                        fillPerSecondCooldown(player);
                        fillPerSecondCooldown(player);
                        fillPerSecondCooldown(player);
                        fillPerSecondCooldown(player);
                        fillPerSecondCooldown(player);
                        fillPerSecondCooldown(player);
                    } else if (data.areMinionsActive || getAbilityActive(player)) {
                        drainPerSecondCooldown(player);
                    }
                    if (data.abilityCooldown >= data.getMAX_COOLDOWN() && data.areMinionsActive) {
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
        getInstance().decrementCooldown(player);
        syncDataToClient(player);
        timerMinionSummon(player);
    }

    @Override
    public void tickClientAbilityData(Player player) {

    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        WitherPlayerDataSets.WitherMinionPlayerData data = getPlayerData(player);
        if(getFinalAbilityActive(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncAbility4Packet(data.abilityCooldown, data.areMinionsActive)
            );
        }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        return abilityData.chosenFinal.equals(getName());
    }


    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.WITHER_AMBIENT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ILLUSIONER_MIRROR_MOVE, SoundSource.PLAYERS, 1.0F, 1.0F);
    }
}
