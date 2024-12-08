package com.sindercube.mobSoup.content.entity;

import com.sindercube.mobSoup.registry.ModEntityTypes;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class JavelinEntity extends PersistentProjectileEntity {

	private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(JavelinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);;
	private boolean dealtDamage;

	public JavelinEntity(EntityType<? extends JavelinEntity> entityType, World world) {
		super(entityType, world);
	}

	public JavelinEntity(World world, LivingEntity owner, ItemStack stack) {
		super(ModEntityTypes.JAVELIN, owner, world, stack, null);
		this.dataTracker.set(ENCHANTED, stack.hasGlint());
	}

	public JavelinEntity(World world, double x, double y, double z, ItemStack stack) {
		super(ModEntityTypes.JAVELIN, x, y, z, world, stack, stack);
		this.dataTracker.set(ENCHANTED, stack.hasGlint());
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(ENCHANTED, false);
	}

	@Override
	public void tick() {
		if (this.inGroundTime > 4) {
			this.dealtDamage = true;
		}
		super.tick();
	}

	public boolean isEnchanted() {
		return this.dataTracker.get(ENCHANTED);
	}

	@Nullable
	@Override
	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity target = entityHitResult.getEntity();
		float damage = 8.0F;
		Entity owner = this.getOwner();
		DamageSource source = this.getDamageSources().trident(this, (Entity)(owner == null ? this : owner));
		World world = this.getWorld();
		if (world instanceof ServerWorld serverWorld)
			damage = EnchantmentHelper.getDamage(serverWorld, this.getWeaponStack(), target, source, damage);

		this.dealtDamage = true;
		if (target.sidedDamage(source, damage)) {
			if (target.getType() == EntityType.ENDERMAN) return;

			if (world instanceof ServerWorld serverWorld) {
				EnchantmentHelper.onTargetDamaged(serverWorld, target, source, this.getWeaponStack(), (item) -> {
					this.kill(serverWorld);
				});
			}

			if (target instanceof LivingEntity livingEntity) {
				this.knockback(livingEntity, source);
				this.onHit(livingEntity);
			}
		}

		this.deflect(ProjectileDeflection.SIMPLE, target, this.getOwner(), false);
		this.setVelocity(this.getVelocity().multiply(0.02, 0.2, 0.02));
		this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
	}

	@Override
	protected void onBlockHitEnchantmentEffects(ServerWorld world, BlockHitResult result, ItemStack stack) {
		Vec3d pos = result.getBlockPos().clampToWithin(result.getPos());
		LivingEntity owner = this.getOwner() instanceof LivingEntity livingEntity ? livingEntity : null;
		EnchantmentHelper.onHitBlock(world, stack, owner, this, null, pos, world.getBlockState(result.getBlockPos()), item ->
			this.kill(world)
		);
	}

	@Override
	public ItemStack getWeaponStack() {
		return this.getItemStack();
	}

	@Override
	protected boolean tryPickup(PlayerEntity player) {
		return super.tryPickup(player) || this.isNoClip() && this.isOwner(player) && player.getInventory().insertStack(this.asItemStack());
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return ModItems.JAVELIN.getDefaultStack();
	}

	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
	}

	@Override
	public void onPlayerCollision(PlayerEntity player) {
		if (this.isOwner(player) || this.getOwner() == null) {
			super.onPlayerCollision(player);
		}
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.dealtDamage = nbt.getBoolean("DealtDamage");
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putBoolean("DealtDamage", this.dealtDamage);
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

}
