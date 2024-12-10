package com.sindercube.mobSoup.registry.entity;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.content.entity.CenturionEntity;
import com.sindercube.mobSoup.content.entity.JavelinEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

public class ModEntityTypes {

    public static void init() {
        FabricDefaultAttributeRegistry.register(CENTURION, CenturionEntity.createAttributes());
        BiomeModifications.addSpawn(BiomeSelectors.tag(ConventionalBiomeTags.IS_UNDERGROUND), SpawnGroup.CREATURE, CENTURION, 20, 1, 3);
        SpawnRestriction.register(CENTURION, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark);
    }

    public static final EntityType<CenturionEntity> CENTURION = register("centurion",
		EntityType.Builder.create(CenturionEntity::new, SpawnGroup.MONSTER)
			.dimensions(0.6F, 1.99F)
			.eyeHeight(1.74F)
			.vehicleAttachment(-0.875F)
			.maxTrackingRange(8)
    );
	public static final EntityType<JavelinEntity> JAVELIN = register("javelin",
		EntityType.Builder.<JavelinEntity>create(JavelinEntity::new, SpawnGroup.MISC)
			.dropsNothing()
			.dimensions(0.5F, 0.5F)
			.eyeHeight(0.13F)
			.maxTrackingRange(4)
			.trackingTickInterval(20)
	);

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        Identifier id = MobSoup.of(name);
        RegistryKey<EntityType<?>> key = RegistryKey.of(RegistryKeys.ENTITY_TYPE, id);
        return Registry.register(Registries.ENTITY_TYPE, key, builder.build(key));
    }

}
