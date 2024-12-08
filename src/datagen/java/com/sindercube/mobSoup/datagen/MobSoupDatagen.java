package com.sindercube.mobSoup.datagen;

import com.sindercube.mobSoup.MobSoup;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MobSoupDatagen implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		FabricDataGenerator.Pack pack = generator.createPack();

		pack.addProvider(TranslationGenerator::new);
	}

	public static class TranslationGenerator extends FabricLanguageProvider {

		protected TranslationGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generateTranslations(RegistryWrapper.WrapperLookup lookup, TranslationBuilder builder) {
			lookup.stream().forEach(wrapper -> wrapper.streamEntries().forEach(
				reference -> {
					Optional<? extends RegistryKey<?>> maybeKey = reference.getKey();
					if (maybeKey.isEmpty()) return;
					RegistryKey<?> registryKey = maybeKey.get();
					Identifier id = registryKey.getValue();
					if (!id.getNamespace().equals(MobSoup.MOD_ID)) return;
					String key = id.toTranslationKey(registryKey.getRegistry().getPath());
					String translation = Arrays
						.stream(id.getPath().split("([_.])"))
						.map(TranslationGenerator::makeUpperCase)
						.collect(Collectors.joining(" "));
					builder.add(key, translation);
				}
			));
		}

		private static String makeUpperCase(String str) {
			return str.substring(0,1).toUpperCase() + str.substring(1);
		}

	}

}
