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


public class LargeSmokeParticlesTickPacket {
    private final double x, y, z;
    private final boolean isInvisible;

    // Add this constructor
    public LargeSmokeParticlesTickPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.isInvisible = buf.readBoolean();
    }

    public LargeSmokeParticlesTickPacket(double x, double y, double z, boolean isInvisible) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.isInvisible = isInvisible;
    }

    public static void encode(LargeSmokeParticlesTickPacket msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
        buf.writeBoolean(msg.isInvisible);
    }

    public static LargeSmokeParticlesTickPacket decode(FriendlyByteBuf buf) {
        return new LargeSmokeParticlesTickPacket(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readBoolean());
    }

    public static void handle(LargeSmokeParticlesTickPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // This will be executed on the side that receives the packet
            if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                handleOnClient(msg);
            }
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOnClient(LargeSmokeParticlesTickPacket msg) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level != null && msg.isInvisible) {
            spawnParticles(level, msg.x, msg.y, msg.z);
        }
    }

    private static void spawnParticles(Level level, double x, double y, double z) {
        Random random = new Random();
        int particleCount = 10;

        for (int i = 0; i < particleCount; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2; // Spread in X direction
            double offsetY = (random.nextDouble() - 0.5) * 2; // Spread in Y direction
            double offsetZ = (random.nextDouble() - 0.5) * 2; // Spread in Z direction

            double velocityX = 0.05; // Random X velocity
            double velocityY = 0.0; // Upward Y velocity
            double velocityZ = 0.05; // Random Z velocity

            level.addParticle(
                    ParticleTypes.LARGE_SMOKE,
                    x + offsetX,
                    y + offsetY,
                    z + offsetZ,
                    velocityX,
                    velocityY,
                    velocityZ
            );
        }
    }
}