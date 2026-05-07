package net.kenji.kenjiscombatforms.mixins.compat;

import net.kenji.epic_fight_combat_hotbar.capability.ModCapabilities;
import net.kenji.epic_fight_combat_hotbar.client.HotbarSlotHandler;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.api.forgeevent.InnateSkillChangeEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mixin(value = ServerPlayerPatch.class, remap = false)
public class MixinCombatHotbarServerPlayerPatch {
    @Unique
    private static Map<UUID, ItemStack> lastHeldItem = new HashMap<>();

    @Inject(method = "updateHeldItem", at = @At("TAIL"), cancellable = true, remap = false)
    public void onUpdateHeldItemTail(CapabilityItem fromCap, CapabilityItem toCap, ItemStack from, ItemStack _to, InteractionHand hand, CallbackInfo ci) {
        ServerPlayerPatch self = (ServerPlayerPatch) (Object) this;
        if(hand != InteractionHand.MAIN_HAND) return;
        String formName = FormManager.getInstance().getOrCreatePlayerFormData(self.getOriginal().getUUID()).selectedForm;
        Form currentForm = FormManager.getInstance().getForm(formName);
        ItemStack formItem = currentForm.getFormItem(self.getOriginal().getUUID());
        if (formItem == null) return;
        CapabilityItem formCap = EpicFightCapabilities.getItemStackCapability(formItem);
        ServerPlayer player = (ServerPlayer) self.getOriginal();

        PlayerPatch<?> patch = EpicFightCapabilities.getPlayerPatch(player);
        if(patch == null) return;
        player.getCapability(ModCapabilities.COMBAT_HOTBAR).ifPresent(handler -> {
            int selectedSlot = HotbarSlotHandler.getSelectedSlot(player);
            ItemStack stack = handler.getStackInSlot(selectedSlot);
            if (!FormManager.isHeldCategoryValid(player, stack)) return;
            ItemStack lastItem = lastHeldItem.getOrDefault(player.getUUID(), player.getMainHandItem());
            if(lastItem.getItem() != patch.getOriginal().getMainHandItem().getItem() || FormManager.getLastForm(player) != FormManager.getCurrentForm(player)) {
                formCap.changeWeaponInnateSkill(patch, formItem);
                MinecraftForge.EVENT_BUS.post(new InnateSkillChangeEvent(self, from, fromCap, formItem, formCap, hand));

                self.modifyLivingMotionByCurrentItem();
            }
            FormManager.updateLastForm(player);
            lastHeldItem.put(player.getUUID(), player.getMainHandItem());
        });
    }
}
