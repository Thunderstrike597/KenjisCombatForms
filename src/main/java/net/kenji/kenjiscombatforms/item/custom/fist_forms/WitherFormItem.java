package net.kenji.kenjiscombatforms.item.custom.fist_forms;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.kenji.kenjiscombatforms.config.EpicFightCombatFormsCommon;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFinalFormClass;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WitherFormItem extends BaseFinalFormClass {
   private static WitherFormItem INSTANCE;

    public WitherFormItem(Properties properties) {
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

            int baseDamage = EpicFightCombatFormsCommon.BASIC_FORM_BASE_DAMAGE.get();
            double damageMultiplier = EpicFightCombatFormsCommon.LEVEL1_DAMAGE_MULTIPLIER.get();
            double finalDamage = baseDamage * damageMultiplier; // Subtracting 2 because Minecraft adds it

            double baseSpeed = EpicFightCombatFormsCommon.BASIC_FORM_BASE_SPEED.get();
            double speedMultiplier = EpicFightCombatFormsCommon.LEVEL1_SPEED_MULTIPLIER.get();
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

    public static WitherFormItem getInstance(){
        return INSTANCE;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return super.getDefaultTooltipHideFlags(stack);
    }


    public void setFormMainHand(Player player, int slot){
             player.getInventory().setItem(slot, this.getDefaultInstance());
    }
}

