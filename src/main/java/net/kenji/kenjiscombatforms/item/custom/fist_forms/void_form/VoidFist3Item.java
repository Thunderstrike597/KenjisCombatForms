package net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form;

import net.kenji.kenjiscombatforms.item.ModItems;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseVoidClass;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

public class VoidFist3Item extends BaseVoidClass {
    private static VoidFist3Item INSTANCE;


    public VoidFist3Item(Properties properties) {
        super(properties);
        if(INSTANCE == null){
            INSTANCE = this;
        }
    }

    public static VoidFist3Item getInstance(){
        return INSTANCE;
    }

    private static final Random RANDOM = new Random();

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        double randomChance = RANDOM.nextDouble();

        if(randomChance < 0.25) {
            Player player = (Player) attacker;
            if (player.getMainHandItem().is(this)) {
                target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 35));
                target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 35));

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


    public void setVoidFormMainHand(Player player, int slot){
        player.getInventory().setItem(slot, this.getDefaultInstance());
    }

    private boolean isValidReplaceItem(Player player){
        ItemStack mainHandItem = player.getMainHandItem();
        return mainHandItem.isEmpty() || mainHandItem.is(Items.AIR) || mainHandItem.getItem() instanceof BaseFistClass &&
                !(mainHandItem.getItem() instanceof VoidFist3Item);
    }

}

