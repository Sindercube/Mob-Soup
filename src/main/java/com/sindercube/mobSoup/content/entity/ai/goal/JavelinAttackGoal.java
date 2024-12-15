package com.sindercube.mobSoup.content.entity.ai.goal;

import com.sindercube.mobSoup.content.entity.AbstractCenturionEntity;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.util.Hand;

public class JavelinAttackGoal extends SmartProjectileAttackGoal<AbstractCenturionEntity> {

	public JavelinAttackGoal(AbstractCenturionEntity entity, double mobSpeed, int intervalTicks, float maxShootRange) {
		super(entity, mobSpeed, intervalTicks, maxShootRange);
	}

	@Override
	public boolean canStart() {
		boolean hasJavelin = this.entity.getMainHandStack().isOf(ModItems.JAVELIN);
		if (!hasJavelin) {
			this.entity.setPhase(AbstractCenturionEntity.Phase.ATTACK_ENRAGED);
			return false;
		}
		return super.canStart();
	}

	@Override
	public boolean shouldContinue() {
		return this.canStart();
	}

	@Override
	public void stop() {
		super.stop();
		if (this.entity.getMainHandStack().isEmpty()) {
			this.entity.setTarget(null);
			this.entity.setPhase(AbstractCenturionEntity.Phase.RESTOCK_GROUND);
		}
	}

}
