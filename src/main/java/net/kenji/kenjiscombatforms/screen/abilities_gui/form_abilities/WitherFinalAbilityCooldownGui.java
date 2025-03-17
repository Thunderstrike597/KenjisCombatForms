package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class WitherFinalAbilityCooldownGui {


    private static float prevCooldown = 0f;
    private static float currentCooldown = 0f;

    public static void updateCooldown() {
        prevCooldown = currentCooldown;
        currentCooldown = (float) ClientWitherData.getCooldown();
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        //if (pPlayer != null) {
        int abilityCooldown = ClientWitherData.getCooldown3();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;


        ResourceLocation simpleAbilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/simple_ability_bars.png");

        ResourceLocation emptyResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/item/forms/fist.png");

        if (player != null) {

        WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
        WitherPlayerDataSets.WitherFormPlayerData wData = dataSets.getOrCreateWitherFormPlayerData(player);

        int voidScreenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int voidScreenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int voidImageWidth = 100;
        int voidImageHeight = 50;

        int abilityX = voidScreenWidth - voidImageWidth;
        int abilityY = voidScreenHeight - voidImageHeight;





            int witherAbility1FullHeight = 53;
            int witherAbility1MaxCooldown = wData.getMAX_COOLDOWN();
            float witherAbility1ElapsedCooldown = witherAbility1MaxCooldown - abilityCooldown;
            float witherAbility1CooldownProgress = (float) witherAbility1ElapsedCooldown / witherAbility1MaxCooldown;
            int abilityBarHeight = (int) (witherAbility1FullHeight * witherAbility1CooldownProgress);


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 107;
            int simpleAbilityHeight = 8;


            int abilitySimpleMaxCooldown = wData.getMAX_COOLDOWN();
            float abilitySimpleElapsedCooldown = abilitySimpleMaxCooldown - abilityCooldown;
            float ability1SimpleCooldownProgress = (float) abilitySimpleElapsedCooldown / abilitySimpleMaxCooldown;
            int abilitySimpleBarWidth = (int) (simpleAbilityWidth * ability1SimpleCooldownProgress);


            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenFinal == AbilityManager.AbilityOption3.WITHER_FINAL ||
                    ClientFistData.getChosenAbility3() == AbilityManager.AbilityOption3.WITHER_FINAL) {

               // SimpleAbilityBarHandler.drawAbility3Icon(event, abilityCooldown, emptyResource, simpleAbilityResource, emptyResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilitySimpleBarWidth);
            }
        }
    }
}
