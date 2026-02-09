package net.bl4st.elytradrag.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin (FireworkRocketEntity.class)
public class FireworkRocketEntitySuppressor {

    @Shadow
    private int lifeTime;

    @Shadow
    private LivingEntity shooter;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci)
    {
        if(shooter != null)
        {
            if(shooter.isGliding() && shooter.isSneaking())
                lifeTime = 0;
        }
    }
}

