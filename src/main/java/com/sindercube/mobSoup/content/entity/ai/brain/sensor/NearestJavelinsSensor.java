package com.sindercube.mobSoup.content.entity.ai.brain.sensor;

import com.google.common.collect.ImmutableSet;
import com.sindercube.mobSoup.content.entity.AbstractCenturionEntity;
import com.sindercube.mobSoup.content.entity.JavelinEntity;
import com.sindercube.mobSoup.registry.entity.ModEntityMemories;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class NearestJavelinsSensor extends Sensor<AbstractCenturionEntity> {

	protected void sense(ServerWorld world, AbstractCenturionEntity entity) {
		double range = entity.getAttributeValue(EntityAttributes.FOLLOW_RANGE);
		Box box = entity.getBoundingBox().expand(range, 8, range);
		List<JavelinEntity> list = world.getEntitiesByClass(JavelinEntity.class, box, entity::ownsProjectile);
		list.sort(Comparator.comparingDouble(entity::squaredDistanceTo));
		Brain<?> brain = entity.getBrain();
		brain.remember(ModEntityMemories.JAVELINS, list);
	}

	public Set<MemoryModuleType<?>> getOutputMemoryModules() {
		return ImmutableSet.of(MemoryModuleType.MOBS);
	}

}
