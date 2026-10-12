package com.example.xrayfree.mixin;

import com.example.xrayfree.Ghost;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
	private void xrayfree$freelook(double dx, double dy, CallbackInfo ci) {
		if (Ghost.freelook && (Object) this == MinecraftClient.getInstance().player) {
			Ghost.camYaw += (float) dx * 0.15f;
			Ghost.camPitch = MathHelper.clamp(Ghost.camPitch + (float) dy * 0.15f, -90.0f, 90.0f);
			ci.cancel();
		}
	}
}
