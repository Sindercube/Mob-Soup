package com.sindercube.mobSoup.client.content.entity.renderer;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.entity.state.CenturionState;
import com.sindercube.mobSoup.content.entity.CenturionEntity;
import net.minecraft.client.render.entity.AbstractSkeletonEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public class CenturionRenderer extends AbstractSkeletonEntityRenderer<CenturionEntity, CenturionState> {

	public static final Identifier DEFAULT_TEXTURE = MobSoup.of("textures/entity/centurion/default.png");
	public static final Identifier ENRAGED_TEXTURE = MobSoup.of("textures/entity/centurion/enraged.png");

	public CenturionRenderer(EntityRendererFactory.Context context, EntityModelLayer layer) {
		super(context, layer, EntityModelLayers.SKELETON_INNER_ARMOR, EntityModelLayers.SKELETON_OUTER_ARMOR);
	}

	@Override
	public void updateRenderState(CenturionEntity entity, CenturionState state, float f) {
		super.updateRenderState(entity, state, f);
		state.enraged = entity.isEnraged();
	}

	@Override
	public CenturionState createRenderState() {
		return new CenturionState();
	}

	@Override
	public Identifier getTexture(CenturionState state) {
		return state.enraged ? ENRAGED_TEXTURE : DEFAULT_TEXTURE;
	}

}
