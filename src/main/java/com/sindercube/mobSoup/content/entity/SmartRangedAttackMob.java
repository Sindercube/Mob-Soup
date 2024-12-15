package com.sindercube.mobSoup.content.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.util.math.Vec3d;

public interface SmartRangedAttackMob extends RangedAttackMob {

	void shootAt(Vec3d position, float pullProgress);

	@Override
	default void shootAt(LivingEntity target, float pullProgress) {
		Vec3d position = new Vec3d(
			target.getX(),
			target.getBodyY(0.333),
			target.getZ()
		);
		this.shootAt(position, pullProgress);
	}

	void readyToShoot(Vec3d position);

}
