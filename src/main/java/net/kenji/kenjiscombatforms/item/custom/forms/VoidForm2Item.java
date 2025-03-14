package net.kenji.kenjiscombatforms.item.custom.forms;

import net.kenji.kenjiscombatforms.api.powers.VoidPowers.TeleportPlayer;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.TeleportPlayerBackstab;
import net.kenji.kenjiscombatforms.api.powers.VoidPowers.VoidAnchorRift;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VoidForm2Item extends BaseFormClass {
    private final TeleportPlayer teleportPlayer;
    private final TeleportPlayerBackstab teleportPlayerBackstab;
    private final VoidAnchorRift voidAnchorRift;
    private static final long PRESS_COOLDOWN = 4;

    public VoidForm2Item(Tier tier, int damageIn, float speedIn, Properties builder) {
        super(tier, damageIn, speedIn, builder);
        this.teleportPlayer = TeleportPlayer.getInstance();
        this.teleportPlayerBackstab = TeleportPlayerBackstab.getInstance();
        this.voidAnchorRift = VoidAnchorRift.getInstance();

    }

    // Remove these static fields
    // public static boolean deactivatedAbility1 = false;
    // public static boolean isHoldingItem = false;
    // private static int pressState = 0;
    // public static Player holdingPlayer;
    // public static ServerPlayer holdingServerPlayer;
    // private static Level hLevel;
    // static private long pressTime = 0;

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof Player player)) {
            return;
        }


        super.inventoryTick(itemStack, level, entity, slot, selected);
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.kenjiscombatforms.void_form_type2.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }


    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

        return enchantment != Enchantments.SHARPNESS &&
                enchantment != Enchantments.MENDING &&
                enchantment != Enchantments.SWEEPING_EDGE &&
                enchantment != Enchantments.UNBREAKING && enchantment.category == EnchantmentCategory.WEAPON;

    }

    @Override
    public boolean isValidRepairItem(ItemStack item1, ItemStack item2) {
        return item2.getItem() == ModItems.EMPTYSLOT.get();
    }


    @Override
    public boolean isEnchantable(ItemStack stack) {
        return !stack.isEnchanted();
    }

    // Other methods remain the same...

}