package com.sindercube.mobSoup.content.entity.ai.goal;

import com.sindercube.mobSoup.content.entity.CenturionEntity;
import com.sindercube.mobSoup.content.entity.JavelinEntity;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.Objects;

public class PickUpJavelinsGoal extends Goal {

	private final CenturionEntity entity;
	private JavelinEntity target;
	private Path path;

	public PickUpJavelinsGoal(CenturionEntity entity) {
		super();
		this.entity = entity;
	}

    @Override
    public boolean canStart() {
		ItemStack javelinStack = this.entity.getMainHandStack();
		if (javelinStack.isOf(ModItems.JAVELIN) && javelinStack.getCount() == javelinStack.getMaxCount()) return false;

		double range = this.entity.getAttributeValue(EntityAttributes.FOLLOW_RANGE);
		List<JavelinEntity> nearbyJavelins = this.entity.getWorld().getEntitiesByClass(
			JavelinEntity.class,
			this.entity.getBoundingBox().expand(range, 8.0, range),
			javelin -> Objects.equals(javelin.getOwner(), this.entity)
		);
		if (nearbyJavelins.isEmpty()) {
			return false;
		}

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
