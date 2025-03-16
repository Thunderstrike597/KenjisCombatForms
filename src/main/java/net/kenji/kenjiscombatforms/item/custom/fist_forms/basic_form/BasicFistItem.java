package net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form;

import net.kenji.kenjiscombatforms.item.custom.base_items.BaseBasicClass;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class BasicFistItem extends BaseBasicClass implements ICurioItem {
   private static BasicFistItem INSTANCE;

    public BasicFistItem(Properties properties) {
        super(properties);
        if(INSTANCE == null){
            INSTANCE = this;
        }
    }


    @Override
    public boolean hasCurioCapability(ItemStack stack) {
        return ICurioItem.super.hasCurioCapability(stack);
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


    public void setFormMainHand(Player player, int slot){
             player.getInventory().setItem(slot, this.getDefaultInstance());
    }


    private boolean isValidReplaceItem(Player player){
        ItemStack mainHandItem = player.getMainHandItem();
        return mainHandItem.getItem() instanceof BaseFistClass &&
                !(mainHandItem.getItem() instanceof BasicFistItem);
    }

    public void syncInventory(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;

        }

        //  private boolean isValidReplaceItem(Player player){
        //     ExtendedInventory extendedInv = new ExtendedInventory(player);
        //    ItemStack extraSlotItem = player.getInventory().getItem(extendedInv.getExtraSlotIndex());
        //     return !(extraSlotItem.getItem() instanceof BasicFistItem) || extraSlotItem.getItem() instanceof AirItem;
        //  }

    }
}

