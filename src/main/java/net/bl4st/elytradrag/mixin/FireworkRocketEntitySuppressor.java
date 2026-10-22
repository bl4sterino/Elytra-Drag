package net.bl4st.elytradrag.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public class FireworkRocketEntitySuppressor {
    @Shadow private LivingEntity attachedToEntity;

    // Run after vanilla resolves the attached player, but before it applies thrust.
    @Inject(method = "tick", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;isFallFlying()Z"), cancellable = true)
    private void elytradrag$stopBoost(CallbackInfo ci) {
        if (attachedToEntity.isFallFlying() && attachedToEntity.isShiftKeyDown()) {
            ((FireworkRocketEntity) (Object) this).discard();
            ci.cancel();
        }
    }
}
