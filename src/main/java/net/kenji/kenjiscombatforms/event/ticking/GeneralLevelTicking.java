package net.kenji.kenjiscombatforms.event.ticking;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.*;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.*;
import net.kenji.kenjiscombatforms.api.powers.power_powers.PowerEffectInflict;
import net.kenji.kenjiscombatforms.api.powers.power_powers.StrengthBoost;
import net.kenji.kenjiscombatforms.api.powers.swift_powers.SpeedBoost;
import net.kenji.kenjiscombatforms.api.powers.swift_powers.SwiftEffectInflict;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GeneralLevelTicking {

    private static final Set<UUID> playersWithOpenGui = new HashSet<>();




    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Level level = event.level;

            if (!level.isClientSide()) {
                for (Player player : level.players()) {
                    if (player instanceof ServerPlayer serverPlayer) {
                        EnderLevitation.getInstance().tickServerAbilityData(serverPlayer);
                        EnderFormAbility.getInstance().tickServerAbilityData(serverPlayer);
                        WitherFormAbility.getInstance().tickServerAbilityData(serverPlayer);
                        SoulDrift.getInstance().tickServerAbilityData(serverPlayer);
                        VoidAnchorRift.getInstance().tickServerAbilityData(serverPlayer);
                        VoidGrab.getInstance().tickServerAbilityData(serverPlayer);
                        WitherMinions.getInstance().tickServerAbilityData(serverPlayer);
                        WitherImplode.getInstance().tickServerAbilityData(serverPlayer);
                        SpeedBoost.getInstance().tickServerAbilityData(serverPlayer);
                        StrengthBoost.getInstance().tickServerAbilityData(serverPlayer);
                        PowerEffectInflict.getInstance().tickServerAbilityData(serverPlayer);

                        SwiftEffectInflict.getInstance().tickServerAbilityData(serverPlayer);
                        WitherFormDashAbility.getInstance().tickServerAbilityData(serverPlayer);
                        TeleportPlayer.getInstance().tickServerAbilityData(serverPlayer);
                        WitherDash.getInstance().tickServerAbilityData(serverPlayer);
                        TeleportPlayerBackstab.getInstance().tickServerAbilityData(serverPlayer);
                    }
                }
            }

            for (Player player : level.players()) {
                if (player.level().isClientSide) {
                    EnderFormAbility.getInstance().tickClientAbilityData(player);
                    WitherFormAbility.getInstance().tickClientAbilityData(player);
                    WitherDash.getInstance().tickClientAbilityData(player);
                    TeleportPlayer.getInstance().tickClientAbilityData(player);
                    SoulDrift.getInstance().tickClientAbilityData(player);
                    WitherImplode.getInstance().tickClientAbilityData(player);
                }
            }
        }
    }
}