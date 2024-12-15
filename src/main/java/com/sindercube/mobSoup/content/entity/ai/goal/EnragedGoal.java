package com.sindercube.mobSoup.content.entity.ai.goal;

import com.sindercube.mobSoup.registry.ModParticleTypes;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class EnragedGoal extends MeleeAttackGoal {

	public EnragedGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
		super(mob, speed, pauseWhenMobIdle);
	}

	@Override
	public void start() {
		if (this.mob.getWorld() instanceof ServerWorld world) {
			Vec3d pos = this.mob.getPos();
			world.addImportantParticle(ModParticleTypes.CONFUSED, pos.x, pos.y, pos.z, 0, 0, 0);
			this.mob.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 5, 255, true, false));
		}
		super.start();
	}

}
