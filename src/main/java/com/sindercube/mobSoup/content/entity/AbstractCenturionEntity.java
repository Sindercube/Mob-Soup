package com.sindercube.mobSoup.content.entity;

import com.mojang.serialization.Codec;
import com.sindercube.mobSoup.content.entity.ai.goal.*;
import com.sindercube.mobSoup.registry.ModTrackedDataTypes;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCenturionEntity extends HostileEntity implements SmartRangedAttackMob {

	private static final TrackedData<Phase> PHASE = DataTracker.registerData(AbstractCenturionEntity.class, ModTrackedDataTypes.PHASE);

	private final Goal attackRangedGoal = new JavelinAttackGoal(this, 1.5, 80, 8);
	private final Goal restockGroundGoal = new PickUpJavelinsGoal(this);
	private final Goal restockTargetGoal = new PickJavelinsFromTargetGoal(this, 1.5, true);
	private final Goal attackEnragedGoal = new EnragedGoal(this, 1.5, true);

	protected AbstractCenturionEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(PHASE, Phase.ATTACK_RANGED);
	}

	@Override
	@Nullable
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData data) {
		this.initEquipment(this.random, difficulty);
		this.setPhase(Phase.ATTACK_RANGED);
		return super.initialize(world, difficulty, reason, data);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putString("phase", this.getPhase().asString());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (nbt.contains("phase")) this.setPhase(Phase.fromString(nbt.getString("phase")));
	}

	public boolean isEnraged() {
		return this.getPhase().equals(Phase.ATTACK_ENRAGED);
	}

	public Phase getPhase() {
		return this.dataTracker.get(PHASE);
	}

	public void setPhase(Phase phase) {
		this.dataTracker.set(PHASE, phase);

		this.goalSelector.remove(this.attackRangedGoal);
		this.goalSelector.remove(this.restockGroundGoal);
		this.targetSelector.remove(this.restockTargetGoal);
		this.targetSelector.remove(this.attackEnragedGoal);

		switch (phase) {
			case ATTACK_RANGED -> this.goalSelector.add(1, this.attackRangedGoal);
			case RESTOCK_GROUND -> this.goalSelector.add(1, this.restockGroundGoal);
			case RESTOCK_TARGET -> this.targetSelector.add(1, this.restockTargetGoal);
			case ATTACK_ENRAGED -> this.targetSelector.add(1, this.attackEnragedGoal);
		}
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new LookAroundGoal(this));
		this.targetSelector.add(2, new RevengeGoal(this));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
	}

	@Override
	public void shootAt(Vec3d position, float pullProgress) {
		ItemStack mainStack = this.getMainHandStack();
		ItemStack javelinStack = mainStack.isOf(ModItems.JAVELIN) ? mainStack : new ItemStack(Items.TRIDENT);
		JavelinEntity javelinEntity = new JavelinEntity(this.getWorld(), this, javelinStack);

		double x = position.x - this.getX();
		double y = position.y - javelinEntity.getY();
		double z = position.z - this.getZ();
		double distance = Math.sqrt(x * x + z * z);

		if (this.getWorld() instanceof ServerWorld world) ProjectileEntity.spawnWithVelocity(
			javelinEntity,
			world,
			javelinStack.copyWithCount(1),
			x,
			y + distance * 0.2F,
			z,
			1.6F,
			(float)(14 - this.getWorld().getDifficulty().getId() * 4)
		);
		this.playSound(SoundEvents.ENTITY_DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
//		this.getMainHandStack().decrement(1);
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
		this.playSound(this.getStepSound(), 0.15F, 1.0F);
	}

	protected abstract SoundEvent getStepSound();

	public static DefaultAttributeContainer.Builder createAttributes() {
		return HostileEntity.createHostileAttributes()
			.add(EntityAttributes.MOVEMENT_SPEED, 0.25);
	}

	@Override
	public void readyToShoot(Vec3d position) {
		this.playSound(this.getRangedAttackReadySound(), 2, 1);
	}

	protected abstract SoundEvent getRangedAttackReadySound();

	public boolean ownsProjectile(ProjectileEntity entity) {
		return entity.getOwner() != null && entity.getOwner().equals(this);
	}

	public enum Phase implements StringIdentifiable {

		ATTACK_RANGED("attack_ranged"),
		RESTOCK_GROUND("restock_ground"),
		RESTOCK_TARGET("restock_target"),
		ATTACK_ENRAGED("attack_enraged");

		public static final Codec<Phase> CODEC = StringIdentifiable.createCodec(Phase::values);

		private final String name;

		Phase(String name) {
			this.name = name;
		}

		public static Phase fromString(String name) {
			Map<String, Phase> phaseMap = new HashMap<>();
			for (Phase phase : Phase.values()) phaseMap.put(phase.asString(), phase);
			return phaseMap.get(name);
		}

		@Override
		public String asString() {
			return this.name;
		}

	}

}
