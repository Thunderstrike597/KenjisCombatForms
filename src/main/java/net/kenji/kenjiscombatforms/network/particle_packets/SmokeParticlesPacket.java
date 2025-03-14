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


public class SmokeParticlesPacket {
    private final double x, y, z;

    // Add this constructor
    public SmokeParticlesPacket(FriendlyByteBuf buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public SmokeParticlesPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void encode(SmokeParticlesPacket msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.x);
        buf.writeDouble(msg.y);
        buf.writeDouble(msg.z);
    }

    public static SmokeParticlesPacket decode(FriendlyByteBuf buf) {
        return new SmokeParticlesPacket(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static void handle(SmokeParticlesPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // This will be executed on the side that receives the packet
            if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                handleOnClient(msg);
            }
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOnClient(SmokeParticlesPacket msg) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level != null) {
            spawnParticles(level, msg.x, msg.y, msg.z);
        }
    }

    private static void spawnParticles(Level level, double x, double y, double z) {
        Random random = new Random();
        int particleCount = 100;
        for (int i = 0; i < particleCount; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 2; // Spread in X direction
            double offsetY = (random.nextDouble() - 0.5) * 2; // Spread in Y direction
            double offsetZ = (random.nextDouble() - 0.5) * 2; // Spread in Z direction

            double velocityX = (random.nextDouble() - 0.5) * 0.5; // Random X velocity
            double velocityY = random.nextDouble() * 0.5; // Upward Y velocity
            double velocityZ = (random.nextDouble() - 0.5) * 0.5; // Random Z velocity

            level.addParticle(
                    ParticleTypes.SMOKE,
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