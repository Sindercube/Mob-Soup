package com.sindercube.mobSoup.registry;

import com.mojang.serialization.Codec;
import com.sindercube.mobSoup.content.entity.AbstractCenturionEntity;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.codec.PacketCodecs;

public class ModTrackedDataTypes {

	public static void init() {
		TrackedDataHandlerRegistry.register(PHASE);
	}

	public static final TrackedDataHandler<AbstractCenturionEntity.Phase> PHASE = create(AbstractCenturionEntity.Phase.CODEC);

	private static <T> TrackedDataHandler<T> create(Codec<T> codec) {
		return TrackedDataHandler.create(PacketCodecs.codec(codec));
	}

}
