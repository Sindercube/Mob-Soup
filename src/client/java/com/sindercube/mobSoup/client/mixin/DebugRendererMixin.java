package com.sindercube.mobSoup.client.mixin;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.render.debug.GoalSelectorDebugRenderer;
import net.minecraft.client.render.debug.PathfindingDebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {

	@Shadow @Final public PathfindingDebugRenderer pathfindingDebugRenderer;
	@Shadow @Final public GoalSelectorDebugRenderer goalSelectorDebugRenderer;

	@Inject(method = "render", at = @At("TAIL"))
	public void renderDebug(MatrixStack matrices, Frustum frustum, VertexConsumerProvider.Immediate vertices, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
		pathfindingDebugRenderer.render(matrices, vertices, cameraX, cameraY, cameraZ);
		goalSelectorDebugRenderer.render(matrices, vertices, cameraX, cameraY, cameraZ);
	}

}
