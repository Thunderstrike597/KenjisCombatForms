package net.kenji.kenjiscombatforms.event;


import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.entity.custom.noAiEntities.EnderEntity;
import net.kenji.kenjiscombatforms.network.NetworkHandler;
import net.kenji.kenjiscombatforms.network.movers.EntityRotationPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class EntityCameraMovements {

    private static boolean isFirstUpdate = true;
    private static final float YAW_SENSITIVITY = 0.15F;
    private static final float PITCH_SENSITIVITY = 0.15F;
    private static final long PACKET_SEND_INTERVAL = 25; // ms
    private static long lastPacketSendTime = 0;
    private static double lastMouseX, lastMouseY;
    private static float clientYaw, clientPitch;
    private static float serverYaw, serverPitch;
    private static float interpolationFactor = 0.2f;


    public static void handleEntityRotation(Entity entity) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.mouseHandler.isMouseGrabbed() && mc.isWindowActive()) {
            double mouseX = mc.mouseHandler.xpos();
            double mouseY = mc.mouseHandler.ypos();

            if (isFirstUpdate) {
                lastMouseX = mouseX;
                lastMouseY = mouseY;
                isFirstUpdate = false;
                return;
            }

            double dx = mouseX - lastMouseX;
            double dy = mouseY - lastMouseY;

            lastMouseX = mouseX;
            lastMouseY = mouseY;

            double sensitivity = mc.options.sensitivity().get();
            dx = applyMinecraftSensitivityCurve(dx, sensitivity);
            dy = applyMinecraftSensitivityCurve(dy, sensitivity);

            clientYaw = (clientYaw + (float) (dx * YAW_SENSITIVITY)) % 360.0f;
            clientPitch = Mth.clamp(clientPitch + (float) (dy * PITCH_SENSITIVITY), -90.0F, 90.0F);

            // Apply rotation locally for responsiveness
            applyRotation(entity, clientYaw, clientPitch);

            // Send to server periodically
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPacketSendTime > PACKET_SEND_INTERVAL) {
                NetworkHandler.INSTANCE.sendToServer(new EntityRotationPacket(entity.getUUID(), clientYaw, clientPitch));
                lastPacketSendTime = currentTime;
            }
        }
        // Interpolate between server and client rotations
        interpolateRotation(entity);
    }

    private static void interpolateRotation(Entity entity) {
        interpolationFactor = Math.min(interpolationFactor + 0.1f, 1.0f);
        float interpolatedYaw = Mth.lerp(interpolationFactor, serverYaw, clientYaw);
        float interpolatedPitch = Mth.lerp(interpolationFactor, serverPitch, clientPitch);
        applyRotation(entity, interpolatedYaw, interpolatedPitch);
    }

    private static void applyRotation(Entity entity, float yaw, float pitch) {
        entity.setYRot(yaw);
        entity.setXRot(pitch);
        entity.setYHeadRot(yaw);
    }

    public static void handleServerRotation(Entity entity, float yaw, float pitch) {
        serverYaw = yaw;
        serverPitch = pitch;
        interpolationFactor = 0.0f;
    }

    private static double applyMinecraftSensitivityCurve(double value, double sensitivity) {
        return value * (0.6F + sensitivity * 0.4F);
    }

    public static void resetRotationTracking() {
        isFirstUpdate = true;
        interpolationFactor = 1.0f;
    }
}
