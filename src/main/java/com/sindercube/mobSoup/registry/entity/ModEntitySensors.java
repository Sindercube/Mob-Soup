package com.sindercube.mobSoup.registry.entity;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.content.entity.ai.brain.sensor.NearestJavelinsSensor;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class ModEntitySensors {

	public static void init() {}

	public static final SensorType<NearestJavelinsSensor> NEAREST_JAVELINS = register("nearest_javelins", NearestJavelinsSensor::new);

	private static <U extends Sensor<?>> SensorType<U> register(String name, Supplier<U> factory) {
		Identifier id = MobSoup.of(name);
		RegistryKey<SensorType<?>> key = RegistryKey.of(RegistryKeys.SENSOR_TYPE, id);
		return Registry.register(Registries.SENSOR_TYPE, key, new SensorType<>(factory));
	}

}
