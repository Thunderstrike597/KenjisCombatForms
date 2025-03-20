package net.kenji.kenjiscombatforms.network;

import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.network.fist_forms.ability_choose.Ability1ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.ability_choose.Ability2ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.ability_choose.Ability3ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.client_data.*;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.BasicFormChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form1ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form2ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_choose.Form3ChoosePacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_level.SyncServerFormLevelPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_level.SyncServerWitherFormLevelPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.Form1SwapPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.Form2SwapPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.Form3SwapPacket;
import net.kenji.kenjiscombatforms.network.fist_forms.form_swap.FormToSwapPacket;
import net.kenji.kenjiscombatforms.network.movers.EntityRotationPacket;
import net.kenji.kenjiscombatforms.network.movers.PlayerInputPacket;
import net.kenji.kenjiscombatforms.network.movers.WitherInputPacket;
import net.kenji.kenjiscombatforms.network.movers.attacking.EnderEntityAttackPacket;
import net.kenji.kenjiscombatforms.network.movers.attacking.WitherEntityAttackPacket;
import net.kenji.kenjiscombatforms.network.particle_packets.*;
import net.kenji.kenjiscombatforms.network.power_form.ability1.StrengthBoostPacket;
import net.kenji.kenjiscombatforms.network.power_form.ability1.SyncPowerDataPacket;
import net.kenji.kenjiscombatforms.network.power_form.ability2.PowerEffectInflictPacket;
import net.kenji.kenjiscombatforms.network.power_form.ability2.SyncPowerData2Packet;
import net.kenji.kenjiscombatforms.network.slots.RemoveItemPacket;
import net.kenji.kenjiscombatforms.network.slots.SwitchItemPacket;
import net.kenji.kenjiscombatforms.network.swift_form.ability1.SpeedBoostPacket;
import net.kenji.kenjiscombatforms.network.swift_form.ability1.SyncSwiftDataPacket;
import net.kenji.kenjiscombatforms.network.swift_form.ability2.SwiftEffectInflictPacket;
import net.kenji.kenjiscombatforms.network.swift_form.ability2.SyncSwiftData2Packet;
import net.kenji.kenjiscombatforms.network.voidform.*;
import net.kenji.kenjiscombatforms.network.voidform.ability1.SyncVoidDataPacket;
import net.kenji.kenjiscombatforms.network.voidform.ability1.TeleportPlayerPacket;
import net.kenji.kenjiscombatforms.network.voidform.ability2.SyncVoidData2Packet;
import net.kenji.kenjiscombatforms.network.voidform.ability2.TeleportPlayerBehindPacket;
import net.kenji.kenjiscombatforms.network.voidform.ability2.VoidRiftPacket;
import net.kenji.kenjiscombatforms.network.voidform.ability3.SyncVoidData3Packet;
import net.kenji.kenjiscombatforms.network.voidform.ability3.ToggleEnderPacket;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.TeleportEnderEntityPacket;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4.EnderLevitationPacket;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability4.SyncVoidData4Packet;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability5.SyncVoidData5Packet;
import net.kenji.kenjiscombatforms.network.voidform.ender_abilities.ability5.VoidGrabPacket;
import net.kenji.kenjiscombatforms.network.witherform.WitherFormDashPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability1.SyncWitherDataPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability1.WitherDashPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability1.WitherPausePacket;
import net.kenji.kenjiscombatforms.network.witherform.ability2.SoulDriftPacket;
import net.kenji.kenjiscombatforms.network.witherform.ability2.SyncWitherData2Packet;
import net.kenji.kenjiscombatforms.network.witherform.ability3.SyncWitherData3Packet;
import net.kenji.kenjiscombatforms.network.witherform.ability3.ToggleWitherFormPacket;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability4.SummonWitherMinionsPacket;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability4.SyncWitherData4Packet;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability5.SyncWitherData5Packet;
import net.kenji.kenjiscombatforms.network.witherform.wither_abilites.ability5.WitherImplodePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(KenjisCombatForms.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {


        INSTANCE.messageBuilder(TeleportPlayerPacket.class, packetId++)
                .encoder(TeleportPlayerPacket::encode)
                .decoder(TeleportPlayerPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    TeleportPlayerPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(TeleportPlayerBehindPacket.class, packetId++)
                .encoder(TeleportPlayerBehindPacket::encode)
                .decoder(TeleportPlayerBehindPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    TeleportPlayerBehindPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(VoidRiftPacket.class, packetId++)
                .encoder(VoidRiftPacket::encode)
                .decoder(VoidRiftPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    VoidRiftPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(ToggleEnderPacket.class, packetId++)
                .encoder(ToggleEnderPacket::encode)
                .decoder(ToggleEnderPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    ToggleEnderPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(EnderLevitationPacket.class, packetId++)
                .encoder(EnderLevitationPacket::encode)
                .decoder(EnderLevitationPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    EnderLevitationPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(VoidGrabPacket.class, packetId++)
                .encoder(VoidGrabPacket::encode)
                .decoder(VoidGrabPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    VoidGrabPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(TeleportEnderEntityPacket.class, packetId++)
                .encoder(TeleportEnderEntityPacket::encode)
                .decoder(TeleportEnderEntityPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    TeleportEnderEntityPacket.handle(msg, ctx.get());
                }).add();


        INSTANCE.messageBuilder(WitherDashPacket.class, packetId++)
                .encoder(WitherDashPacket::encode)
                .decoder(WitherDashPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    WitherDashPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(WitherPausePacket.class, packetId++)
                .encoder(WitherPausePacket::encode)
                .decoder(WitherPausePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    WitherPausePacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SoulDriftPacket.class, packetId++)
                .encoder(SoulDriftPacket::encode)
                .decoder(SoulDriftPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SoulDriftPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(ToggleWitherFormPacket.class, packetId++)
                .encoder(ToggleWitherFormPacket::encode)
                .decoder(ToggleWitherFormPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    ToggleWitherFormPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SummonWitherMinionsPacket.class, packetId++)
                .encoder(SummonWitherMinionsPacket::encode)
                .decoder(SummonWitherMinionsPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SummonWitherMinionsPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(WitherImplodePacket.class, packetId++)
                .encoder(WitherImplodePacket::encode)
                .decoder(WitherImplodePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    WitherImplodePacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(WitherFormDashPacket.class, packetId++)
                .encoder(WitherFormDashPacket::encode)
                .decoder(WitherFormDashPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    WitherFormDashPacket.handle(msg, ctx.get());
                }).add();


        INSTANCE.messageBuilder(SpeedBoostPacket.class, packetId++)
                .encoder(SpeedBoostPacket::encode)
                .decoder(SpeedBoostPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SpeedBoostPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SwiftEffectInflictPacket.class, packetId++)
                .encoder(SwiftEffectInflictPacket::encode)
                .decoder(SwiftEffectInflictPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SwiftEffectInflictPacket.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(StrengthBoostPacket.class, packetId++)
                .encoder(StrengthBoostPacket::encode)
                .decoder(StrengthBoostPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    StrengthBoostPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(PowerEffectInflictPacket.class, packetId++)
                .encoder(PowerEffectInflictPacket::encode)
                .decoder(PowerEffectInflictPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    PowerEffectInflictPacket.handle(msg, ctx.get());
                }).add();



        INSTANCE.messageBuilder(SyncVoidDataPacket.class, packetId++)
                .encoder(SyncVoidDataPacket::encode)
                .decoder(SyncVoidDataPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncVoidDataPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncVoidData2Packet.class, packetId++)
                .encoder(SyncVoidData2Packet::encode)
                .decoder(SyncVoidData2Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncVoidData2Packet.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncVoidData3Packet.class, packetId++)
                .encoder(SyncVoidData3Packet::encode)
                .decoder(SyncVoidData3Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncVoidData3Packet.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncVoidData4Packet.class, packetId++)
                .encoder(SyncVoidData4Packet::encode)
                .decoder(SyncVoidData4Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncVoidData4Packet.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncVoidData5Packet.class, packetId++)
                .encoder(SyncVoidData5Packet::encode)
                .decoder(SyncVoidData5Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncVoidData5Packet.handle(msg, ctx.get());
                }).add();



        INSTANCE.messageBuilder(SyncWitherDataPacket.class, packetId++)
                .encoder(SyncWitherDataPacket::encode)
                .decoder(SyncWitherDataPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncWitherDataPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncWitherData2Packet.class, packetId++)
                .encoder(SyncWitherData2Packet::encode)
                .decoder(SyncWitherData2Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncWitherData2Packet.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncWitherData3Packet.class, packetId++)
                .encoder(SyncWitherData3Packet::encode)
                .decoder(SyncWitherData3Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncWitherData3Packet.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncWitherData4Packet.class, packetId++)
                .encoder(SyncWitherData4Packet::encode)
                .decoder(SyncWitherData4Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncWitherData4Packet.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncWitherData5Packet.class, packetId++)
                .encoder(SyncWitherData5Packet::encode)
                .decoder(SyncWitherData5Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncWitherData5Packet.handle(msg, ctx.get());
                }).add();


        INSTANCE.messageBuilder(SyncSwiftDataPacket.class, packetId++)
                .encoder(SyncSwiftDataPacket::encode)
                .decoder(SyncSwiftDataPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncSwiftDataPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncSwiftData2Packet.class, packetId++)
                .encoder(SyncSwiftData2Packet::encode)
                .decoder(SyncSwiftData2Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncSwiftData2Packet.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(SyncPowerDataPacket.class, packetId++)
                .encoder(SyncPowerDataPacket::encode)
                .decoder(SyncPowerDataPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncPowerDataPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncPowerData2Packet.class, packetId++)
                .encoder(SyncPowerData2Packet::encode)
                .decoder(SyncPowerData2Packet::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncPowerData2Packet.handle(msg, ctx.get());
                }).add();



        INSTANCE.messageBuilder(EndParticlesTickPacket.class, packetId++)
                .encoder(EndParticlesTickPacket::encode)
                .decoder(EndParticlesTickPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    EndParticlesTickPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(RiftParticlesTickPacket.class, packetId++)
                .encoder(RiftParticlesTickPacket::encode)
                .decoder(RiftParticlesTickPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    RiftParticlesTickPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(RiftSummonParticlesTickPacket.class, packetId++)
                .encoder(RiftSummonParticlesTickPacket::encode)
                .decoder(RiftSummonParticlesTickPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    RiftSummonParticlesTickPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SoulParticlesTickPacket.class, packetId++)
                .encoder(SoulParticlesTickPacket::encode)
                .decoder(SoulParticlesTickPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SoulParticlesTickPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(LargeSmokeParticlesTickPacket.class, packetId++)
                .encoder(LargeSmokeParticlesTickPacket::encode)
                .decoder(LargeSmokeParticlesTickPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    LargeSmokeParticlesTickPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(MinionSummonParticlesPacket.class, packetId++)
                .encoder(MinionSummonParticlesPacket::encode)
                .decoder(MinionSummonParticlesPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    MinionSummonParticlesPacket.handle(msg, ctx.get());
                }).add();


        INSTANCE.messageBuilder(SmokeParticlesPacket.class, packetId++)
                .encoder(SmokeParticlesPacket::encode)
                .decoder(SmokeParticlesPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SmokeParticlesPacket.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(BlinkPacket.class, packetId++)
                .encoder(BlinkPacket::encode)
                .decoder(BlinkPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    BlinkPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(PlayerInputPacket.class, packetId++)
                .encoder(PlayerInputPacket::encode)
                .decoder(PlayerInputPacket::decode)
                .consumerMainThread((msg, ctx) -> {
                    PlayerInputPacket.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(EntityRotationPacket.class, packetId++)
                .encoder(EntityRotationPacket::encode)
                .decoder(EntityRotationPacket::decode)
                .consumerMainThread((msg, ctx) -> {
                    EntityRotationPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(EnderEntityAttackPacket.class, packetId++)
                .encoder(EnderEntityAttackPacket::encode)
                .decoder(EnderEntityAttackPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    EnderEntityAttackPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(WitherEntityAttackPacket.class, packetId++)
                .encoder(WitherEntityAttackPacket::encode)
                .decoder(WitherEntityAttackPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    WitherEntityAttackPacket.handle(msg, ctx.get());
                }).add();



        INSTANCE.messageBuilder(Ability2ChoosePacket.class, packetId++)
                .encoder(Ability2ChoosePacket::encode)
                .decoder(Ability2ChoosePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Ability2ChoosePacket.handle(msg, ctx.get());
                }).add();



        INSTANCE.messageBuilder(Ability1ChoosePacket.class, packetId++)
                .encoder(Ability1ChoosePacket::encode)
                .decoder(Ability1ChoosePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Ability1ChoosePacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(Ability2ChoosePacket.class, packetId++)
                .encoder(Ability2ChoosePacket::encode)
                .decoder(Ability2ChoosePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Ability2ChoosePacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(Ability3ChoosePacket.class, packetId++)
                .encoder(Ability3ChoosePacket::encode)
                .decoder(Ability3ChoosePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Ability3ChoosePacket.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(BasicFormChoosePacket.class, packetId++)
                .encoder(BasicFormChoosePacket::encode)
                .decoder(BasicFormChoosePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    BasicFormChoosePacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(Form1ChoosePacket.class, packetId++)
                .encoder(Form1ChoosePacket::encode)
                .decoder(Form1ChoosePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Form1ChoosePacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(Form2ChoosePacket.class, packetId++)
                .encoder(Form2ChoosePacket::encode)
                .decoder(Form2ChoosePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Form2ChoosePacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(Form3ChoosePacket.class, packetId++)
                .encoder(Form3ChoosePacket::encode)
                .decoder(Form3ChoosePacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Form3ChoosePacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(Form1SwapPacket.class, packetId++)
                .encoder(Form1SwapPacket::encode)
                .decoder(Form1SwapPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Form1SwapPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(Form2SwapPacket.class, packetId++)
                .encoder(Form2SwapPacket::encode)
                .decoder(Form2SwapPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Form2SwapPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(Form3SwapPacket.class, packetId++)
                .encoder(Form3SwapPacket::encode)
                .decoder(Form3SwapPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    Form3SwapPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(FormToSwapPacket.class, packetId++)
                .encoder(FormToSwapPacket::encode)
                .decoder(FormToSwapPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    FormToSwapPacket.handle(msg, ctx.get());
                }).add();


        INSTANCE.messageBuilder(SyncClientFormsPacket.class, packetId++)
                .encoder(SyncClientFormsPacket::encode)
                .decoder(SyncClientFormsPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncClientFormsPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncClientAbilityPacket.class, packetId++)
                .encoder(SyncClientAbilityPacket::encode)
                .decoder(SyncClientAbilityPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncClientAbilityPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncClientBasicFistPacket.class, packetId++)
                .encoder(SyncClientBasicFistPacket::encode)
                .decoder(SyncClientBasicFistPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncClientBasicFistPacket.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(SyncServerFormLevelPacket.class, packetId++)
                .encoder(SyncServerFormLevelPacket::encode)
                .decoder(SyncServerFormLevelPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncServerFormLevelPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(SyncServerWitherFormLevelPacket.class, packetId++)
                .encoder(SyncServerWitherFormLevelPacket::encode)
                .decoder(SyncServerWitherFormLevelPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncServerWitherFormLevelPacket.handle(msg, ctx.get());
                }).add();


        INSTANCE.messageBuilder(UpdateInventoryOpenPacket.class, packetId++)
                .encoder(UpdateInventoryOpenPacket::encode)
                .decoder(UpdateInventoryOpenPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    UpdateInventoryOpenPacket.handle(msg, ctx.get());
                }).add();
        INSTANCE.messageBuilder(UpdateHandCombatPacket.class, packetId++)
                .encoder(UpdateHandCombatPacket::encode)
                .decoder(UpdateHandCombatPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    UpdateHandCombatPacket.handle(msg, ctx.get());
                }).add();



        INSTANCE.messageBuilder(SyncAbilitiesAndFormsPacket.class, packetId++)
                .encoder(SyncAbilitiesAndFormsPacket::encode)
                .decoder(SyncAbilitiesAndFormsPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SyncAbilitiesAndFormsPacket.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(SwitchItemPacket.class, packetId++)
                .encoder(SwitchItemPacket::encode)
                .decoder(SwitchItemPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    SwitchItemPacket.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(RemoveItemPacket.class, packetId++)
                .encoder(RemoveItemPacket::encode)
                .decoder(RemoveItemPacket::new)
                .consumerMainThread((msg, ctx) -> {
                    RemoveItemPacket.handle(msg, ctx.get());
                }).add();

        INSTANCE.messageBuilder(WitherInputPacket.class, packetId++)
                .encoder(WitherInputPacket::encode)
                .decoder(WitherInputPacket::decode)
                .consumerMainThread((msg, ctx) -> {
                    WitherInputPacket.handle(msg, ctx.get());
                }).add();
    }
}