package net.kenji.kenjiscombatforms.enchantments;




import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class QuickStrikerEventHandler {

    static UUID uuid = UUID.randomUUID();
    static UUID uuid2 = UUID.randomUUID();

    // Convert the UUID to a string
   static  String stringUUID = uuid.toString();
    static  String stringUUID2 = uuid2.toString();

    private static final UUID ATTACK_SPEED_MODIFIER_UUID = UUID.fromString("d3e7e3b3-6e4d-4f3d-9f3d-2c3e3e3b3e3d");
    private static final UUID ATTACK_SPEED_MODIFIER2_UUID = UUID.fromString(stringUUID);
    private static final UUID ATTACK_SPEED_MODIFIER3_UUID = UUID.fromString(stringUUID2);
    private static final String MODIFIER_NAME = "QuickStriker bonus";
    private static final String MODIFIER_NAME2 = "QuickStriker bonus2";
    private static final String MODIFIER_NAME3 = "QuickStriker bonus3";
    private static final double LVL1_ATTACK_SPEED_BONUS = 0.35;
    private static final double LVL2_ATTACK_SPEED_BONUS = 0.42;
    private static final double LVL3_ATTACK_SPEED_BONUS = 0.48;




    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        ItemStack mainHandItem = player.getMainHandItem();

        int enchantmentLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.QUICK_STRIKER_ENCHANTMENT.get(), mainHandItem);

        if (enchantmentLevel == 1) {
            double attackSpeedBonus = LVL1_ATTACK_SPEED_BONUS * enchantmentLevel;
            AttributeModifier modifier = new AttributeModifier(ATTACK_SPEED_MODIFIER_UUID, MODIFIER_NAME, attackSpeedBonus, AttributeModifier.Operation.ADDITION);

            if (player.getAttribute(Attributes.ATTACK_SPEED).getModifier(ATTACK_SPEED_MODIFIER_UUID) == null) {
                player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(modifier);
            }
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER2_UUID);
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER3_UUID);
        }

        if (enchantmentLevel == 2) {
            double attackSpeedBonus = LVL2_ATTACK_SPEED_BONUS * enchantmentLevel;
            AttributeModifier modifier = new AttributeModifier(ATTACK_SPEED_MODIFIER2_UUID, MODIFIER_NAME2, attackSpeedBonus, AttributeModifier.Operation.ADDITION);

            if (player.getAttribute(Attributes.ATTACK_SPEED).getModifier(ATTACK_SPEED_MODIFIER2_UUID) == null) {
                player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(modifier);
            }
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER_UUID);
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER3_UUID);
        }

        if (enchantmentLevel == 3) {
            double attackSpeedBonus = LVL3_ATTACK_SPEED_BONUS * enchantmentLevel;
            AttributeModifier modifier = new AttributeModifier(ATTACK_SPEED_MODIFIER3_UUID, MODIFIER_NAME3, attackSpeedBonus, AttributeModifier.Operation.ADDITION);

            if (player.getAttribute(Attributes.ATTACK_SPEED).getModifier(ATTACK_SPEED_MODIFIER3_UUID) == null) {
                player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(modifier);
            }

            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER_UUID);
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER2_UUID);
        }

        else if(enchantmentLevel == 0){
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER_UUID);
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER2_UUID);
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(ATTACK_SPEED_MODIFIER3_UUID);
        }
    }
}