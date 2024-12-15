package com.sindercube.mobSoup.registry;

import com.sindercube.mobSoup.MobSoup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSoundEvents {

	public static void init() {}

	public static final SoundEvent ENTITY_CENTURION_ATTACK_RANGED_READY = register("entity.centurion.attack_ranged.ready");

	private static SoundEvent register(String name) {
		Identifier id = MobSoup.of(name);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}

}
