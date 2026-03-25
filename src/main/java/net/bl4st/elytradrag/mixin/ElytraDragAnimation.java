package net.bl4st.elytradrag.mixin;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraEntityModel.class)
public class ElytraDragAnimation {
    @Final
    @Shadow
    private ModelPart rightWing;
    @Unique
    LivingEntity spe;
    @Final
    @Shadow
    private ModelPart leftWing;
    @Unique
    float playerVelocity;
    @Unique
    float playerSpeed = playerVelocity * 20.0f;

    public ElytraDragAnimation() {
        playerVelocity = (float) spe.getVelocity().length();
    }

    /**
     * Scuffed way to handle some kind of animation,
     * it just works™
     */
    @Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/BipedEntityRenderState;)V", at = @At("TAIL"))
    private void SetAngles(BipedEntityRenderState bipedEntityRenderState, CallbackInfo ci) {
        if (!bipedEntityRenderState.isInSneakingPose || !bipedEntityRenderState.isGliding)
            return;
        long FLAPPING_SPEED = 3L;
        float progressCycle = (float)(System.currentTimeMillis() * FLAPPING_SPEED % 1000L) / 1000.0F;
        float progress = ((float)Math.sin(progressCycle * Math.PI * 2.0D) + 1.0F) / 2.0F;
        float animSpeedCoef = GetSpeedAnimationCoef(playerSpeed * 20.0F);
        this.rightWing.yaw = -1.5f * animSpeedCoef;
        this.leftWing.yaw = 1.5f * animSpeedCoef;
        this.rightWing.pitch = progress * (1f - animSpeedCoef);
        this.leftWing.pitch = progress * (1f - animSpeedCoef);

        this.rightWing.roll = 1f;
        this.leftWing.roll = -1f;
    }

    @Unique
    private float GetSpeedAnimationCoef(float value) {
        value = Math.max(2.0F, Math.min(45.0F, value));
        return (float)Math.pow(((value - 2.0F) / 45.0F), 0.25D);
    }
}
