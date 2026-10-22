package net.bl4st.elytradrag.mixin;

import net.minecraft.world.entity.ElytraAnimationState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ElytraAnimationState.class)
public class ElytraDragAnimation {
    @Shadow @Final private LivingEntity entity;
    @Shadow private float rotX;
    @Shadow private float rotY;
    @Shadow private float rotZ;

    @Inject(method = "tick", at = @At("TAIL"))
    private void elytradrag$animate(CallbackInfo ci) {
        if (!entity.isShiftKeyDown() || !entity.isFallFlying()) return;
        float speed = (float) entity.getDeltaMovement().length() * 20.0f;
        float coefficient = (float) Math.pow((Math.clamp(speed, 2.0f, 45.0f) - 2.0f) / 45.0f, 0.25);
        float cycle = (entity.tickCount * 3 % 20) / 20.0f;
        float progress = ((float) Math.sin(cycle * Math.PI * 2.0) + 1.0f) / 2.0f;
        rotX = progress * (1.0f - coefficient);
        rotY = 1.5f * coefficient;
        rotZ = -1.0f;
    }
}
