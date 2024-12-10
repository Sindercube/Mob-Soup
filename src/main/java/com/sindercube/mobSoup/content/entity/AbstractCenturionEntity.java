package com.sindercube.mobSoup.content.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.sindercube.mobSoup.content.entity.ai.brain.CenturionBrain;
import com.sindercube.mobSoup.registry.entity.ModEntityActivities;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractCenturionEntity extends HostileEntity implements RangedAttackMob {

	protected AbstractCenturionEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	@Nullable
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData data) {
		this.initEquipment(random, difficulty);
		return super.initialize(world, difficulty, reason, data);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
	}

	protected Brain.Profile<AbstractCenturionEntity> createBrainProfile() {
		return CenturionBrain.createProfile();
	}

	@Override
	protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
		return CenturionBrain.create(this, this.createBrainProfile().deserialize(dynamic));
	}

	@Override
	protected void mobTick(ServerWorld world) {
		Profiler profiler = Profilers.get();
		profiler.push("centurion_brain");
		Brain<AbstractCenturionEntity> brain = (Brain<AbstractCenturionEntity>)this.getBrain();
		brain.tick((ServerWorld) this.getWorld(), this);
		profiler.pop();
		profiler.push("centurion_activity_update");
		CenturionBrain.updateActivities(this);
		profiler.pop();
		super.mobTick(world);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
	}

	public boolean isEnraged() {
		return this.getBrain().hasActivity(ModEntityActivities.CENTURION_ATTACK_ENRAGED);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new LookAroundGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
	}

	@Override
	public void shootAt(LivingEntity target, float pullProgress) {
		ItemStack mainStack = this.getMainHandStack();
		ItemStack javelinStack = mainStack.isOf(ModItems.JAVELIN) ? mainStack : new ItemStack(Items.TRIDENT);
		JavelinEntity javelinEntity = new JavelinEntity(this.getWorld(), this, javelinStack);

		double x = target.getX() - this.getX();
		double y = target.getBodyY(0.3333333333333333) - javelinEntity.getY();
		double z = target.getZ() - this.getZ();
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
		this.getMainHandStack().decrement(1);
	}

	@Override
	@Nullable
	public LivingEntity getTarget() {
		return this.getTargetInBrain();
	}

	@Override
	protected void sendAiDebugData() {
		super.sendAiDebugData();
		DebugInfoSender.sendBrainDebugData(this);
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

	public boolean ownsProjectile(ProjectileEntity entity) {
		return entity.getOwner() != null && entity.getOwner().equals(this);
	}

	public enum Phase implements StringIdentifiable {

		ATTACK_RANGED("attack_ranged"),
		RESTOCK_GROUND("restock_ground"),
		RESTOCK_TARGET("restock_target"),
//		ATTACK_ENRAGED("attack_enraged", AbstractCenturionEntity.Phase::changeToEnraged);
		ATTACK_ENRAGED("attack_enraged");

//		public static void changeToEnraged(AbstractCenturionEntity entity) {
//			World world = entity.getWorld();
//			if (world.isClient) return;
//
//			if (entity.getMainHandStack().isEmpty()) entity.giveOrDropStack(Items.IRON_SWORD.getDefaultStack());
//
//			Vec3d pos = entity.getPos();
//			world.addParticle(ModParticleTypes.CONFUSED, pos.x, pos.y + entity.getEyeY() + (entity.getHeight() / 4), pos.z, 0, 0, 0);
//		}

		public static final Codec<Phase> CODEC = StringIdentifiable.createCodec(Phase::values);

		private final String name;
//		public final Consumer<AbstractCenturionEntity> changeFunction;

		Phase(String name) {
			this.name = name;
		}

//		Phase(String name) {
//			this(name, i -> {});
//		}

//		Phase(String name, Consumer<AbstractCenturionEntity> changeFunction) {
//			this.name = name;
//			this.changeFunction = changeFunction;
//		}

		@Override
		public String asString() {
			return this.name;
		}

	}

}
