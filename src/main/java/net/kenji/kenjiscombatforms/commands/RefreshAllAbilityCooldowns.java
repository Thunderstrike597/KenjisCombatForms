package net.kenji.kenjiscombatforms.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class RefreshAllAbilityCooldowns {


    private static final SimpleCommandExceptionType RESET_FAILED =
            new SimpleCommandExceptionType(Component.literal("Failed to refresh cooldowns"));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal(KenjisCombatForms.MOD_ID)
                .then(Commands.literal("refresh_ability_cooldowns")
                        .requires(source -> source.hasPermission(2)) // Requires op permission
                        .executes(context -> refreshAbilityCooldowns(context.getSource())));
    }

    private static int refreshAbilityCooldowns(CommandSourceStack source) throws CommandSyntaxException {

        EnderPlayerDataSets enderPlayerDataSets = EnderPlayerDataSets.getInstance();
        WitherPlayerDataSets witherPlayerDataSets = WitherPlayerDataSets.getInstance();

        refreshAllCooldowns(source.getPlayer(), enderPlayerDataSets, witherPlayerDataSets);

                source.sendSuccess(() -> Component.literal("Have refreshed all cooldowns"), true);
                return Command.SINGLE_SUCCESS;
        }
        private static void refreshAllCooldowns(Player player, EnderPlayerDataSets enderDataSets, WitherPlayerDataSets witherDataSets){
        EnderPlayerDataSets.TeleportPlayerData tData = enderDataSets.getOrCreateTeleportPlayerData(player);
        EnderPlayerDataSets.VoidRiftPlayerData vData = enderDataSets.getOrCreateVoidRiftPlayerData(player);
        EnderPlayerDataSets.EnderFormPlayerData eData = enderDataSets.getOrCreateEnderFormPlayerData(player);
         WitherPlayerDataSets.WitherDashPlayerData dData = witherDataSets.getOrCreateDashPlayerData(player);
         WitherPlayerDataSets.SoulDriftPlayerData sData = witherDataSets.getOrCreateSoulDriftPlayerData(player);
         WitherPlayerDataSets.WitherFormPlayerData wData = witherDataSets.getOrCreateWitherFormPlayerData(player);

            EnderPlayerDataSets.VoidGrabPlayerData vgData = enderDataSets.getOrCreateVoidGrabPlayerData(player);

            WitherPlayerDataSets.WitherMinionPlayerData wmData = witherDataSets.getOrCreateMinionPlayerData(player);
            WitherPlayerDataSets.WitherImplodePlayerData wiData = witherDataSets.getOrCreateWitherImplodePlayerData(player);

         tData.abilityCooldown = 0;
         vData.abilityCooldown = 0;
         eData.abilityCooldown = 0;
         dData.abilityCooldown = 0;
         sData.abilityCooldown = 0;
         wData.abilityCooldown = 0;

         vgData.abilityCooldown = 0;
         wmData.abilityCooldown = 0;
         wiData.abilityCooldown = 0;
    }
}
