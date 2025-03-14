package net.kenji.kenjiscombatforms.screen.abilities_gui.form_abilities;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.api.handlers.power_data.WitherPlayerDataSets;
import net.kenji.kenjiscombatforms.api.managers.AbilityManager;
import net.kenji.kenjiscombatforms.api.managers.client_data.ClientFistData;
import net.kenji.kenjiscombatforms.config.KenjisCombatFormsCommon;
import net.kenji.kenjiscombatforms.network.voidform.ClientVoidData;
import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, value = Dist.CLIENT)
public class WitherAbility1CooldownGui {

    private static final SimpleAbilityBarHandler simpleAbilityBarHandler = new SimpleAbilityBarHandler();


    private static float prevCooldown = 0f;
    private static float currentCooldown = 0f;

    public static void updateCooldown() {
        prevCooldown = currentCooldown;
        currentCooldown = (float) ClientVoidData.getCooldown();
    }


    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent event) {


        //if (pPlayer != null) {
        int abilityCooldown = ClientWitherData.getCooldown();
        int ability4Cooldown = ClientWitherData.getMinionCooldown();

        boolean areMinionsActive = ClientWitherData.getMinionsActive();
        boolean isWitherActive = ClientWitherData.getIsWitherActive();
        boolean isEnderActive = ClientVoidData.getIsEnderActive();

        Minecraft mcInstance = Minecraft.getInstance();
        Player player = mcInstance.player;

        ResourceLocation simpleAbilityResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/gui/abilities/simple_ability_bars.png");


        ResourceLocation emptyResource = new ResourceLocation(KenjisCombatForms.MOD_ID, "textures/item/forms/fist.png");


        if (player != null) {

            WitherPlayerDataSets dataSets = WitherPlayerDataSets.getInstance();
            WitherPlayerDataSets.WitherDashPlayerData tpData = dataSets.getOrCreateDashPlayerData(player);

            int voidScreenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int voidScreenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

            int voidImageWidth = 100;
            int voidImageHeight = 50;

            int abilityX = voidScreenWidth - voidImageWidth;
            int abilityY = voidScreenHeight - voidImageHeight;

            WitherPlayerDataSets.WitherMinionPlayerData mData = dataSets.getOrCreateMinionPlayerData(player);
            int minionMaxCooldown = mData.getMAX_COOLDOWN();

            int witherAbility1FullHeight = 30;
            int voidAbility1FullHeight = 30;
            int voidAbility1MaxCooldown = tpData.getMAX_COOLDOWN();
            float voidAbility1ElapsedCooldown = voidAbility1MaxCooldown - abilityCooldown;
            float voidAbility1CooldownProgress = (float) voidAbility1ElapsedCooldown / voidAbility1MaxCooldown;
            int abilityBarHeight = (int) (voidAbility1FullHeight * voidAbility1CooldownProgress);

            int minionElapsedCooldown = minionMaxCooldown - ability4Cooldown;
            float minionCooldownProgress = (float) minionElapsedCooldown / minionMaxCooldown;
            int ability4BarHeight = (int) (witherAbility1FullHeight * minionCooldownProgress);


            float bgAlpha = 0.5f;


            int simpleAbilityU = 0;
            int simpleAbilityV = 0;
            int simpleAbilityWidth = 107;
            int simpleAbilityHeight = 5;
            int simpleAbilityWidth2 = 5;
            int simpleAbilityHeight2 = 62;

            int abilitySimpleMaxCooldown = tpData.getMAX_COOLDOWN();

            int abilitySimpleElapsedCooldown = abilitySimpleMaxCooldown - abilityCooldown;
            int abilitySimpleBarWidth = (int) ((float) abilitySimpleElapsedCooldown / abilitySimpleMaxCooldown * simpleAbilityWidth);

            int voidAbilityElapsedCooldown2 = minionMaxCooldown - ability4Cooldown;
            int simpleBarHeight2 = (int) ((float) voidAbilityElapsedCooldown2 / minionMaxCooldown * simpleAbilityHeight2);


            boolean areFinalsActive = isWitherActive || isEnderActive;

            if (AbilityManager.getInstance().getPlayerAbilityData(player).chosenAbility1 == AbilityManager.AbilityOption1.WITHER_ABILITY1 || ClientFistData.getChosenAbility1() == AbilityManager.AbilityOption1.WITHER_ABILITY1) {

                if (!areFinalsActive) {
                    simpleAbilityBarHandler.drawAbility1Icon(event, simpleAbilityResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth, simpleAbilityHeight, abilitySimpleBarWidth);
                }
            }
            if (isWitherActive || areMinionsActive) {
                SimpleAbilityBarHandler.drawAbility4Icon(event, simpleAbilityResource, abilityX, abilityY, simpleAbilityU, simpleAbilityV, simpleAbilityWidth2, simpleAbilityHeight2, simpleBarHeight2);
            }
        }
    }
}
