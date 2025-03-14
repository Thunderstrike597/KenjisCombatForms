package net.kenji.kenjiscombatforms.network.voidform;

public class ClientVoidData {
    private static int cooldown = 0;
    private static int cooldown2 = 0;
    private static int cooldown3 = 0;
    private static int levitationCooldown = 0;
    private static int grabCooldown = 0;
    private static boolean isEnderActive = false;


    public static void setCooldown(int value) {
        cooldown = value;
    }

    public static void setCooldown2(int value) {
        cooldown2 = value;
    }
    public static void setCooldown3(int value) {
        cooldown3 = value;
    }

    public static void setIsEnderActive(boolean value) {
        isEnderActive = value;
    }

    public static void setLevitationCooldown(int value) {
        levitationCooldown = value;
    }

    public static void setGrabCooldown(int value) {
        grabCooldown = value;
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

    public static int getGrabCooldown() {
        return grabCooldown;
    }
    public static int getLevitationCooldown() {
        return levitationCooldown;
    }

    public static boolean getIsEnderActive() {
        return isEnderActive;
    }

    public static void setEnderActive(boolean value) {
        isEnderActive = value;
    }
}