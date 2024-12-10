package com.sindercube.mobSoup.mixin;

import com.sindercube.mobSoup.content.item.GaleaItem;
import com.sindercube.mobSoup.registry.ModItems;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Inject(method = "tick", at = @At("RETURN"))
	private void afterTick(CallbackInfo ci) {
		PlayerEntity player = (PlayerEntity)(Object)this;
		if (!(player.getWorld() instanceof ServerWorld world)) return;

		ItemStack headStack = player.getEquippedStack(EquipmentSlot.HEAD);
		if (headStack.isOf(ModItems.GALEA)) {
			GaleaItem galeaItem = (GaleaItem)headStack.getItem();
			galeaItem.tick(world, player);
		}
	}

}
