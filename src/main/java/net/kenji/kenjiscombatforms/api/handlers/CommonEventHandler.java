package net.kenji.kenjiscombatforms.api.handlers;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.capabilities.ExtraContainerCapability;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.utils.SkillUtils;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.capability.SyncNBTPacket;
import net.kenji.kenjiscombatforms.network.capability.SyncRemovedNBTPacket;
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
import net.minecraftforge.network.PacketDistributor;
import reascer.wom.gameasset.WOMSkills;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.List;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonEventHandler {

    int originalSlot = -1;

    public int getOriginalSlot(Player player) {
        CompoundTag nbt = player.getPersistentData();
        return nbt.getInt("originalSlot");
    }

    public void setOriginalSlot(Player player, int slotIndex) {
        CompoundTag nbt = player.getPersistentData();
        nbt.putInt("originalSlot", slotIndex);
    }

    public void setStoredItemNBT(Player player, ItemStack storedItem) {
        CompoundTag nbt = player.getPersistentData();
        nbt.put(ExtraContainerCapability.storedItem, storedItem.copy().serializeNBT());
    }

    public ItemStack getStoredItem(Player player) {
        CompoundTag nbt = player.getPersistentData();
        return ItemStack.of(nbt.getCompound(ExtraContainerCapability.storedItem));
    }


    private FormLevelManager.PlayerFormLevelData getOrCreateLevelData(ServerPlayer player) {
        return LevelHandler.getInstance().getOrCreatePlayerLevelData(player);
    }

    private static final CommonEventHandler INSTANCE = new CommonEventHandler();

    public static CommonEventHandler getInstance() {
        return INSTANCE;
    }

    public boolean getIsNearItem(Player player) {
        return getInstance().isNearItem(player);
    }

    public boolean getIsHoldingFistForm(Player player) {
        return isHoldingFistForm(player);
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (player == null) return;

        // Retrieve the stored item from the capability before the dimension change

        player.getCapability(ExtraContainerCapability.EXTRA_CONTAINER_CAP).ifPresent(container -> {
            ItemStack storedItem = container.getStoredItem();
            int originalSlot = container.getOriginalSlot();
            if (getInstance().getOriginalSlot(player) != -1 && container.getOriginalSlot() != -1) {
                System.out.println("Stored Item before dimension change: " + storedItem);
                System.out.println("slot before dimension change: " + originalSlot);
                CompoundTag nbt = player.getPersistentData();
                getInstance().setStoredItemNBT(player, storedItem);
                getInstance().setOriginalSlot(player, originalSlot);
                System.out.println("Stored Item after dimension change: " + getInstance().getStoredItem(player));
                System.out.println("slot after dimension change: " + getInstance().getOriginalSlot(player));
                player.getInventory().setItem(getInstance().getOriginalSlot(player), ItemStack.EMPTY);
                player.getInventory().setItem(getInstance().getOriginalSlot(player), getInstance().getStoredItem(player));

                player.inventoryMenu.broadcastChanges();
                if (player instanceof ServerPlayer serverPlayer) {
                    NetworkHandler.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> serverPlayer),
                            new SyncRemovedNBTPacket(storedItem, originalSlot)
                    );
                }
            }
        });
    }
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        CompoundTag nbt = player.getPersistentData();
        int offHandSlot = 40;
        ItemStack storedItem = getInstance().getStoredItem(player);
        int originalSlot = getInstance().getOriginalSlot(player);


        if(player instanceof ServerPlayer serverPlayer) {

            AbilityChangeHandler abilityChangeHandler = AbilityChangeHandler.getInstance();
            abilityChangeHandler.deactivateCurrentAbilities(player);

            if (getInstance().getOriginalSlot(player) != -1) {
                if(!storedItem.isEmpty()){
                    if(player.getInventory().getItem(originalSlot).getItem() instanceof BaseFistClass){
                        player.getInventory().setItem(originalSlot, ItemStack.EMPTY);
                    }
                    player.getInventory().setItem(originalSlot, storedItem);
                    NetworkHandler.INSTANCE.send(
                            PacketDistributor.PLAYER.with(() -> serverPlayer),
                            new SyncRemovedNBTPacket(storedItem, originalSlot)
                    );
                    getInstance().setStoredItemNBT(player, ItemStack.EMPTY);
                }
            }
        }
        if (nbt.contains("storedDodgeSkill")) {
            String skillName = nbt.getString("storedDodgeSkill");
            Skill skillToSet = SkillUtils.getSkillByName(skillName);

            if (skillToSet != null) {
                event.getEntity().getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
                    if (cap instanceof PlayerPatch<?> playerPatch) {
                        if (getInstance().getSkillValid(playerPatch)) {
                            playerPatch.getSkill(SkillSlots.DODGE).setSkill(skillToSet);
                            nbt.remove("storedDodgeSkill");
                            System.out.println("storeDodgeSkill" + skillName);
                        }
                    }
                });
            }
        }
    }
    boolean getSkillValid(PlayerPatch<?> playerPatch){
        return playerPatch.getSkill(SkillSlots.DODGE).getSkill() == WOMSkills.SHADOWSTEP ||
                playerPatch.getSkill(SkillSlots.DODGE).getSkill() == WOMSkills.ENDERSTEP ||
                playerPatch.getSkill(SkillSlots.DODGE).getSkill() == null;
    }





    @SubscribeEvent
    public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide) {
            AbilityChangeHandler abilityChangeHandler = AbilityChangeHandler.getInstance();
            abilityChangeHandler.deactivateCurrentAbilities(player);

            if (player instanceof ServerPlayer serverPlayer) {
                ItemStack storedItem = getInstance().getStoredItem(player);
                int originalSlot = getInstance().getOriginalSlot(player);
                int selectedSlot = player.getInventory().selected;
                ItemStack currentItem = player.getInventory().getItem(selectedSlot);
                if (originalSlot != -1) {
                    if (player.getInventory().getItem(originalSlot).getItem() instanceof BaseFistClass) {
                        player.getInventory().setItem(originalSlot, ItemStack.EMPTY);
                    }
                    if (!storedItem.isEmpty()) {
                        player.getInventory().setItem(originalSlot, storedItem);
                        getInstance().setStoredItemNBT(player, ItemStack.EMPTY);
                    }
                } else {
                    if (player.getInventory().getItem(selectedSlot).getItem() instanceof BaseFistClass) {
                        player.getInventory().setItem(selectedSlot, ItemStack.EMPTY);
                    }
                }
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new SyncNBTPacket(storedItem, originalSlot)
                );
            }
        }
    }


    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if(entity instanceof Player player) {
            AbilityChangeHandler abilityChangeHandler = AbilityChangeHandler.getInstance();
            abilityChangeHandler.deactivateCurrentAbilities(player);


            ItemStack storedItem = getInstance().getStoredItem(player);
            int originalSlot = getInstance().getOriginalSlot(player);
            int selectedSlot = player.getInventory().selected;

            if (player instanceof ServerPlayer serverPlayer) {

                if (originalSlot != -1) {
                    if (player.getInventory().getItem(originalSlot).getItem() instanceof BaseFistClass) {
                        player.getInventory().setItem(originalSlot, ItemStack.EMPTY);
                    }
                    if (!storedItem.isEmpty()) {
                        player.getInventory().setItem(originalSlot, storedItem);
                        getInstance().setStoredItemNBT(player, ItemStack.EMPTY);
                    }
                } else {
                    if (player.getInventory().getItem(selectedSlot).getItem() instanceof BaseFistClass) {
                        player.getInventory().setItem(selectedSlot, ItemStack.EMPTY);
                    }
                }
                NetworkHandler.INSTANCE.send(
                        PacketDistributor.PLAYER.with(() -> serverPlayer),
                        new SyncNBTPacket(storedItem, originalSlot)
                );
            }
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
