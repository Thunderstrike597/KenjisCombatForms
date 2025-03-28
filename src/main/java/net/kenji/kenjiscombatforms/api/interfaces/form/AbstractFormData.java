package net.kenji.kenjiscombatforms.api.interfaces.form;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;

public abstract class AbstractFormData {
    protected String storedAbility1;
    protected String storedAbility2;
    protected String storedFinalAbility;
    protected FormLevelManager.FormLevel formLevel;
    protected int formXp;
    protected int formXpMAX;
    protected String previousAbility1;
    protected String previousAbility2;
    protected String previousAbility3;


    public abstract String getCurrentStoredAbility1();
    public abstract void setCurrentStoredAbility1(String ability);
    public abstract String getCurrentStoredAbility2();
    public abstract void setCurrentStoredAbility2(String ability);
    public abstract String getStoredAbility3();
    public abstract void setStoredAbility3(String ability);
    public abstract FormLevelManager.FormLevel getCurrentFormLevel();
    public abstract int getCurrentFormXp();
    public abstract int getCurrentFormXpMAX();
    public abstract void setCurrentFormXp(int amount);
    public abstract void setCurrentFormXpMAX(int amount);
    public abstract void setCurrentFormLevel(FormLevelManager.FormLevel currentFormLevel);


}
