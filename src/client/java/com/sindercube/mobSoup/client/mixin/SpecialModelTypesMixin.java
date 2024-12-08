package com.sindercube.mobSoup.client.mixin;

import com.mojang.serialization.MapCodec;
import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.model.renderer.JavelinModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpecialModelTypes.class)
public class SpecialModelTypesMixin {

	@Shadow @Final public static Codecs.IdMapper<Identifier, MapCodec<? extends SpecialModelRenderer.Unbaked>> ID_MAPPER;

	@Inject(method = "bootstrap", at = @At("TAIL"))
	private static void afterBoostrap(CallbackInfo ci) {
		ID_MAPPER.put(MobSoup.of("javelin"), JavelinModelRenderer.Unbaked.CODEC);
	}

}
