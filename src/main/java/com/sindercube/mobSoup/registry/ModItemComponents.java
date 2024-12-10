package com.sindercube.mobSoup.registry;

import com.mojang.serialization.Codec;
import com.sindercube.mobSoup.MobSoup;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItemComponents {

	public static void init() {}

//	public static final ComponentType<NearbyAllyBuffingComponent> NEARBY_ALLY_BUFFING = register("nearby_ally_buffing", NearbyAllyBuffingComponent.CODEC);

	private static <T> ComponentType<T> register(String name, Codec<T> codec) {
		Identifier id = MobSoup.of(name);
		RegistryKey<ComponentType<?>> key = RegistryKey.of(RegistryKeys.DATA_COMPONENT_TYPE, id);
		ComponentType<T> type = ComponentType.<T>builder()
			.codec(codec)
			.packetCodec(PacketCodecs.codec(codec))
			.build();
		return Registry.register(Registries.DATA_COMPONENT_TYPE, key, type);
	}

}
