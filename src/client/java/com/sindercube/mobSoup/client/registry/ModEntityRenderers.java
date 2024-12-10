package com.sindercube.mobSoup.client.registry;

import com.sindercube.mobSoup.client.content.entity.model.CenturionModel;
import com.sindercube.mobSoup.client.content.entity.model.JavelinModel;
import com.sindercube.mobSoup.client.content.entity.renderer.CenturionRenderer;
import com.sindercube.mobSoup.client.content.entity.renderer.JavelinRenderer;
import com.sindercube.mobSoup.registry.entity.ModEntityTypes;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.ArmorEntityModel;

public class ModEntityRenderers {

	private static final Dilation ARMOR_DILATION = new Dilation(1.0F);
	private static final Dilation HAT_DILATION = new Dilation(0.5F);

	public static void init() {
		EntityRendererRegistry.register(ModEntityTypes.CENTURION, context -> new CenturionRenderer(context, ModEntityModelLayers.CENTURION, ModEntityModelLayers.CENTURION_INNER_ARMOR, ModEntityModelLayers.CENTURION_OUTER_ARMOR));
		EntityRendererRegistry.register(ModEntityTypes.JAVELIN, context -> new JavelinRenderer(context, ModEntityModelLayers.JAVELIN));
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.CENTURION, CenturionModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.CENTURION_INNER_ARMOR, ModEntityRenderers::innerArmor);
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.CENTURION_OUTER_ARMOR, ModEntityRenderers::outerArmor);
		EntityModelLayerRegistry.registerModelLayer(ModEntityModelLayers.JAVELIN, JavelinModel::getTexturedModelData);
	}

	private static TexturedModelData innerArmor() {
		return TexturedModelData.of(ArmorEntityModel.getModelData(HAT_DILATION), 64, 32);
	}

	private static TexturedModelData outerArmor() {
		return TexturedModelData.of(ArmorEntityModel.getModelData(ARMOR_DILATION), 64, 32);
	}

}
