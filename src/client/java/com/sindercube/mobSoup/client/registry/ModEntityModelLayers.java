package com.sindercube.mobSoup.client.registry;

import com.sindercube.mobSoup.MobSoup;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class ModEntityModelLayers {

	public static void init() {}

	public static final EntityModelLayer CENTURION = of("centurion", "main");
	public static final EntityModelLayer CENTURION_INNER_ARMOR = of("centurion", "inner_armor");
	public static final EntityModelLayer CENTURION_OUTER_ARMOR = of("centurion", "outer_armor");
	public static final EntityModelLayer JAVELIN = of("javelin", "main");

	private static EntityModelLayer of(String name, String path) {
		return new EntityModelLayer(MobSoup.of(name), path);
	}

}
