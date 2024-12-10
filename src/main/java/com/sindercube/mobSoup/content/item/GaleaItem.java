package com.sindercube.mobSoup.content.item;

import com.sindercube.mobSoup.registry.ModArmorMaterials;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;

import java.util.List;

public class GaleaItem extends ArmorItem {

	protected static final StatusEffectInstance STRENGTH_EFFECT = new StatusEffectInstance(
		StatusEffects.STRENGTH,
		200
	);

	protected static final StatusEffectInstance RESISTANCE_EFFECT = new StatusEffectInstance(
		StatusEffects.RESISTANCE,
		200
	);

	public GaleaItem(Settings settings) {
		super(ModArmorMaterials.GALEA, EquipmentType.HELMET, settings);
	}

	public void tick(ServerWorld world, PlayerEntity player) {
		if (player.age % 200 != 0) return;

		Box box = player.getBoundingBox().expand(8, 4, 8);
		List<LivingEntity> allies = world.getEntitiesByClass(LivingEntity.class, box, entity -> isAlly(entity, player));

		for (LivingEntity ally : allies) {
			ally.addStatusEffect(STRENGTH_EFFECT, player);
			ally.addStatusEffect(RESISTANCE_EFFECT, player);
		}
	}

	public static boolean isAlly(LivingEntity entity, PlayerEntity player) {
		if (entity.equals(player)) return false;
		if (player.isTeammate(entity)) return true;
		if (entity instanceof Tameable tameable && tameable.getOwner() != null) return tameable.getOwner().equals(player);
		return false;
	}

}
