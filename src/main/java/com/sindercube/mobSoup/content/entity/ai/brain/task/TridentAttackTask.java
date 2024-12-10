package com.sindercube.mobSoup.content.entity.ai.brain.task;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.MultiTickTask;
import net.minecraft.entity.ai.brain.task.TargetUtil;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;

public class TridentAttackTask<T extends MobEntity> extends MultiTickTask<T> {

	private static final Map<MemoryModuleType<?>, MemoryModuleState> MEMORY_MODULES = ImmutableMap.of(
		MemoryModuleType.LOOK_TARGET,
		MemoryModuleState.REGISTERED,
		MemoryModuleType.ATTACK_TARGET,
		MemoryModuleState.VALUE_PRESENT
	);

	public TridentAttackTask() {
		super(MEMORY_MODULES, 1200);
	}

	@Override
	protected boolean shouldRun(ServerWorld serverWorld, T entity) {
		System.out.println("test");
		LivingEntity target = getAttackTarget(entity);
		return entity.isHolding(ModItems.JAVELIN)
			&& TargetUtil.isVisibleInMemory(entity, target)
			&& TargetUtil.isTargetWithinAttackRange(entity, target, 0);
	}

	@Override
	protected boolean shouldKeepRunning(ServerWorld serverWorld, T entity, long time) {
		return entity.getBrain().hasMemoryModule(MemoryModuleType.ATTACK_TARGET) && this.shouldRun(serverWorld, entity);
	}

	@Override
	protected void keepRunning(ServerWorld serverWorld, T entity, long time) {
		LivingEntity target = getAttackTarget(entity);
		this.setLookTarget(entity, target);
		this.tickState(entity, target);
	}

	@Override
	protected void finishRunning(ServerWorld serverWorld, T entity, long time) {
		if (entity.isUsingItem()) entity.clearActiveItem();
	}

	private void tickState(T entity, LivingEntity target) {
//		if (this.state == CrossbowAttackTask.CrossbowState.UNCHARGED) {
//			entity.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(entity, Items.CROSSBOW));
//			this.state = CrossbowAttackTask.CrossbowState.CHARGING;
//			((CrossbowUser)entity).setCharging(true);
//		} else if (this.state == CrossbowAttackTask.CrossbowState.CHARGING) {
//			if (!entity.isUsingItem()) {
//				this.state = CrossbowAttackTask.CrossbowState.UNCHARGED;
//			}
//
//			int i = entity.getItemUseTime();
//			ItemStack itemStack = entity.getActiveItem();
//			if (i >= CrossbowItem.getPullTime(itemStack, entity)) {
//				entity.stopUsingItem();
//				this.state = CrossbowAttackTask.CrossbowState.CHARGED;
//				this.chargingCooldown = 20 + entity.getRandom().nextInt(20);
//				((CrossbowUser)entity).setCharging(false);
//			}
//		} else if (this.state == CrossbowAttackTask.CrossbowState.CHARGED) {
//			--this.chargingCooldown;
//			if (this.chargingCooldown == 0) {
//				this.state = CrossbowAttackTask.CrossbowState.READY_TO_ATTACK;
//			}
//		} else if (this.state == CrossbowAttackTask.CrossbowState.READY_TO_ATTACK) {
//			((RangedAttackMob)entity).shootAt(target, 1.0F);
//			this.state = CrossbowAttackTask.CrossbowState.UNCHARGED;
//		}
	}

	private void setLookTarget(T entity, LivingEntity target) {
		entity.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(target, true));
	}

	private static LivingEntity getAttackTarget(LivingEntity entity) {
		return entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
	}

}
