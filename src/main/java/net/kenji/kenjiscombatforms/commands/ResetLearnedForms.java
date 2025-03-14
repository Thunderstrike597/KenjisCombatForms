package net.kenji.kenjiscombatforms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ResetLearnedForms {


    private static final SimpleCommandExceptionType RESET_FAILED =
            new SimpleCommandExceptionType(Component.literal("Failed to reset forms"));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal(KenjisCombatForms.MOD_ID)
                .then(Commands.literal("reset_forms")
                        .requires(source -> source.hasPermission(2)) // Requires op permission
                        .executes(context -> resetAbilitiesUnlocked(context.getSource())));
    }

    private static int resetAbilitiesUnlocked(CommandSourceStack source) throws CommandSyntaxException {

        FormChangeHandler.getInstance().resetAllFormValues(source.getPlayer());
        FormChangeHandler.getInstance().chooseBasicForm(source.getPlayer());

        if (FormChangeHandler.getInstance().getFormValuesReset(source.getPlayer())) {
                source.sendSuccess(() -> Component.literal("Have reset learned forms"), true);
                return Command.SINGLE_SUCCESS;
            } else {
                source.sendFailure(Component.literal("Failed to reset forms"));
                return 0;
            }
        }
}
