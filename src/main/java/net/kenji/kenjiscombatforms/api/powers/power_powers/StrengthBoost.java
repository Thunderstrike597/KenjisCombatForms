package net.kenji.kenjiscombatforms.api.powers.power_powers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.power_data.PowerPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.event.CommonFunctions;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.globalformpackets.SyncAbility1Packet;
import net.kenji.kenjiscombatforms.network.power_form.ClientPowerData;
import net.kenji.kenjiscombatforms.network.power_form.ability1.StrengthBoostPacket;
import net.kenji.kenjiscombatforms.network.power_form.ability1.SyncPowerDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.Map;
import java.util.UUID;

public class StrengthBoost implements Ability {

    private final PowerPlayerDataSets dataSets = PowerPlayerDataSets.getInstance();
    private final Map<UUID, PowerPlayerDataSets.StrengthPlayerData> playerDataMap = dataSets.A1playerDataMap;
    private static final StrengthBoost INSTANCE = new StrengthBoost();
    private final CommonFunctions dataHandlers = CommonFunctions.getInstance();


    @Override
    public String getName() {
        return "POWER_ABILITY1";
    }

    @Override
    public int getGUIDrawPosY() {
        return 230;
    }

    @Override
    public int getGUIDrawPosX() {
        return 186;
    }


    PowerPlayerDataSets.StrengthPlayerData getPlayerData(Player player){
        return (PowerPlayerDataSets.StrengthPlayerData) getAbilityData(player);
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

                AbstractAbilityData originalData = getInstance().getPlayerData(originalPlayer);
                AbstractAbilityData newData = getInstance().getPlayerData(newPlayer);


                getInstance().syncDataToClient(newPlayer);
            }
        }
    }

    public StrengthBoost() {
        // Private constructor to prevent instantiation
    }

    public static StrengthBoost getInstance() {
        return INSTANCE;
    }


    public float getAbilityCooldown(Player player) {
        PowerPlayerDataSets.StrengthPlayerData data = getPlayerData(player);

        return data.abilityCooldown;
    }
    public void setAbilityCooldown(Player player) {
        PowerPlayerDataSets.StrengthPlayerData data = getPlayerData(player);
       data.abilityCooldown = data.abilityCooldown + data.getMAX_COOLDOWN() / 2;
    }


    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return dataSets.getOrCreateStrengthPlayerData(player);
    }

    @Override
    public void fillPerSecondCooldown(Player player) {
        PowerPlayerDataSets.StrengthPlayerData data = getPlayerData(player);
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
    public boolean getAbilityActive(Player player) {
        return getAbilityData(player).isAbilityActive();
    }

    @Override
    public void sendPacketToServer(Player player) {
        if(!ClientEventHandler.getInstance().getAreFinalsActive()){
            NetworkHandler.INSTANCE.sendToServer(new StrengthBoostPacket());
        }
    }


    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {
        long currentTime = System.currentTimeMillis();
        PowerPlayerDataSets.StrengthPlayerData data = getPlayerData(serverPlayer);

        if(data.abilityCooldown  <= 0) {
            activateAbility(serverPlayer);
            playSound(serverPlayer);

        }
    }


    @Override
    public void activateAbility(ServerPlayer player) {
        PowerPlayerDataSets.StrengthPlayerData data = getPlayerData(player);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 1, false, true));
    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }


    public void decrementCooldown(Player player) {
        PowerPlayerDataSets.StrengthPlayerData data = playerDataMap.computeIfAbsent(player.getUUID(), k -> new PowerPlayerDataSets.StrengthPlayerData());
            if (!player.hasEffect(MobEffects.DAMAGE_BOOST)) {
                fillPerSecondCooldown(player);
            }
        if(player.hasEffect(MobEffects.DAMAGE_BOOST)){
            data.abilityCooldown = data.getMAX_COOLDOWN();
        }
    }

    @Override
    public void tickServerAbilityData(ServerPlayer player) {
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);

        if(abilityData.chosenAbility1.equals(getName())) {
            getInstance().decrementCooldown(player);
            syncDataToClient(player);
        }

    }

    @Override
    public void tickClientAbilityData(Player player) {

    }

    @Override
    public void syncDataToClient(ServerPlayer player) {
        PowerPlayerDataSets.StrengthPlayerData data = getPlayerData(player);
        if(isAbilityChosenOrEquipped(player)) {
            NetworkHandler.INSTANCE.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncAbility1Packet(data.abilityCooldown, false, false)
            );
        }
    }

    public boolean isAbilityChosenOrEquipped(Player player){
        AbilityManager.PlayerAbilityData abilityData = AbilityManager.getInstance().getPlayerAbilityData(player);
        return abilityData.chosenAbility1.equals(getName());
    }

    @Override
    public void playSound(Player player) {
        player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void playSound2(Player player) {

    }
}
