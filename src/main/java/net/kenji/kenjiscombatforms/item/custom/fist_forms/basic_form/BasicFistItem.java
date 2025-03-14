package net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form;

import net.kenji.kenjiscombatforms.item.custom.base_items.BaseBasicClass;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicFistItem extends BaseBasicClass {
   private static BasicFistItem INSTANCE;

    public BasicFistItem(Properties p_41383_) {
        super(p_41383_);
        if(INSTANCE == null){
            INSTANCE = this;
        }
    }

    public static BasicFistItem getInstance(){
        return INSTANCE;
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return super.getDefaultTooltipHideFlags(stack);
    }

    public void setDefaultFormMainHand(Player player){
        if(isValidReplaceItem(player)){
            player.setItemInHand(InteractionHand.MAIN_HAND, this.getDefaultInstance());
        }
    }

    private boolean isValidReplaceItem(Player player){
        ItemStack mainHandItem = player.getMainHandItem();
        return mainHandItem.isEmpty() || mainHandItem.is(Items.AIR) || mainHandItem.getItem() instanceof BaseFistClass &&
                !(mainHandItem.getItem() instanceof BasicFistItem);
    }

    @Override
    public boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return false;
    }
}

