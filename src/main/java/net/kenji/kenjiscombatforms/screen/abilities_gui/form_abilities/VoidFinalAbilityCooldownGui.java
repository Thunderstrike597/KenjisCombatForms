package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.EnderPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class VoidFinalAbilityCooldownGui {


    private static float prevCooldown = 0f;
    private static float currentCooldown = 0f;

    public static void updateCooldown() {
        prevCooldown = currentCooldown;
        currentCooldown = (float) ClientVoidData.getCooldown();
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        //if (pPlayer != null) {
        int abilityCooldown = ClientVoidData.getCooldown3();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation emptyResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/item/forms/fist.png");

        ResourceLocation simpleAbilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/simple_ability_bars.png");


        if (player != null) {

            EnderPlayerDataSets dataSets = EnderPlayerDataSets.getInstance();
            EnderPlayerDataSets.TeleportPlayerData tpData = dataSets.getOrCreateTeleportPlayerData(player);
            EnderPlayerDataSets.EnderFormPlayerData eData = dataSets.getOrCreateEnderFormPlayerData(player);


            int voidScreenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int voidScreenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int voidImageWidth = 100;
            int voidImageHeight = 50;

            int abilityX = voidScreenWidth - voidImageWidth;
            int abilityY = voidScreenHeight - voidImageHeight;


            int voidAbility1FullHeight = 44;
            int voidAbility1MaxCooldown = tpData.getMAX_COOLDOWN();
            float voidAbility1ElapsedCooldown = voidAbility1MaxCooldown - abilityCooldown;
            float voidAbility1CooldownProgress = (float) voidAbility1ElapsedCooldown / voidAbility1MaxCooldown;
            int abilityBarHeight = (int) (voidAbility1FullHeight * voidAbility1CooldownProgress);



            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 107;
            int simpleAbilityHeight = 8;


            int abilitySimpleMaxCooldown = eData.getMAX_COOLDOWN();
            float abilitySimpleElapsedCooldown = abilitySimpleMaxCooldown - abilityCooldown;
            float ability1SimpleCooldownProgress = (float) abilitySimpleElapsedCooldown / abilitySimpleMaxCooldown;
            int abilitySimpleBarWidth = (int) (simpleAbilityWidth * ability1SimpleCooldownProgress);



            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenFinal == AbilityManager.AbilityOption3.VOID_FINAL ||
                    ClientFistData.getChosenAbility3() == AbilityManager.AbilityOption3.VOID_FINAL) {

                SimpleAbilityBarHandler.drawAbility3Icon(event, abilityCooldown, emptyResource, simpleAbilityResource, emptyResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilitySimpleBarWidth);
            }
        }
    }
}
