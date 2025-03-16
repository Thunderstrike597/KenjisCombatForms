package net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form;

import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.item.custom.base_items.BasePowerClass;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class PowerFist3Item extends BasePowerClass {
    private static PowerFist3Item INSTANCE;


    public PowerFist3Item(Properties properties) {
        super(properties);
        if(INSTANCE == null){
            INSTANCE = this;
        }
    }

    public static PowerFist3Item getInstance(){
        return INSTANCE;
    }
    private static final Random RANDOM = new Random();



    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        double randomChance = RANDOM.nextDouble();
        Player player = (Player) attacker;

        if(randomChance < 0.15) {
            if (player.getMainHandItem().is(this)) {
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 35, 0, true, true));

            }
        }
        return super.hurtEnemy(itemStack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("tooltip.kenjiscombatforms.void_fist3.tooltip"));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return super.getDefaultTooltipHideFlags(stack);
    }


    public void setFormMainHand(Player player, int slot){
        player.getInventory().setItem(slot, this.getDefaultInstance());
    }
    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int i, boolean b) {
        double randomChance = RANDOM.nextDouble();
        Player player = (Player) entity;

        if(randomChance < 0.25) {

            if (player.getMainHandItem().is(this)) {
                if(player.hurtTime > 0) {
                    player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 55, 1, true , true));
                }
            }
        }
        super.inventoryTick(itemStack, level, entity, i, b);
    }


    private boolean isValidReplaceItem(Player player){
        ItemStack mainHandItem = player.getMainHandItem();
        return mainHandItem.isEmpty() || mainHandItem.is(Items.AIR) || mainHandItem.getItem() instanceof BaseFistClass &&
                !(mainHandItem.getItem() instanceof PowerFist3Item);
    }
}

