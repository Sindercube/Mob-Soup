package com.sindercube.mobSoup.client;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.registry.ModParticleFactories;
import com.sindercube.mobSoup.client.registry.ModEntityRenderers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.client.render.item.model.special.TridentModelRenderer;

public class MobSoupClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModParticleFactories.init();
		ModEntityRenderers.init();
	}

}
