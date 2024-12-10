package com.sindercube.mobSoup.registry.entity;

import com.sindercube.mobSoup.MobSoup;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntityActivities {

	public static void init() {}

	public static final Activity CENTURION_ATTACK_RANGED = register("centurion/attack_ranged");
	public static final Activity CENTURION_RESTOCK_GROUND = register("centurion/restock_ground");
	public static final Activity CENTURION_RESTOCK_TARGET = register("centurion/restock_target");
	public static final Activity CENTURION_ATTACK_ENRAGED = register("centurion/attack_enraged");

	private static Activity register(String name) {
		Identifier id = MobSoup.of(name);
		RegistryKey<Activity> key = RegistryKey.of(RegistryKeys.ACTIVITY, id);
		return Registry.register(Registries.ACTIVITY, key, new Activity(id.toString()));
	}

}
