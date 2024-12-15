package com.sindercube.mobSoup.content.entity.ai.goal;

import com.sindercube.mobSoup.content.entity.AbstractCenturionEntity;
import com.sindercube.mobSoup.content.entity.JavelinEntity;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.Objects;

public class PickUpJavelinsGoal extends Goal {

	private final AbstractCenturionEntity entity;
	private JavelinEntity target;
	private Path path;

	public PickUpJavelinsGoal(AbstractCenturionEntity entity) {
		super();
		this.entity = entity;
	}

    @Override
    public boolean canStart() {
		System.out.println("test");

		ItemStack javelinStack = this.entity.getMainHandStack();
		if (javelinStack.isOf(ModItems.JAVELIN) && javelinStack.getCount() == javelinStack.getMaxCount()) {
			this.entity.setPhase(AbstractCenturionEntity.Phase.ATTACK_RANGED);
			return false;
		}

		System.out.println("test");

		double range = this.entity.getAttributeValue(EntityAttributes.FOLLOW_RANGE);
		Box box = this.entity.getBoundingBox().expand(range, 8, range);
		List<JavelinEntity> nearbyJavelins = this.entity.getWorld().getEntitiesByClass(JavelinEntity.class, box, this.entity::ownsProjectile);
		if (nearbyJavelins.isEmpty()) {
			this.entity.setPhase(AbstractCenturionEntity.Phase.RESTOCK_TARGET);
			return false;
		}

		System.out.println("test");

		JavelinEntity javelin = nearbyJavelins.getFirst();
		Path path = this.entity.getNavigation().findPathTo(javelin, 1);
		if (path == null || !path.reachesTarget()) return false;

		this.path = path;
		this.target = javelin;
		return true;
	}

	@Override
	public boolean shouldContinue() {
		return this.target != null || this.path != null;
	}

	@Override
	public void start() {
		this.entity.getNavigation().startMovingAlong(this.path, 1.15);
	}

	@Override
	public void stop() {
		this.path = null;
		this.target = null;
	}

	@Override
	public void tick() {
		if (this.target == null) return;
		if (!this.target.isInRange(this.entity, 1)) return;
		if (!(this.target.getWorld() instanceof ServerWorld world)) return;

		ItemStack stack = this.target.getItemStack().copyWithCount(1);
		ItemStack pickedStack = this.entity.tryEquip(world, stack);
		if (pickedStack.isEmpty()) return;

		this.target.discard();
	}

}
