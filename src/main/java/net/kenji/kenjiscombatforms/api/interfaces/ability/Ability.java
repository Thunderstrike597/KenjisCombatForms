package net.kenji.kenjiscombatforms.api.interfaces.ability;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface Ability {

    String getName();
    int getGUIDrawPosY();
    int getGUIDrawPosX();

    AbstractAbilityData getAbilityData(Player player);

    void fillPerSecondCooldown(Player player);
    void drainPerSecondCooldown(Player player);
    boolean getAbilityActive(Player player);


    void sendPacketToServer(Player player);
    void triggerAbility(ServerPlayer serverPlayer);
    void activateAbility(ServerPlayer serverPlayer);
    void deactivateAbilityOptional(ServerPlayer serverPlayer);
    void decrementCooldown(Player player);
    void tickServerAbilityData(ServerPlayer player);
    void tickClientAbilityData(Player player);
    void syncDataToClient(ServerPlayer player);

    void playSound(Player player);
    void playSound2(Player player);
}