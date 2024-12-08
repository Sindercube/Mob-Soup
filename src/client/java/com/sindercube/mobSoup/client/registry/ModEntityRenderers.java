package com.sindercube.mobSoup.client.registry;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.entity.model.CenturionModel;
import com.sindercube.mobSoup.client.content.entity.model.JavelinModel;
import com.sindercube.mobSoup.client.content.entity.renderer.CenturionRenderer;
import com.sindercube.mobSoup.client.content.entity.renderer.JavelinRenderer;
import com.sindercube.mobSoup.client.content.model.renderer.JavelinModelRenderer;
import com.sindercube.mobSoup.registry.ModEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.item.model.special.SpecialModelTypes;
import net.minecraft.client.render.item.model.special.TridentModelRenderer;

public class ModEntityRenderers {

	public static final EntityModelLayer CENTURION_LAYER = new EntityModelLayer(MobSoup.of("centurion"), "main");
	public static final EntityModelLayer JAVELIN_LAYER = new EntityModelLayer(MobSoup.of("javelin"), "main");

	public static void init() {
		SpecialModelTypes.ID_MAPPER.put(MobSoup.of("javelin"), JavelinModelRenderer.Unbaked.CODEC);

		EntityRendererRegistry.register(ModEntityTypes.CENTURION, context -> new CenturionRenderer(context, CENTURION_LAYER));
		EntityRendererRegistry.register(ModEntityTypes.JAVELIN, context -> new JavelinRenderer(context, JAVELIN_LAYER));
		EntityModelLayerRegistry.registerModelLayer(CENTURION_LAYER, CenturionModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(JAVELIN_LAYER, JavelinModel::getTexturedModelData);
	}

}
