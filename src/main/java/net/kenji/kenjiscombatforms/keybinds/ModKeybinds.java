package net.kenji.kenjiscombatforms.keybinds;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static String KEY_CATEGORY = "key.category.kenjiscombatforms.abilities";
    public static String KEY_ABILITY1 = "key.kenjiscombatforms.ability1";
    public static String KEY_ABILITY2 = "key.kenjiscombatforms.ability2";
    public static String KEY_ABILITY3 = "key.kenjiscombatforms.ability3";

    public static String MISC_KEY_CATEGORY = "key.category.kenjiscombatforms.misc";
    public static String TOGGLE_HAND_COMBAT = "key.kenjiscombatforms.toggle_hand_combat";

    public static String KEY_FORM_MENU_OPEN = "key.kenjiscombatforms.open_form_menu";
    public static String KEY_QUICK_FORM_CHANGE = "key.kenjiscombatforms.quick_form_change";

    public static String SWITCH_CURRENT_ABILITY = "key.kenjiscombatforms.switch_current_ability";
    public static String ACTIVATE_CURRENT_ABILITY = "key.kenjiscombatforms.activate_current_ability";

   public static KeyMapping ABILITY1_KEY = new KeyMapping(KEY_ABILITY1, KeyConflictContext.IN_GAME,
           InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY);
    public static KeyMapping ABILITY2_KEY = new KeyMapping(KEY_ABILITY2, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, KEY_CATEGORY);
    public static KeyMapping ABILITY3_KEY = new KeyMapping(KEY_ABILITY3, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, KEY_CATEGORY);
    public static KeyMapping FORM_MENU_OPEN_KEY = new KeyMapping(KEY_FORM_MENU_OPEN, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MISC_KEY_CATEGORY);
    public static KeyMapping QUICK_FORM_CHANGE_KEY = new KeyMapping(KEY_QUICK_FORM_CHANGE, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MISC_KEY_CATEGORY);
    public static KeyMapping TOGGLE_HAND_COMBAT_KEY = new KeyMapping(TOGGLE_HAND_COMBAT, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MISC_KEY_CATEGORY);
    public static KeyMapping ACTIVATE_CURRENT_ABILITY_KEY = new KeyMapping(ACTIVATE_CURRENT_ABILITY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MISC_KEY_CATEGORY);
    public static KeyMapping SWITCH_CURRENT_ABILITY_KEY = new KeyMapping(SWITCH_CURRENT_ABILITY, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, MISC_KEY_CATEGORY);
}
