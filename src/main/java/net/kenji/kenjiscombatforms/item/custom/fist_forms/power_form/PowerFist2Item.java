package net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.item.custom.base_items.BasePowerClass;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PowerFist2Item extends BasePowerClass {
    private static PowerFist2Item INSTANCE;


    public PowerFist2Item(Properties properties) {
        super();
        if (INSTANCE == null) {
            INSTANCE = this;
        }
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(slot));

            int baseDamage = KenjisCombatFormsCommon.POWER_FORM_BASE_DAMAGE.get();
            double damageMultiplier = KenjisCombatFormsCommon.LEVEL2_DAMAGE_MULTIPLIER.get();
            double finalDamage = baseDamage * damageMultiplier; // Subtracting 2 because Minecraft adds it

            double baseSpeed = KenjisCombatFormsCommon.POWER_FORM_BASE_SPEED.get();
            double speedMultiplier = KenjisCombatFormsCommon.LEVEL2_SPEED_MULTIPLIER.get();
            double finalSpeed = baseSpeed * speedMultiplier;

            builder.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier",
                            finalDamage - 1, AttributeModifier.Operation.ADDITION));

            builder.put(Attributes.ATTACK_SPEED,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon speed modifier",
                            finalSpeed - 4, AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
        return super.getDefaultAttributeModifiers(slot);
    }



    public static PowerFist2Item getInstance(){
        return INSTANCE;
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.kenjiscombatforms.void_fist2.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return super.getDefaultTooltipHideFlags(stack);
    }


    public void setFormMainHand(Player player, int slot){
        player.getInventory().setItem(slot, this.getDefaultInstance());
    }
}

