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

public class RefillAllAbilityCooldowns {


    private static final SimpleCommandExceptionType RESET_FAILED =
            new SimpleCommandExceptionType(Component.literal("Failed to refill cooldowns"));

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal(KenjisCombatForms.MOD_ID)
                .then(Commands.literal("refill_ability_cooldowns")
                        .requires(source -> source.hasPermission(2)) // Requires op permission
                        .executes(context -> refillAbilityCooldowns(context.getSource())));
    }

    private static int refillAbilityCooldowns(CommandSourceStack source) throws CommandSyntaxException {

        EnderPlayerDataSets enderPlayerDataSets = EnderPlayerDataSets.getInstance();
        WitherPlayerDataSets witherPlayerDataSets = WitherPlayerDataSets.getInstance();

        resetAllCooldowns(source.getPlayer(), enderPlayerDataSets, witherPlayerDataSets);

                source.sendSuccess(() -> Component.literal("Have refilled all cooldowns to max value"), true);
                return Command.SINGLE_SUCCESS;
        }
        private static void resetAllCooldowns(Player player, EnderPlayerDataSets enderDataSets, WitherPlayerDataSets witherDataSets){
        EnderPlayerDataSets.TeleportPlayerData tData = enderDataSets.getOrCreateTeleportPlayerData(player);
        EnderPlayerDataSets.VoidRiftPlayerData vData = enderDataSets.getOrCreateVoidRiftPlayerData(player);
        EnderPlayerDataSets.EnderFormPlayerData eData = enderDataSets.getOrCreateEnderFormPlayerData(player);
         WitherPlayerDataSets.WitherDashPlayerData dData = witherDataSets.getOrCreateDashPlayerData(player);
         WitherPlayerDataSets.SoulDriftPlayerData sData = witherDataSets.getOrCreateSoulDriftPlayerData(player);
         WitherPlayerDataSets.WitherFormPlayerData wData = witherDataSets.getOrCreateWitherFormPlayerData(player);

            EnderPlayerDataSets.VoidGrabPlayerData vgData = enderDataSets.getOrCreateVoidGrabPlayerData(player);

            WitherPlayerDataSets.WitherMinionPlayerData wmData = witherDataSets.getOrCreateMinionPlayerData(player);
            WitherPlayerDataSets.WitherImplodePlayerData wiData = witherDataSets.getOrCreateWitherImplodePlayerData(player);

         tData.abilityCooldown = tData.getMAX_COOLDOWN();
         vData.abilityCooldown = vData.getMAX_COOLDOWN();
         eData.abilityCooldown = eData.getMAX_COOLDOWN();
         dData.abilityCooldown = dData.getMAX_COOLDOWN();
         sData.abilityCooldown = sData.getMAX_COOLDOWN();
         wData.abilityCooldown = wData.getMAX_COOLDOWN();

         vgData.abilityCooldown = vgData.getMAX_COOLDOWN();
         wmData.abilityCooldown = wmData.getMAX_COOLDOWN();
         wiData.abilityCooldown = wiData.getMAX_COOLDOWN();
    }
}
