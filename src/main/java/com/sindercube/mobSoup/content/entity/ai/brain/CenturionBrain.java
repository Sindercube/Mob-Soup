package com.sindercube.mobSoup.content.entity.ai.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.sindercube.mobSoup.content.entity.AbstractCenturionEntity;
import com.sindercube.mobSoup.content.entity.ai.brain.task.TridentAttackTask;
import com.sindercube.mobSoup.registry.entity.ModEntityActivities;
import com.sindercube.mobSoup.registry.entity.ModEntityMemories;
import com.sindercube.mobSoup.registry.entity.ModEntitySensors;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.mob.CreakingEntity;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.server.world.ServerWorld;

public class CenturionBrain {

	private static final ImmutableList<SensorType<? extends Sensor<? super AbstractCenturionEntity>>> SENSORS = ImmutableList.of(
		SensorType.NEAREST_LIVING_ENTITIES,
		SensorType.NEAREST_PLAYERS,
		ModEntitySensors.NEAREST_JAVELINS
	);

	private static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(
		MemoryModuleType.MOBS,
		MemoryModuleType.VISIBLE_MOBS,
		MemoryModuleType.NEAREST_VISIBLE_PLAYER,
		MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
		MemoryModuleType.LOOK_TARGET,
		MemoryModuleType.WALK_TARGET,
		MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
		MemoryModuleType.PATH,
		MemoryModuleType.ATTACK_COOLING_DOWN,
		MemoryModuleType.ATTACK_TARGET,
		ModEntityMemories.CENTURION_PHASE
	);

	private static final ImmutableList<Activity> ACTIVITIES = ImmutableList.of(
		Activity.FIGHT,
		ModEntityActivities.CENTURION_ATTACK_RANGED,
		ModEntityActivities.CENTURION_RESTOCK_GROUND,
		ModEntityActivities.CENTURION_RESTOCK_TARGET,
		ModEntityActivities.CENTURION_ATTACK_ENRAGED,
		Activity.IDLE
	);

	public static Brain.Profile<AbstractCenturionEntity> createProfile() {
		return Brain.createProfile(MEMORY_MODULES, SENSORS);
	}

	public static Brain<?> create(AbstractCenturionEntity entity, Brain<AbstractCenturionEntity> brain) {
		addCoreActivities(brain);
		addIdleActivities(brain);

		addFightTasks(entity, brain);
//		addAttackRangeTasks(entity, brain);
//		addRestockGroundTasks(brain);
//		addRestockTargetTasks(brain);
//		addAttackEnragedTasks(brain);

		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.resetPossibleActivities();
//		brain.remember(ModEntityMemories.CENTURION_PHASE, AbstractCenturionEntity.Phase.ATTACK_RANGED);
		return brain;
	}

	private static void addCoreActivities(Brain<AbstractCenturionEntity> brain) {
		brain.setTaskList(
			Activity.CORE,
			0,
			ImmutableList.of(
//	  			new StayAboveWaterTask(0.8F) {
//					protected boolean shouldRun(ServerWorld serverWorld, AbstractCenturionEntity entity) {
//						return super.shouldRun(serverWorld, entity);
//					}
//				},
				new UpdateLookControlTask(45, 90),
				new MoveToTargetTask()
			)
		);
	}

	private static void addIdleActivities(Brain<AbstractCenturionEntity> brain) {
		brain.setTaskList(
			Activity.IDLE, ImmutableList.of());
	}

	private static void addFightTasks(AbstractCenturionEntity entity, Brain<AbstractCenturionEntity> brain) {
		brain.setTaskList(
			Activity.FIGHT,
			0,
			ImmutableList.of(
				MeleeAttackTask.create(40),
//				RangedApproachTask.create(1.0F),
//				MeleeAttackTask.create(e -> false, 40),
				ForgetAttackTargetTask.create()
			),
			MemoryModuleType.ATTACK_TARGET
		);
	}

	private static void addAttackRangeTasks(AbstractCenturionEntity entity, Brain<AbstractCenturionEntity> brain) {
		brain.setTaskList(
			Activity.FIGHT,
			10,
			ImmutableList.of(
				MeleeAttackTask.create(15)
			),
			MemoryModuleType.ATTACK_TARGET
		);
	}

	private static void addRestockGroundTasks(Brain<AbstractCenturionEntity> brain) {
		brain.setTaskList(
			ModEntityActivities.CENTURION_RESTOCK_GROUND,
			ImmutableList.of(
				Pair.of(0, new TridentAttackTask<>())
			)
		);
	}

	private static void addRestockTargetTasks(Brain<AbstractCenturionEntity> brain) {
		brain.setTaskList(
			ModEntityActivities.CENTURION_RESTOCK_TARGET,
			ImmutableList.of(
				Pair.of(0, new TridentAttackTask<>())
			)
		);
	}

	private static void addAttackEnragedTasks(Brain<AbstractCenturionEntity> brain) {
		brain.setTaskList(
			ModEntityActivities.CENTURION_ATTACK_ENRAGED,
			ImmutableList.of(
				Pair.of(0, new TridentAttackTask<>())
			)
		);
	}

	public static void updateActivities(AbstractCenturionEntity entity) {
		Brain<?> brain = entity.getBrain();
		brain.resetPossibleActivities(ACTIVITIES);
		System.out.println(brain.getFirstPossibleNonCoreActivity());
		System.out.println(brain.hasActivity(Activity.IDLE));
		entity.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
	}

}
