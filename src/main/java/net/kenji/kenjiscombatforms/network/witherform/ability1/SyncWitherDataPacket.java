package net.kenji.kenjiscombatforms.network.witherform.ability1;

import net.kenji.kenjiscombatforms.network.witherform.ClientWitherData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncWitherDataPacket {
    private final int cooldown;
    private final boolean isDashActive;
    private final boolean canIgnoreCollide;

    public SyncWitherDataPacket(int cooldown, boolean isDashActive, boolean canIgnoreCollide) {
        this.cooldown = cooldown;
        this.isDashActive = isDashActive;
        this.canIgnoreCollide = canIgnoreCollide;
    }

    public SyncWitherDataPacket(FriendlyByteBuf buf) {
        this.cooldown = buf.readInt();
        this.isDashActive = buf.readBoolean();
        this.canIgnoreCollide = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(cooldown);
        buf.writeBoolean(isDashActive);
        buf.writeBoolean(canIgnoreCollide);
    }

    public static void handle(SyncWitherDataPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // Update client-side data
            ClientWitherData.setCooldown(msg.cooldown);
            ClientWitherData.setIsDashActive(msg.isDashActive);
            ClientWitherData.setCanIgnoreCollide(msg.canIgnoreCollide);

        });
    }
}
