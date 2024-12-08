package com.sindercube.mobSoup.content.item;

import com.sindercube.mobSoup.content.entity.JavelinEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class JavelinItem extends TridentItem implements ThrowableItem {

    public JavelinItem(Item.Settings settings) {
        super(settings);
    }

	@Override
	public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		if (!(user instanceof PlayerEntity player)) return false;

		int useTime = this.getMaxUseTime(stack, user) - remainingUseTicks;
		if (useTime < 10) return false;

		if (stack.willBreakNextUse()) return false;

		player.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!(world instanceof ServerWorld serverWorld)) return false;

		stack.damage(1, player);
		JavelinEntity javelin = ProjectileEntity.spawnWithVelocity(
			JavelinEntity::new,
			serverWorld,
			stack.copyWithCount(1),
			player,
			0.0F, 2.5F, 1.0F
		);
		stack.decrement(1);
		if (player.isInCreativeMode()) {
			javelin.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
		}
//		else {
//			player.getInventory().removeOne(stack.copyWithCount(1));
//		}

		world.playSoundFromEntity(null, javelin, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
		return true;
	}

	@Override
	public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
		JavelinEntity javelin = new JavelinEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1));
		javelin.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
		return javelin;
	}

	@Override
	public TridentEntity create(World world, LivingEntity owner, ItemStack stack) {
		return new TridentEntity(world, owner, stack);
	}

}
