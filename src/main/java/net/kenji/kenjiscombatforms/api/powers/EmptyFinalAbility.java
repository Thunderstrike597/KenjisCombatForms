package net.kenji.kenjiscombatforms.api.powers;

import net.kenji.kenjiscombatforms.api.handlers.power_data.EmptyData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.Ability;
import net.kenji.kenjiscombatforms.api.interfaces.ability.AbstractAbilityData;
import net.kenji.kenjiscombatforms.api.interfaces.ability.FinalAbility;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class EmptyFinalAbility implements FinalAbility {
  private static EmptyFinalAbility INSTANCE = new EmptyFinalAbility();

   public static EmptyFinalAbility getInstance(){
       return INSTANCE;
   }



    @Override
    public String getName() {
        return "NONE";
    }


    @Override
    public String getFinalAbilityName() {
    return "NONE";
   }

    @Override
    public AbstractAbilityData getAbilityData(Player player) {
        return EmptyData.getInstance().getOrCreateEmptyPlayerData(player);
    }

 @Override
 public boolean getFinalAbilityActive(Player player) {
  return false;
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
