package com.sindercube.mobSoup.content.entity;

import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class CenturionEntity extends AbstractSkeletonEntity {

    private static final TrackedData<Boolean> ENRAGED = DataTracker.registerData(CenturionEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

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
		this.equipStack(EquipmentSlot.MAINHAND, ModItems.JAVELIN.getDefaultStack());
		this.equipStack(EquipmentSlot.OFFHAND, ModItems.SCUTUM.getDefaultStack());
	}

    @Override
    public void tick() {
        super.tick();
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
			.add(EntityAttributes.MAX_HEALTH, 40);
	}

}
