package net.kenji.kenjiscombatforms.network.swift_form;

public class ClientSwiftData {
    private static int cooldown = 0;
    private static int cooldown2 = 0;
    private static int cooldown3 = 0;


    public static void setCooldown(int value) {
        cooldown = value;
    }
    public static void setCooldown2(int value) {
        cooldown2 = value;
    }
    public static void setCooldown3(int value) {
        cooldown3 = value;
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


}