package net.kenji.kenjiscombatforms.network.particle_packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;


public class MinionSummonParticlesPacket {
    private final BlockPos blockPos;
    private final int radius;
    private final int particleCount;
    private final int lineCount;

    // Add this constructor
    public MinionSummonParticlesPacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.radius = buf.readInt();
        this.particleCount = buf.readInt();
        this.lineCount = buf.readInt();
    }

    public MinionSummonParticlesPacket(BlockPos pos, int radius, int particleCount, int lineCount) {
        this.blockPos = pos;
        this.radius = radius;
        this.particleCount = particleCount;
        this.lineCount = lineCount;
    }

    public static void encode(MinionSummonParticlesPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.blockPos);
        buf.writeInt(msg.radius);
        buf.writeInt(msg.particleCount);
        buf.writeInt(msg.lineCount);

    }

    public static MinionSummonParticlesPacket decode(FriendlyByteBuf buf) {
        return new MinionSummonParticlesPacket(buf.readBlockPos(), buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void handle(MinionSummonParticlesPacket msg, NetworkEvent.Context ctx) {
        ctx.enqueueWork(() -> {
            // This will be executed on the side that receives the packet
            if (ctx.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                handleOnClient(msg);
            }
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOnClient(MinionSummonParticlesPacket msg) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level != null) {
            spawnParticles(level, msg.blockPos, msg.radius, msg.particleCount, msg.lineCount);
        }
    }

    public static void spawnParticles(ClientLevel level, BlockPos riftPos, int radius, int particleCount, int lineCount) {
        RandomSource random = level.getRandom();
        double riftX = riftPos.getX() + 0.5;
        double riftY = riftPos.getY() + 0.5;
        double riftZ = riftPos.getZ() + 0.5;



        double flatnessFactor = 0.6;
        // Generate random lines
        for (int line = 0; line < lineCount; line++) {
            // Generate a random direction for the line, flattened based on flatnessFactor
            double theta = random.nextDouble() * 2 * Math.PI;
            double phi = (Math.PI / 2) + (random.nextDouble() - 0.5) * Math.PI * (1 - flatnessFactor);

            double dirX = Math.sin(phi) * Math.cos(theta);
            double dirY = Math.cos(phi) * (1 - flatnessFactor); // Reduce vertical component
            double dirZ = Math.sin(phi) * Math.sin(theta);

            // Normalize the direction vector
            double dirLength = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);
            dirX /= dirLength;
            dirY /= dirLength;
            dirZ /= dirLength;

            // Spawn particles along this line
            for (int i = 0; i < particleCount / lineCount; i++) {
                // Calculate position along the line
                double distance = random.nextDouble() * radius;
                double x = riftX + dirX * distance;
                double y = riftY + dirY * distance;
                double z = riftZ + dirZ * distance;

                // Calculate velocity towards the rift
                double deltaX = riftX - x;
                double deltaY = riftY - y;
                double deltaZ = riftZ - z;

                // Normalize and scale the velocity
                double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
                double speed = 0.02 + (distance / radius) * 0.05; // Particles further away move faster
                deltaX = (deltaX / length) * speed;
                deltaY = (deltaY / length) * speed;
                deltaZ = (deltaZ / length) * speed;

                // Spawn the particle
                level.addParticle(
                        ParticleTypes.LARGE_SMOKE,
                        x, y, z,
                        deltaX, deltaY, deltaZ
                );
            }
        }
    }
}