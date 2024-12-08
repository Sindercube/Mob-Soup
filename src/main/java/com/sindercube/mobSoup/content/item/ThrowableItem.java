package com.sindercube.mobSoup.content.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ThrowableItem {

	TridentEntity create(World world, LivingEntity owner, ItemStack stack);

}
