package com.sindercube.mobSoup.content.entity;

import com.mojang.serialization.Codec;
import com.sindercube.mobSoup.content.entity.ai.goal.JavelinAttackGoal;
import com.sindercube.mobSoup.content.entity.ai.goal.PickJavelinsFromTargetGoal;
import com.sindercube.mobSoup.content.entity.ai.goal.PickUpJavelinsGoal;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class CenturionEntity extends AbstractSkeletonEntity {

    private static final TrackedData<Boolean> ENRAGED = DataTracker.registerData(CenturionEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public double prevCapeX;
	public double prevCapeY;
	public double prevCapeZ;
	public double capeX;
	public double capeY;
	public double capeZ;

	public float prevStrideDistance;
	public float strideDistance;

    public CenturionEntity(EntityType<? extends CenturionEntity> entityType, World world) {
        super(entityType, world);
    }

    public boolean isEnraged() {
        return this.dataTracker.get(ENRAGED);
    }

    public void setEnraged(boolean enraged) {
        this.dataTracker.set(ENRAGED, enraged);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(ENRAGED, false);
    }

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new PickUpJavelinsGoal(this));
		this.targetSelector.add(3, new PickJavelinsFromTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new PickJavelinsFromTargetGoal<>(this, IronGolemEntity.class, true));
		this.goalSelector.add(3, new JavelinAttackGoal(this, 1, 60, 8));
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new LookAroundGoal(this));
		this.targetSelector.add(3, new RevengeGoal(this));
	}

	@Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("enraged", this.isEnraged());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("enraged")) this.setEnraged(nbt.getBoolean("enraged"));
    }

	@Override
	protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
		this.equipStack(EquipmentSlot.HEAD, ModItems.GALEA.getDefaultStack());
		this.equipStack(EquipmentSlot.MAINHAND, ModItems.JAVELIN.getDefaultStack().copyWithCount(3));
		this.equipStack(EquipmentSlot.OFFHAND, ModItems.SCUTUM.getDefaultStack());
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
	public boolean tryAttack(ServerWorld world, Entity target) {
		this.pickJavelins(world, target);
		return super.tryAttack(world, target);
	}

	public void pickJavelins(ServerWorld world, Entity target) {
		if (!(target instanceof LivingEntity livingTarget)) return;
		if (!(livingTarget instanceof JavelinPorcupineEntity porcupine)) return;

		if (porcupine.getStuckJavelinCount() < 0) return;

		porcupine.setStuckJavelinCount(porcupine.getStuckJavelinCount() - 1);
	}

	@Override
    public void tick() {
        super.tick();
		this.updateCapeAngles();
	}

	@Override
	public void tickRiding() {
		super.tickRiding();
		this.prevStrideDistance = this.strideDistance;
		this.strideDistance = 0.0F;
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
		this.prevStrideDistance = this.strideDistance;
		float speed = 0;
		if (this.isOnGround() && !this.isDead() && !this.isSwimming()) {
			speed = Math.min(0.1F, (float)this.getVelocity().horizontalLength());
		}
		this.strideDistance += (speed - this.strideDistance) * 0.4F;
	}

	private void updateCapeAngles() {
		this.prevCapeX = this.capeX;
		this.prevCapeY = this.capeY;
		this.prevCapeZ = this.capeZ;
		double d = this.getX() - this.capeX;
		double e = this.getY() - this.capeY;
		double f = this.getZ() - this.capeZ;
		double g = 10.0;
		if (d > 10.0) {
			this.capeX = this.getX();
			this.prevCapeX = this.capeX;
		}

		if (f > 10.0) {
			this.capeZ = this.getZ();
			this.prevCapeZ = this.capeZ;
		}

		if (e > 10.0) {
			this.capeY = this.getY();
			this.prevCapeY = this.capeY;
		}

		if (d < -10.0) {
			this.capeX = this.getX();
			this.prevCapeX = this.capeX;
		}

		if (f < -10.0) {
			this.capeZ = this.getZ();
			this.prevCapeZ = this.capeZ;
		}

		if (e < -10.0) {
			this.capeY = this.getY();
			this.prevCapeY = this.capeY;
		}

		this.capeX += d * 0.25;
		this.capeZ += f * 0.25;
		this.capeY += e * 0.25;
	}

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_SKELETON_STEP;
    }

    @Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
        super.dropEquipment(world, source, causedByPlayer);
        Entity entity = source.getAttacker();
        if (entity instanceof CreeperEntity creeperEntity) {
            if (creeperEntity.shouldDropHead()) {
                creeperEntity.onHeadDropped();
                this.dropItem(world, Items.SKELETON_SKULL);
            }
        }
    }

	public static DefaultAttributeContainer.Builder createAttributes() {
		return createAbstractSkeletonAttributes()
			.add(EntityAttributes.SCALE, 1.5F)
			.add(EntityAttributes.MAX_HEALTH, 120);
	}

	public enum AttackPhase implements StringIdentifiable {
		ATTACK_RANGED("attack_ranged"),
		ATTACK_RESTOCK("attack_restock"),
		ATTACK_ENRAGED("attack_enraged");

		public static final Codec<AttackPhase> CODEC = StringIdentifiable.createCodec(AttackPhase::values);

		private final String name;

		AttackPhase(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return this.name;
		}

	}

}
