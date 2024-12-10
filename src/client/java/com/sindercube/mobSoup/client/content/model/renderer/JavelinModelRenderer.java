package com.sindercube.mobSoup.client.content.model.renderer;

import com.mojang.serialization.MapCodec;
import com.sindercube.mobSoup.client.content.entity.model.JavelinModel;
import com.sindercube.mobSoup.client.content.entity.renderer.JavelinRenderer;
import com.sindercube.mobSoup.client.registry.ModEntityModelLayers;
import com.sindercube.mobSoup.client.registry.ModEntityRenderers;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.item.model.special.SimpleSpecialModelRenderer;
import net.minecraft.client.render.item.model.special.SpecialModelRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ModelTransformationMode;

public class JavelinModelRenderer implements SimpleSpecialModelRenderer {

	private final JavelinModel model;

	public JavelinModelRenderer(JavelinModel model) {
		this.model = model;
	}

	public void render(ModelTransformationMode modelTransformationMode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, boolean glint) {
		matrices.push();
		matrices.scale(1.0F, -1.0F, -1.0F);
		VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(vertexConsumers, this.model.getLayer(JavelinRenderer.TEXTURE), false, glint);
		this.model.render(matrices, vertexConsumer, light, overlay);
		matrices.pop();
	}

	@Environment(EnvType.CLIENT)
	public record Unbaked() implements SpecialModelRenderer.Unbaked {

		public static final MapCodec<JavelinModelRenderer.Unbaked> CODEC = MapCodec.unit(new JavelinModelRenderer.Unbaked());

		public MapCodec<JavelinModelRenderer.Unbaked> getCodec() {
			return CODEC;
		}

		public SpecialModelRenderer<?> bake(LoadedEntityModels entityModels) {
			return new JavelinModelRenderer(new JavelinModel(entityModels.getModelPart(ModEntityModelLayers.JAVELIN)));
		}

	}

}
