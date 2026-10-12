package com.example.xrayfree.mixin;

import com.example.xrayfree.Ghost;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {
	@Shadow
	protected abstract void setRotation(float yaw, float pitch);

	@Inject(
		method = "update",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V",
			shift = At.Shift.AFTER,
			ordinal = 0
		)
	)
	private void xrayfree$freelook(BlockView area, Entity focusedEntity, boolean thirdPerson,
								   boolean inverseView, float tickDelta, CallbackInfo ci) {
		if (Ghost.freelook && focusedEntity == MinecraftClient.getInstance().player) {
			this.setRotation(Ghost.camYaw, Ghost.camPitch);
		}
	}

	@Inject(method = "clipToSpace", at = @At("HEAD"), cancellable = true)
	private void xrayfree$noClip(float desiredCameraDistance, CallbackInfoReturnable<Float> cir) {
		if (Ghost.freelook && Ghost.seeThrough) {
			cir.setReturnValue(desiredCameraDistance * Ghost.CAMERA_DISTANCE_MULT);
		}
	}
}
