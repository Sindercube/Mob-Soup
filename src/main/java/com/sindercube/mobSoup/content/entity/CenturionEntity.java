package com.sindercube.mobSoup.content.entity;

import com.sindercube.mobSoup.registry.ModItems;
import com.sindercube.mobSoup.registry.ModSoundEvents;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class CenturionEntity extends AbstractCenturionEntity {

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

	@Override
	protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
		this.equipStack(EquipmentSlot.HEAD, ModItems.GALEA.getDefaultStack());
		this.equipStack(EquipmentSlot.MAINHAND, ModItems.JAVELIN.getDefaultStack().copyWithCount(3));
		this.equipStack(EquipmentSlot.OFFHAND, ModItems.SCUTUM.getDefaultStack());
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
		double x = this.getX() - this.capeX;
		double y = this.getY() - this.capeY;
		double z = this.getZ() - this.capeZ;
		int max = 10;
		if (x > max) {
			this.capeX = this.getX();
			this.prevCapeX = this.capeX;
		}

		if (z > max) {
			this.capeZ = this.getZ();
			this.prevCapeZ = this.capeZ;
		}

		if (y > max) {
			this.capeY = this.getY();
			this.prevCapeY = this.capeY;
		}

		if (x < -max) {
			this.capeX = this.getX();
			this.prevCapeX = this.capeX;
		}

		if (z < -max) {
			this.capeZ = this.getZ();
			this.prevCapeZ = this.capeZ;
		}

		if (y < -max) {
			this.capeY = this.getY();
			this.prevCapeY = this.capeY;
		}

		this.capeX += x * 0.25;
		this.capeZ += z * 0.25;
		this.capeY += y * 0.25;
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
	protected SoundEvent getRangedAttackReadySound() {
		return ModSoundEvents.ENTITY_CENTURION_ATTACK_RANGED_READY;
	}

	@Override
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer) {
//        super.dropEquipment(world, source, causedByPlayer);
        Entity entity = source.getAttacker();
        if (entity instanceof CreeperEntity creeperEntity) {
            if (creeperEntity.shouldDropHead()) {
                creeperEntity.onHeadDropped();
				// TODO custom charged creeper drop
                this.dropItem(world, Items.SKELETON_SKULL);
            }
        }
    }

	public static DefaultAttributeContainer.Builder createAttributes() {
		return AbstractCenturionEntity.createAttributes()
			.add(EntityAttributes.SCALE, 1.5F)
			.add(EntityAttributes.MAX_HEALTH, 120);
	}

}
