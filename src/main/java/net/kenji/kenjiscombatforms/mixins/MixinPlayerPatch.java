package net.kenji.kenjiscombatforms.mixins;

import com.p1nero.invincible.client.InputManager;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.gameasset.ModSkills;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.*;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

import java.util.Optional;

@Mixin(value = PlayerPatch.class, remap = false)
public class MixinPlayerPatch {

    @Inject(method = "getSkill(Lyesman/epicfight/skill/SkillSlot;)Lyesman/epicfight/skill/SkillContainer;", at = @At("RETURN"), cancellable = true, remap = false)
    public void onUpdateHeldItem(SkillSlot slot, CallbackInfoReturnable<SkillContainer> cir) {

        PlayerPatch<?> self = (PlayerPatch<?>) (Object)this;
        if (self.getOriginal() == null) return;
        if (((Player) self.getOriginal()).getInventory() == null) return;
        String formName = FormManager.getInstance().getOrCreatePlayerFormData(self.getOriginal().getUUID()).selectedForm;
        Form currentForm = FormManager.getInstance().getForm(formName);
        ItemStack formItem = currentForm.getFormItem(self.getOriginal().getUUID());
        if (formItem == null) return;
        CapabilityItem formCap = EpicFightCapabilities.getItemStackCapability(formItem);
        CapabilityItem originalStack = EpicFightCapabilities.getItemStackCapability(self.getOriginal().getMainHandItem());

        boolean isCorrectCategory = originalStack.getWeaponCategory() == CapabilityItem.WeaponCategories.NOT_WEAPON
                || originalStack.getWeaponCategory() == CapabilityItem.WeaponCategories.FIST;
        if(!isCorrectCategory) return;
        if(slot != SkillSlots.WEAPON_INNATE) return;

        Skill finalSkill = currentForm.getFormSkill(self.getOriginal());
        if(finalSkill == null) return;
        SkillContainer container = self.getSkillCapability().getSkillContainer(finalSkill);
        if(container == null) return;

        cir.setReturnValue(container);
    }

}