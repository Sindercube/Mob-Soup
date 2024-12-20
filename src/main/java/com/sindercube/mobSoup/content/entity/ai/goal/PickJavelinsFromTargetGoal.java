package com.sindercube.mobSoup.content.entity.ai.goal;

import com.sindercube.mobSoup.content.entity.AbstractCenturionEntity;
import com.sindercube.mobSoup.content.entity.JavelinPorcupineEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class PickJavelinsFromTargetGoal extends MeleeAttackGoal {

	private final AbstractCenturionEntity entity;

	public PickJavelinsFromTargetGoal(AbstractCenturionEntity entity, double speed, boolean pauseWhenMobIdle) {
		super(entity, speed, pauseWhenMobIdle);
		this.entity = entity;
	}

	public void pickJavelins(Entity target) {
		if (!(target instanceof LivingEntity livingTarget)) return;
		if (!(livingTarget instanceof JavelinPorcupineEntity porcupine)) return;

		if (porcupine.getStuckJavelinCount() <= 0) {
			this.entity.setPhase(AbstractCenturionEntity.Phase.ATTACK_RANGED);
			return;
		}

		porcupine.setStuckJavelinCount(porcupine.getStuckJavelinCount() - 1);
	}

}
