package com.sindercube.mobSoup.registry;

import com.sindercube.mobSoup.MobSoup;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModParticleTypes {

	public static void init() {}

	public static final SimpleParticleType CONFUSED = register("confused", false);

	private static SimpleParticleType register(String name, boolean alwaysShow) {
		return Registry.register(Registries.PARTICLE_TYPE, MobSoup.of(name), FabricParticleTypes.simple(alwaysShow));
	}

}
