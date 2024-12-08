package com.sindercube.mobSoup.client.content.entity.renderer;

import com.sindercube.mobSoup.client.content.entity.model.JavelinModel;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.StuckObjectsFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;

public class StuckJavelinsFeatureRenderer<M extends PlayerEntityModel> extends StuckObjectsFeatureRenderer<M> {

	public StuckJavelinsFeatureRenderer(LivingEntityRenderer<?, PlayerEntityRenderState, M> entityRenderer, EntityRendererFactory.Context context) {
		super(entityRenderer, new JavelinModel(context.getPart(EntityModelLayers.ARROW)), ArrowEntityRenderer.TEXTURE, RenderPosition.IN_CUBE);
	}

	protected int getObjectCount(PlayerEntityRenderState playerRenderState) {
		return playerRenderState.stuckArrowCount;
	}

}
