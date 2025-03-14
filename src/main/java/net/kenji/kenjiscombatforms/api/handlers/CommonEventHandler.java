package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.event.ticking.FormChangeTick;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {



    private FormLevelManager.PlayerFormLevelData getOrCreateLevelData(ServerPlayer player){
        return LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
    }

    private static final CommonEventHandler INSTANCE = new CommonEventHandler();
    public static CommonEventHandler getInstance(){
        return INSTANCE;
    }

    public boolean getIsNearItem(Player player){
        return getInstance().isNearItem(player);
    }

    public boolean getIsHoldingFistForm(Player player){
        return isHoldingFistForm(player);
    }









    @SubscribeEvent
public void itemTossEvent(ItemTossEvent event){
        if(isHoldingFistForm(event.getPlayer())){

            event.setCanceled(true);
        }
}

    @SubscribeEvent
    public static void onGuiOpen(PlayerContainerEvent.Open event) {
        Player player = event.getEntity();
            //removeFistFormFromInventory(player);
    }

    private static boolean isHoldingFistForm(Player player){
        Item handItem = player.getMainHandItem().getItem();
        return handItem instanceof BaseFistClass;
    }

    public static void removeFistFormFromInventory(Player player){
        WitherPlayerDataSets.WitherFormPlayerData wData = WitherPlayerDataSets.getInstance().getOrCreateWitherFormPlayerData(player);
        EnderPlayerDataSets.EnderFormPlayerData eData = EnderPlayerDataSets.getInstance().getOrCreateEnderFormPlayerData(player);
        boolean isWitherActive = wData.isAbilityActive();
        boolean isEnderActive = eData.isAbilityActive();

        boolean areFinalActive = isWitherActive || isEnderActive;


        for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof BaseFistClass) {
                if (i != KenjisCombatFormsCommon.FORM_LOCK_SLOT.get() || !FormChangeTick.isHandCombat(player) || areFinalActive) {
                    player.getInventory().setItem(i,ItemStack.EMPTY);
                }
            }
        }
    }


    private boolean isNearItem(Player player) {
      final double ITEM_DETECTION_RANGE = player.getPickRadius();
                AABB detectionBox = player.getBoundingBox().inflate(ITEM_DETECTION_RANGE);
        List<ItemEntity> nearbyItems = player.level().getEntitiesOfClass(ItemEntity.class, detectionBox);
        return !nearbyItems.isEmpty();
    }
}
