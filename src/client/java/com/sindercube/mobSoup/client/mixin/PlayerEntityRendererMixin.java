package com.sindercube.mobSoup.client.mixin;

import com.sindercube.mobSoup.client.content.entity.renderer.StuckJavelinsFeatureRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel> {

	public PlayerEntityRendererMixin(EntityRendererFactory.Context c, PlayerEntityModel m, float s) {
		super(c, m, s);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	public void afterInit(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
		this.addFeature(new StuckJavelinsFeatureRenderer<>(this, ctx));
	}

}
