package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.slots.RemoveItemPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {

    int originalSlot = -1;

    int getOriginalSlot(){
        return originalSlot;
    }

    public void setOriginalSlot(int slotIndex){
        originalSlot = slotIndex;
    }

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
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag nbt = player.getPersistentData();

        if (nbt.contains("storedItem")) {
            ItemStack storedItem = ItemStack.of(nbt);
            player.getInventory().setItem(originalSlot, storedItem);
            nbt.remove("storedItem");
        }
    }



    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();

        int selectedSlot = player.getInventory().selected;
        ItemStack currentItem = player.getInventory().getItem(selectedSlot);

        player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
            ItemStack storedItem = container.getStoredItem();
            if (!container.getStoredItem().isEmpty()){
                player.getInventory().setItem(getInstance().getOriginalSlot(), storedItem);
                container.setStoredItem(ItemStack.EMPTY);
                NetworkHandler.INSTANCE.sendToServer(new RemoveItemPacket(getInstance().getOriginalSlot(),storedItem));

                //getInstance().setOriginalSlot(-1);
                player.getInventory().setChanged();
            }
        });
    }


    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if(entity instanceof Player player) {

            int selectedSlot = player.getInventory().selected;
        ItemStack currentItem = player.getInventory().getItem(selectedSlot);


            player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
                ItemStack storedItem = container.getStoredItem();
                if (!container.getStoredItem().isEmpty()) {
                    player.getInventory().setItem(getInstance().getOriginalSlot(), storedItem);
                    container.setStoredItem(ItemStack.EMPTY);
                    NetworkHandler.INSTANCE.sendToServer(new RemoveItemPacket(getInstance().getOriginalSlot(), storedItem));

                    //getInstance().setOriginalSlot(-1);
                    player.getInventory().setChanged();
                }
            });
        }
    }

        @SubscribeEvent
        public void itemTossEvent(ItemTossEvent event) {
            if (isHoldingFistForm(event.getPlayer())) {

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


    private boolean isNearItem(Player player) {
      final double ITEM_DETECTION_RANGE = player.getPickRadius();
                AABB detectionBox = player.getBoundingBox().inflate(ITEM_DETECTION_RANGE);
        List<ItemEntity> nearbyItems = player.level().getEntitiesOfClass(ItemEntity.class, detectionBox);
        return !nearbyItems.isEmpty();
    }
}
