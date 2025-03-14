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

public class ResetCurrentFormLevel {


    private static final SimpleCommandExceptionType RESET_FAILED =
            new SimpleCommandExceptionType(Component.literal("Failed to reset form level"));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal(KenjisCombatForms.MOD_ID)
                .then(Commands.literal("reset_current_level")
                        .requires(source -> source.hasPermission(2)) // Requires op permission
                        .executes(context -> resetFormLevels(context.getSource())));
    }

    private static int resetFormLevels(CommandSourceStack source) throws CommandSyntaxException {

        FormChangeHandler.getInstance().resetCurrentFormLevel(source.getPlayer());

                source.sendSuccess(() -> Component.literal("Have reset current form level"), true);
                return Command.SINGLE_SUCCESS;
        }
}
