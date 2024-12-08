package com.sindercube.mobSoup.client.content.entity.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;

public class CenturionArmorRenderer<S extends EntityRenderState, M extends EntityModel<? super S>> extends FeatureRenderer<S, M> {

	public CenturionArmorRenderer(FeatureRendererContext<S, M> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, S state, float limbAngle, float limbDistance) {

	}

}
