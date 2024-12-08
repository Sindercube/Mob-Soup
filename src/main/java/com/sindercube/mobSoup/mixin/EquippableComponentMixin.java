package com.sindercube.mobSoup.mixin;

import net.minecraft.component.type.EquippableComponent;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(EquippableComponent.class)
public class EquippableComponentMixin {

	@Inject(method = "assetId", at = @At("RETURN"), cancellable = true)
	public void getAssetId(CallbackInfoReturnable<Optional<RegistryKey<EquipmentAsset>>> cir) {
		Optional<RegistryKey<EquipmentAsset>> result = cir.getReturnValue();
		if (result.isPresent()) {
			RegistryKey<EquipmentAsset> key = result.orElseThrow();
			if (key.getValue() == null) cir.setReturnValue(Optional.empty());
		}
	}

}
