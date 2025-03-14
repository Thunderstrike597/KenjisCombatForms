package net.kenji.kenjiscombatforms.event;

public class CommonFunctions {
    private static final CommonFunctions INSTANCE = new CommonFunctions();

    public static CommonFunctions getInstance(){
        return INSTANCE;
    }

    public int getTickCount(int tickCount){
        tickCount++;
        if(tickCount > 20) {
            tickCount = 0;
        }
        return tickCount;
    }

    public int decreaseCooldown(int abilityCooldown, int tickCount){
        if(tickCount == 0) {
            abilityCooldown--;
        }
        return abilityCooldown;
    }

    public int increaseCooldown(int abilityCooldown, int tickCount){
        if(tickCount == 0) {
            abilityCooldown++;
        }
        return abilityCooldown;
    }
}
