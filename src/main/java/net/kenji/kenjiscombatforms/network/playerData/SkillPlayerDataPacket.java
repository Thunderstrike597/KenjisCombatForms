package net.kenji.kenjiscombatforms.network.playerData;

import net.kenji.kenjiscombatforms.api.handlers.FormChangeHandler;
import net.kenji.kenjiscombatforms.api.powers.WitherPowers.WitherFormAbility;
import net.kenji.kenjiscombatforms.item.custom.base_items.BaseFistClass;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SkillPlayerDataPacket {

    public SkillPlayerDataPacket() {}

    public SkillPlayerDataPacket(FriendlyByteBuf buf) {}

    public void encode(FriendlyByteBuf buf) {}

    public SkillPlayerDataPacket decode(FriendlyByteBuf buf) {
        return new SkillPlayerDataPacket();
    }

    public static void handle(SkillPlayerDataPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {

            ServerPlayer player = ctx.getSender();

            if (player != null) {
                player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).ifPresent(cap -> {
                    if (cap instanceof PlayerPatch<?> playerPatch) {
                        playerPatch.getSkill(SkillSlots.DODGE).setSkill(WitherFormAbility.getStoredSkill(playerPatch.getOriginal()));

                    }
                });
            }
        });
        ctx.setPacketHandled(true);
    }
}