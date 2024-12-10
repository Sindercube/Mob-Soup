package com.sindercube.mobSoup.registry.entity;

import com.mojang.serialization.Codec;
import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.content.entity.AbstractCenturionEntity;
import com.sindercube.mobSoup.content.entity.JavelinEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

public class ModEntityMemories {

	public static void init() {}

	public static final MemoryModuleType<AbstractCenturionEntity.Phase> CENTURION_PHASE = register("centurion_phase", AbstractCenturionEntity.Phase.CODEC);
	public static final MemoryModuleType<List<JavelinEntity>> JAVELINS = register("mobs");

	private static <U> MemoryModuleType<U> register(String name, Codec<U> codec) {
		Identifier id = MobSoup.of(name);
		RegistryKey<MemoryModuleType<?>> key = RegistryKey.of(RegistryKeys.MEMORY_MODULE_TYPE, id);
		return Registry.register(Registries.MEMORY_MODULE_TYPE, key, new MemoryModuleType<>(Optional.of(codec)));
	}

	private static <U> MemoryModuleType<U> register(String name) {
		Identifier id = MobSoup.of(name);
		RegistryKey<MemoryModuleType<?>> key = RegistryKey.of(RegistryKeys.MEMORY_MODULE_TYPE, id);
		return Registry.register(Registries.MEMORY_MODULE_TYPE, key, new MemoryModuleType<>(Optional.empty()));
	}

}
