package com.sindercube.mobSoup.registry;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.content.item.GaleaItem;
import com.sindercube.mobSoup.content.item.JavelinItem;
import com.sindercube.mobSoup.content.item.ScutumItem;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ModItems {

    public static void init() {}

	public static final Item THE_MOB_SOUP = register("the_mob_soup", Item::new,
		new Item.Settings()
			.rarity(Rarity.EPIC)
			.food(new FoodComponent(4, 4, false))
	);
    public static final Item GALEA = register("galea", GaleaItem::new,
		new Item.Settings()
			.rarity(Rarity.RARE)
    );
    public static final Item JAVELIN = register("javelin", JavelinItem::new,
		new Item.Settings()
			.rarity(Rarity.RARE)
			.maxCount(3)
			.enchantable(1)
    );
    public static final Item SCUTUM = register("scutum", ScutumItem::new,
		new Item.Settings()
			.rarity(Rarity.RARE)
			.maxDamage(512)
			.repairable(ItemTags.WOODEN_TOOL_MATERIALS)
			.equippableUnswappable(EquipmentSlot.OFFHAND)
    );
    public static final Item CENTURION_SPAWN_EGG = register("centurion_spawn_egg",
		settings -> new SpawnEggItem(ModEntityTypes.CENTURION, settings),
		new Item.Settings()
    );

    public static Item register(String name, Function<Item.Settings, Item> function, Item.Settings settings) {
        Identifier id = MobSoup.of(name);
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, id);
        settings = settings.registryKey(key);
        return Registry.register(Registries.ITEM, key, function.apply(settings));
    }

}
