package net.kenji.kenjiscombatforms.render;

import com.google.gson.JsonElement;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kenji.kenjiscombatforms.KenjisCombatForms;
import net.kenji.kenjiscombatforms.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jline.utils.Log;
import org.joml.Quaternionf;
import org.lwjgl.glfw.GLFW;
import yesman.epicfight.api.animation.AnimationPlayer;
import yesman.epicfight.api.animation.Joint;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = KenjisCombatForms.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CombatDaggerRender extends RenderItemBase {
    private final ItemStack stack;
    private final boolean alwInHand;
    private static final Map<EntityType<?>, Float> HAND_INWARD_OFFSETS = new HashMap<>();

    public CombatDaggerRender(JsonElement jsonElement) {
        super(jsonElement);
        if (jsonElement.getAsJsonObject().has("combat_dagger")) {
            this.stack = new ItemStack((ItemLike) Objects.requireNonNull((Item) ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(jsonElement.getAsJsonObject().get("combat_dagger").getAsString()))));
        } else {
            this.stack = new ItemStack((ItemLike) ModItems.COMBAT_DAGGER.get());
        }
        this.alwInHand = jsonElement.getAsJsonObject().has("alwaysInHand") && GsonHelper.getAsBoolean(jsonElement.getAsJsonObject(), "alwaysInHand");
    }

    static {
        HAND_INWARD_OFFSETS.put(EntityType.PLAYER, 0F);
        HAND_INWARD_OFFSETS.put(EntityType.ZOMBIE_VILLAGER, 0.035f);
    }

    public static class ValueAdjustments {
        public float x;
        public float y;
        public float z;
    }

    public static ValueAdjustments posAdjustments = new ValueAdjustments();
    public static ValueAdjustments rotAdjustments = new ValueAdjustments();


    @Override
    public void renderItemInHand(ItemStack stack, LivingEntityPatch<?> entitypatch, InteractionHand hand, OpenMatrix4f[] poses, MultiBufferSource buffer, PoseStack poseStack, int packedLight, float partialTicks) {
        Joint handJoint = hand == InteractionHand.MAIN_HAND ? Armatures.BIPED.get().handR : Armatures.BIPED.get().handL;
        Joint toolJoint = hand == InteractionHand.MAIN_HAND ? Armatures.BIPED.get().toolR : Armatures.BIPED.get().toolL;

            poseStack.pushPose();


            OpenMatrix4f handPose = poses[handJoint.getId()];
            OpenMatrix4f toolPose = poses[toolJoint.getId()];

            AnimationPlayer animPlayer = entitypatch.getAnimator().getPlayerFor(null);
            boolean isAttacking = animPlayer != null && animPlayer.getAnimation().get() instanceof AttackAnimation;

            OpenMatrix4f modelMatrix;


            if (isAttacking) {
                modelMatrix = mixBoneMatrices(handPose, toolPose);
            } else {
                modelMatrix = new OpenMatrix4f(poses[handJoint.getId()]);
            }

            MathUtils.mulStack(poseStack, modelMatrix);

            if (!isAttacking) {
                poseStack.mulPose(new Quaternionf().rotateZ((float) Math.toRadians(180)));
                poseStack.translate(0.4, 0.24, 0.3);
            }
            else {
                poseStack.translate(0.4, 0.3, 0.2);
            }


            Minecraft.getInstance().getItemRenderer().renderStatic(
                    this.stack,
                    hand == InteractionHand.MAIN_HAND ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.THIRD_PERSON_LEFT_HAND,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    null,
                    0
            );
            poseStack.popPose();

    }

    private OpenMatrix4f mixBoneMatrices(OpenMatrix4f handPose, OpenMatrix4f toolPose) {
        OpenMatrix4f result = new OpenMatrix4f();

        // Translation (position) from hand bone - column 3
        result.m30 = handPose.m30;
        result.m31 = handPose.m31;
        result.m32 = handPose.m32;
        result.m33 = handPose.m33;

        // Rotation from tool bone - upper-left 3x3
        // But we need to strip scale from tool and apply hand's scale
        // First get scale from hand bone
        float scaleX = (float) Math.sqrt(handPose.m00 * handPose.m00 + handPose.m01 * handPose.m01 + handPose.m02 * handPose.m02);
        float scaleY = (float) Math.sqrt(handPose.m10 * handPose.m10 + handPose.m11 * handPose.m11 + handPose.m12 * handPose.m12);
        float scaleZ = (float) Math.sqrt(handPose.m20 * handPose.m20 + handPose.m21 * handPose.m21 + handPose.m22 * handPose.m22);

        // Get tool bone rotation column lengths (its scale)
        float tScaleX = (float) Math.sqrt(toolPose.m00 * toolPose.m00 + toolPose.m01 * toolPose.m01 + toolPose.m02 * toolPose.m02);
        float tScaleY = (float) Math.sqrt(toolPose.m10 * toolPose.m10 + toolPose.m11 * toolPose.m11 + toolPose.m12 * toolPose.m12);
        float tScaleZ = (float) Math.sqrt(toolPose.m20 * toolPose.m20 + toolPose.m21 * toolPose.m21 + toolPose.m22 * toolPose.m22);

        // Normalize tool rotation then apply hand scale
        result.m00 = (toolPose.m00 / tScaleX) * scaleX;
        result.m01 = (toolPose.m01 / tScaleX) * scaleX;
        result.m02 = (toolPose.m02 / tScaleX) * scaleX;

        result.m10 = (toolPose.m10 / tScaleY) * scaleY;
        result.m11 = (toolPose.m11 / tScaleY) * scaleY;
        result.m12 = (toolPose.m12 / tScaleY) * scaleY;

        result.m20 = (toolPose.m20 / tScaleZ) * scaleZ;
        result.m21 = (toolPose.m21 / tScaleZ) * scaleZ;
        result.m22 = (toolPose.m22 / tScaleZ) * scaleZ;

        return result;
    }

    public static void LogPosAdjustments() {
        Log.info("X: " + posAdjustments.x + " | Y: " + posAdjustments.y + " | Z: " + posAdjustments.z);
    }

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event) {
        // Get the player's currently selected hotbar slot
        UUID uuid = Minecraft.getInstance().getUser().getProfileId();
        Player player = Minecraft.getInstance().player;

        if(event.getModifiers() != GLFW.GLFW_MOD_SHIFT) {
            if (event.getKey() == GLFW.GLFW_KEY_RIGHT) {
                posAdjustments.x += 0.05F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_LEFT) {
                posAdjustments.x -= 0.05F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_UP) {
                posAdjustments.y += 0.05F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_DOWN) {
                posAdjustments.y -= 0.05F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_KP_ADD) {
                posAdjustments.z += 0.05F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_KP_SUBTRACT) {
                posAdjustments.z -= 0.05F;
                LogPosAdjustments();
            }
        }
        else{
            if (event.getKey() == GLFW.GLFW_KEY_RIGHT) {
                rotAdjustments.x += 0.5F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_LEFT) {
                rotAdjustments.x -= 0.5F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_UP) {
                rotAdjustments.y += 0.5F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_DOWN) {
                rotAdjustments.y -= 0.5F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_KP_ADD) {
                rotAdjustments.z += 0.5F;
                LogPosAdjustments();
            }
            if (event.getKey() == GLFW.GLFW_KEY_KP_SUBTRACT) {
                rotAdjustments.z -= 0.5F;
                LogPosAdjustments();
            }
        }
    }
}
