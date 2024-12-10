package com.sindercube.mobSoup.client;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.model.renderer.JavelinModelRenderer;
import com.sindercube.mobSoup.client.registry.ModEntityModelLayers;
import com.sindercube.mobSoup.client.registry.ModParticleFactories;
import com.sindercube.mobSoup.client.registry.ModEntityRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;

public class MobSoupClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		SpecialModelTypes.ID_MAPPER.put(MobSoup.of("javelin"), JavelinModelRenderer.Unbaked.CODEC);

		ModEntityModelLayers.init();
		ModParticleFactories.init();
		ModEntityRenderers.init();
	}

}
