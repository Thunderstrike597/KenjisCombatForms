package net.kenji.kenjiscombatforms.api.interfaces.form;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;

public abstract class AbstractFormData {
    protected AbilityManager.AbilityOption1 storedAbility1;
    protected AbilityManager.AbilityOption2 storedAbility2;
    protected AbilityManager.AbilityOption3 storedFinalAbility;
    protected FormLevelManager.FormLevel formLevel;
    protected int formXp;
    protected int formXpMAX;
    protected AbilityManager.AbilityOption1 previousAbility1;
    protected AbilityManager.AbilityOption2 previousAbility2;
    protected AbilityManager.AbilityOption3 previousAbility3;


    public abstract AbilityManager.AbilityOption1 getCurrentStoredAbility1();
    public abstract void setCurrentStoredAbility1(AbilityManager.AbilityOption1 ability);
    public abstract AbilityManager.AbilityOption2 getCurrentStoredAbility2();
    public abstract void setCurrentStoredAbility2(AbilityManager.AbilityOption2 ability);
    public abstract AbilityManager.AbilityOption3 getStoredAbility3();
    public abstract void setStoredAbility3(AbilityManager.AbilityOption3 ability);
    public abstract FormLevelManager.FormLevel getCurrentFormLevel();
    public abstract int getCurrentFormXp();
    public abstract int getCurrentFormXpMAX();
    public abstract void setCurrentFormXp(int amount);
    public abstract void setCurrentFormXpMAX(int amount);
    public abstract void setCurrentFormLevel(FormLevelManager.FormLevel currentFormLevel);
    public abstract void setPreviousAbility1(AbilityManager.AbilityOption1 ability1);
    public abstract void setPreviousAbility2(AbilityManager.AbilityOption2 ability2);
    public abstract void setPreviousAbility3(AbilityManager.AbilityOption3 ability3);

    public abstract AbilityManager.AbilityOption1 getPreviousAbility1();
    public abstract AbilityManager.AbilityOption2 getPreviousAbility2();
    public abstract AbilityManager.AbilityOption3 getPreviousAbility3();


}
