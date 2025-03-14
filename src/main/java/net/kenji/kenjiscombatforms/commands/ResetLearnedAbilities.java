package net.kenji.kenjiscombatforms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.AbilityChangeHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class ResetLearnedAbilities {


    private static final SimpleCommandExceptionType RESET_FAILED =
            new SimpleCommandExceptionType(Component.literal("Failed to reset abilities"));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal(KenjisCombatForms.MOD_ID)
                .then(Commands.literal("reset_abilities")
                        .requires(source -> source.hasPermission(2)) // Requires op permission
                        .executes(context -> resetAbilitiesUnlocked(context.getSource())));
    }

    private static int resetAbilitiesUnlocked(CommandSourceStack source) throws CommandSyntaxException {

       AbilityChangeHandler.getInstance().resetAllAbilityValues(source.getPlayer());
        AbilityChangeHandler.getInstance().resetAllChosenAbilities(source.getPlayer());

                source.sendSuccess(() -> Component.literal("Have reset unlocked abilities"), true);
                return Command.SINGLE_SUCCESS;
        }
}
