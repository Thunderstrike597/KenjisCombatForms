package net.kenji.kenjiscombatforms.event.forge_events;

import jeresources.compatibility.api.JERAPI;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.compat.JERPlugin;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {
    private static boolean jerRegistered = false;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        KenjisCombatForms.registerCommands(event);
    }

    @SubscribeEvent
    public static void onWorldLoad(TickEvent.PlayerTickEvent event) {
        if (jerRegistered) return;
        if (ModList.get().isLoaded("jeresources")) {
            JERPlugin.init();
            JERPlugin.receive(JERAPI.getInstance());
            jerRegistered = true;
        }
    }
}
