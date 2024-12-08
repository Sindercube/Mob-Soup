package com.sindercube.mobSoup.client;

import com.sindercube.mobSoup.client.registry.ModEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class MobSoupClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModEntityRenderers.init();
	}

}
