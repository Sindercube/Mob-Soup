package com.sindercube.mobSoup.registry;

import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;

import java.util.Map;

public class ModArmorMaterials {

    public static final ArmorMaterial CENTURION = new ArmorMaterial(
		33,
		Map.of(EquipmentType.HELMET, 3),
		10,
		SoundEvents.ITEM_ARMOR_EQUIP_IRON,
		2.0F,
		0.0F,
		ItemTags.REPAIRS_IRON_ARMOR,
		ModEquipmentAssetKeys.EMPTY
    );

}
