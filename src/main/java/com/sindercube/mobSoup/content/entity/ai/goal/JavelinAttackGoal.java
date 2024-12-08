package com.sindercube.mobSoup.content.entity.ai.goal;

import com.sindercube.mobSoup.content.entity.CenturionEntity;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.util.Hand;

public class JavelinAttackGoal extends ProjectileAttackGoal {

	private final CenturionEntity entity;

	public JavelinAttackGoal(RangedAttackMob entity, double mobSpeed, int intervalTicks, float maxShootRange) {
		super(entity, mobSpeed, intervalTicks, maxShootRange);
		this.entity = (CenturionEntity) entity;
	}

	public boolean canStart() {
		return super.canStart() && this.entity.getMainHandStack().isOf(ModItems.JAVELIN);
	}

	public void start() {
		super.start();
		this.entity.setAttacking(true);
		this.entity.setCurrentHand(Hand.MAIN_HAND);
	}

	public void stop() {
		super.stop();
		this.entity.clearActiveItem();
		this.entity.setAttacking(false);
	}

}
