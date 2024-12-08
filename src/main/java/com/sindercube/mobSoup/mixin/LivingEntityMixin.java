package com.sindercube.mobSoup.mixin;

import com.sindercube.mobSoup.content.entity.JavelinPorcupineEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements JavelinPorcupineEntity {

	@Unique
	private static final TrackedData<Integer> STUCK_JAVELIN_COUNT = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void afterInitDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
		builder.add(STUCK_JAVELIN_COUNT, 0);
	}

	@Override
	public int getStuckJavelinCount() {
		return this.dataTracker.get(STUCK_JAVELIN_COUNT);
	}

	@Override
	public void setStuckJavelinCount(int count) {
		this.dataTracker.set(STUCK_JAVELIN_COUNT, count);
	}

}
