package net.kenji.kenjiscombatforms.event.forge_events;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventBusEvents {
    private int previousSelectedSlot = -1;

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        KenjisCombatForms.registerCommands(event);
    }
}
