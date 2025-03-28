package net.kenji.kenjiscombatforms.api.powers;

import net.kenji.kenjiscombatforms.api.handlers.power_data.EmptyData;
import net.kenji.kenjiscombatforms.api.handlers.power_data.PowerPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EmptyAbility implements Ability {
 private static final EmptyAbility INSTANCE = new EmptyAbility(); // final ensures it's initialized once

 public static EmptyAbility getInstance() {
  return INSTANCE;
 }

 private final Map<UUID, EmptyData.EmptyPlayerData> playerDataMap = new HashMap<>(); // Initialize correctly

 @Override
 public String getName() {
  return "NONE";
 }

 @Override
 public int getGUIDrawPosY() {
  return 256;
 }

    @Override
    public int getGUIDrawPosX() {
        return 0;
    }

    @Override
 public AbstractAbilityData getAbilityData(Player player) {
  return EmptyData.getInstance().getOrCreateEmptyPlayerData(player);
 }

    @Override
    public void fillPerSecondCooldown(Player player) {

    }

    @Override
    public void drainPerSecondCooldown(Player player) {

    }

    @Override
    public boolean getAbilityActive(Player player) {
        return false;
    }

    @Override
    public void sendPacketToServer(Player player) {

    }

    @Override
    public void triggerAbility(ServerPlayer serverPlayer) {

    }

    @Override
    public void activateAbility(ServerPlayer serverPlayer) {

    }

    @Override
    public void deactivateAbilityOptional(ServerPlayer serverPlayer) {

    }

    @Override
    public void decrementCooldown(Player player) {

    }

    @Override
    public void tickServerAbilityData(ServerPlayer player) {

    }

    @Override
    public void tickClientAbilityData(Player player) {

    }

    @Override
    public void syncDataToClient(ServerPlayer player) {

    }

    @Override
    public void playSound(Player player) {

    }

    @Override
    public void playSound2(Player player) {

    }
}
