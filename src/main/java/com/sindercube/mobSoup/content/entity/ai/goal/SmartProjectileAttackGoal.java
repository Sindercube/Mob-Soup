package com.sindercube.mobSoup.content.entity.ai.goal;

import com.sindercube.mobSoup.content.entity.SmartRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class SmartProjectileAttackGoal<T extends MobEntity & SmartRangedAttackMob> extends Goal {

	protected final T entity;
	@Nullable private LivingEntity target;
	private final double mobSpeed;
	private int seenTargetTicks;
	private final int attackInterval;
	private final float maxShootRange;
	private final float squaredMaxShootRange;

	@Nullable private Vec3d predictedLocation;

	public SmartProjectileAttackGoal(T entity, double mobSpeed, int attackInterval, float maxShootRange) {
		this.entity = entity;
		this.mobSpeed = mobSpeed;
		this.attackInterval = attackInterval;
		this.maxShootRange = maxShootRange;
		this.squaredMaxShootRange = maxShootRange * maxShootRange;
		this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
	}

	public boolean canStart() {
		LivingEntity livingEntity = this.entity.getTarget();
		if (livingEntity != null && livingEntity.isAlive()) {
			this.target = livingEntity;
			return true;
		} else {
			return false;
		}
	}

	public boolean shouldContinue() {
		return this.canStart() || this.target != null && this.target.isAlive() && !this.entity.getNavigation().isIdle();
	}

	@Override
	public void start() {
		this.entity.setAttacking(true);
		this.entity.setCurrentHand(Hand.MAIN_HAND);
	}

	public void stop() {
		this.entity.clearActiveItem();
		this.entity.setAttacking(false);
		this.target = null;
		this.seenTargetTicks = 0;
	}

	public boolean shouldRunEveryTick() {
		return true;
	}

	public void tick() {
		if (this.target == null) {
			this.stop();
			return;
		}

		float distance = this.entity.distanceTo(this.target);
		double squaredDistance = this.entity.squaredDistanceTo(this.target.getPos());
		boolean visible = this.entity.getVisibilityCache().canSee(this.target);

		if (visible) ++this.seenTargetTicks;
		else this.seenTargetTicks = 0;

		if (!(squaredDistance > (double)this.squaredMaxShootRange) && this.seenTargetTicks >= 5) {
			this.entity.getNavigation().stop();
		} else {
			this.entity.getNavigation().startMovingTo(this.target, this.mobSpeed);
		}

		if (visible && this.target.isInRange(this.entity, this.maxShootRange + 2)) {
			entity.getMoveControl().strafeTo((float) -this.mobSpeed, 0.0F);
		} else if (visible && this.target.isInRange(this.entity, this.maxShootRange - 2)) {
			entity.getMoveControl().strafeTo((float) this.mobSpeed, 0.0F);
		}

		if (this.predictedLocation != null) {
			this.entity.getLookControl().lookAt(this.predictedLocation.add(0, this.target.getHeight(), 0));
		} else if (visible) {
			this.entity.getLookControl().lookAt(this.target, 30, 30);
		}

		if (this.entity.getItemUseTime() == this.attackInterval) {
			this.predictedLocation = this.predictLocation(this.target, distance, 10);
			this.entity.readyToShoot(this.predictedLocation);
		}
		else if (this.entity.getItemUseTime() == this.attackInterval + 10) {
			if (this.predictedLocation != null) {
				this.entity.shootAt(this.predictedLocation, 1);
				this.predictedLocation = null;
			}
			this.entity.stopUsingItem();
			this.entity.setCurrentHand(Hand.MAIN_HAND);
		}
	}

	public Vec3d predictLocation(LivingEntity entity, double distance, int time) {
		System.out.println(distance);
		Vec3d velocity;
		if (entity instanceof PlayerEntity player) velocity = player.getMovement();
		else velocity = entity.getVelocity();

		velocity = velocity.multiply(time * (distance / 4));
		System.out.printf("Velocity: %s\n", velocity);
		System.out.printf("Position: %s\n", entity.getPos());
		System.out.printf("Pred Pos: %s\n", entity.getPos().add(velocity));
		System.out.println(entity.getPos());
		System.out.println(entity.getPos().add(velocity));
		return entity.getPos().add(velocity);
	}

}
