package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.ticking.FormChangeTick;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class InventoryLockHandler {

    private static int getLockedHotbarSlot() {
        return KenjisCombatFormsCommon.FORM_LOCK_SLOT.get();
    }
    private static boolean canLockSlot() {
        return KenjisCombatFormsCommon.CAN_LOCK_SLOT.get();
    }

    @SubscribeEvent
    public static void onScreenMouseClicked(ScreenEvent.MouseButtonPressed.Pre event) {
        Player player = Minecraft.getInstance().player;

        if(player != null) {
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (data.isHandCombat) {
                if (canLockSlot()) {
                    if (event.getScreen() instanceof AbstractContainerScreen<?> containerScreen) {
                        Slot slot = containerScreen.getSlotUnderMouse();
                        if (slot != null && isHotbarSlotLocked(slot)) {
                            event.setCanceled(true);
                            Minecraft.getInstance().player.displayClientMessage(Component.literal("This hotbar slot is locked!"), true);
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void onScreenMouseDragged(ScreenEvent.MouseDragged event) {
        Player player = Minecraft.getInstance().player;

        if(player != null) {
            ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
            if (data.isHandCombat) {
                if (canLockSlot()) {
                    if (event.getScreen() instanceof AbstractContainerScreen<?> containerScreen) {
                        Slot slot = containerScreen.getSlotUnderMouse();
                        if (slot != null && isHotbarSlotLocked(slot)) {
                            event.setCanceled(true);
                            Minecraft.getInstance().player.displayClientMessage(Component.literal("You cannot move this item!"), true);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onInventoryMouseClicked(ScreenEvent.MouseButtonReleased.Pre event){
        Player player = Minecraft.getInstance().player;

        if(player != null){
        ControlHandler.controlRelatedEvents.PlayerData data = ControlHandler.controlRelatedEvents.getInstance().getOrCreatePlayerData(player);
        if (data.isHandCombat) {
            if (canLockSlot()) {
                if (event.getScreen() instanceof AbstractContainerScreen<?> containerScreen) {
                    Slot slot = containerScreen.getSlotUnderMouse();
                    if (slot != null && isHotbarSlotLocked(slot)) {
                        event.setCanceled(true);
                        Minecraft.getInstance().player.displayClientMessage(Component.literal("This hotbar slot is locked!"), true);
                    }
                }
            }
        }
            }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
           // if(mc.screen instanceof AbstractContainerScreen<?> containerScreen) {
                if (event.getKey() == mc.options.keyHotbarSlots[0].getKey().getValue()) {
                    if (event.getAction() == GLFW.GLFW_PRESS) {
                        mc.options.keyHotbarSlots[0].setDown(false);
                    }
                }
           // }
        }
    }
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            Player clientPlayer = mc.player;
            mc.options.keyHotbarSlots[0].setDown(false);

        }
    }

    private static boolean isHotbarSlotLocked(Slot slot) {
        // Check if the slot is in the hotbar range
        // Check if the slot contains your specific item
        return slot.getSlotIndex() == getLockedHotbarSlot() && isFistFormItem(slot.getItem());
    }

    private static boolean isFistFormItem(ItemStack stack) {
        return stack.getItem() instanceof BaseFistClass;
    }
}