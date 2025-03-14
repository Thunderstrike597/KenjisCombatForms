package net.kenji.kenjiscombatforms.network.witherform;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

public class ClientWitherData {
    private static int cooldown = 0;
    private static int cooldown2 = 0;
    private static int cooldown3 = 0;
    private static int minionCooldown = 0;
    private static int implodeCooldown = 0;
    private static boolean isWitherActive = false;
    private static boolean areMinionsActive = false;
    private static UUID witherEntityUUID;


    public static void setCooldown(int value) {
        cooldown = value;
    }

    public static void setCooldown2(int value) {
        cooldown2 = value;
    }

    public static void setCooldown3(int value) {
        cooldown3 = value;
    }
    public static void setMinionCooldown(int value) {
        minionCooldown = value;
    }
    public static void setImplodeCooldown(int value) {
        implodeCooldown = value;
    }
    public static void setIsWitherActive(boolean value) {
        isWitherActive = value;
    }
    public static void setAreMinionsActive(boolean value) {
        areMinionsActive = value;
    }


    public static int getCooldown() {
        return cooldown;
    }
    public static int getCooldown2() {
        return cooldown2;
    }
    public static int getCooldown3() {
        return cooldown3;
    }
    public static int getMinionCooldown() {
        return minionCooldown;
    }
    public static int getImplodeCooldown() {
        return implodeCooldown;
    }

    public static boolean getMinionsActive(){
        return areMinionsActive;
    }
    public static boolean getIsWitherActive(){
        return isWitherActive;
    }
    public static void setWitherUUID(UUID value){
         witherEntityUUID = value;
    }



    @Nullable
    public static Entity getWitherEntity() {
        if (witherEntityUUID != null) {
            Minecraft minecraft = Minecraft.getInstance();
            ClientLevel level = minecraft.level;
            if (level != null) {
                for (Entity entity : level.entitiesForRendering()) {
                    if (entity.getUUID().equals(witherEntityUUID)) {
                        return entity;
                    }
                }
            }
        }
        return null;
    }
}