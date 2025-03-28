package net.kenji.kenjiscombatforms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.fist_forms.form_level.SyncServerFormLevelPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class LevelUpCurrentForm {


    private static final SimpleCommandExceptionType RESET_FAILED =
            new SimpleCommandExceptionType(Component.literal("Failed to level up"));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal(KenjisCombatForms.MOD_ID)
                .then(Commands.literal("level_up_form")
                        .requires(source -> source.hasPermission(2)) // Requires op permission
                        .executes(context -> resetFormLevels(context.getSource())));
    }

    private static int resetFormLevels(CommandSourceStack source) throws CommandSyntaxException {
        Player player = source.getPlayer();
        if (player != null) {
            FormManager.PlayerFormData formData = FormManager.getInstance().getOrCreatePlayerFormData(player);
            String currentForm = formData.selectedForm;
            Form form = FormManager.getInstance().getForm(currentForm);
            AbstractFormData currentFormData = form.getFormData(player.getUUID());
            int addedXPMAX = currentFormData.getCurrentFormXpMAX() * EpicFightCombatFormsCommon.LEVEL2_FORM_MAX_XP_ADDITION.get();

            switch (currentFormData.getCurrentFormLevel()) {
                case LEVEL1 ->
                        NetworkHandler.INSTANCE.sendToServer(new SyncServerFormLevelPacket(form.getName(), currentFormData.getCurrentFormXp(), addedXPMAX, FormLevelManager.FormLevel.LEVEL2));
                case LEVEL2 ->
                        NetworkHandler.INSTANCE.sendToServer(new SyncServerFormLevelPacket(form.getName(), currentFormData.getCurrentFormXp(), currentFormData.getCurrentFormXpMAX(), FormLevelManager.FormLevel.LEVEL3));
            }
        }
            source.sendSuccess(() -> Component.literal("You have leveled up your current form"), true);
            return Command.SINGLE_SUCCESS;
    }
}
