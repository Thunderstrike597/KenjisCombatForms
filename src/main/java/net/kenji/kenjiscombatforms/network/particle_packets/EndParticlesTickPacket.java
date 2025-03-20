package net.kenji.kenjiscombatforms.network.particle_packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.Random;


public class EndParticlesTickPacket {
    private final double x, y, z;
    private final boolean isInvisible;

    // Add this constructor
    public EndParticlesTickPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.isInvisible = buf.readBoolean();
    }

    public EndParticlesTickPacket(double x, double y, double z, boolean isInvisible) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.isInvisible = isInvisible;
    }

    public static void encode(EndParticlesTickPacket msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeBoolean(msg.isInvisible);
    }

    public static EndParticlesTickPacket decode(FriendlyByteBuf buf) {
        return new EndParticlesTickPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readBoolean());
    }

    public static void handle(EndParticlesTickPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // This will be executed on the side that receives the packet
            if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                handleOnClient(msg);
            }
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOnClient(EndParticlesTickPacket msg) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level != null) {
            spawnParticles(level, msg.x, msg.y, msg.z);
        }
    }

    private static void spawnParticles(Level level, double x, double y, double z) {
        Random random = new Random();
        int particleCount = 10;

        for (int i = 0; i < particleCount; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2;
            double offsetY = (random.nextDouble() - 0.5) * 2;
            double offsetZ = (random.nextDouble() - 0.5) * 2;

            double velocityX = (random.nextDouble() - 0.5) * 0.5;
            double velocityY = random.nextDouble() * 0.5;
            double velocityZ = (random.nextDouble() - 0.5) * 0.5;

            level.addParticle(
                    ParticleTypes.REVERSE_PORTAL,
                    x + offsetX,
                    y + 1 + offsetY,
                    z + offsetZ,
                    velocityX,
                    velocityY,
                    velocityZ
            );
        }
    }
}