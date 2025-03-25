package net.kenji.kenjiscombatforms.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.ClientEventHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.skill.Skill;

public class DebugCrashCommand {

    static Player nullValue = null;

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal(KenjisCombatForms.MOD_ID) // Root: kenjiscombatforms
                .then(Commands.literal("debug") // Subcommand: debug
                        .then(Commands.literal("crashgame") // Final command: crashgame
                                .requires(source -> source.hasPermission(2)) // Requires OP
                                .executes(context -> crashGame(context.getSource()))));
    }
    private static int crashGame(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Crashing server/client for debugging..."), true);
        // Adding a sleep to delay the crash (gives time for the game to handle it properly)

        ClientEventHandler.getInstance().setCanCrash(true);

       return 1;
    }
}