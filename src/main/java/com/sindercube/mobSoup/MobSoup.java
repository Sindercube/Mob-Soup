package com.sindercube.mobSoup;

import com.sindercube.mobSoup.registry.*;
import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobSoup implements ModInitializer {

	public static final String MOD_ID = "mob_soup";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier of(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModSoundEvents.init();
		ModItemComponents.init();
		ModItems.init();
		ModEntityTypes.init();
		ModTrackedDataTypes.init();
	}

}
