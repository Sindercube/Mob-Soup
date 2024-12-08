package com.sindercube.mobSoup.content.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;

public class PickJavelinsFromTargetGoal<T extends LivingEntity> extends ActiveTargetGoal<T> {

	public PickJavelinsFromTargetGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
		super(mob, targetClass, checkVisibility);
	}

}
