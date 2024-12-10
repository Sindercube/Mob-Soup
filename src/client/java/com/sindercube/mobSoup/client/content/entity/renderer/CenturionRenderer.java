package com.sindercube.mobSoup.client.content.entity.renderer;

import com.sindercube.mobSoup.MobSoup;
import com.sindercube.mobSoup.client.content.entity.model.CenturionModel;
import com.sindercube.mobSoup.client.content.entity.state.CenturionState;
import com.sindercube.mobSoup.content.entity.CenturionEntity;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.AbstractSkeletonEntityRenderer;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CenturionRenderer extends BipedEntityRenderer<CenturionEntity, CenturionState, CenturionModel> {

	public static final Identifier DEFAULT_TEXTURE = MobSoup.of("textures/entity/centurion/default.png");
	public static final Identifier ENRAGED_TEXTURE = MobSoup.of("textures/entity/centurion/enraged.png");

	public CenturionRenderer(EntityRendererFactory.Context context, EntityModelLayer layer, EntityModelLayer armorInnerLayer, EntityModelLayer armorOuterLayer) {
		super(context, new CenturionModel(context.getPart(layer)), 0.5F);
		this.addFeature(new ArmorFeatureRenderer(this, new SkeletonEntityModel(context.getPart(armorInnerLayer)), new SkeletonEntityModel(context.getPart(armorOuterLayer)), context.getEquipmentRenderer()));
		this.addFeature(new CenturionCapeFeatureRenderer(this, context.getEntityModels(), context.getEquipmentModelLoader()));
	}

	@Override
	public void updateRenderState(CenturionEntity entity, CenturionState state, float f) {
		super.updateRenderState(entity, state, f);
		updateCape(entity, state, f);
		state.enraged = entity.isEnraged();
	}

	private static void updateCape(CenturionEntity player, CenturionState state, float tickDelta) {
		double d = MathHelper.lerp(tickDelta, player.prevCapeX, player.capeX) - MathHelper.lerp(tickDelta, player.prevX, player.getX());
		double e = MathHelper.lerp(tickDelta, player.prevCapeY, player.capeY) - MathHelper.lerp(tickDelta, player.prevY, player.getY());
		double f = MathHelper.lerp(tickDelta, player.prevCapeZ, player.capeZ) - MathHelper.lerp(tickDelta, player.prevZ, player.getZ());
		float g = MathHelper.lerpAngleDegrees(tickDelta, player.prevBodyYaw, player.bodyYaw);
		double h = MathHelper.sin(g * 0.017453292F);
		double i = -MathHelper.cos(g * 0.017453292F);
		state.capeFlap = (float)e * 10.0F;
		state.capeFlap = MathHelper.clamp(state.capeFlap, -6.0F, 32.0F);
		state.capeLean = (float)(d * h + f * i) * 100.0F;
		state.capeLean = MathHelper.clamp(state.capeLean, 0.0F, 150.0F);
		state.capeLean2 = (float)(d * i - f * h) * 100.0F;
		state.capeLean2 = MathHelper.clamp(state.capeLean2, -20.0F, 20.0F);
		float j = MathHelper.lerp(tickDelta, player.prevStrideDistance, player.strideDistance);
//		float k = MathHelper.lerp(tickDelta, player.lastDistanceMoved, player.distanceMoved);
		float k = 1;
		state.capeFlap += MathHelper.sin(k * 6.0F) * 32.0F * j;
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
