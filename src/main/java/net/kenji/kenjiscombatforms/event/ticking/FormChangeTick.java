package net.kenji.kenjiscombatforms.event.ticking;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.CommonEventHandler;
import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.handlers.LevelHandler;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.interfaces.form.AbstractFormData;
import net.kenji.kenjiscombatforms.api.interfaces.form.Form;
import net.kenji.kenjiscombatforms.api.managers.FormLevelManager;
import net.kenji.kenjiscombatforms.api.managers.FormManager;
import net.kenji.kenjiscombatforms.api.managers.forms.*;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.basic_form.BasicFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.power_form.PowerFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.swift_form.SwiftFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.void_form.VoidFistItem;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFist2Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFist3Item;
import net.kenji.kenjiscombatforms.item.custom.fist_forms.wither_form.WitherFistItem;
import net.kenji.kenjiscombatforms.item.custom.forms.BaseFormClass;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jline.utils.Log;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FormChangeTick {

    private static final Set<UUID> playersWithOpenGui = new HashSet<>();
    private static final Map<UUID, Boolean> playerGuiStates = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> playerCombatStates = new ConcurrentHashMap<>();
    private static final Map<UUID, Boolean> hasSetup = new ConcurrentHashMap<>();

    private static final Map<UUID, ItemStack> lastStack = new HashMap<>();

    private static final Map<UUID, Set<UUID>> appliedModifiers = new HashMap<>();

    public static boolean isGuiOpen(Player player) {
        return playerGuiStates.getOrDefault(player.getUUID(), false);
    }

    public static void setGuiOpen(UUID playerUUID, boolean value) {
        playerGuiStates.put(playerUUID, value);
    }

    public static boolean isHandCombat(Player player) {
        return playerCombatStates.getOrDefault(player.getUUID(), true);
    }

    public static void setHandCombat(UUID playerUUID, boolean value) {
        playerCombatStates.put(playerUUID, value);
    }



    @SubscribeEvent
    public static void onLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Level level = event.level;
            LevelHandler levelHandler = LevelHandler.getInstance();
            BasicFistItem defaultFistItem = BasicFistItem.getInstance();


            for (Player player : level.players()) {
                WitherPlayerDataSets.WitherFormPlayerData wData = WitherPlayerDataSets.getInstance().getOrCreateWitherFormPlayerData(player);
                EnderPlayerDataSets.EnderFormPlayerData eData = EnderPlayerDataSets.getInstance().getOrCreateEnderFormPlayerData(player);
                boolean isWitherActive = wData.isAbilityActive();
                boolean isEnderActive = eData.isAbilityActive();
                boolean areFinalActive = isWitherActive || isEnderActive;

                if (player instanceof ServerPlayer serverPlayer) {
                    List<ItemEntity> itemEntities = level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(5.0));
                    for (ItemEntity entity : itemEntities) {
                        ItemStack itemStack = entity.getItem();
                        if (itemStack.getItem() instanceof BaseFormClass) {
                            entity.kill();
                        }
                    }
                }
            }
        }
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        String formName = FormManager.getInstance().getOrCreatePlayerFormData(player.getUUID()).selectedForm;
        Form currentForm = FormManager.getInstance().getForm(formName);

        Form lastForm = FormManager.lastForm.get(player.getUUID());
        ItemStack currentStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        CapabilityItem currentCap = EpicFightCapabilities.getItemStackCapability(currentStack);
        WeaponCategory currentStackCategory = EpicFightCapabilities.getItemStackCapability(currentStack).getWeaponCategory();

        WeaponCategory lastStackCategory = EpicFightCapabilities.getItemStackCapability(lastStack.getOrDefault(player.getUUID(), currentStack)).getWeaponCategory();
        boolean isCurrentCategoryValid = FormManager.isHeldCategoryValid(player, currentStack);

        if (player instanceof ServerPlayer serverPlayer) {
            ServerPlayerPatch playerPatch = EpicFightCapabilities.getServerPlayerPatch(serverPlayer);
            if (playerPatch == null) return;
            if(currentForm == null) return;
            if(!hasSetup.getOrDefault(player.getUUID(), false)) {
                hasSetup.put(player.getUUID(), true);
                SkillContainer weaponInnate = playerPatch.getSkill(SkillSlots.WEAPON_INNATE);
                if (weaponInnate == null) return;
                playerPatch.updateHeldItem(currentCap, currentCap, currentStack, currentStack, InteractionHand.MAIN_HAND);
            }
            /*

            if (isCurrentCategoryValid) {
                ItemStack heldItem = player.getMainHandItem();
                if (!heldItem.isEmpty()) {
                    heldItem.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((attribute, modifier) -> {
                        AttributeInstance instance = player.getAttribute(attribute);
                        if (instance != null) instance.removeModifier(modifier.getId());
                    });
                }

                Set<UUID> applied = appliedModifiers.computeIfAbsent(player.getUUID(), k -> new HashSet<>());
                for (Attribute attribute : currentForm.getFormItem(player.getUUID()).getAttributeModifiers(EquipmentSlot.MAINHAND).keySet()) {
                    if (attribute == null) continue;
                    AttributeInstance instance = player.getAttribute(attribute);
                    if (instance == null) continue;
                    for (AttributeModifier modifier : currentForm.getFormItem(player.getUUID()).getAttributeModifiers(EquipmentSlot.MAINHAND).get(attribute)) {
                        if (modifier == null) continue;
                        if (!instance.hasModifier(modifier)) {
                            instance.addTransientModifier(modifier);
                            applied.add(modifier.getId()); // track it
                        }
                    }
                }
            } else {
                Set<UUID> applied = appliedModifiers.getOrDefault(player.getUUID(), Collections.emptySet());
                for (Attribute attribute : currentForm.getFormItem(player.getUUID()).getAttributeModifiers(EquipmentSlot.MAINHAND).keySet()) {
                    if (attribute == null) continue;
                    AttributeInstance instance = player.getAttribute(attribute);
                    if (instance == null) continue;
                    for (AttributeModifier modifier : currentForm.getFormItem(player.getUUID()).getAttributeModifiers(EquipmentSlot.MAINHAND).get(attribute)) {
                        if (modifier == null) continue;
                        if (applied.contains(modifier.getId()) && instance.hasModifier(modifier)) {
                            instance.removeModifier(modifier.getId());
                            applied.remove(modifier.getId());
                        }
                    }
                }
                ItemStack heldItem = player.getMainHandItem();
                if (!heldItem.isEmpty()) {
                    heldItem.getAttributeModifiers(EquipmentSlot.MAINHAND).forEach((attribute, modifier) -> {
                        AttributeInstance instance = player.getAttribute(attribute);
                        if (instance != null && !instance.hasModifier(modifier)) {
                            instance.addTransientModifier(modifier);
                        }
                    });
                }
            }
            */

            FormChangeTick.lastStack.put(player.getUUID(), currentStack);
        }
    }

    private static boolean basicSelected(Player player){
       return FormChangeHandler.getInstance().getBasicSelected(player);
    }
    private static boolean voidSelected(Player player){
        return FormChangeHandler.getInstance().getVoidSelected(player);
    }
    private static boolean witherSelected(Player player){
        return FormChangeHandler.getInstance().getWitherSelected(player);
    }
    private static boolean swiftSelected(Player player){
        return FormChangeHandler.getInstance().getSwiftSelected(player);
    }
    private static boolean powerSelected(Player player){
        return FormChangeHandler.getInstance().getPowerSelected(player);
    }


    private static boolean isNearItem(Player player){
        return CommonEventHandler.getInstance().getIsNearItem(player);
    }



    private static boolean isGuiClosed(Player player){
        return !playersWithOpenGui.contains(player.getUUID());
    }
}