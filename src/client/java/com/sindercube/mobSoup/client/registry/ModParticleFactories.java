package com.sindercube.mobSoup.client.registry;

import com.sindercube.mobSoup.client.content.particle.ConfusedParticle;
import com.sindercube.mobSoup.registry.ModParticleTypes;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticleFactories {

	public static void init() {
		ParticleFactoryRegistry.getInstance().register(ModParticleTypes.CONFUSED, ConfusedParticle.Factory::new);
	}

}
