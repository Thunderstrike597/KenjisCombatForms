package net.kenji.kenjiscombatforms.api.managers.client_data;

import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.forms.BasicForm;
import net.minecraft.world.entity.player.Player;

public class ClientBasicFistData {
    private static final BasicForm.BasicFormData clientData = new BasicForm.BasicFormData();

    public static void setBasicAbility1(Player player, String value) {
        clientData.setCurrentStoredAbility1(value);
    }
    public static void setBasicAbility2(Player player, String value) {
        clientData.setCurrentStoredAbility2(value);
    }

}