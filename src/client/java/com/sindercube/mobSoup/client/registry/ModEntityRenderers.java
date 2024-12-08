package com.sindercube.mobSoup.client.registry;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.entity.model.CenturionModel;
import com.sindercube.mobSoup.client.content.entity.renderer.CenturionRenderer;
import com.sindercube.mobSoup.registry.ModEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;

public class ModEntityRenderers {

	public static final EntityModelLayer CENTURION_LAYER = new EntityModelLayer(MobSoup.of("centurion"), "main");

	public static void init() {
		EntityRendererRegistry.register(ModEntityTypes.CENTURION, context -> new CenturionRenderer(context, CENTURION_LAYER));
		EntityModelLayerRegistry.registerModelLayer(CENTURION_LAYER, CenturionModel::getTexturedModelData);
	}

}
